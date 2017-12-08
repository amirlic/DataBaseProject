package logic;

import java.io.BufferedReader;
import java.io.FileReader;
/*
 * Tal Sharon 302826797
 * Lea Tordjman 327321188
 * Amir Lichter 316129881
 * Orly paknahad 315444646
 */
public class FileToQueries {
    private static String input = "";

    public static String readFile(String fileLocation){
        try {
            FileReader file = new FileReader(fileLocation);
            BufferedReader reader = new BufferedReader(file);
            String line;
            while((line = reader.readLine()) != null){
                input = input + line;
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }

        return input;
    }
}
