package app.com.ezata.model;

public class NewOrder {
    String orderNumber;
    String customerName;
    String orderTime;
    String orderCount;

    public NewOrder(String orderNumber, String customerName, String orderTime, String orderCount) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.orderTime = orderTime;
        this.orderCount = orderCount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }
}
