package huffman;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Decode {

    public static void main(String[] args) {

        String hufFile = args[0];
        String targetFile = args[1];

        try {
            // Use this for reading the data.
            byte[] buffer = new byte[1000];

            FileInputStream inputStream = new FileInputStream(hufFile);

            // read fills buffer with data and returns
            // the number of bytes read (which of course
            // may be less than the buffer size, but
            // it will never be more).
            int total = 0;
            int nRead = 0;
            while((nRead = inputStream.read(buffer)) != -1) {
                // Convert to String so we can display it.
                // Of course you wouldn't want to do this with
                // a 'real' binary file.
                System.out.println(new String(buffer));
                total += nRead;
            }

            // Always close files.
            inputStream.close();

            System.out.println("Read " + total + " bytes");
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + hufFile + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + hufFile + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }


        String[] split = splitHuf(hufFile);
        String header = split[0];
        String content = split[1];

    }

    public static String[] splitHuf(String hufFile){
        String[] split = new String[2];

        //read chars till System.getProperty("line.separator") in split[0]

        //when System.getProperty("line.separator") is hit change to split[1]




        return split;
    }


}
