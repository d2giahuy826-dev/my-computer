// =========== DO NOT EDIT THE GIVEN CONTENT OF THIS FILE ============

class Node {

    Student info;
    Node next;

    Node() {
    }

    Node(Student x, Node p) {
        info = x;
        next = p;
    }

    Node(Student x) {
        this(x, null);
    }
}
