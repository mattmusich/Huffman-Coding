package huffman;

import java.io.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Encode {

    public static String newline = System.getProperty("line.separator");

    public static void main(String[] args) {

        String sourceFile = args[0];
        sourceFile = sourceFile.replace("\r","").replace("\n","");

        Map<String,Integer> charFreqs = checkCharFreq(sourceFile);

        for(String key : charFreqs.keySet()){
            System.out.println(key +":"+ charFreqs.get(key));
        }

    }

    public static Map<String,Integer> checkCharFreq(String sourceFile){

        Map<String,Integer> charCount = new HashMap<String,Integer>();
        int sourceChar;
        String line = null;
        int r;

        try {
            FileReader sourceReader = new FileReader(sourceFile);
            BufferedReader sourceBufferReader = new BufferedReader(sourceReader);

            while((r = sourceBufferReader.read()) != -1){
                char c = (char)r;
                String cString;
                if(c == '\n') {
                    cString = "\n";
                } else if(c == '\r') {
                    cString = "\r";
                } else {
                    cString = Character.toString(c);
                }
                charCount = addChar(charCount,cString);
            }

        }
        catch(FileNotFoundException ex) {
            System.out.println("Error: File " + sourceFile + "Not Found");
        }
        catch(IOException ex) {
            System.out.println("Error: File " + sourceFile + "Cannot be Read");
        }

    return charCount;
    }

    public static Map<String,Integer> addChar(Map<String,Integer> tempMap, String ch) {

        if (ch != null) {
            if (ch == "\n") {
                tempMap.put("/n", tempMap.getOrDefault("/n", 0) + 1);
            } else if (ch == "\r") {
                tempMap.put("/r", tempMap.getOrDefault("/r", 0) + 1);
            } else {
                tempMap.put(ch, tempMap.getOrDefault(ch, 0) + 1);
            }
        }
        return tempMap;
    }

}

