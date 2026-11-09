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
        int n = a.length;
        for (int i = 0; i < n; i++) {
            insert(a[i], b[i], c[i]);
        }
    }

//===========================================================================
//(2)===YOU CAN EDIT OR EVEN ADD NEW FUNCTIONS IN THE FOLLOWING PART========
//===========================================================================
    void insert(String xForest, int xRate, int xSound) {
        //You should insert here statements to complete this function

        if (xForest.charAt(0) == 'A') {
            return;
        }
        Tiger d = new Tiger(xForest, xRate, xSound);
        root = insertNode(root, d);
    }

    Node insertNode(Node r, Tiger c) {
        if (r == null) {
            return new Node(c);
        }
        int cmp = Integer.compare(c.sound, r.info.sound);
        if (cmp < 0) {
            r.left = insertNode(r.left, c);
        } else if (cmp > 0) {
            r.right = insertNode(r.right, c);
        }
        // cmp == 0: model da co -> khong lam gi
        return r;
    }

    void preOrder2(Node p, RandomAccessFile f) throws Exception {
        if (p == null) {
            return;
        }
        if (p.info.rate < 6) {
            fvisit(p, f);
        }
        preOrder2(p.left, f);
        preOrder2(p.right, f);
    }

    int cnt = 0;
    Node target = null;

    void findKth(Node p, int k) {
        if (p == null || target != null) {
            return;
        }
        cnt++;
        if (cnt == k) {
            target = p;
            return;
        }
        findKth(p.left, k);
        findKth(p.right, k);
    }

    Node deleteByCopy(Node r, int key) {
        if (r == null) {
            return null;
        }
        if (key < r.info.sound) {
            r.left = deleteByCopy(r.left, key);
        } else if (key > r.info.sound) {
            r.right = deleteByCopy(r.right, key);
        } else {
            if (r.left == null) {
                return r.right;
            }
            if (r.right == null) {
                return r.left;
            }
            Node m = r.left;
            while (m.right != null) {
                m = m.right;
            }
            r.info = m.info;                              // copy du lieu
            r.left = deleteByCopy(r.left, m.info.sound);  // xoa node phai nhat
        }
        return r;

    }

    Node rotateLeftAt(Node r, int key) {
        if (r == null) {
            return null;
        }
        if (key < r.info.sound) {
            r.left = rotateLeftAt(r.left, key);
        } else if (key > r.info.sound) {
            r.right = rotateLeftAt(r.right, key);
        } else {
            Node q = r.right;
            if (q == null) {
                return r;
            }
            r.right = q.left;
            q.left = r;
            return q;
        }
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

//=============================================================
    void f2() throws Exception {
        clear();
        loadData(5);
        String fname = "f2.txt";
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
        preOrder2(root, f);
        //------------------------------------------------------------------------------------
        f.writeBytes("\r\n");
        f.close();
    }

//=============================================================
    void f3() throws Exception {
        clear();
        loadData(9);
        String fname = "f3.txt";
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
        cnt = 0;
        target = null;
        findKth(root, 4);
        if (target != null) {
            root = deleteByCopy(root, target.info.sound);
        }
        //------------------------------------------------------------------------------------
        preOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

//=============================================================
    void f4() throws Exception {
        clear();
        loadData(13);;
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
        cnt = 0;
        target = null;
        findKth(root, 4);
        if (target != null && target.right != null) {
            root = rotateLeftAt(root, target.info.sound);
        }
        //------------------------------------------------------------------------------------
        preOrder(root, f);
        f.writeBytes("\r\n");
        f.close();
    }

}
