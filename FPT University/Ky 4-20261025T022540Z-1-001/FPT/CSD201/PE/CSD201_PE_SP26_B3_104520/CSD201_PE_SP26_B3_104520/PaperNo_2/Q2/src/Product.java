// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Product {

    String name;
    int price, sku;

    Product() {
    }

    Product(String name, int price, int sku) {
        this.name = name;
        this.price = price;
        this.sku = sku;
    }

    public String toString() {
        return ("(" + sku + "," + name + "," + price + ")");
    }
}
