import { Customer } from "./customer";
import { CustomerArchived } from "./customer-archived";

export class CustomerList {
    archivedCustomers!: CustomerArchived[];
    currentCustomers!: Customer[];
}
