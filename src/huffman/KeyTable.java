package huffman;
/*
    Author: Matthew Musich
 */

/**
 * Stores the data needed for sorting and table lookups, cannot make a tree with this
 * sorts the canonical way based on the compareTo
 */
public class KeyTable implements Comparable<KeyTable>{

    public int ch; //char as an int
    public int depth; //depth in tree
    public int binary; //decimal val of the binary
    public String code; //String of the binary values as 8 bits
    public byte[] data; // the binary data as a byte array

    /**
     * Constructor for a new KeyTable
     * @param ch the char as in
     * @param depth the depth or length within a tree
     */
    public KeyTable(int ch, int depth){
        this.ch = ch;
        this.depth = depth;
    }

    /**
     * for debug dump
     * @return the debug string
     */
    public String toString(){
        return Integer.toString(ch) + ":" + Integer.toString(depth) + ":" + Integer.toString(binary) + ":"+ code;
    }

    /**
     * Converts the keytable data to a charNode
     * Allows the option of building a tree with the keytable data
     * @return the node it creates
     */
    public CharNode toCharNode(){
        CharNode build = new CharNode(this.ch,0,this.code,true);
            //build.depth = this.depth;
        return build;
    }

    /**
     * Provides the proper sort for the canoncial code ordering,  Depth high to low, then Lex low to high
     * @param i the keytable to be sorted
     * @return the pos or neg or equal value
     */
    @Override
    public int compareTo(KeyTable i) {
        int thisdepth = this.depth;
        int thatdepth = i.depth;

        int thisch = this.ch;
        int thatch = i.ch;

        if(thisdepth < thatdepth){
            return 1;
        } else if (thisdepth > thatdepth){
            return -1;
        } else {
             if(thisch < thatch){
                 return -1;
             } else {
                 return 1;
             }
        }
    }

}
