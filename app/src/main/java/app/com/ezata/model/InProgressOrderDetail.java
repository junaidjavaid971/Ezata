package app.com.ezata.model;

public class InProgressOrderDetail {
    String quantity;
    String items;
    String price;
    String instruction;

    public InProgressOrderDetail(String quantity, String items, String price, String instruction) {
        this.quantity = quantity;
        this.items = items;
        this.price = price;
        this.instruction=instruction;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
