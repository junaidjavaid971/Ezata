package app.com.ezata.model;

public class OnRouteOrders {
    String orderNumber;
    String courierName;
    int vehicle;
    String deliveryTime;

    public OnRouteOrders(String orderNumber, String courierName, int vehicle, String deliveryTime) {
        this.orderNumber = orderNumber;
        this.courierName = courierName;
        this.vehicle = vehicle;
        this.deliveryTime = deliveryTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public int getVehicle() {
        return vehicle;
    }

    public void setVehicle(int vehicle) {
        this.vehicle = vehicle;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
