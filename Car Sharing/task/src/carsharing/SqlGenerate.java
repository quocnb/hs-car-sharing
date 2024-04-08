package carsharing;

public class SqlGenerate {
    public static String CREATE_TBL_COMPANY = """
            CREATE TABLE IF NOT EXISTS COMPANY (
              ID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
              NAME VARCHAR(255) NOT NULL UNIQUE
            )""";
    public static String INSERT_COMPANY = "INSERT INTO COMPANY (NAME) VALUES (?)";
}
