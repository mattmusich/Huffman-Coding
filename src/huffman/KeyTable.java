package huffman;

public class KeyTable {

    public int freq;
    public int length;
    public int binary;

    public KeyTable(int freq, int length, int binary){
        this.freq = freq;
        this.length = length;
        this.binary = binary;
    }

    public String toString(){
        return Integer.toString(freq) + "," + Integer.toString(length) + "," + Integer.toString(binary);
    }


}
