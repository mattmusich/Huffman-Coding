package huffman;
/*
    Author: Matthew Musich
 */

/**
 * This contains the data to manage all info within the trees
 */
public class CharNode implements Comparable<CharNode> {

    public int ch; //character as int
    public int freq; //amount of times in file
    public String name = ""; //can be the binary code in some places or is the char as a string
    public char[] bits = name.toCharArray();
    public int depth; //height or binary length
    public boolean isLeaf; //is it a leaf node?
    public CharNode left;
    public CharNode right;
    public CharNode parent;

    /**
     * Constructor for most cases
     * @param ch char in int
     * @param freq amt of chars
     * @param name binary vals or char as string depends on the situation
     * @param isLeaf if is is a leaf node(coded char)
     */
    public CharNode(int ch,int freq,String name, boolean isLeaf){
        this.name = name;
        this.ch = ch;
        this.freq = freq;
        this.isLeaf = isLeaf;
    }

    /**
     * constructor if need a branch temp
     */
    public CharNode(){
        isLeaf = false;
        depth = 0;
    }

    /**
     * converts the node to a keytable
     * @return a keytable with same info minus the tree locations
     */
    public KeyTable toKeyTable(){
        KeyTable build = new KeyTable(this.ch,this.depth);
        build.code = this.name;
        return build;
    }

    /**
     * how to sort them based on freq
     * @param i the node to sort
     * @return the ordering pos or neg or equal
     */
    @Override
    public int compareTo(CharNode i) {
       return this.freq - i.freq;
    }

    /**
     * Debug for a charnode
     */
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

    /**
     * prints the tree and can get the depths based on this
     * @param n
     * @param dashes
     */
    public void printNodes(CharNode n, String dashes) {
        /* print with colon if leaf node*/
        if (Integer.toString(n.ch) != "") {
            n.depth = dashes.length()-1;
           // System.out.println(dashes + n.name + ":" +  n.freq + " Depth:" + n.depth);
        }
        else {
           // System.out.println(dashes + n.freq);
        }

        /* Start recursive on left child then right*/
        if (n.left != null) {
            printNodes(n.left, dashes + "-");
        }
        if (n.right != null) {
            printNodes(n.right, dashes + "-");
        }
    }




}
