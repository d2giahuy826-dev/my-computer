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

    void loadData(int k) { //do not edit this function
        String[] a = Lib.readLineToStrArray("data.txt", k);
        int[] b = Lib.readLineToIntArray("data.txt", k + 1);
        int[] c = Lib.readLineToIntArray("data.txt", k + 2);
        int[] d = Lib.readLineToIntArray("data.txt", k + 3);
        int n = a.length;
        for (int i = 0; i < n; i++) {
            insert(a[i], b[i], c[i], d[i]);
        }
    }

//===========================================================================
//(2)===YOU CAN EDIT OR EVEN ADD NEW FUNCTIONS IN THE FOLLOWING PART========
//===========================================================================
    void insert(String model, int ram, int slots, int code) {
        if (ram <= 0 || code <= 0) {
            return;
        }
        Computer d = new Computer(model, ram, slots, code);
        root = insertNode(root, d);
    }

    Node insertNode(Node r, Computer c) {
        if (r == null) {
            return new Node(c);
        }
        int cmp = c.model.compareTo(r.info.model);
        if (cmp < 0) {
            r.left = insertNode(r.left, c);
        } else if (cmp > 0) {
            r.right = insertNode(r.right, c);
        }
        // cmp == 0: model da co -> khong lam gi
        return r;
    }

//Do not edit this function. Your task is to complete insert function above only.
    void f1() throws Exception {
        clear();
        loadData(1);
        String fname = "f1.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        breadth(root, f);
        f.writeBytes("\r\n");
        inOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

    void preOrder2(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        if (p.info.slots < 10) {
            fvisit(p, f);
        }
        preOrder2(p.left, f);
        preOrder2(p.right, f);
    }
//=============================================================

    void f2() throws Exception {
        clear();
        loadData(6);
        String fname = "f2.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        breadth(root, f);
        f.writeBytes("\r\n");
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
      Your task is to insert statements here, just after this comment,
      to complete the question in the exam paper.*/
         if (root != null) {
            Queue q = new Queue();
            q.enqueue(root);
            Node r;
            while (!q.isEmpty()) {
                r = q.dequeue();
                if (r.left == null && r.right == null) {
                    fvisit(r, f);
                }
                if (r.left != null) q.enqueue(r.left);
                if (r.right != null) q.enqueue(r.right);
            }
        }
        //------------------------------------------------------------------------------------
        f.writeBytes("\r\n");
        f.close();
    }

    public Node _getRightMost(Node r) {
        Node q = r;
        while (q.right != null) {
            q = q.right;
        }
        return q;
    }

    public Node _deleteNode(Node root, int x) {
        if (root == null) {
            return null;
        }
        // backtracking
        if (root.info.code > x) {
            root.left = this._deleteNode(root.left, x);
        } else if (root.info.code < x) {
            root.right = this._deleteNode(root.right, x);
        } else {
            // 1. no child or only right child
            if (root.left == null) {
                return root.right;
            }
            // 2. only left child
            if (root.right == null) {
                return root.left;
            }
            // 3. two children
            Node rightMost = this._getRightMost(root.left);
            root.info = rightMost.info;
            root.left = this._deleteNode(root.left, rightMost.info.code);
        }
        return root;
    }

//=============================================================
    void f3() throws Exception {
        clear();
        loadData(11);
        String fname = "f3.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        postOrder(root, f);
        f.writeBytes("\r\n");
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
      Your task is to insert statements here, just after this comment,
      to complete the question in the exam paper.*/
         if (root != null) {
            Queue q = new Queue();
            q.enqueue(root);
            Node r, minNode = root;
            while (!q.isEmpty()) {
                r = q.dequeue();
                if (r.info.code < minNode.info.code) minNode = r;
                if (r.left != null) q.enqueue(r.left);
                if (r.right != null) q.enqueue(r.right);
            }
            minNode.info.ram = 999;
        }
        //------------------------------------------------------------------------------------
        postOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

    int _getHeight(Node r) {
        if (r == null) {
            return 0;
        }
        return Integer.max(this._getHeight(r.left),
                this._getHeight(r.right)) + 1;
    }

    int count = 0;
    Node p = null;

    void find3rdPreOrder(Node r) {
        if (r == null || p != null) {
            return;
        }

        find3rdPreOrder(r.left);
        count++;
        if (count == 4) {
            p = r;
            return;
        }
        find3rdPreOrder(r.right);
    }
//=============================================================

    void f4() throws Exception {
        clear();
        loadData(16);
        String fname = "f4.txt";
        File g123 = new File(fname);
        if (g123.exists()) {
            g123.delete();
        }
        RandomAccessFile f = new RandomAccessFile(fname, "rw");
        preOrder(root, f);
        f.writeBytes("\r\n");
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
      Your task is to insert statements here, just after this comment,
      to complete the question in the exam paper.*/
                if (root != null) {
            Queue q = new Queue();
            q.enqueue(root);
            Node r;
            while (!q.isEmpty()) {
                r = q.dequeue();
                if (r.info.ram > 8) r.info.slots++;
                if (r.left != null) q.enqueue(r.left);
                if (r.right != null) q.enqueue(r.right);
            }
        }
        //------------------------------------------------------------------------------------
        preOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }
}
