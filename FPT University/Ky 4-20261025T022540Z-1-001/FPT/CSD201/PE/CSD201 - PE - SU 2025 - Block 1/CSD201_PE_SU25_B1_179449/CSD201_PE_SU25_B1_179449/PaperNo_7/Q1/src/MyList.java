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
    void addLast(String xPlace, int xSong, int xWing) {
        //You should write here appropriate statements to complete this function.
        if (xPlace.charAt(0) == 'A') {
            return;
        }
        Node q = new Node(new Canary(xPlace, xSong, xWing));
        if (head == null) {
            head = tail = q;
        } else {
            tail.next = q;
            tail = q;
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
        Canary x, y, z;
        x = new Canary("X", 1, 2);
        y = new Canary("Y", 2, 3);
        z = new Canary("Z", 3, 4);
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/

        //------------------------------------------------------------------------------------
        int[] pos = {2, 5, 6};
        Canary[] items = {x, y, z};
        for (int t = 0; t < 3; t++) {
            Node nn = new Node(items[t]);
            Node pre = head;
            int idx = 0;
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
        Node p = head;
        while (p != null && !p.info.place.equals("E")) {
            p = p.next;
        }
        if (p != null) {
            int k = p.info.wing;
            // remove nodes at the head
            while (head != null && head.info.wing < k) {
                head = head.next;
            }
            if (head == null) {
                tail = null;
            } else {
                Node cur = head;
                while (cur.next != null) {
                    if (cur.next.info.wing < k) {
                        cur.next = cur.next.next;
                    } else {
                        cur = cur.next;
                    }
                }
                tail = cur;
            }
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

        //------------------------------------------------------------------------------------
        Node p = head;
        while (p != null && !p.info.place.equals("E")) {
            p = p.next;
        }
        Node q = head;
        while (q != null && !q.info.place.equals("K")) {
            q = q.next;
        }
        if (p != null && q != null) {
            boolean swapped = true;
            while (swapped) {
                swapped = false;
                for (Node i = p; i != q; i = i.next) {
                    if (i.info.wing > i.next.info.wing) {
                        Canary tmp = i.info;
                        i.info = i.next.info;
                        i.next.info = tmp;
                        swapped = true;
                    }
                }
            }
        }
        ftraverse(f);
        f.close();
    }

}
