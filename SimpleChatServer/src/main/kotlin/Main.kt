import com.google.gson.*
import data.Chat
import data.ChatMessage
import data.ErrorResponse
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.options
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import org.litote.kmongo.toId
import utils.time.Time

fun main() {
    val messageHandler = MessageHandler()
    val db = MongoWrapper()
    val stream = Stream()
    suspend fun ApplicationCall.respondWithError(string: String) = respondText(contentType = ContentType.Application.Json) { Gson().toJson(ErrorResponse("Invalid ChatID")) }

    val gson = buildGson()

    embeddedServer(Netty, 1111) {
        routing {
            get("/chats")  {
                call.respondText(contentType = ContentType.Application.Json, text = gson.toJson(db.chats()))
            }
            get("/stream") {
                val chatId = call.parameters["chatId"]?:let { call.respondWithError("No chatId"); return@get }
                call.respondText(contentType = ContentType.Application.Json, text = gson.toJson(db.readChat(chatId.toId())))
            }
            options("/PostMessage") {
                call.respond(HttpStatusCode.OK, "OK")
            }
            post("/PostMessage" ) {
                val chatId = call.parameters["chatId"]?:let { call.respondWithError("No chatId"); return@post }
                val message = JsonParser.parseString(call.receiveText()).asJsonObject.let{messageHandler.handle(it)}
                if (message is ReceivedMessage.UnknownMessage) {
                    call.respondText(contentType = ContentType.Application.Json) { gson.toJson(ErrorResponse("Unknown message")) }
                } else {
                    when (message) {
                        is ReceivedMessage.TextMessage -> {
                            val msg = ChatMessage(newId(), chatId.toId(), message.text , Time.now)
                            db.addMessage(msg)
                            call.respondText(contentType = ContentType.Application.Json) { gson.toJson(msg) }
                        }
                        else -> {call.respondWithError("Not a text message") }
                    }
                }
            }
            options("/CreateChat") {
                call.respond(HttpStatusCode.OK, "OK")
            }
            post("/CreateChat") {
                val chatName = call.parameters["chatName"]?:let { call.respondWithError("No chat name"); return@post }
                val chat = Chat(newId(), chatName)
                db.addChat(chat)
                call.respond(HttpStatusCode.OK, gson.toJson(chat))
            }
            get("/help") {
                call.respondText(contentType = ContentType.Text.Html) {
                    listOf("<html><body>",
                        "/PostMessage {kind: text, Text: text}",
                        "/PostMessage {kind: image, url: imageurl, title: title}",
                        "</body></html>"
                    ).joinToString("</br>") }
            }
        }
    }.start(wait=true)
}


private fun buildGson() = GsonBuilder().run {
    registerTypeAdapter(Id::class.java, JsonSerializer<Id<Any>> { id, _, _ -> JsonPrimitive(id?.toString()) })
    registerTypeAdapter(Id::class.java, JsonDeserializer<Id<Any>> { id, _, _ -> id.asString.toId() })
    create()
}