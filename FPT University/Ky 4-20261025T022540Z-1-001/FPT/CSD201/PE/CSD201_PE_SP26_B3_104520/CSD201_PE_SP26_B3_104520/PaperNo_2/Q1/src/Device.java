// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Device {

    String brand;
    int price, type;

    Device() {
    }

    Device(String brand, int price, int type) {
        this.brand = brand;
        this.price = price;
        this.type = type;
    }

    public String toString() {
        return ("(" + brand + "," + price + "," + type + ")");
    }
}
