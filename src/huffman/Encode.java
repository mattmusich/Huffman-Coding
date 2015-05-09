package huffman;

import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Encode {



    public static void main(String[] args) {

        String sourceFile = args[0];
        sourceFile = sourceFile.replace("\r","").replace("\n","");

        Map<Integer,Integer> charFreqs = checkCharFreq(sourceFile);
        PriorityQueue<CharNode> pr = new PriorityQueue<CharNode>();

        //sets up the priority queue
        for(Integer key : charFreqs.keySet()){
            int i = key;
            char c = (char)i;
            System.out.println(key +":"+ charFreqs.get(key) + " =" + c );
            pr.add(new CharNode(key, charFreqs.get(key),String.valueOf(c) , true));
        }

        //builds the tree from the PRQUEUE and prints
        CharNode head = buildHuffTree(pr);
        System.out.println("Printing tree:\n");
        head.printNodes(head,"-");

        //gets all leaf nodes and prints the depth pairs
        HashMap<Integer,Integer> leaves = new HashMap<Integer,Integer>();
        leaves = getLeaves(head, leaves);
        System.out.println("\nCH : Depth");
        for(Integer key : leaves.keySet()) {
            System.out.println(key.toString() + " : " + leaves.get(key).toString());
        }

        //builds 2 key tables to store the combined data
        PriorityQueue<KeyTable> chAndDepth = new PriorityQueue<KeyTable>();
        PriorityQueue<KeyTable> chAndDepth2 = new PriorityQueue<KeyTable>();
        PriorityQueue<KeyTable> printpq = new PriorityQueue<KeyTable>();
        for(Integer key : leaves.keySet()) {
            //System.out.println(key +":"+ charFreqs.get(key) + " =" + key );
            chAndDepth.add(new KeyTable(key,leaves.get(key)));
            chAndDepth2.add(new KeyTable(key,leaves.get(key)));
            printpq.add(new KeyTable(key,leaves.get(key)));
        }

        //prints/removes all from one of the PQs (FOR DEBUG)
        System.out.println("\nPQ CHECK");
        int si = printpq.size();
        for (int i=0; i < si; i++) {
            System.out.println(printpq.poll());
        }

        //Creates all of the Binary codes from the other PQ and sets it to a new PQ
        System.out.println("\nBINARY CHECK");
        PriorityQueue<KeyTable> codedTable = createBinaryCodes(chAndDepth);
        PriorityQueue<KeyTable> codedTable2 = createBinaryCodes(chAndDepth2);


        //creates the header string
        byte[] header = encodeHeader(codedTable);


        //creates the Hash for content encode
        HashMap<Integer,String> codeHash = new HashMap<Integer,String>();
        int ctSize = codedTable2.size();
        for (int i=0; i < ctSize; i++) {
            KeyTable current = codedTable2.poll();
            codeHash.put(current.ch,current.code);
            //System.out.println(current.ch+ ":"+current.code);
        }

        //creates the content String
        byte[] content = encodeContent(sourceFile,codeHash);


        String targetFile = args[1];
        writeHuf(header, content ,targetFile);

        System.out.println("K goal"+leaves.size());

            //End of Main
        }

    public static Map<Integer,Integer> checkCharFreq(String sourceFile){

        Map<Integer,Integer> charCount = new HashMap<Integer,Integer>();
        //int sourceChar;
        //String line = null;
        int r;

        try {
            FileReader sourceReader = new FileReader(sourceFile);
            BufferedReader sourceBufferReader = new BufferedReader(sourceReader);

            while((r = sourceBufferReader.read()) != -1){
                char c = (char)r;
                int cString;
                cString = c;
                charCount = addChar(charCount,cString);
            }

        }
        catch(FileNotFoundException ex) {
            System.out.println("Error: File " + sourceFile + "Not Found");
        }
        catch(IOException ex) {
            System.out.println("Error: File " + sourceFile + "Cannot be Read");
        }

        charCount.put(0,1);
        return charCount;
    }


    public static Map<Integer,Integer> addChar(Map<Integer,Integer> tempMap, Integer ch) {
        if (ch != null) {
            tempMap.put(ch, tempMap.getOrDefault(ch, 0) + 1);
        }
        return tempMap;
    }

    public static CharNode buildHuffTree(PriorityQueue<CharNode> pr){

        int size = pr.size();
        CharNode root = null;

        for (int i = 0; i < size-1; i++){
            CharNode l = pr.poll();
            CharNode r = pr.poll();
            CharNode m = new CharNode(0,l.freq + r.freq,l.name + r.name,false);
            m.right = l;
            m.left = r;
            l.parent = m;
            r.parent = m;
            pr.add(m);
            root = m;
        }

        return root;
    }

    public static HashMap<Integer,Integer> getLeaves(CharNode n, HashMap<Integer,Integer> leaves){
        if(n == null){
            return leaves;
        }
        if(n.isLeaf){
            leaves.put(n.ch,n.depth);
        }
        getLeaves(n.left, leaves);
        getLeaves(n.right,leaves);

        return leaves;
    }


    public static PriorityQueue<KeyTable> createBinaryCodes(PriorityQueue<KeyTable> cd){
        PriorityQueue<KeyTable> btable = new PriorityQueue<KeyTable>(); //new table to get out info
        int pastDepth = cd.peek().depth; //set the size for the eof case based on its size so it will always match
        int pqSize = cd.size();
        int code = -1;
        String sCode;

        for (int i = 0; i < pqSize ; i++){
            KeyTable current = cd.poll();
            code = (code + 1);
            if (pastDepth != current.depth){
                code = code / 2; //right shift
            }
            sCode = String.format("%" + Integer.toString(current.depth) + "s", Integer.toBinaryString(code)).replace(' ', '0');
            //System.out.println(current.ch + ":" + current.depth + ":" +sCode);
            current.code = sCode;
            pastDepth = current.depth;
            current.data = sCode.getBytes();
            btable.add(current);
            System.out.println(current.ch + ":" + current.depth + ":" +current.code);
        }
        return btable;
    }

    public static byte[] encodeHeader(PriorityQueue<KeyTable> codedTable){

        int ctSize = codedTable.size();
        byte[] header = new byte[(ctSize *2)+1];
        int bitLength = 8;
        //header += String.format("%" + bitLength + "s", Integer.toBinaryString(ctSize)).replace(' ', '0');

        byte count = (byte)ctSize;
        header[0] = count;
        int j = 1;
        for (int i=0; i < ctSize; i++) {
            KeyTable curr = codedTable.poll();
            header[j] = (byte)curr.ch;
            header[j+1] = (byte)curr.depth;
           j += 2;
        }
        return header;
    }


    public static byte[] encodeContent(String sourceFile,HashMap<Integer,String> codeHash){
        String content = "";
        int r;
        try {
            FileReader sourceReader = new FileReader(sourceFile);
            BufferedReader sourceBufferReader = new BufferedReader(sourceReader);
            while((r = sourceBufferReader.read()) != -1){
                content += codeHash.get(r);
            }
        }
        catch(FileNotFoundException ex) {
            System.out.println("Error: File " + sourceFile + "Not Found");
        }
        catch(IOException ex) {
            System.out.println("Error: File " + sourceFile + "Cannot be Read");
        }
        content += codeHash.get(0); //add EOF

        //System.out.println(content);

        //adds 0s to the end to make divisible by 8
        int remain = content.length() % 8;
        for(int i = 0; i < 8 - remain; i++){
            content += "0";
        }

       // System.out.println(content);

        String[] byteStrings = new String[(content.length()) / 8];
        byte[] con = new byte[(content.length()) / 8];

        //create an array of bytes
        int len = content.length();
        int j = 0;
        for (int i=0; i < len; i+=8){
            byteStrings[j] = content.substring(i, Math.min(len, i + 8));
            //con[j] = (byte) Integer.parseInt(content.substring(i, Math.min(len, i + 8)), 2);;
            j++;
        }

        //System.out.println(Arrays.toString(byteStrings));

        for (int i = 0; i < byteStrings.length; i++){
            //String s  = byteStrings[i];
            con[i] = (byte) Integer.parseInt(byteStrings[i], 2);
        }

        //System.out.println(Arrays.toString(con));


        return con;
    }

    public static void writeHuf(byte[] header, byte[] content,String targetFile){
        try {

            FileOutputStream outputStream = new FileOutputStream(targetFile);

            outputStream.write(header);
            outputStream.write(content);

            outputStream.close();

            System.out.println("Wrote " + (header.length + content.length) + " bytes");
        }
        catch(IOException ex) {
            System.out.println("Error writing file '"+ targetFile + "'");
        }

    }


}

