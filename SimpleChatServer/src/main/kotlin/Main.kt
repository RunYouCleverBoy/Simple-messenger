import com.google.gson.Gson
import com.google.gson.JsonParser
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.header
import io.ktor.request.receiveText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val messageHandler = MessageHandler()
    val stream = Stream()
    embeddedServer(Netty, 1111) {
        routing {
            get("/stream") {
                call.respondText(HtmlRenderer().render(stream),contentType = ContentType.Text.Html)
            }
            post("/PostMessage" ) {
                val message = JsonParser.parseString(call.receiveText()).asJsonObject.let{messageHandler.handle(it)}
                if (message is UnknownMessage) {
                    call.respondText(contentType = ContentType.Text.Html) { "Error, ${message.errorKind}" }
                } else {
                    stream.addMessage(message)
                    call.respondText(contentType = ContentType.Application.Json) {"{\"Result\": \"OK\"}"}
                }
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
