public class Customer {
    String id;
    String name;
    int priority;

    public Customer(String id, String name, int priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public String toString() {
        return id + " " + name + " " + priority;
    }
}