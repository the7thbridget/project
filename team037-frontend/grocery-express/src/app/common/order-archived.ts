import { Item } from "./item";

export class OrderArchived {
    archive_order_id!:number;
    drone_id!:number;
    customer_id!: number;
    close_date!: string;
    create_date!: string;
    order_name!: string;
    status!: number;
    archived_date!: string;
    items!:Item[]
}
