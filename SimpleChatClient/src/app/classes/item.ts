export class Item {
  kind = 'text';
  text: string;
  ts: number;
  tsAsString: string;

  constructor(message: string, date: Date = new Date()) {
    this.text = message;
    this.ts = date.getTime();
    this.applyTs();
  }

  from(message: string, timeStampMs: number) {
    this.text = message;
    this.ts = timeStampMs;
  }

  public applyTs() {
    this.tsAsString = this.getTs();
  }

  public getTs(): string {
    const d: Date = (new Date(this.ts));

    return `${d.getHours()}:${d.getMinutes()}:${d.getSeconds()}`;
  }
}
