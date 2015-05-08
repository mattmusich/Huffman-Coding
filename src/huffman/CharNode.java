package huffman;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CharNode implements Comparable<CharNode> {

    public int ch;
    public int freq;
    public String name = "";
    public int depth;
    public boolean isLeaf;
    public CharNode left;
    public CharNode right;
    public CharNode parent;

    public CharNode(int ch,int freq,String name, boolean isLeaf){
        this.name = name;
        this.ch = ch;
        this.freq = freq;
        this.isLeaf = isLeaf;
    }

    @Override
    public int compareTo(CharNode i) {

       return this.freq - i.freq;
    }

    public void dumpData(){
        if(this.isLeaf){
            System.out.println("NAME:" +this.name + " CH:" + Integer.toString(this.ch) +" FREQ:"
                    + Integer.toString(this.freq) + " DEPTH:" + Integer.toString(this.depth) + " LEAF:"
                    + Boolean.toString(this.isLeaf));
        } else {
            System.out.println("NAME:" + this.name + " CH:" + Integer.toString(this.ch) + " FREQ:"
                    + Integer.toString(this.freq) + " DEPTH:" + Integer.toString(this.depth) + " LEAF:"
                    + Boolean.toString(this.isLeaf) + " LEFT:" + this.left.name + " RIGHT:" + this.right.name);
        }
    }

    public void printNodes(CharNode n, String dashes) {
        // print with colon if leaf node
        if (Integer.toString(n.ch) != "") {
            n.depth = dashes.length()-1;
            System.out.println(dashes + n.name + ":" +  n.freq + " Depth:" + n.depth);
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




}
