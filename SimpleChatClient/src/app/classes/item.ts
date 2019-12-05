export class Item {
  message: string;
  date: Date;

  constructor(message: string, date: Date = new Date()) {
    this.message = message;
    this.date = date;
  }

  from(message: string, timeStampMs: number) {
    this.message = message;
    this.date = new Date(timeStampMs);
  }
}
