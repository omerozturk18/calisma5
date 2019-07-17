export interface IProduct {
  id?: number;
}

export class Product implements IProduct {
  constructor(public id?: number) {}
}
