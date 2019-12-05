import { Component, OnInit } from '@angular/core';
import {Item} from '../classes/item';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.css']
})
export class ChatWindowComponent implements OnInit {
  items: Item[] = [];
  messageText = '';

  constructor(private http: HttpClient) { }

  ngOnInit() {
    // HttpClient.get('http://localhost:8000', null)
    this.post('Hello');
    this.post('Bye');
    this.post('What\'s up?');
    this.post('Get out');

    this.refresh();
  }

  onSend() {
    const text = String(this.messageText);
    this.items.push(new Item(this.messageText));
    this.post(text);
    this.messageText = '';
  }

  private post(text: string) {
    const httpOptions = { headers: new HttpHeaders({'Content-Type':  'application/json'})};
    this.http.post<Item>('http://localhost:1111/PostMessage', new Item(text), httpOptions).subscribe(data => console.log(data));
  }

  refresh() {
    const thiz = this;
    this.http.get<Item[]>('http://localhost:1111/stream').toPromise().then(r => thiz.items = r.map<Item>(x => new Item(x.text, new Date(x.ts))));
  }
}
