import gui.GUI;
import logic.ConfReader;
import logic.DbQueryController;

import java.util.Map;

/*
 * Tal Sharon 302826797
 * Lea Tordjman 327321188
 * Amir Lichter 316129881
 * Orly paknahad 315444646
 */

public class Main{
    public static void main(String[] args){
        Map<String, String> input = ConfReader.readConf("conf.txt");
        DbQueryController dbQueryController = new DbQueryController();
        GUI.connectingDialog(dbQueryController, input);
        GUI.run(dbQueryController);

    }
}
