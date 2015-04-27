package huffman;

import java.io.*;

public class Encode {

    public static void main(String[] args) {

        String sourceFile = args[0];

        String line = null;

        try {
            FileReader sourceReader = new FileReader(sourceFile);

            BufferedReader sourceBufferReader = new BufferedReader(sourceReader);
        }
        catch(FileNotFoundException ex) {
            System.out.println("File: " + sourceFile + "Not Found");
        }



    }








}

