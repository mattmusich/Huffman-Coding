package huffman;

public class KeyTable implements Comparable<KeyTable>{

    public int ch;
    public int depth;
    public int binary;
    public String code;
    public byte[] data;

    public KeyTable(int ch, int depth){
        this.ch = ch;
        this.depth = depth;
    }

    public String toString(){
        return Integer.toString(ch) + ":" + Integer.toString(depth) + ":" + Integer.toString(binary) + ":"+ code;
    }

    public CharNode toCharNode(){
        CharNode build = new CharNode(this.ch,0,this.code,true);
        return build;
    }

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
