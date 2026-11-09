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
    void addLast(String name, int score, int grade) {
        //You should write here appropriate statements to complete this function.
        if(score<0 || score>100) return;
        Student d = new Student(name,score,grade);
        Node add = new Node(d);
        
        if (head == null) {
            head = tail = add;
        } else {
            tail.next = add;
            tail = add;
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
        Integer count = 0;
        //------------------------------------------------------------------------------------
        /*You must keep statements pre-given in this function.
       Your task is to insert statements here, just after this comment,
       to complete the question in the exam paper.*/
        Node current = head;

        while (current != null) {
            if (current.info.score>=75){
                count++;
            }
            current = current.next;
        }


        //------------------------------------------------------------------------------------
        
        f.writeBytes(count.toString());
        f.writeBytes("\r\n");
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
        Node current = head;
        Integer min=head.info.score;
        Integer indexMin=0;
        Integer index=0;
        while (current != null) {
            if (current.info.score<min){
                min=current.info.score;
                indexMin=index;
            }
            index++;
            current = current.next;
        }
        current = head;
        for (int i = 0; i < indexMin; i++) {
            current = current.next;
        }
        current.info.name="POOR";


        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }
    
     boolean shouldComeBefore(int firstData, int secondData, boolean ascending) {
        return ascending ? firstData < secondData : firstData > secondData;
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
        Node sortedHead = null;
        Node sortedTail = null;
        Node current = head;

        while (current != null) {
            Node next = current.next;
            current.next = null;

            if (sortedHead == null) {
                sortedHead = current;
                sortedTail = current;
            } else if (shouldComeBefore(current.info.score, sortedHead.info.score, true)) {
                current.next = sortedHead;
                sortedHead = current;
            } else {
                Node sortedCurrent = sortedHead;

                while (sortedCurrent.next != null
                        && !shouldComeBefore(current.info.score, sortedCurrent.next.info.score, true)) {
                    sortedCurrent = sortedCurrent.next;
                }

                current.next = sortedCurrent.next;
                sortedCurrent.next = current;

                if (current.next == null) {
                    sortedTail = current;
                }
            }

            current = next;
        }

        head = sortedHead;
        tail = sortedTail;

        

        //------------------------------------------------------------------------------------
        ftraverse(f);
        f.close();
    }


}
