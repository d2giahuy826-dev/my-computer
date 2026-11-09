// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Node {

    Device info;
    Node next;

    Node() {
    }

    Node(Device x, Node p) {
        info = x;
        next = p;
    }

    Node(Device x) {
        this(x, null);
    }
}
