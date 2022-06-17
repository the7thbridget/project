# CS6310_fa21_team037 Grocery Express Project

This *source_code.zip* file contains a spring boot website project.  
It contains two parts, the frontend and backend. The frontend is implemented with Angular framework using TypeScript, and the backend is developed using Java Spring Boot with MySQL for the database system.

## Quickstart

One can simply run the service by:  
download the *source_code.zip* file, unzip it. Make sure to cd to the corresponding directory.

### Docker 

#### Register or login through website
Run docker and enter in terminal:
`docker-compose up`

In a browser, visit [http://localhost:4201] to view the application. 

Steps:
For the first run, a new user has to be first registered to use the application. Simply click on "register" and input the username, password, and the desired user type. Then click on "login now" and enter the information again to log in.
After logging in, different users can click on various buttons to perform related actions.

	"Stores": display the store list with items, drones, and orders buttons for each store. Authorized users can also "add store" and see store revenues.
		"Items": display all the items in specific stores with buttons to "add item" and "add item to order".
		"Drones": display all the drones in specific stores with buttons of "add drone" and "fly drone" to assign a pilot. "Maintain drone" is used to refuel drones, which is realized in backend but not offered in the frontend yet.
		"Orders": display all the orders in specific stores with buttons of "add order", "view details" to show items ordered or add more and check out or cancel order, and "reassign drone" to assign another drone to the order, which is realized in backend but not offered in the frontend yet. There are also buttons to show current or archived orders.
	"Pilots": display the pilot list with "add pilot" button to add pilots.
	"Customers": display the customer list with "add customer" button and buttons to show current or archived data. There is also "sort" to sort customers by rating descending or ascending order. Each customer also has a "change rating" button to manually change the ratings.
	"Setting": display to authorized users the settings that can be changed including, archiving criteria, rating policy, drone assignment policy, and authorization settings. The "update settings" is also implemented in backend and not offered in the frontend yet.

#### Register or login through command-line
Enter the following to only run the backend for command-line register or login:
`docker-compose run grocery` 

It should show
```console
Please log in or register...
```
Input format: (for userType: enter 1 for adminUser, 2 for employees, 3 for customers) 
`register,yourUsername,yourPassword,yourUserType`  
or  
`logIn,yourUsername,yourPassword,yourUserType`  

If you successfully logged in, it will show
```console
Log in successfully!
```
Then proceed to the browser and visit [http://localhost:4201] to view the application.


## Functions implemented on the backend but not shown on the frontend:
(refer to "/cs6310_fa21_team037/src/main/java/edu/gatech/cs6310/groceryexpress/Controller" for details)

### 1. Maintain drone
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

### 2. Manually reassign drone:
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

### 3. Auto reassign drone
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

### 4. Auto reassign customer rating
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

### 5. Update setting criteria
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