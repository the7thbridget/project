package edu.gatech.cs6310.groceryexpress.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.gatech.cs6310.groceryexpress.DAO.*;
import edu.gatech.cs6310.groceryexpress.POJO.Order;
import edu.gatech.cs6310.groceryexpress.common.*;
import edu.gatech.cs6310.groceryexpress.common.exception.*;
import edu.gatech.cs6310.groceryexpress.service.DroneService;
import edu.gatech.cs6310.groceryexpress.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String DRONE_ID = "drone_id";
    private static final String DRONE_IDENTIFIER = "drone_identifier";
    private static final String USER_ID = "user_id";
    private static final String ORDER_ID = "order_id";
    private static final String PILOT_ID = "pilot_id";
    private static final String REMAINING_CAP = "remaining_cap";
    private static final String UNIT_WEIGHT = "unit_weight";
    private static final String UNIT_PRICE = "unit_price";
    private static final String STATUS = "status";


    @Autowired
    OrderDAO orderDAO;

    @Autowired
    StoreDAO storeDAO;

    @Autowired
    DroneDAO droneDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    PilotDAO pilotDAO;

    @Autowired
    DroneService droneService;

    @Autowired
    ArchivedOrderDAO archivedOrderDAO;

    @Autowired
    SettingCriteriaDAO settingCriteriaDAO;

    @Autowired
    CommonInfoHelper commonInfoHelper;

    @Override
    public R addOrder(Order order) {
        try {
            String storeName = order.getStoreName();
            String orderName = order.getOrderName();
            String droneIdentifier = order.getDroneIdentifier();
            String customerUsername = order.getCustomerUsername();
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            Integer droneId = getDroneIdByStoreIdAndDroneName(storeId, droneIdentifier);
            Integer customerID = getCustomerIdByUsername(customerUsername);
            List<Map<String, Object>> orderByStoreNameAndOrderName = orderDAO.findOrderByStoreNameAndOrderName(storeName, orderName);
            if (!orderByStoreNameAndOrderName.isEmpty()) return R.error(ErrorCode.ORDER_DUPLICATE.getErrorCode(), ErrorCode.ORDER_DUPLICATE.getErrorMsg());
            boolean res = orderDAO.addOrder(droneId, customerID, orderName, OrderStatusCode.PENDING.getOrderStatusCode());
            return res ? R.ok("Successfully started an order!") : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        } catch (StoreNotFoundException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        } catch (DroneNotFoundException e) {
            return R.error(ErrorCode.DRONE_NOT_FOUND.getErrorCode(), ErrorCode.DRONE_NOT_FOUND.getErrorMsg());
        } catch (UserNotFoundException e) {
            return R.error(ErrorCode.CUSTOMER_NOT_FOUND.getErrorCode(), ErrorCode.CUSTOMER_NOT_FOUND.getErrorMsg());
        } catch (JsonProcessingException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        }
    }

    @Override
    public R displayOrderByStoreName(String storeName) {
        try {
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            Map<String, List<Map<String, Object>>> allOrders = new HashMap<>();
            List<Map<String, Object>> droneList = droneDAO.findDroneByStoreId(storeId);
            List<Map<String, Object>> orderList = new ArrayList<>();
            for (Map<String, Object> drone : droneList) {
                Integer droneId = (Integer) drone.get(DRONE_ID);
                List<Map<String, Object>> orderForCurrDroneId = orderDAO.findAllOrderByDroneId(droneId);
                for (Map<String, Object> eachOrder : orderForCurrDroneId) {
                    Integer orderId = (Integer) eachOrder.get(ORDER_ID);
                    List<Map<String, Object>> items = orderDAO.findItemsByOrderId(orderId);
                    eachOrder.put("items", items);
                }
                orderList.addAll(orderForCurrDroneId);
            }
            allOrders.put("currentOrders", orderList);
            allOrders.put("archivedOrders", this.getArchivedOrder(storeName));
            return R.ok().put("orders", allOrders);
        } catch (StoreNotFoundException | JsonProcessingException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        } 
    }


    @Override
    public R requestItem(String storeName, String orderName, String itemName, Integer quantity, Integer unitPrice) {
        try {

            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            Boolean res = new Boolean(null);
            List<Map<String, Object>> order = orderDAO.findOrderByStoreNameAndOrderName(storeName, orderName);
            if (order.isEmpty())
                return R.error(ErrorCode.ORDER_NOT_FOUND.getErrorCode(), ErrorCode.ORDER_NOT_FOUND.getErrorMsg());
            Integer orderId = (Integer) order.get(0).get(ORDER_ID);
            Integer itemId = getItemId(storeId, itemName);
            List<Map<String, Object>> itemOnOrderByItemIdAndOrderId = orderDAO.findItemOnOrderByItemIdAndOrderId(itemId, orderId);
            if (!itemOnOrderByItemIdAndOrderId.isEmpty() && !itemOnOrderByItemIdAndOrderId.get(0).get(UNIT_PRICE).equals(unitPrice)) {
                return R.error(ErrorCode.ITEM_ALREADY_ORDERED.getErrorCode(), ErrorCode.ITEM_ALREADY_ORDERED.getErrorMsg());
            }
//            Integer currOrderCredit = ((Integer) orderDAO.findOrderCredit(orderId).get(0).get("credit")) == null ? 0 : (Integer) orderDAO.findOrderCredit(orderId).get(0).get("credit");
//            Integer currWeight = getOrderWeight(orderId) == null ? 0 : getOrderWeight(orderId);
            Integer droneId = (Integer) order.get(0).get(DRONE_ID);
            Integer unitWeight = (Integer) itemDAO.findItemByStoreIdAndItemName(storeId, itemName).get(0).get(UNIT_WEIGHT);
            Integer remainingCapacity = findRemainingCapacity(droneId);
            Integer userId = (Integer) order.get(0).get(USER_ID);
//            Integer remainingCredit = getOrderCredit(orderId);
            Integer totalCustomerCredit = (Integer) customerDAO.getCreditByUserId(userId).get(0).get("credit");
            List<Integer> ordersByUserId = getOrderIdByUserId(userId);
            Integer totalCurrCredit = 0;
            for (Integer eachOrderId : ordersByUserId) {
                totalCurrCredit += getOrderCredit(eachOrderId);
            }
            Integer remainingCredit = totalCustomerCredit - totalCurrCredit;
            if (quantity * unitPrice > remainingCredit)
                return R.error(ErrorCode.NOT_ENOUGH_CREDIT.getErrorCode(), ErrorCode.NOT_ENOUGH_CREDIT.getErrorMsg());
            if (unitWeight * quantity > remainingCapacity)
                return R.error(ErrorCode.NOT_ENOUGH_CAPACITY.getErrorCode(), ErrorCode.NOT_ENOUGH_CAPACITY.getErrorMsg());
            if (!itemOnOrderByItemIdAndOrderId.isEmpty()) {
                res = orderDAO.updateItemQuantity(orderId, itemId, quantity);
            } else {
                res = orderDAO.requestItem(orderId, itemId, quantity, unitPrice);
            }
            return res ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), "Unable to add the item!");
        } catch (StoreNotFoundException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        } catch (ItemNotFoundException e) {
            return R.error(ErrorCode.ITEM_NOT_FOUND.getErrorCode(),  ErrorCode.ITEM_NOT_FOUND.getErrorMsg());
        } catch (JsonProcessingException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        }
    }

    @Override
    @Transactional
    public R purchaseOrder(String storeName, String orderName) {
        try {
            Integer storeId = commonInfoHelper.getStoreIdFromStoreName(storeName);
            List<Map<String, Object>> orders = orderDAO.findOrderByStoreNameAndOrderName(storeName, orderName);
            if (orders.isEmpty())
                return R.error(ErrorCode.ORDER_NOT_FOUND.getErrorCode(), ErrorCode.ORDER_NOT_FOUND.getErrorMsg());
            Map<String, Object> order = orders.get(0);
            if (order.get(STATUS).equals(OrderStatusCode.COMPLETE.getOrderStatusCode()))
                return R.error(ErrorCode.ORDER_ALREADY_COMPLETE.getErrorCode(), ErrorCode.ORDER_ALREADY_COMPLETE.getErrorMsg());
            Integer orderId = (Integer) order.get(ORDER_ID);
            Integer customerId = (Integer) order.get(USER_ID);
            Integer droneId = (Integer) order.get(DRONE_ID);
            // Change order status
            boolean res = droneDAO.updateRemainingTrip(droneId);
            if (!res) return R.error(ErrorCode.DRONE_NOT_HAVE_ENOUGH_TRIPS.getErrorCode(),  ErrorCode.DRONE_NOT_HAVE_ENOUGH_TRIPS.getErrorMsg());
            Integer pilotId = getPilotIdByDroneId(droneId);
            res = res && pilotDAO.updateExperience(pilotId);
            if (!res) return R.error(ErrorCode.DRONE_NEEDS_PILOT.getErrorCode(), ErrorCode.DRONE_NEEDS_PILOT.getErrorMsg());
            // Customer deduce cost
            res = res && customerDAO.updateCredit(customerId, orderId);
            // Drone reduce trip
            res = res && orderDAO.changeOrderStatus(orderId);
            // Pilot add experience
            Integer currOrderCredit = getOrderCredit(orderId);
            res = res && storeDAO.updateRevenue(storeId, currOrderCredit);
            return res ? R.ok() : R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        } catch (OrderNotFoundException e) {
            return R.error(ErrorCode.ORDER_NOT_FOUND.getErrorCode(), ErrorCode.ORDER_NOT_FOUND.getErrorMsg());
        } catch (StoreNotFoundException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        } catch (JsonProcessingException e) {
            return R.error(ErrorCode.UNKNOWN_ERROR.getErrorCode(), ErrorCode.UNKNOWN_ERROR.getErrorMsg());
        }
    }

    @Override
    public R cancelOrder(String storeName, String orderName) {
        try {
            Integer storeId  = commonInfoHelper.getStoreIdFromStoreName(storeName);
            List<Map<String, Object>> orders = orderDAO.findOrderByStoreNameAndOrderName(storeName, orderName);
            if (orders.isEmpty())
                return R.error(ErrorCode.ORDER_NOT_FOUND.getErrorCode(), ErrorCode.ORDER_NOT_FOUND.getErrorMsg());
            Map<String, Object> order = orders.get(0);
            if (!order.get(STATUS).equals(OrderStatusCode.PENDING.getOrderStatusCode())) return R.error(ErrorCode.UNABLE_TO_CANCEL_ORDER.getErrorCode(), ErrorCode.UNABLE_TO_CANCEL_ORDER.getErrorMsg());
            Integer orderId = (Integer) order.get(ORDER_ID);
            boolean res = orderDAO.cancelOrderFromOrder(orderId);
            return res ? R.ok() : R.error(ErrorCode.UNABLE_TO_CANCEL_ORDER.getErrorCode(), ErrorCode.UNABLE_TO_CANCEL_ORDER.getErrorMsg());
        } catch (StoreNotFoundException | JsonProcessingException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        }
    }

    @Override
    public R showCustomerOrder(String username, String storeName) {
        try {
            Map<String, List<Map<String, Object>>> allCustomerOrders = new HashMap<>();
            List<Map<String, Object>> archived = new ArrayList<>();
            Integer customerId = getCustomerIdByUsername(username);
            Integer storeId = getStoreId(storeName);
            List<Map<String, Object>> orderList = orderDAO.showCustomerOrder(customerId, storeId);
            for (Map<String, Object> eachOrder : orderList) {
                Integer orderId = (Integer) eachOrder.get(ORDER_ID);
                List<Map<String, Object>> items = orderDAO.findItemsByOrderId(orderId);
                eachOrder.put("items", items);
            }
            allCustomerOrders.put("currentOrders", orderList);
            allCustomerOrders.put("archivedOrders", archived);
            return R.ok().put("orders", allCustomerOrders);
        } catch (UserNotFoundException e) {
            return R.error(ErrorCode.USER_NOT_FOUND.getErrorCode(), ErrorCode.USER_NOT_FOUND.getErrorMsg());
        } catch (StoreNotFoundException e) {
            return R.error(ErrorCode.STORE_NOT_FOUND.getErrorCode(), ErrorCode.STORE_NOT_FOUND.getErrorMsg());
        }

    }

    private List<Map<String, Object>> getArchivedOrder(String storeName) {
        Integer storeId = getStoreId(storeName);
        List<Map<String, Object>> droneList = droneDAO.findDroneByStoreId(storeId);
        List<Map<String, Object>> orderList = new ArrayList<>();
        for (Map<String, Object> drone : droneList) {
            Integer droneId = (Integer) drone.get(DRONE_ID);
            List<Map<String, Object>> orderForCurrDroneId = archivedOrderDAO.findArchivedOrderByDroneId(droneId);
            for (Map<String, Object> eachOrder : orderForCurrDroneId) {
                Integer orderId = (Integer) eachOrder.get(ORDER_ID);
                List<Map<String, Object>> items = archivedOrderDAO.findArchivedOrderItemsByOrderId(orderId);
                eachOrder.put("items", items);
            }
            orderList.addAll(orderForCurrDroneId);
        }
        return orderList;
    }

    private Integer getStoreId(String storeName) {
        List<Map<String, Object>> res = storeDAO.findStoreIdByName(storeName);
        if (res.isEmpty()) throw new StoreNotFoundException("Store name not found!");
        return (Integer) res.get(0).get("store_id");
    }


    private Integer getDroneIdByStoreIdAndDroneName(Integer storeId, String droneIdentifier) {
        List<Map<String, Object>> droneList = droneDAO.findDroneByStoreId(storeId);
        for (Map<String, Object> drone : droneList) {
            if (drone.get(DRONE_IDENTIFIER).equals(droneIdentifier)) return (Integer) drone.get(DRONE_ID);
        }
        throw new DroneNotFoundException();
    }


    private Integer getCustomerIdByUsername(String username) {
        List<Map<String, Object>> customerList = userDAO.findUserIdByUsernameAndType(username, UserTypeEnum.CUSTOMER.getUserTypeCode());
        if (customerList.isEmpty()) throw new UserNotFoundException();
        if (customerList.size() > 1) throw new DuplicateUserException();
        return (Integer) customerList.get(0).get(USER_ID);
    }


    private Integer getOrderWeight(Integer orderId) {
        try {
            List<Map<String, Object>> res = orderDAO.findOrderWeight(orderId);
            if (res.isEmpty()) throw new OrderNotFoundException("Order not found!");
            return (Integer) res.get(0).get("order_weight");
        } catch (DataAccessException e) {
            throw new DataConnectionException();
        }
    }

    private Integer getOrderCredit(Integer orderId) {
        try {
            List<Map<String, Object>> res = orderDAO.findOrderCredit(orderId);
            if (res.isEmpty()) throw new OrderNotFoundException("Order not found!");
            if (res.get(0).get("order_credit") == null) {
                return 0;
            } else return ((BigDecimal) res.get(0).get("order_credit")).intValue();
        } catch (DataAccessException e) {
            throw new DataConnectionException();
        }
    }


    private Integer getItemId(Integer storeId, String itemName) {
            List<Map<String, Object>> res = itemDAO.findItemByStoreIdAndItemName(storeId, itemName);
            if (res.isEmpty()) throw new ItemNotFoundException("Item not found!");
            return (Integer) res.get(0).get("item_id");
    }

    private Integer findRemainingCapacity(Integer droneId) {
        try {
            List<Map<String, Object>> res = droneDAO.findRemainingCapacity(droneId, OrderStatusCode.PENDING.getOrderStatusCode());
            if (res.isEmpty()) throw new DroneNotFoundException("Drone not found!");

            return ((BigDecimal) res.get(0).get(REMAINING_CAP)).intValue();
        } catch (DataAccessException e) {
            throw new DataConnectionException();
        }
    }


    private Integer getPilotIdByDroneId(Integer droneId) {
        List<Map<String, Object>> pilotList = droneDAO.findPilotId(droneId);
        if (pilotList.isEmpty()) throw new PilotNotFoundException();
        Map<String, Object> pilot = pilotList.get(0);
        return (Integer) pilot.get(PILOT_ID);
    }


    private List<Integer> getOrderIdByUserId(Integer userId) {
        List<Map<String, Object>> orders = customerDAO.findOrderByUserId(userId);
        List<Integer> ans = new ArrayList<>();
        for (Map<String, Object> order : orders) {
            if (!((Integer)order.get(STATUS)).equals(OrderStatusCode.PENDING.getOrderStatusCode())) continue;
            Integer orderId = (Integer) order.get(ORDER_ID);
            ans.add(orderId);
        }
        return ans;
    }

    @Override
    public R manualReassignDroneToOrder(Integer orderId, Integer droneId, String droneIdentifier) {
        // Change the drone that was assigned to the pending order
        // Check order is not delivered

        // check droneidentifier is available in the specified store
        // check drone has enough remaining capacity and fuel for the current order and pilot available
        // Pilot? Whether drone is assigned

        try {
            List<Map<String, Object>> orderWeightRes = orderDAO.findOrderWeight(orderId);
            if (orderWeightRes.isEmpty()) throw new OrderNotFoundException("Order not found!");
            Integer orderWeight = ((BigDecimal) orderWeightRes.get(0).get("order_weight")).intValue();

            List<Map<String, Object>> droneIdRes = droneDAO.findDroneByDroneIdAndDroneIdentifier(droneId, droneIdentifier);
            if (droneIdRes.isEmpty()) throw new DroneNotFoundException("Drone not found!");
            Integer newDroneId = (Integer) droneIdRes.get(0).get("drone_id");
            Integer pilotId = getPilotIdByDroneId(droneId);

            List<Map<String, Object>> newDroneRemainingCapRes = droneDAO.findRemainingCapacity(newDroneId, OrderStatusCode.PENDING.getOrderStatusCode());
            if (newDroneRemainingCapRes.isEmpty()) throw new DroneNotFoundException("Drone not found!");
            Integer newDroneRemainingCap = ((BigDecimal) newDroneRemainingCapRes.get(0).get("remaining_cap")).intValue();

            if (orderWeight <= newDroneRemainingCap) {
                orderDAO.assignDroneToOrder(newDroneId, orderId);
//                droneDAO.updateRemainingTrip(droneId);
//                droneDAO.updateRemainingCapacity(droneId, orderId, OrderStatusCode.PENDING.getOrderStatusCode());
                return R.ok();
            }
            return R.error(ErrorCode.NOT_ENOUGH_CAPACITY.getErrorCode(), ErrorCode.NOT_ENOUGH_CAPACITY.getErrorMsg());
        } catch (OrderNotFoundException e) {
            return R.error(ErrorCode.ORDER_NOT_FOUND.getErrorCode(), ErrorCode.ORDER_NOT_FOUND.getErrorMsg());
        }
        catch (DroneNotFoundException e) {
            return R.error(ErrorCode.DRONE_NOT_FOUND.getErrorCode(), ErrorCode.DRONE_NOT_FOUND.getErrorMsg());
        }
    }

    @Override
    public R autoReassignDroneToOrder(Integer orderId, Integer droneId) {
        try {
            List<Map<String, Object>> orderWeightRes = orderDAO.findOrderWeight(orderId);
            if (orderWeightRes.isEmpty()) throw new OrderNotFoundException("Order not found!");
            Integer orderWeight = ((BigDecimal) orderWeightRes.get(0).get("order_weight")).intValue();

            List<Map<String, Object>> droneIdRes = new ArrayList<>();
            List<Map<String, Object>> droneAssignRuleRes = settingCriteriaDAO.findDroneAssignRule();
            if (droneAssignRuleRes.isEmpty()) throw new OrderNotFoundException("Drone assign rule for order not found!");
            Integer assignRule = (Integer) droneAssignRuleRes.get(0).get("drone_assign_rule");
            switch (assignRule) {
                case 1: {
                    droneIdRes = droneDAO.recommendDroneByCapacity(droneId, orderWeight, OrderStatusCode.PENDING.getOrderStatusCode());
                    if (droneIdRes.isEmpty()) throw new DroneNotFoundException("No drone satisfies the capacity first requirement.");
                    break;
                }
                case 2: {
                    droneIdRes = droneDAO.recommendDroneByTrip(droneId, orderWeight, OrderStatusCode.PENDING.getOrderStatusCode());
                    if (droneIdRes.isEmpty()) throw new DroneNotFoundException("No drone satisfies the fuel first requirement.");
                    break;
                }
            }

            Integer newDroneId = (Integer) droneIdRes.get(0).get("drone_id");

            boolean res = orderDAO.assignDroneToOrder(newDroneId, orderId);
            return res ? R.ok() : R.error(ErrorCode.NOT_ENOUGH_CAPACITY.getErrorCode(), ErrorCode.NOT_ENOUGH_CAPACITY.getErrorMsg());
        } catch (DroneNotFoundException e) {
            return R.error(ErrorCode.DRONE_NOT_FOUND.getErrorCode(), ErrorCode.DRONE_NOT_FOUND.getErrorMsg());
        }
    }
}
