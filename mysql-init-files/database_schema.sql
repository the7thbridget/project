create table `Function`
(
    function_id   int auto_increment,
    function_name varchar(64) not null,
    primary key (function_id, function_name)
);

create table User_type
(
    user_type_id   int auto_increment,
    user_type_name varchar(16) not null,
    primary key (user_type_id, user_type_name)
);

create table User_type_2_function
(
    user_type_id int not null,
    function_id  int not null,
    primary key (user_type_id, function_id),
    constraint User_type_2_function_fk0
        foreign key (user_type_id) references User_type (user_type_id),
    constraint User_type_2_function_fk1
        foreign key (function_id) references `Function` (function_id)
);

create table Store
(
    store_id   int auto_increment,
    store_name varchar(255) not null,
    revenue    int          not null,
    primary key (store_id, store_name),
    constraint store_name
        unique (store_name)
);

create table `User`
(
    user_id   int auto_increment,
    username  varchar(64)  not null,
    password  varchar(255) null,
    salt      varchar(4)   null,
    user_type int          not null,
    store_id  int          null,
    primary key (username, user_type),
    constraint user_id
        unique (user_id),
    constraint User_fk0
        foreign key (user_type) references User_type (user_type_id),
    constraint User_fk1
        foreign key (store_id) references Store (store_id)
);

create table Rating_orderNum
(
    rating      int not null
        primary key,
    lower_bound int null,
    upper_bound int null,
    constraint Rating_orderNum_rating_uindex
        unique (rating)
);

create table Rating_orderValue
(
    rating      int not null
        primary key,
    lower_bound int null,
    upper_bound int null,
    constraint Rating_orderValue_rating_uindex
        unique (rating)
);

create table Pilot
(
    pilot_id     int auto_increment
        primary key,
    account      varchar(255) not null,
    first_name   varchar(16)  not null,
    last_name    varchar(16)  not null,
    phone_number varchar(16)  not null,
    tax_id       varchar(16)  not null,
    license_id   varchar(16)  not null,
    experience   int          not null,
    constraint Pilot_tax_id_uindex
        unique (tax_id),
    constraint account
        unique (account),
    constraint license_id
        unique (license_id)
);

create table Drone
(
    drone_id         int auto_increment,
    drone_identifier varchar(255) not null,
    store_id         int          not null,
    max_capacity     int          not null,
    max_trip         int          not null,
    pilot_id         int          null,
    remaining_trip   int          null,
    primary key (drone_identifier, store_id),
    constraint drone_id
        unique (drone_id),
    constraint Drone_fk0
        foreign key (store_id) references Store (store_id),
    constraint Drone_fk1
        foreign key (pilot_id) references Pilot (pilot_id)
);

create table Active_customer
(
    active_customer_id int auto_increment,
    user_id            int          not null,
    rating             int          not null,
    credit             int          not null,
    last_login         datetime     null,
    first_name         varchar(255) not null,
    last_name          varchar(255) not null,
    phone_number       varchar(255) not null,
    primary key (active_customer_id, user_id),
    constraint Active_customer_fk0
        foreign key (user_id) references User (user_id)
);

create table Archived_customer
(
    archived_customer_id int          not null,
    user_id              int          not null,
    rating               int          not null,
    credit               int          not null,
    last_login           datetime     not null,
    first_name           varchar(255) not null,
    last_name            varchar(255) not null,
    phone_number         varchar(255) not null,
    archived_date        datetime     not null,
    primary key (archived_customer_id, user_id),
    constraint Archived_customer_fk0
        foreign key (user_id) references User (user_id)
);

create table Archived_order
(
    archive_order_id int          not null,
    drone_id         int          not null,
    customer_id      int          not null,
    close_date       datetime     null,
    create_date      datetime     null,
    order_name       varchar(255) null,
    status           int          null,
    archived_date    datetime     not null
);

create table Archived_order_2_item
(
    archived_order_id int not null,
    archived_item_id  int not null,
    quantity          int null,
    unit_price        int null
);

create table Item
(
    item_id     int auto_increment,
    item_name   varchar(255) not null,
    unit_weight int          not null,
    store_id    int          not null,
    primary key (item_name, store_id),
    constraint item_id
        unique (item_id),
    constraint Item_fk0
        foreign key (store_id) references Store (store_id)
);

create table `Order`
(
    order_id    int auto_increment,
    drone_id    int           not null,
    customer_id int           not null,
    order_name  varchar(255)  not null,
    close_date  datetime      null,
    create_date datetime      not null,
    status      int default 0 not null,
    primary key (drone_id, customer_id, order_name),
    constraint order_id
        unique (order_id),
    constraint Order_fk0
        foreign key (drone_id) references Drone (drone_id),
    constraint Order_fk1
        foreign key (customer_id) references User (user_id)
);

create table Order_2_item
(
    order_id   int not null,
    item_id    int not null,
    quantity   int not null,
    unit_price int not null,
    primary key (order_id, item_id),
    constraint Order_2_item_fk0
        foreign key (order_id) references `Order` (order_id)
            on delete cascade,
    constraint Order_2_item_fk1
        foreign key (item_id) references Item (item_id)
);

create table Setting_criteria
(
    row_num              int           null,
    drone_assign_rule    int default 0 null,
    archive_data_type    int default 0 null,
    time_period          int default 0 null,
    archive_period       int default 0 null,
    customer_rating_rule int default 0 null
);

/* INSERT QUERY NO: 1 */
INSERT INTO User_type(user_type_id, user_type_name)
VALUES
    (
        1, 'admin'
    );

/* INSERT QUERY NO: 2 */
INSERT INTO User_type(user_type_id, user_type_name)
VALUES
    (
        2, 'employee'
    );

/* INSERT QUERY NO: 3 */
INSERT INTO User_type(user_type_id, user_type_name)
VALUES
    (
        3, 'customer'
    );

INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        1, 'make_store'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        2, 'display_stores'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        3, 'sell_item'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        4, 'display_items'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        5, 'make_pilot'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        6, 'display_pilots'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        7, 'make_drone'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        8, 'display_drones'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        9, 'fly_drone'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        11, 'make_customer'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        12, 'display_customers'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        13, 'start_order'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        14, 'display_orders'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        15, 'request_item'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        16, 'purchase_order'
    );


INSERT INTO `Function`(function_id, function_name)
VALUES
    (
        17, 'cancel_order'
    );

INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 1
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 2
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        3, 2
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 3
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 3
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 4
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 4
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        3, 4
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 5
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 6
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 7
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 8
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 9
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 11
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 11
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 12
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 12
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 13
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 13
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 14
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 14
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 15
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 15
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 16
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 16
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        1, 17
    );


INSERT INTO User_type_2_function(user_type_id, function_id)
VALUES
    (
        2, 17
    );
    
INSERT INTO Store(store_id, store_name, revenue)
VALUES
(
4, 'kroger', 33000
);

INSERT INTO Store(store_id, store_name, revenue)
VALUES
(
5, 'publix', 33000
);

INSERT INTO User(user_id, username, password, salt, user_type, store_id)
VALUES
    (
        32, 'aapple2', '', '', 3, null
    );

INSERT INTO User(user_id, username, password, salt, user_type, store_id)
VALUES
    (
        21, 'customer2', '', '', 3, null
    );

INSERT INTO User(user_id, username, password, salt, user_type, store_id)
VALUES
    (
        22, '6310customer', '', '', 3, null
    );

INSERT INTO Rating_orderNum(rating, lower_bound, upper_bound)
VALUES
(
1, 0, 1
);

INSERT INTO Rating_orderNum(rating, lower_bound, upper_bound)
VALUES
(
2, 2, 10
);

INSERT INTO Rating_orderNum(rating, lower_bound, upper_bound)
VALUES
(
3, 11, 15
);

INSERT INTO Rating_orderNum(rating, lower_bound, upper_bound)
VALUES
(
4, 16, 20
);

INSERT INTO Rating_orderNum(rating, lower_bound, upper_bound)
VALUES
(
5, 21, 0
);

INSERT INTO Rating_orderValue(rating, lower_bound, upper_bound)
VALUES
(
1, 0, 1
);

INSERT INTO Rating_orderValue(rating, lower_bound, upper_bound)
VALUES
(
2, 2, 200
);

INSERT INTO Rating_orderValue(rating, lower_bound, upper_bound)
VALUES
(
3, 201, 300
);

INSERT INTO Rating_orderValue(rating, lower_bound, upper_bound)
VALUES
(
4, 301, 400
);

INSERT INTO Rating_orderValue(rating, lower_bound, upper_bound)
VALUES
(
5, 401, 0
);

INSERT INTO Pilot(pilot_id, account, first_name, last_name, phone_number, tax_id, license_id, experience)
VALUES
(
28, 'ffig8', 'Finneas', 'Fig', '888-888-8888', '890-12-3456', 'panam_10', 39
);

INSERT INTO Pilot(pilot_id, account, first_name, last_name, phone_number, tax_id, license_id, experience)
VALUES
(
29, 'ggrape17', 'Gillian', 'Grape', '999-999-9999', '234-56-7890', 'twa_21', 37
);



INSERT INTO Drone(drone_id, drone_identifier, store_id, max_capacity, max_trip, pilot_id, remaining_trip)
VALUES
(
15, 2, 4, 20, 3, 28, 3
);

INSERT INTO Active_customer(active_customer_id, user_id, rating, credit, last_login, first_name, last_name, phone_number)
VALUES
(
33, 32, 3, 100, null, 'Alana', 'Apple', '222-222-2222'
);


INSERT INTO Archived_customer(archived_customer_id, user_id, rating, credit, last_login, first_name, last_name, phone_number, archived_date)
VALUES
(
1, 21, 5, 100, '2020-12-07 02:33:31', 'customer', 'one', '123-456-7890', '2021-12-07 00:00:00'
);

INSERT INTO Archived_customer(archived_customer_id, user_id, rating, credit, last_login, first_name, last_name, phone_number, archived_date)
VALUES
(
2, 22, 4, 300, '2021-06-07 02:33:38', 'customer', 'two', '234-567-8901', '2021-12-07 00:00:00'
);

INSERT INTO Archived_order(archive_order_id, drone_id, customer_id, close_date, create_date, order_name, status, archived_date)
VALUES
(
1, 15, 22, '2020-12-07 02:44:42', '2020-05-07 02:44:47', 'order2', 2, '2021-11-07 02:45:09'
);

INSERT INTO Archived_order_2_item(archived_order_id, archived_item_id, quantity, unit_price)
VALUES
(
1, 8, 4, 30
);

INSERT INTO Archived_order_2_item(archived_order_id, archived_item_id, quantity, unit_price)
VALUES
(
1, 9, 3, 50
);

INSERT INTO Item(item_id, item_name, unit_weight, store_id)
VALUES
(
9, 'apple', 1, 4
);

INSERT INTO Item(item_id, item_name, unit_weight, store_id)
VALUES
(
10, 'apple', 1, 5
);


