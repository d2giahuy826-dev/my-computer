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
//(2)===YOU CAN EDIT OR EVEN ADD NEW FUNCTIONS IN THE FOLLOWING PART========
//===========================================================================
/* 
   Khong su dung tieng Viet co dau de viet ghi chu.
   Neu dung khi chay truc tiep se bao loi va nhan 0 diem
     */
    void addLast(String xTower, int xSound, int xType) {
        //You should write here appropriate statements to complete this function.
        if (xTower.charAt(0) == 'B') {
            return;
        }
        Node p = new Node(new Drum(xTower, xSound, xType));
        if (head == null) {
            head = tail = p;
        } else {
            tail.next = p;
            tail = p;
        }

    }

    //You do not need to edit this function. Your task is to complete the addLast function above only.
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
        f.close();
    }

//==================================================================
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
        Drum x, y;
        x = new Drum("X", 1, 2);
        y = new Drum("Y", 3, 4);
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        int[] pos = {3, 5};
        Drum[] items = {x, y};
        for (int t = 0; t < 2; t++) {
            Node nn = new Node(items[t]);
            Node pre = head;
            int idx = 1;
            while (pre != null && pre.next != null && idx < pos[t] - 1) {
                pre = pre.next;
                idx++;
            }
            if (pre != null) {
                nn.next = pre.next;
                pre.next = nn;
                if (nn.next == null) {
                    tail = nn;
                }
            }
        }
//Node p = head; p = p.next;            // node 2
//Node a = new Node(x); a.next = p.next; p.next = a;
//p = head; for (int i = 1; i < 4; i++) p = p.next;   // node 4
//Node b = new Node(y); b.next = p.next; p.next = b;

        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }

//==================================================================
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
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        Node p = head, q = head;
        int max = head.info.sound;
        while (q != null) {
            if (q.info.sound > max) {   // dung > de lay node dau tien
                max = q.info.sound;
                p = q;
            }
            q = q.next;
        }
        Node del = p.next;
        p.next = del.next;
        if (del == tail) {
            tail = p;      // neu xoa trúng tail thi cap nhat
        }
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }

//==================================================================
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
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
// 1. dem so node
        int n = 0;
        Node t = head;
        while (t != null) {
            n++;
            t = t.next;
        }

// 2. tim node dau tien cua 4 node cuoi (node thu n-3)
        Node start = head;
        for (int i = 1; i < n - 3; i++) {
            start = start.next;
        }

// 3. bubble sort giam dan theo sound tren 4 node
        for (int i = 0; i < 3; i++) {
            Node a = start;
            for (int j = 0; j < 3 - i; j++) {
                Node b = a.next;
                if (a.info.sound < b.info.sound) {   // giam dan: nho hon thi doi
                    Drum tmp = a.info;
                    a.info = b.info;
                    b.info = tmp;
                }
                a = a.next;
            }
        }
                
        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }

}
