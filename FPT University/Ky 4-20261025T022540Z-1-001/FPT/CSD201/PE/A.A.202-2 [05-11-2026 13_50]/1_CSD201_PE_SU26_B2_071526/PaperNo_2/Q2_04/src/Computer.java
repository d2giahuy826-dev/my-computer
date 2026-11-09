// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Computer {

    String model;
    int ram, slots, code;


    Computer() {
    }

    Computer(String model, int ram, int slots, int code) {
        this.model = model;
        this.ram = ram;
        this.slots = slots;
        this.code = code;
    }

    public String toString() {
        return ("(" + model + "," + ram + "," + slots + "," + code + ")");
    }
}
