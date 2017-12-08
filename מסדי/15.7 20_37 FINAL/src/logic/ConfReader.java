package logic;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Tal Sharon 302826797
 * Lea Tordjman 327321188
 * Amir Lichter 316129881
 * Orly paknahad 315444646
 */

public class ConfReader {
    private static List<String> input = new ArrayList<>();
    private static Map<String, String> inputMap = new HashMap<>();

    public static Map<String, String> readConf(String fileName){
        try {
            FileReader file = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(file);
            String line;
            while((line = reader.readLine()) != null){
                input.add(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }

        if(!input.isEmpty()){
            inputMap.put("url", input.get(0));
            inputMap.put("userName", input.get(1));
            inputMap.put("password", input.get(2));
        }

        return inputMap;
    }
}
