package huffman;

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

        for(Integer key : charFreqs.keySet()){
            int i = key;
            char c = (char)i;
            System.out.println(key +":"+ charFreqs.get(key) + " =" + c );
            pr.add(new CharNode(key, charFreqs.get(key), String.valueOf(c) ,true));
        }



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

//    public static CharNode bld(){
//
//
//    }




}

