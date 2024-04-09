package carsharing;

public class SqlGenerate {
    public static String CREATE_TBL_COMPANY = """
            CREATE TABLE IF NOT EXISTS COMPANY (
              ID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
              NAME VARCHAR(255) NOT NULL UNIQUE
            )""";
    public static String CREATE_TBL_CAR = """
            CREATE TABLE IF NOT EXISTS CAR (
              ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,\s
              NAME VARCHAR(255) NOT NULL UNIQUE,\s
              COMPANY_ID INT NOT NULL,\s
              FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)
            )""";
    public static String SELECT_TBL_COMPANY = "SELECT * FROM COMPANY ORDER BY ID";
    public static String SELECT_TBL_CAR = "SELECT * FROM CAR WHERE COMPANY_ID = ? ORDER BY ID";
    public static String INSERT_COMPANY = "INSERT INTO COMPANY (NAME) VALUES (?)";
    public static String INSERT_CAR = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
}
