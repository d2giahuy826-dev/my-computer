/* This program contains 2 parts: (1) and (2)
   YOUR TASK IS TO COMPLETE THE PART  (2)  ONLY
 */
//(1)==============================================================
import java.util.*;
import java.io.*;

public class MyList {

    Node head, tail;

    MyList() {
        head = tail = null;
    }

    boolean isEmpty() {
        return (head == null);
    }

    void clear() {
        head = tail = null;
    }

    void fvisit(Node p, RandomAccessFile f) throws Exception {
        if (p != null) {
            f.writeBytes(p.info + " ");
        }
    }

    void ftraverse(RandomAccessFile f) throws Exception {
        Node p = head;
        while (p != null) {
            fvisit(p, f); // You will use this statement to write information of the node p to the file
            p = p.next;
        }
        f.writeBytes("\r\n");
    }

    void loadData(int k) { //do not edit this function
        String[] a = Lib.readLineToStrArray("data.txt", k);
        int[] b = Lib.readLineToIntArray("data.txt", k + 1);
        int[] c = Lib.readLineToIntArray("data.txt", k + 2);
        int n = a.length;
        for (int i = 0; i < n; i++) {
            addLast(a[i], b[i], c[i]);
        }
    }

//===========================================================================
//(2)===YOU CAN EDIT OR ADD NEW FUNCTIONS IN THE FOLLOWING PART ONLY===========
//===========================================================================
    void addLast(String tag, int urgency, int waiting) {
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */
        // END STUDENT CODE
        if (urgency < 1 || urgency > 5 || waiting < 0) {
            return;
        }
        Node q = new Node(new Animal(tag, urgency, waiting));
        if (head == null) {
            head = tail = q;
        } else {
            tail.next = q;
            tail = q;
        }
    }

    // BEGIN OPTIONAL HELPERS
    // END OPTIONAL HELPERS
    void f1() throws Exception {
        clear();
        loadData(1);
        String fname = "f1.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        // Do not edit f1. Complete addLast above only.
        f.close();
    }

    void f2() throws Exception {
        clear();
        loadData(5);
        String fname = "f2.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        Integer count = 0;
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */

        Node p = head;
        while (p != null) {
            if (p.info.urgency >= 4 && p.info.waiting >= 3) {
                count++;
            }
            p = p.next;
        }

        // END STUDENT CODE
        f.writeBytes(count.toString());
        f.writeBytes("\r\n");
        f.close();
    }

    void f3() throws Exception {
        clear();
        loadData(9);
        String fname = "f3.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */

        int max = maxType();
        int count = 0;
        Node p = head;
        while (p != null) {
            if (p.info.urgency == max) {
                count++;
                if (count == 3) {
                    remove(p);
                }
            }
            p = p.next;
        }

        // END STUDENT CODE
        ftraverse(f);
        f.close();
    }

    void removeFirst() {
        if (isEmpty()) {
            return;
        }
        head = head.next;
        if (head == null) {
            tail = null;
        }
    }

    void remove(Node q) {
        if (isEmpty() || q == null) {
            return;
        }
        if (q == head) {
            removeFirst();
            return;
        }
        Node f = head;
        while (f != null && f.next != q) {
            f = f.next;
        }
        if (f == null) {
            return; // q is not in the list
        }
        Node q1 = q.next;
        f.next = q1;
        if (f.next == null) {
            tail = f;
        }
    }

    int maxType() {
        Node p = head;
        int max = p.info.urgency;
        while (p != null) {
            if (p.info.urgency > max) {
                max = p.info.urgency;
            }
            p = p.next;
        }
        return max;
    }

    void f4() throws Exception {
        clear();
        loadData(13);
        String fname = "f4.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        ftraverse(f);
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */

      
        

        // END STUDENT CODE
        ftraverse(f);
        f.close();
    }

}
