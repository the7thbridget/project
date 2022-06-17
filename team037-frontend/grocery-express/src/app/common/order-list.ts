import { Order } from "./order";
import { OrderArchived } from "./order-archived";

export class OrderList {
    archivedOrders!: OrderArchived[];
    currentOrders!: Order[]
}
