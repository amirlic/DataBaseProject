package logic;

import java.util.ArrayList;
import java.util.List;

/*
 * Tal Sharon 302826797
 * Lea Tordjman 327321188
 * Amir Lichter 316129881
 * Orly paknahad 315444646
 */

public class Table {
    private List<List<String>> rowList = new ArrayList<List<String>>();

    private String tableName;
    private String allData;
    private List<String> columns = new ArrayList<String>();

    public Table(List<List<String>> rowList){
        this.rowList = rowList;
    }

    public Table(String name, String all){
        this.tableName = name;
        this.allData = all;
    }

    public String myToString() {
        String s = "";
        for (List<String> row : rowList) {
            s += row.toString() + "\n";
        }
        return s;
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getColumns() {
        String[] rows = allData.split("\n");
        for(String r: rows){
            String[] cols = r.split(",");
            columns.add(cols[0].replace("[",""));
        }

        String[] colsArray = new String[columns.size()];
        for(int i=0; i<colsArray.length; i++){
            colsArray[i] = columns.get(i);
        }
        return colsArray;
    }
}
