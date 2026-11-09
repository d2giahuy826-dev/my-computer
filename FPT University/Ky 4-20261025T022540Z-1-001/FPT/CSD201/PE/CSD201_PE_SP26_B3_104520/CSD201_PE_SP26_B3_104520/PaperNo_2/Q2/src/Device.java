// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Device {

    String brand;
    int price, type, serialNo;


    Device() {
    }

    Device(String brand, int price, int type, int serialNo) {
        this.brand = brand;
        this.price = price;
        this.type = type;
        this.serialNo = serialNo;
    }

    public String toString() {
        return ("(" + brand + "," + price + "," + type + "," + serialNo + ")");
    }
}
