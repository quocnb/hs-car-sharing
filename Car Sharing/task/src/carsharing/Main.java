package carsharing;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String dbFileName = getDbFileName(args);
        if (!dbFileName.isEmpty()) {
            H2DatabaseHelper.setDbFileName(dbFileName);
        }
        List<String> sql = List.of(SqlGenerate.CREATE_TBL_COMPANY);
        H2DatabaseHelper.connectAndExecuteSql(sql);
    }

    public static String getDbFileName(String[] args) {
        boolean readDbFileName = false;
        for (String arg : args) {
            if (readDbFileName) {
                return arg;
            }
            if ("-databaseFileName".equals(arg)) {
                readDbFileName = true;
            }
        }
        return "";
    }
}