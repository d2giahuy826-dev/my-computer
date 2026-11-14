import java.io.RandomAccessFile;

public class WaitingQueue {
    class Node {
        Customer data;
        Node next;
        Node(Customer data) {
            this.data = data;
        }
    }

    Node front, rear;

    public void ftraverse(RandomAccessFile f) throws Exception {
        if (front == null) {
            f.writeBytes("Empty\r\n");
            return;
        }
        Node cur = front;
        while (cur != null) {
            f.writeBytes("(" + cur.data.id + "," + cur.data.name + "," + cur.data.priority + ")");
            cur = cur.next;
        }
    }

    public void enqueue(Customer x){
        Node p = new Node(x);
        if (front == null) {
            front = rear = p;
        } else {
            rear.next = p;
            rear = p;
        }
    }

    public Customer front() {
        if (front == null)
        return null;
        return front.data;
    }

    public Customer dequeue() {
        if (front == null)
        return null;
        Customer x = front.data;
        front = front.next;
        if (front == null) rear = null;
        return x;
    }
}