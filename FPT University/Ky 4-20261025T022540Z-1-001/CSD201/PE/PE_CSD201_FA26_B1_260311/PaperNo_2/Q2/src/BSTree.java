/* This program contains 2 parts: (1) and (2)
   YOUR TASK IS TO COMPLETE THE PART  (2)  ONLY
 */
//(1)==============================================================
import java.io.*;
import java.util.*;

public class BSTree {

    Node root;

    BSTree() {
        root = null;
    }

    boolean isEmpty() {
        return (root == null);
    }

    void clear() {
        root = null;
    }

    void visit(Node p) {
        System.out.print("p.info: ");
        if (p != null) {
            System.out.println(p.info + " ");
        }
    }

    void fvisit(Node p, RandomAccessFile f) throws Exception {
        if (p != null) {
            f.writeBytes(p.info + " ");
        }
    }

    void breadth(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        Queue q = new Queue();
        q.enqueue(p);
        Node r;
        while (!q.isEmpty()) {
            r = q.dequeue();
            fvisit(r, f);
            if (r.left != null) {
                q.enqueue(r.left);
            }
            if (r.right != null) {
                q.enqueue(r.right);
            }
        }
    }

    void preOrder(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        fvisit(p, f);
        preOrder(p.left, f);
        preOrder(p.right, f);
    }

    void inOrder(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        inOrder(p.left, f);
        fvisit(p, f);
        inOrder(p.right, f);
    }

    void postOrder(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        postOrder(p.left, f);
        postOrder(p.right, f);
        fvisit(p, f);
    }

    void loadData(int k) { // Do not edit this function.
        int[] ids = Lib.readLineToIntArray("data.txt", k);
        int[] capacities = Lib.readLineToIntArray("data.txt", k + 1);
        int[] occupied = Lib.readLineToIntArray("data.txt", k + 2);
        for (int i = 0; i < ids.length; i++) {
            insert(ids[i], capacities[i], occupied[i]);
        }
    }

//===========================================================================
//(2)===YOU CAN EDIT OR ADD NEW FUNCTIONS IN THE FOLLOWING PART ONLY===========
//===========================================================================
    void insert(int id, int capacity, int occupied) {
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */
        
          if (id <= 0 || capacity <= 0  || occupied < 0 || occupied > capacity) {
            return;
        }
        Shelter d = new Shelter( id, capacity, occupied);
        root = insertNode(root, d);
    }

    Node insertNode(Node r, Shelter s) {
        if (r == null) {
            return new Node(s);
        }
        int cmp = Integer.compare(s.id, r.info.id);
        if (cmp < 0) {
            r.left = insertNode(r.left, s);
        } else if (cmp > 0) {
            r.right = insertNode(r.right, s);
        }
        // cmp == 0: model da co -> khong lam gi
        return r;
        // END STUDENT CODE
    }
    // BEGIN OPTIONAL HELPERS
    // END OPTIONAL HELPERS
    void f1() throws Exception {
        clear();
        loadData(1);
        String fname = "f1.txt";
        File g123 = new File(fname);
        if (g123.exists()) g123.delete();
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        breadth(root, f);
        f.writeBytes("\r\n");
        // Do not edit f1. Complete insert above only.
        inOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

    void f2() throws Exception {
        clear();
        loadData(5);
        String fname = "f2.txt";
        File g123 = new File(fname);
        if (g123.exists()) g123.delete();
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        breadth(root, f);
        f.writeBytes("\r\n");
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */
         
        
        
        // END STUDENT CODE
        f.writeBytes("\r\n");
        f.close();
    }

    void f3() throws Exception {
        clear();
        loadData(9);
        String fname = "f3.txt";
        File g123 = new File(fname);
        if (g123.exists()) g123.delete();
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        postOrder(root, f);
        f.writeBytes("\r\n");
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */
        // END STUDENT CODE
        postOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

    void f4() throws Exception {
        clear();
        loadData(13);
        String fname = "f4.txt";
        File g123 = new File(fname);
        if (g123.exists()) g123.delete();
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        preOrder(root, f);
        f.writeBytes("\r\n");
        // BEGIN STUDENT CODE
        /* Keep all pre-given statements in this function.
           Insert your solution here. You may add helpers in PART (2). */
        // END STUDENT CODE
        preOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

}
