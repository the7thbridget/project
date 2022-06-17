import { Item } from "./item";

export class Order {
    order_id!: number;
    drone_id!: number;
    customer_id!: number;
    order_name!: string;
    close_date!: string;
    create_date!: string;
    status!: number;
    items!:Item[]
}
