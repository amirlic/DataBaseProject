package logic;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;
/*
 * Tal Sharon 302826797
 * Lea Tordjman 327321188
 * Amir Lichter 316129881
 * Orly paknahad 315444646
 */

public interface QueryListener {

    String sendDDL(String ddl);
    String sendDML(String dml);
    boolean isDDLInputCorrect(String ddlInput);
    boolean isDMLInputCorrect(String dmlInput);
    boolean isDMLResponseCorrectStructure(String dmlResponse);
    boolean isDMLResponseCorrectLogic(String dmlResponse);
    Pair<String[], Map<String, Table>> getDatabaseMetaData();
    void setDBName(String input);
}
