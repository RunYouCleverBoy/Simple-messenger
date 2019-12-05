import { Component, OnInit } from '@angular/core';
import {Item} from '../classes/item';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.css']
})
export class ChatWindowComponent implements OnInit {
  items: Item[] = [];
  messageText = '';

  constructor() { }

  ngOnInit() {
    this.items.push(new Item('Hello'));
    this.items.push(new Item('Bye'));
    this.items.push(new Item('What\'s up?'));
    this.items.push(new Item('Get out'));
  }

  onSend() {
    const text = String(this.messageText);
    this.items.push(new Item(this.messageText));
    this.messageText = '';
  }
}
