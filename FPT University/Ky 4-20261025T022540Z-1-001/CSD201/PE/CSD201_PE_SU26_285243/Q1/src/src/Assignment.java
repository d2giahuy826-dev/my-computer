import java.io.File;
import java.io.RandomAccessFile;

public class Assignment {
    WaitingQueue q;
    VIPBST bst;
    String dataFile = "data.txt";

    public Assignment() {
        q = new WaitingQueue();
        bst = new VIPBST();
    }

    public void loadDataQueue() {
        String[] ids = Lib.readLineToStrArray(dataFile, 1);
        String[] names = Lib.readLineToStrArray(dataFile, 2);
        int[] priorities = Lib.readLineToIntArray(dataFile, 3);

        for (int i = 0; i < ids.length; i++) {
            q.enqueue(new Customer(ids[i], names[i], priorities[i]));
        }
    }

    public void loadDataBST() {
        String[] ids = Lib.readLineToStrArray(dataFile, 5);
        String[] names = Lib.readLineToStrArray(dataFile, 6);
        int[] priorities = Lib.readLineToIntArray(dataFile, 7);

        for (int i = 0; i < ids.length; i++) {
            bst.insert(new Customer(ids[i], names[i], priorities[i]));
        }
    }

    public void f1() throws Exception {
        String fname = "f1.txt";
        File f = new File(fname);
        if (f.exists()) f.delete();
        RandomAccessFile rf = new RandomAccessFile(fname, "rw");
        loadDataQueue();
        rf.writeBytes("Current Waiting Queue:\r\n");
        q.ftraverse(rf);
        Customer first = q.front();
        rf.writeBytes("\r\nFirst waiting customer:\r\n");
        if (first == null) {
            rf.writeBytes("null\r\n");
        } else {
            rf.writeBytes("(" + first.id + "," + first.name + "," + first.priority + ")\r\n");
        }
        rf.close();
    }

    public void f2() throws Exception {
        String fname = "f2.txt";
        File f = new File(fname);
        if (f.exists()) f.delete();
        RandomAccessFile rf = new RandomAccessFile(fname, "rw");
        loadDataQueue();
        rf.writeBytes("Current Waiting Queue:\r\n");
        q.ftraverse(rf);
        Customer removed = q.dequeue();
        rf.writeBytes("\r\nRemoved customer:\r\n");
        if (removed == null) {
            rf.writeBytes("null\r\n");
        } else {
            rf.writeBytes("(" + removed.id + "," + removed.name + "," + removed.priority + ")\r\n");
        }
        rf.writeBytes("Queue after dequeue:\r\n");
        q.ftraverse(rf);
        rf.close();
    }

    public void f3() throws Exception {
        String fname = "f3.txt";
        File f = new File(fname);
        if (f.exists()) f.delete();
        RandomAccessFile rf = new RandomAccessFile(fname, "rw");
        loadDataBST();
        rf.writeBytes("Current VIP Customers (Inorder by Priority):\r\n");
        bst.ftraverse(rf);
        rf.close();
    }

    public void f4() throws Exception {
        String fname = "f4.txt";
        File f = new File(fname);
        if (f.exists()) f.delete();
        RandomAccessFile rf = new RandomAccessFile(fname, "rw");
        loadDataBST();
        rf.writeBytes("Current VIP Customers:\r\n");
        bst.ftraverse(rf);
        Customer top = bst.min();
        rf.writeBytes("\r\nHighest-priority VIP:\r\n");
        if (top == null) {
            rf.writeBytes("null\r\n");
        } else {
            rf.writeBytes("(" + top.id + "," + top.name + "," + top.priority + ")\r\n");
        }
        rf.close();
    }
}