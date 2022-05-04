package app.com.ezata.model;

public class PickUpReady {
    String orderNumber;
    String customerName;
    int vehicleId;
    String courierName;
    String orderTime;
    String pickUpTime;
    String pinCode;

    public PickUpReady(String orderNumber, String customerName, int vehicleId, String courierName, String orderTime, String pickUpTime, String pinCode) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.vehicleId = vehicleId;
        this.courierName = courierName;
        this.orderTime = orderTime;
        this.pickUpTime = pickUpTime;
        this.pinCode = pinCode;
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

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
