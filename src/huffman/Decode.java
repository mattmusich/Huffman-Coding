package huffman;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Decode {

    public static void main(String[] args) {

        String hufFile = args[0];
        String targetFile = args[1];
        int k = 0;
        HashMap<Integer,Integer> headerTable = new HashMap<Integer,Integer>();
        PriorityQueue<KeyTable> sortedTable = new PriorityQueue<KeyTable>();
        PriorityQueue<KeyTable> codedTable = new PriorityQueue<KeyTable>();

        try {

            DataInputStream inputStream = new DataInputStream(new FileInputStream(hufFile));
            byte[] buffer = new byte[100000];
            int total = 0;
            byte nRead;
            int test = 0;
            int x = 0;
            int y = 0;

            //get k value
            k = inputStream.readByte();

            //loop for HeaderTable build
            for (int j = 0; j < k; j++){
                x = inputStream.readByte();
                y = inputStream.readByte();
                headerTable.put(x,y);
            }

            //Builds the sorted table needed for tree building
            //System.out.println(k);
            for (Integer name: headerTable.keySet()){
                String key = name.toString();
                String value = headerTable.get(name).toString();
                //System.out.println(key + ":" + value);
                sortedTable.add(new KeyTable(name,headerTable.get(name)));
            }

            //TODO:build canoncial tree
            //use sortedTable to do this
            codedTable = Encode.createBinaryCodes(sortedTable);
            CharNode conTree = buildconTree(codedTable);

            System.out.println("Printing tree:\n");
            conTree.printNodes(conTree,"-");

            //TODO:loop though encoded content compair bits to tree
            while((test = inputStream.readByte()) != -1 ) {

                total += test;
            }


            inputStream.close();
            System.out.println("Read " + total + " bytes");
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + hufFile + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + hufFile + "'");
        }



    }

    public static CharNode buildconTree(PriorityQueue<KeyTable> pr){

        int size = pr.size();
        CharNode root = null;

        for (int i = 0; i < size-1; i++){
            CharNode l = pr.poll().toCharNode();
            CharNode r = pr.poll().toCharNode();
            CharNode m = new CharNode(0,l.freq + r.freq,l.name + r.name,false);
            m.right = l;
            m.left = r;
            l.parent = m;
            r.parent = m;
            pr.add(m.toKeyTable());
            root = m;
        }

        return root;
    }




}
