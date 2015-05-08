package huffman;

import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import org.jetbrains.annotations.NotNull;

import java.io.*;
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
        String header = encodeHeader(codedTable);


        //creates the Hash for content encode
        HashMap<Integer,String> codeHash = new HashMap<Integer,String>();
        int ctSize = codedTable2.size();
        for (int i=0; i < ctSize; i++) {
            KeyTable current = codedTable2.poll();
            codeHash.put(current.ch,current.code);
            //System.out.println(current.ch+ ":"+current.code);
        }

        //creates the content String
        String content = encodeContent(sourceFile,codeHash);
        String outBinary = header + content;

        String targetFile = args[1];
        writeHuf(outBinary,targetFile);


            //End of Main
        }

    public static Map<Integer,Integer> checkCharFreq(String sourceFile){

        Map<Integer,Integer> charCount = new HashMap<Integer,Integer>();
        int sourceChar;
        String line = null;
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
            System.out.println(current.ch + ":" + current.depth + ":" +sCode);
            current.code = sCode;
            pastDepth = current.depth;
            current.data = sCode.getBytes();
            btable.add(current);
        }
        return btable;
    }

    public static ArrayList<byte[]> encodeHeader(PriorityQueue<KeyTable> codedTable){

        ArrayList<byte[]> header = new ArrayList<byte[]>();

        int ctSize = codedTable.size();
        int bitLength = 8;
        //header += String.format("%" + bitLength + "s", Integer.toBinaryString(ctSize)).replace(' ', '0');

        byte[] count = new byte[bitLength];
        count = Integer.toBinaryString(ctSize).getBytes();

        for (int i=0; i < ctSize; i++) {
            KeyTable curr = codedTable.poll();
            byte[] tempB = new byte[curr.data.length];
            tempB = curr.data;
            header.add(tempB);
           // String bitChar = String.format("%" + bitLength + "s", Integer.toBinaryString(curr.ch)).replace(' ', '0');
           // String bitDepth = String.format("%" + bitLength + "s", Integer.toBinaryString(curr.depth)).replace(' ', '0');
           // header += bitChar + bitDepth;
        }
        return header;
    }


    public static String encodeContent(String sourceFile,HashMap<Integer,String> codeHash){
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
        return content;
    }

    public static void writeHuf(String outBinary,String targetFile){
        try {
            // Put some bytes in a buffer so we can
            // write them. Usually this would be
            // image data or something. Or it might
            // be unicode text.
            byte[] buffer = outBinary.getBytes();

            FileOutputStream outputStream = new FileOutputStream(targetFile);

            // write() writes as many bytes from the buffer
            // as the length of the buffer. You can also
            // use
            // write(buffer, offset, length)
            // if you want to write a specific number of
            // bytes, or only part of the buffer.
            outputStream.write(buffer);

            // Always close files.
            outputStream.close();

            System.out.println("Wrote " + buffer.length +" bytes");
        }
        catch(IOException ex) {
            System.out.println("Error writing file '"+ targetFile + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

    }


}

