
import java.io.RandomAccessFile;

public class VIPBST {

    class Node {

        Customer data;
        Node left, right;

        Node(Customer data) {
            this.data = data;
        }
    }

    Node root;

    public void ftraverse(RandomAccessFile f, Node root) throws Exception {
        if (root == null) {
            return;
        }
        this.ftraverse(f, root.left);
        f.writeBytes("(" + root.data.id + "," + root.data.name + "," + root.data.priority + ")");
        this.ftraverse(f, root.right);
    }

    public void ftraverse(RandomAccessFile f) throws Exception {
        if (root == null) {
            f.writeBytes("Empty\r\n");
            return;
        }
        this.ftraverse(f, root);
    }

    public void insert(Customer x) {
        Node p = new Node(x);
        if (root == null) {
            root = p;
            return;
        }
        Node cur = root;
        while (cur != null) {
            if (x.priority < cur.data.priority) {
                if (cur.left == null) {
                    cur.left = p;
                    return;

                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    cur.right = p;
                    return;

                }
                cur = cur.right;
            }
        }
    }
    public Customer min() {
        if (root == null) {
            return null;
        }
        Node cur = root;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur.data;
    }
}
