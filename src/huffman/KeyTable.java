package huffman;

public class KeyTable{

    public int ch;
    public int freq;
    public int length;
    public int binary;

    public KeyTable(int ch, int freq, int length, int binary){
        this.ch = ch;
        this.freq = freq;
        this.length = length;
        this.binary = binary;
    }

    public String toString(){
        return Integer.toString(ch) + ":" + Integer.toString(freq) + ":" + Integer.toString(length) + ":" + Integer.toString(binary);
    }



}
