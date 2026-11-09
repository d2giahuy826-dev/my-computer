// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Product {

    String name;
    int quantity, price;

    Product() {
    }

    Product(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        return ("(" + name + "," + quantity + "," + price + ")");
    }
}
