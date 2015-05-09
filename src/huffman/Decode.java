package huffman;

/*
    Author: Matthew Musich
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Decode {

    /**
     * This takes in a .huf and will convert it back to .txt based on a canoncial tree in the header of the .huf
     * @param args 0= .huf 1=.txt
     */
    public static void main(String[] args) {

        /*sets the two files and create the needed tables */
        String hufFile = args[0];
        String targetFile = args[1];
        int k = 0;
        HashMap<Integer,Integer> headerTable = new HashMap<Integer,Integer>();
        PriorityQueue<KeyTable> sortedTable = new PriorityQueue<KeyTable>();
        PriorityQueue<KeyTable> codedTable = new PriorityQueue<KeyTable>();

        /*Reads the binary.huf*/
        try {
            DataInputStream inputStream = new DataInputStream(new FileInputStream(hufFile));
                //byte[] buffer = new byte[100000];
                //int total = 0;
                //byte nRead;
            /* init the values that will control the bytes*/
            int test = 0;
            int x = 0;
            int y = 0;

            /*get k value*/
            k = inputStream.readByte();

            /*loop for HeaderTable build*/
            for (int j = 0; j < k; j++){
                x = inputStream.readByte();
                y = inputStream.readByte();
                headerTable.put(x,y);
            }

            /*Builds the sorted table needed for tree building*/
                //System.out.println(k);
            for (Integer name: headerTable.keySet()){
                String key = name.toString();
                String value = headerTable.get(name).toString();
                    //System.out.println(key + ":" + value);
                sortedTable.add(new KeyTable(name,headerTable.get(name)));
            }

            /*build canonical tree*/
            /*use sortedTable to do this*/
            codedTable = Encode.createBinaryCodes(sortedTable);
            CharNode conTree = buildconTree(codedTable);
                //System.out.println("Printing tree:\n");
            conTree.printNodes(conTree,"-");

            /*Setup for the content decode*/
            String endcontent = "";
            CharNode treeRoot = conTree;
            CharNode currentNode = conTree;
            String remainder = "";
            /*loop though encoded content comp bits to tree*/
            while(inputStream.available() > 0) {

                /*gets a byte converts to string in binary format converts to byte[]*/
                test = inputStream.readByte();
                    //System.out.println("BYTE:"+test);
                String binary = String.format("%8s", Integer.toBinaryString(test & 0xFF)).replace(' ', '0');
                    //System.out.println("B w/out:"+binary);
                String binaryOverflow = binary ; //remainder + IS NOT NEEDED I was carrying the tree loc so i did not need it alongside
                    //System.out.println("Bw/OVER:"+binaryOverflow);
                char[] bits = binaryOverflow.toCharArray();

                for (int i = 0;i<binaryOverflow.length();i++){
                    char currentBit = bits[i];

                    /* checks if it going to be left or right on the tree*/
                        //System.out.println("C BIT:"+currentBit);
                    if(currentBit == '0'){
                      currentNode = currentNode.left;
                    } else {
                      currentNode = currentNode.right;
                    }

                    /* if it is a leaf it adds the char to a string other wise it carries the remainder chars*/
                    if(currentNode.isLeaf){
                            //System.out.println("LEAF:"+currentNode.ch);
                        endcontent += (char) currentNode.ch;
                            //System.out.println("ADD:"+(char) currentNode.ch);
                        remainder = "";
                        currentNode = treeRoot;
                    } else {
                            //System.out.println("BRANCH:"+currentBit);
                        remainder += currentBit;
                    }

                }

               // total += test;
            }

            /*write the file*/
            writeFile(endcontent,targetFile);

            inputStream.close();
            //System.out.println("Read " + total + " bytes");
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + hufFile + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + hufFile + "'");
        }



    }

    /**
     * sets up the recursive call for the building of the canonical tree
     * @param pr the PQ containing all of the codes
     * @return the head of the tree
     */
    public static CharNode buildconTree(PriorityQueue<KeyTable> pr){
        ArrayList<CharNode> nodeTable = new ArrayList<CharNode>();
        for(KeyTable n : pr){
            nodeTable.add(n.toCharNode());

        }
        int x = 0;
        CharNode root = recurseBuild(nodeTable,x);
            //System.out.println(root.left.left.right.ch);
        return root;
    }

    /**
     * recursive function that takes in an ArrayList and divides it by 0 or 1 into left or right
     * and does that till 1 is in each leaf spot
     * @param pr the codes and chars
     * @param depth the depth of the binary that is being worked on
     * @return the head of the tree
     */
    public static CharNode recurseBuild(ArrayList<CharNode> pr, int depth){

            //for (CharNode c : pr)
            //    c.dumpData();
            //System.out.println("\n");
        /* Checks if there is only 1 node left, if so it stops that recursive call*/
        if (pr.size() == 1) {
            return pr.get(0);
        }

        /* creates the two arrayLists for both recursive sides*/
        ArrayList<CharNode> left = new ArrayList<CharNode>();
        ArrayList<CharNode> right = new ArrayList<CharNode>();

        /* for each bit in code set the node in the left or right array*/
        for (int i = 0; i < pr.size(); i++) {
            char[] c = pr.get(i).name.toCharArray();
            if (c[depth] == '0') {
                left.add(pr.get(i));
            }
            else {
                right.add(pr.get(i));
            }
        }

        /* Create a new node which is root for the tables and set each table to
        left and right and increment depth by 1 to reach the next binary bit*/
        CharNode head = new CharNode();
        head.left = recurseBuild(left, depth + 1);
        head.right = recurseBuild(right, depth + 1);
        head.depth = (depth + 1);

        return head;
    }

    /**
     * This takes the translated bytes to string and writes the string to a file
     * @param content the string to write
     * @param targetFile the file to write to
     */
    public static void writeFile(String content,String targetFile){
        try {
            FileWriter fileWriter = new FileWriter(targetFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
            System.out.println("Wrote " + content.length() + " bytes");
        }
        catch(IOException ex) {
            System.out.println("Error writing file '"+ targetFile + "'");
        }

    }


}
