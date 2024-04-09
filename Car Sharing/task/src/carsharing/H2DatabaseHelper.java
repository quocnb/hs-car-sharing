package carsharing;

import carsharing.db.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2DatabaseHelper {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

    private Connection connection;
    public H2DatabaseHelper(String dbFileName) {
        if (dbFileName == null || dbFileName.isEmpty()) {
            dbFileName = "carsharing";
        }
        String dbnUrl = DB_URL + dbFileName;
        try {
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            connection = DriverManager.getConnection(dbnUrl,"","");
            connection.setAutoCommit(true);
            migrate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    //region Company
    public List<Company> getAllCompanies() {
        List<Company> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SqlGenerate.SELECT_TBL_COMPANY);

            // Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String name = rs.getString("NAME");
                result.add(new Company(id, name));
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public Company getCompany(int companyId) {
        Company company = new Company(companyId, "");
        try {
            PreparedStatement stmt = connection.prepareStatement(SqlGenerate.SELECT_COMPANY_BY_ID);
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String name = rs.getString("NAME");
                company = new Company(id, name);
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return company;
    }

    public void addNewCompany(String name) {
        try {
            String sql = SqlGenerate.INSERT_COMPANY;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    //endregion

    //region Cars
    public List<Car> getAllCars(Company company) {
        List<Car> result = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(SqlGenerate.SELECT_TBL_CAR);
            stmt.setInt(1, company.id());
            ResultSet rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String name = rs.getString("NAME");
                int companyId = rs.getInt("COMPANY_ID");
                result.add(new Car(id, name, companyId));
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public List<Car> getAvailableCars(Company company) {
        List<Car> result = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(SqlGenerate.SELECT_AVAILABLE_CAR);
            stmt.setInt(1, company.id());
            ResultSet rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String name = rs.getString("NAME");
                int companyId = rs.getInt("COMPANY_ID");
                result.add(new Car(id, name, companyId));
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public Car getCar(int carId) {
        Car car = new Car(0, "", 0);
        try {
            PreparedStatement stmt = connection.prepareStatement(SqlGenerate.SELECT_CAR_WITH_ID);
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String name = rs.getString("NAME");
                int companyId = rs.getInt("COMPANY_ID");
                car = new Car(id, name, companyId);
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return car;
    }

    public void addNewCar(String name, Company company) {
        try {
            PreparedStatement stmt = connection.prepareStatement(SqlGenerate.INSERT_CAR);
            stmt.setString(1, name);
            stmt.setInt(2, company.id());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    //endregion

    //region Customer
    public List<Customer> getCustomers() {
        List<Customer> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SqlGenerate.SELECT_TBL_CUSTOMER);

            // Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String name = rs.getString("NAME");
                int rentedCar = rs.getInt("RENTED_CAR_ID");
                result.add(new Customer(id, name, rentedCar));
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public Customer getCustomer(int customerId) {
        Customer result = new Customer(0, "", 0);
        try {
            PreparedStatement stmt = connection.prepareStatement(SqlGenerate.SELECT_CUSTOMER_WITH_ID);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String name = rs.getString("NAME");
                int rentedCar = rs.getInt("RENTED_CAR_ID");
                result = new Customer(id, name, rentedCar);
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public void addNewCustomer(String name) {
        try {
            String sql = SqlGenerate.INSERT_CUSTOMER;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Customer rentACar(Customer customer, Car car) {
        try {
            String sql = SqlGenerate.CUSTOMER_RENT_CAR;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, car.id());
            stmt.setInt(2, customer.id());
            stmt.executeUpdate();
            customer = getCustomer(customer.id());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return customer;
    }

    public Customer returnCar(Customer customer) {
        try {
            String sql = SqlGenerate.CUSTOMER_RETURN_CAR;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, customer.id());
            stmt.executeUpdate();
            customer = getCustomer(customer.id());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return customer;
    }
    //endregion

    private void migrate() {
        List<String> sqlList = List.of(
                SqlGenerate.CREATE_TBL_COMPANY,
                SqlGenerate.CREATE_TBL_CAR,
                SqlGenerate.CREATE_TBL_CUSTOMER
                );
        executeSql(sqlList);
    }

    private void executeSql(List<String> sqlList) {
        Statement stmt = null;
        try {
            // Execute sql list
            stmt = connection.createStatement();
            for (String sql: sqlList) {
                stmt.executeUpdate(sql);
            }
            // Close statement
            stmt.close();
        } catch(Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Clean up
            try {
                if (stmt!=null) stmt.close();
            } catch(SQLException se2) {
                System.err.println(se2.getMessage());
            }
        }
    }

}
