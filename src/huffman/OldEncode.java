package huffman;
/*
    Author: Matthew Musich
 */
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class OldEncode {


    /**
     * Main takes in the two args that will be sent from the console
     * It edits all of the carrage movements and then builds a hash map containing the Frequencies
     * It then puts that into a Priority Queue that sorts it by freq
     * then the first tree is built from that
     * then find the depths of the leafs
     * takes the depths to build the codes based on the depth PQ
     * then use that to encode all of the header
     * then encode the content
     * and then write it all to the file in (hex/Binary/Bytes)
     * @param args 0= read file 1=new .huf file
     */
    public static void main(String[] args) {

        String sourceFile = args[0];
        sourceFile = sourceFile.replace("\r","").replace("\n","");

        Map<Integer,Integer> charFreqs = checkCharFreq(sourceFile);
        PriorityQueue<CharNode> pr = new PriorityQueue<CharNode>();

        /*sets up the priority queue*/
        for(Integer key : charFreqs.keySet()){
            int i = key;
            char c = (char)i;
                //System.out.println(key +":"+ charFreqs.get(key) + " =" + c );
            pr.add(new CharNode(key, charFreqs.get(key),String.valueOf(c) , true));
        }

        /*builds the tree from the PRQUEUE and prints*/
        CharNode head = buildHuffTree(pr);
            //System.out.println("Printing tree:\n");
        head.printNodes(head,"-");


        /*gets all leaf nodes and prints the depth pairs*/
        HashMap<Integer,Integer> leaves = new HashMap<Integer,Integer>();
        leaves = getLeaves(head, leaves);

            //System.out.println("\nCH : Depth");
            //for(Integer key : leaves.keySet()) {
            //    System.out.println(key.toString() + " : " + leaves.get(key).toString());
            //}

        /*builds 2 key tables to store the combined data*/
        PriorityQueue<KeyTable> chAndDepth = new PriorityQueue<KeyTable>();
        PriorityQueue<KeyTable> chAndDepth2 = new PriorityQueue<KeyTable>();
            //PriorityQueue<KeyTable> printpq = new PriorityQueue<KeyTable>();
        for(Integer key : leaves.keySet()) {
                //System.out.println(key +":"+ charFreqs.get(key) + " =" + key );
            chAndDepth.add(new KeyTable(key,leaves.get(key)));
            chAndDepth2.add(new KeyTable(key,leaves.get(key)));
                //printpq.add(new KeyTable(key,leaves.get(key)));
        }

            //prints/removes all from one of the PQs (FOR DEBUG)
            //System.out.println("\nPQ CHECK");
            //int si = printpq.size();
            //for (int i=0; i < si; i++) {
            //    System.out.println(printpq.poll());
            //}

        /*Creates all of the Binary codes from the other PQ and sets it to a new PQ*/
            //System.out.println("\nBINARY CHECK");
        PriorityQueue<KeyTable> codedTable = createBinaryCodes(chAndDepth);
        PriorityQueue<KeyTable> codedTable2 = createBinaryCodes(chAndDepth2);


        /*creates the header string*/
        byte[] header = encodeHeader(codedTable);

        /*creates the Hash for content encode*/
        HashMap<Integer,String> codeHash = new HashMap<Integer,String>();
        int ctSize = codedTable2.size();
        for (int i=0; i < ctSize; i++) {
            KeyTable current = codedTable2.poll();
            codeHash.put(current.ch,current.code);
                //System.out.println(current.ch+ ":"+current.code);
        }

        /*creates the content String*/
        byte[] content = encodeContent(sourceFile,codeHash);


        String targetFile = args[1];
        writeHuf(header, content ,targetFile);

        //writeContent(sourceFile,targetFile,codeHash);


        /*End of Main*/
        }

    /**
     * takes in a file and the buffer reader will get each char and build a table
     * based on its frequency
     * @param sourceFile this is the file that is read
     * @return A Map:Integer,Integer that contains the char(int) value and freqency #
     */
    public static Map<Integer,Integer> checkCharFreq(String sourceFile){

        Map<Integer,Integer> charCount = new HashMap<Integer,Integer>();
        //int sourceChar;
        //String line = null;
        int r;

        /*create reader*/
        try {
            FileReader sourceReader = new FileReader(sourceFile);
            BufferedReader sourceBufferReader = new BufferedReader(sourceReader);

            /* gets the chars for all chars in file*/
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

    /**
     * Called from checkCharFreq
     * this builds the map from the int based ch
     * @param tempMap the map to write to
     * @param ch the char in int form
     * @return the map that has a new char
     */
    public static Map<Integer,Integer> addChar(Map<Integer,Integer> tempMap, Integer ch) {
        if (ch != null) {
            tempMap.put(ch, tempMap.getOrDefault(ch, 0) + 1);
        }
        return tempMap;
    }

    /**
     * Creates the CharNode/Huffman tree from the sorted Freqs
     * @param pr the sorted queue
     * @return the head of the CharNode tree
     */
    public static CharNode buildHuffTree(PriorityQueue<CharNode> pr){

        int size = pr.size();
        CharNode root = null;

        /* takes 2 lowest freqs and sets them as node children for a new parent and readd the parent to the pq*/
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

    /**
     * takes in a tree and returns the nodes in hashmap
     * @param n the head of the tree to get leaves
     * @param leaves send it an empty hashmap to store leaves
     * @return the hashmap with the new leaves
     */
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

    /**
     * Creates the codes for each node based on size then lexagraphic order AKA canonical tree codes
     * @param cd the PQ that you want to cannonize
     * @return
     */
    public static PriorityQueue<KeyTable> createBinaryCodes(PriorityQueue<KeyTable> cd){
        PriorityQueue<KeyTable> btable = new PriorityQueue<KeyTable>(); //new table to get out info
        int pastDepth = cd.peek().depth; //set the size for the eof case based on its size so it will always match
        int pqSize = cd.size();
        int code = -1;
        String sCode;

        /*gets keyTable and increment and if size if diff than the last then shift right*/
        /*Then it converts the codes to proper binary Strings*/
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

    /**
     * takes the coded table and generates the header as a byte[]
     * @param codedTable the PQ needed to get the info to build header
     * @return a byte[] that contains the header of the file
     */
    public static byte[] encodeHeader(PriorityQueue<KeyTable> codedTable){

        int ctSize = codedTable.size();
        byte[] header = new byte[(ctSize *2)+1];
        int bitLength = 8;
            //header += String.format("%" + bitLength + "s", Integer.toBinaryString(ctSize)).replace(' ', '0');

        /*adds the K value as count and the pairs of char and depth*/
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

    /**
     * This takes in the source file to read again and a hashmap of references
     * It scans each char and converts it based on the map to the value in the corresponding key-Char
     * @param sourceFile the file being read
     * @param codeHash the table containing the chars and codes
     * @return
     */
    public static byte[] encodeContent(String sourceFile,HashMap<Integer,String> codeHash){
        System.out.println("STARTING ENCODE");
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

        /*adds 0s to the end to make divisible by 8 makes sure no straggle bits*/
        int remain = content.length() % 8;
        for(int i = 0; i < 8 - remain; i++){
            content += "0";
        }

            // System.out.println(content);

        String[] byteStrings = new String[(content.length()) / 8];
        byte[] con = new byte[(content.length()) / 8];

        /*create an array of bytes*/
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


    /**
     * takes the header and content and writes them to a file in binary/hex
     * 10:6 compression
     * @param header
     * @param content
     * @param targetFile
     */
    public static void writeHuf(byte[] header, byte[] content,String targetFile){
        try {

            FileOutputStream outputStream = new FileOutputStream(targetFile);
            outputStream.write(header);
            //outputStream.write(System.getProperty("line.separator").getBytes());
            outputStream.write(content);
            outputStream.close();
            System.out.println("Wrote " + (header.length + content.length) + " bytes");
        }
        catch(IOException ex) {
            System.out.println("Error writing file '"+ targetFile + "'");
        }

    }


}

