#Project API

Show all stores
```
URL: /api/store/all
Request type: GET
Parameter: NONE
Response: {
code: XXX,
msg: XXX, 
stores: [
{
"store_id": 1,
"store_name": "xxx",
"revenue": 11111,
"incoming_revenue": 123
}, {}
]
}
```

Add a store
```
URL: /api/store/add
Request type: POST
Parameter: JSON
{
"storeName":"XXX",
"revenue": 12345
}
Response: {
"code": 200,
"msg": "xxx"
}
```

Add an item
```
URL: /api/item/add
Request type: POST
Parameter: JSON
{
"storeName":"",
"itemName":"",
"weight":3
}
Response: {
"msg":
"code":
}
```

Show all items in a store
```
URL: /api/item/all
Request type: GET
Parameter: Params(storeName)
Response: {
"msg":
"code":
"items": [
{
"item_id": 1,
"item_name": "xxx",
"unit_weight": 4,
"store_id": 1
}, {}
]
}
```

Maintain drone
```
URL: /api/drone/maintain
Request type: POST
Parameter: Params(drone_id)
Response: {
"msg":
"code":
}, {}
]
}
```

Add a pilot
```
URL: /api/pilot/add
Request type: POST
Parameter: JSON
{
"account":"kkiwi23",
"firstName":"Gillian",
"lastName":"Grape",
"phoneNumber":"999-999-9999",
"taxID":"234-56-7890",
"licenseID":"panam_10",
"experience":31
}
Response:
{
"msg":
"code": 
}
```

Show all pilots
```
URL: /api/pilot/all
Request type: GET
Parameter: NONE
Response: {
"msg":
"code":
"pilots": [
{
"pilot_id": 1,
"account": "ggrape17",
"first_name": "Gillian",
"last_name": "Grape",
"phone_number": "999-999-9999",
"tax_id": "234-56-7890",
"license_id": "panam_10",
"experience": 31
} {}
]
}
```

Add a drone
```
URL: /api/drone/add
Request type: POST
Parameter: JSON
{
"storeName":"kroger",
"droneIdentifier":"1",
"maxCapacity":40,
"maxTrip":3
}
Response:
{
"msg":
"code": 
}
```

Show all drones in a store
```
URL: /api/drone/all
Request type: GET
Parameter: Params(storeName)
Response: {
"msg":
"code":
"drones": [
{
"drone_id": 1,
"drone_identifier": "1",
"store_id": 1,
"max_capacity": 40,
"max_trip": 3,
"pilot_id": 1,
"pilot_name": "XXX"
"remaining_trip": 3,
"num_of_order": 2,
"remaining_capacity": 20
}, {}
]
}
```

Assign pilot to drone
```
URL: /api/drone/fly
Request type: POST
Parameter: Params(storeName, droneIdentifier, pilotAccount)
Response:
{
"msg":
"code": 
}
```

Add a customer
```
URL: /api/customer/add
Request type: POST
Parameter: JSON
{
"account":"aapple2",
"firstName":"Alana",
"lastName":"Apple",
"phoneNumber":"222-222-2222",
"rating":4,
"credit":100
}
Response:
{
"msg":
"code"
}
```

Show all customers
```
URL: /api/customer/all
Request type: GET
Parameter: ()
Response: {
"msg":
"code":
"customers": [ "currentCustomers",
{
"name": Alana Apple,
"phone_number":"222-222-2222"
"rating": 4,
"credit": 100,
"last_login": 2021-11-25 00:00:00
}, 
"archivedCustomers", {}
]
}
```
Show customer order
```
URL: /api/order/showCustomerOrder
Request type: GET
Parameter: (storeName, username)
Response: {
"msg":
"code":
"customerOrders": [
{
"order_id": 1,
"drone_id": 1,
"customer_id": 1,
"order_name": "purchaseA",
"close_date": 2021-11-25 04:00:00,
"create_date": 2021-11-25 00:00:00,
"status": 1
"item_name": "XXX"
"quantity": 3
"total_price": 30
"total_weight": 15
"
}, {}
]
}
```

manually assign customer rating
```
URL: /api/customer/manualReassignRating
Request type: POST
Parameter: Parameters(Integer userId, Integer manualRating)
Response:
{
"msg":
"code"
}
```
auto reassign customer rating
```
URL: /api/customer/autoReassignRating
Request type: POST
Parameter: Parameter(Integer userId)
Response:
{
"msg":
"code"
}
```
Add an order
```
URL: /api/order/add
Request type: POST
Parameter: JSON
{
"storeName":"kroger",
"orderName":"purchaseA",
"droneIdentifier":"1",
"customerUsername":"aapple2"
}
Response:
{
"msg":
"code": 
}
```

Show all orders in a store
```
URL: /api/order/all
Request type: GET
Parameter: Params(storeName)
Response: {
"msg":
"code":
"orders": [ "currentCustomers",
{
"name": Alana Apple,
"phone_number":"222-222-2222"
"rating": 4,
"credit": 100,
"last_login": 2021-11-25 00:00:00
}, 
"archivedCustomers", {}
]
]
}
```

Request items for an order
```
URL: /api/order/request
Request type: POST
Parameter: Params(storeName, orderName, itemName, quantity, unitPrice)
Response:
{
"msg":
"code": 
}
```

Purchase an order
```
URL: /api/order/purchase
Request type: POST
Parameter: Params(storeName, orderName)
Response:
{
"msg":
"code": 
}
```

Cancel an order
```
URL: /api/order/cancel
Request type: POST
Parameter: Params(storeName, orderName)
Response:
{
"msg":
"code": 
}
```
show archived orders
```
URL: /api/order/showArchived
Request type: POST
Parameter: Params(storeName, archivedData)
Response: {
"msg":
"code":
"archivedOrders": [
{
"order_id": 1,
"drone_id": 1,
"customer_id": 1,
"order_name": "purchaseA",
"close_date": 2021-11-25 04:00:00,
"create_date": 2021-11-25 00:00:00,
"status": 1
"item_name": "XXX"
"quantity": 3
"total_price": 30
"total_weight": 15
"
}, {}
]
}
```
manually reassign drone:
```
URL: /api/order/manualReassignDrone
Request type: POST
Parameter: Params(orderId, droneId, droneIdentifier)
Response:
{
"msg":
"code": 
}
```
auto reassign drone
```
URL: /api/order/autoReassignDrone
Request type: POST
Parameter: Params(orderId, droneId)
Response:
{
"msg":
"code": 
}
```
update setting criteria
```
URL: /api/setting/updateSetting
Request type: POST
Parameter: JSON
{
"droneAssignRule":1,
"customerRatingRule":1,
"archiveDataType":1,
"timePeriod":3
"archivePeriod":6
}
Response:
{
"msg":
"code": 
}
```
register user
```
URL: /api/user/register
Request type: POST
Parameter: JSON
{
"username":"XXX",
"password":"XXX",
"userType":3,
}
Response:
{
"msg":
"code": 
}
```
login user
```
URL: /api/user/login
Request type: POST
Parameter: JSON
{
"username":"XXX",
"password":"XXX",
"userType":3,
}
Response:
{
"msg":
"code": 
}
```
auto assign drone
```
URL: /api/order/autoReassignDrone
Request type: POST
Parameter: JSON
{
"orderId":"XXX",
"droneId":"XXX",
}
Response:
{
"msg":
"code": 
}
```

manual reassign drone
```
URL: /api/order/manualReassignDrone
Request type: POST
Parameter: JSON
{
"orderId":"XXX",
"droneId":"XXX",
"droneIdentifier": "XXX"
}
Response:
{
"msg":
"code": 
}
```

auto assign active customer rating
```

URL: /api/customer/autoReassignRating
Request type: POST
Parameter: JSON
{
"userId":"XXX"
}
Response:
{
"msg":
"code": 
}
```

manual reassign active customer rating
```
URL: /api/customer/manualReassignRating
Request type: POST
Parameter: JSON
{
"userId":"XXX",
"manualRating":"XXX",
}
Response:
{
"msg":
"code": 
}
```




Error:
"code": 500
"msg": "Unknown error!"

"code": 1000
"msg": "Store not found!"

"code": 2000
"msg": "Pilot not found!"

"code": 3000
"msg": "Drone not found!"

STORE_NOT_FOUND(1000, "Store not found!"),
UNKNOWN_ERROR(500, "Unknown error!"),
PILOT_NOT_FOUND(2000, "Pilot not found!"),
DRONE_NOT_FOUND(3000, "Drone not found!"),
USER_NOT_FOUND(4000, "Invalid username or password!"),
CUSTOMER_NOT_FOUND(3500, "Customer not found!"),
USER_DUPLICATE(4100, "Duplicate user found!"),
ORDER_NOT_FOUND(5000, "Order not found!"),
NOT_ENOUGH_CREDIT(6000, "The user does not have enough credit!"),
NOT_ENOUGH_CAPACITY(6100, "The drone does not have enough capacity!"),
DRONE_NOT_HAVE_ENOUGH_TRIPS(7000, "Drone does not have enough trips!"),
DRONE_NEEDS_PILOT(7100, "The drone needs pilot!"),
UNABLE_TO_CANCEL_ORDER(7200, "Unable to cancel the order!"),
ITEM_NOT_FOUND(8000, "Item not found!"),
USER_TYPE_NOT_SUPPORTED(9000, "User type not supported!"),
