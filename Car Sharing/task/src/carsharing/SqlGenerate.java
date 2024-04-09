package carsharing;

public class SqlGenerate {
    //region
    public static String CREATE_TBL_COMPANY = """
            CREATE TABLE IF NOT EXISTS COMPANY (
              ID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
              NAME VARCHAR(255) NOT NULL UNIQUE
            )""";
    public static String SELECT_TBL_COMPANY = "SELECT * FROM COMPANY ORDER BY ID";
    public static String SELECT_COMPANY_BY_ID = "SELECT * FROM COMPANY WHERE ID=?";
    public static String INSERT_COMPANY = "INSERT INTO COMPANY (NAME) VALUES (?)";

    //region Car
    public static String CREATE_TBL_CAR = """
            CREATE TABLE IF NOT EXISTS CAR (
              ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
              NAME VARCHAR(255) NOT NULL UNIQUE,
              COMPANY_ID INT NOT NULL,
              FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)
            )""";
    public static String SELECT_TBL_CAR = "SELECT * FROM CAR WHERE COMPANY_ID = ? ORDER BY ID";
    public static String SELECT_AVAILABLE_CAR = """
            SELECT Car.Id ID, Car.Name NAME, Car.Company_Id COMPANY_ID
            FROM Car
            LEFT JOIN Customer ON Car.id = Customer.rented_car_id
            WHERE Car.Company_Id=? AND Customer.rented_car_id IS NULL
            ORDER BY Car.Id""";
    public static String SELECT_CAR_WITH_ID = "SELECT * FROM CAR WHERE ID = ?";
    public static String INSERT_CAR = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
    //endregion

    //region Customer
    public static String CREATE_TBL_CUSTOMER = """
            CREATE TABLE IF NOT EXISTS CUSTOMER (
              ID INT PRIMARY KEY AUTO_INCREMENT,
              NAME VARCHAR(255) NOT NULL UNIQUE,
              RENTED_CAR_ID INT,
              FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)
            )""";

    public static String SELECT_TBL_CUSTOMER = "SELECT * FROM CUSTOMER ORDER BY ID";
    public static String SELECT_CUSTOMER_WITH_ID = "SELECT * FROM CUSTOMER WHERE ID=?";
    public static String INSERT_CUSTOMER = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
    public static String CUSTOMER_RENT_CAR = "UPDATE CUSTOMER SET RENTED_CAR_ID=? WHERE ID=?";
    public static String CUSTOMER_RETURN_CAR = "UPDATE CUSTOMER SET RENTED_CAR_ID=NULL WHERE ID=?";

    //endregion
}
