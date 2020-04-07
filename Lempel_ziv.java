import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Lempel_ziv {

    public static void main(String[] args) throws IOException {
        File file = new File("test.txt");
        compress(file);
    }

    public static void compress(File input) throws IOException {
        ArrayList<String> outputs = new ArrayList<>();
        Map<String, Integer> dictionary = new HashMap<>();

        for (int i = 0; i < 256; i++)
            dictionary.put(Character.toString((char) i), i);

        String text = getFullText(input);
        int counter = 256;
        int currentIndex = 0;
        String p = "" + text.charAt(currentIndex);
        while (currentIndex < text.length()) {
            String c = "";
            if (currentIndex != text.length() - 1) {
                c += "" + text.charAt(currentIndex + 1);
            } else {
                c = "" + text.charAt(currentIndex);
            }
            if (dictionary.containsKey(p + c)) {
                p = p + c;
            } else {
                outputs.add(Integer.toBinaryString(dictionary.get(p)));
                dictionary.put(p + c, counter);
                counter++;
                p = c;
            }
            currentIndex++;
            c = "";
        }

        writeToOutputFile(outputs, dictionary);
    }


    public static void writeToOutputFile(ArrayList<String> outputs, Map<String, Integer> dictionary) throws FileNotFoundException, UnsupportedEncodingException {
       /* PrintWriter writer = new PrintWriter("output", "UTF-8");

        for (String str : outputs) {
            int value = dictionary.get(str);
            writer.print(Integer.toBinaryString(value) + " ");
        }
        writer.close(); */

        String encoded = "";
        for (String str : outputs)
            encoded += str;

        //System.out.println(encoded.length());
        BitSet bitSet = new BitSet(encoded.length());

        int bitcounter = 0;
        for (Character c : encoded.toCharArray()) {
            if (c.equals('1')) {
                bitSet.set(bitcounter);
            }
            bitcounter++;
        }
        //System.out.println(bitSet.length());


        Path path = Paths.get("output.txt");
        byte[] bytes = bitSet.toByteArray();
        try {
            Files.write(path, bytes);
            //System.out.println(bytes.length);
            System.out.println("Successfully written data to the file");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getFullText(File input) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(input));
        String text = "";
        String line;
        while ((line = br.readLine()) != null)
            text += line;

        return text;
    }

}


