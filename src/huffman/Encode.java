package huffman;

import java.io.*;

public class Encode {

    public static void main(String[] args) {

        String sourceFile = args[0];

        String line = null;

        try {
            FileReader sourceReader = new FileReader(sourceFile);

            BufferedReader sourceBufferReader = new BufferedReader(sourceReader);

            while((line = sourceBufferReader.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch(FileNotFoundException ex) {
            System.out.println("Error: File " + sourceFile + "Not Found");
        }
        catch(IOException ex) {
            System.out.println("Error: File " + sourceFile + "Cannot be Read");
        }


    }

    public 






}

