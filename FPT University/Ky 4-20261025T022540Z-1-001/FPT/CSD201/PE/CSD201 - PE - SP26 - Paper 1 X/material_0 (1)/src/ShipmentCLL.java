import java.io.RandomAccessFile;

public class ShipmentCLL {

    class Node {
        Fruit info;
        Node next;

        Node(Fruit x) {
            info = x;
            next = null;
        }
    }

    Node head;

    public ShipmentCLL() {
        head = null;
    }

    public void addToShipment(Fruit fruit) {
        Node newNode = new Node(fruit);
        if (head == null) {
            head = newNode;
            head.next = head;
            return;
        }
        Node p = head;
        while (p.next != head) {
            p = p.next;
        }
        p.next = newNode;
        newNode.next = head;
    }

    public int countFruits() {
        int count = 0;
        // ---------- Student's code starts from here ----------
        // Students are welcomed to use any helper function(s)
        if (head == null) {
            return 0;
        }
        Node cur = head;
        do {
            count++;
            cur = cur.next;
        } while (cur != head);
        // -----------------------------------------------------
        return count;
    }
    
    public void removeById(String id) {
        // ---------- Student's code starts from here ----------
        // Students are welcomed to use any helper function(s)
        if (head == null || id == null) {
            return;
        }
        // only one node
        if (head.next == head) {
            if (head.info.fruitId.equals(id)) {
                head = null;
            }
            return;
        }
        // prev starts at the last node
        Node prev = head;
        while (prev.next != head) {
            prev = prev.next;
        }
        Node cur = head;
        do {
            if (cur.info.fruitId.equals(id)) {
                if (cur == head) {
                    head = head.next;
                    prev.next = head;
                } else {
                    prev.next = cur.next;
                }
                return;
            }
            prev = cur;
            cur = cur.next;
        } while (cur != head);
        // -----------------------------------------------------
    }

    public void ftraverse(RandomAccessFile f) throws Exception {
        if (head == null) {
            f.writeBytes("Empty\r\n");
            return;
        }
        Node cur = head;
        do {
            f.writeBytes("(" + cur.info.fruitId + "," + cur.info.name + "," + cur.info.quantity + "," + cur.info.price + ")");
            cur = cur.next;
        } while (cur != head);
    }
}