package huffman;

import java.util.HashMap;
import java.util.Map;

public class CharNode implements Comparable<CharNode> {

    public int ch;
    public int freq;
    public boolean isLeaf;
    public CharNode left;
    public CharNode right;
    public CharNode parent;

    public CharNode(int ch,int freq, boolean isLeaf){
        this.ch = ch;
        this.freq = freq;
        this.isLeaf = isLeaf;
    }

    @Override
    public int compareTo(CharNode i) {
       return this.freq - i.freq;
    }

    public void printNodes(CharNode n, String dashes) {
        // print with colon if leaf node
        if (Integer.toString(n.ch) != "") {
            System.out.println(dashes + Integer.toString(n.ch) + ":" +  n.freq);
        }
        else {
            System.out.println(dashes + n.freq);
        }

        // Start recursive on left child then right
        if (n.left != null) {
            printNodes(n.left, dashes + "-");
        }
        if (n.right != null) {
            printNodes(n.right, dashes + "-");
        }
    }

    public void getDepth(CharNode n, String dashes, int count) {
        Map<Integer,KeyTable> table = new HashMap<Integer, KeyTable>();

        // print with colon if leaf node
        if (Integer.toString(n.ch) != "") {
            table.put(n.ch, new KeyTable(n.freq, count, 0 ));
            System.out.println(n.ch + ":" + table.get(n.ch).toString());
        }
        else {
            System.out.println(dashes + n.freq);
        }

        // Start recursive on left child then right
        if (n.left != null) {
            getDepth(n.left, dashes + "-", count++);
        }
        if (n.right != null) {
            getDepth(n.right, dashes + "-", count++);
        }


    }


}
