package carsharing;

import carsharing.db.Customer;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static  H2DatabaseHelper dbHelper;
    static  Scanner scanner;

    static Customer currentUser;
    public static void main(String[] args) {
        // write your code here
        String dbFileName = getDbFileName(args);
        dbHelper = new H2DatabaseHelper(dbFileName);
        scanner = new Scanner(System.in);

        do {
            System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit""");
            int mode = Integer.parseInt(scanner.nextLine());
            if (mode == 0) {
                break;
            }
            System.out.println();
            switch (mode) {
                case 1:
                    currentUser = null;
                    companyMode();
                    break;
                case 2:
                    showCustomer();
                    break;
                case 3:
                    createCustomer();
                    break;
            }
            System.out.println();
        } while (true);
    }

    static void companyMode() {
        do {
            System.out.println();
            System.out.println("""
                    1. Company list
                    2. Create a company
                    0. Back""");
            int mode = Integer.parseInt(scanner.nextLine());
            if (mode == 0) {
                return;
            }
            System.out.println();
            switch (mode) {
                case 1:
                    showCompanyMode();
                    break;
                case 2:
                    addCompanyMode();
                    break;
            }
        } while (true);
    }

    static void createCustomer() {
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        dbHelper.addNewCustomer(customerName);
        System.out.println("The customer was added!");
    }

    static void showCustomer() {
        List<Customer> customers = dbHelper.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }
        System.out.println("Customer list:");
        AtomicInteger id = new AtomicInteger();
        customers.stream().peek(s -> id.addAndGet(1))
                .forEach(s -> System.out.printf("%d. %s\n", id.get(), s.name()));
        System.out.println("0. Back");

        int chosen = Integer.parseInt(scanner.nextLine());
        if (chosen == 0) {
            return;
        }
        currentUser = customers.get(chosen - 1);
        rentCar();
    }

    static void rentCar() {
        do {
            System.out.println();
            System.out.println("""
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back""");
            int mode = Integer.parseInt(scanner.nextLine());
            if (mode == 0) {
                break;
            }
            System.out.println();
            switch (mode) {
                case 1:
                    if (currentUser.rentedCar() != 0) {
                        System.out.println("You've already rented a car!");
                    } else {
                        showCompanyMode();
                    }
                    break;
                case 2:
                    if (currentUser.rentedCar() == 0) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        currentUser = dbHelper.returnCar(currentUser);
                        System.out.println("You've returned a rented car!");
                    }
                    break;
                case 3:
                        Car car = dbHelper.getCar(currentUser.rentedCar());
                        if (car.id() == 0) {
                            System.out.println("You didn't rent a car!");
                        } else {
                            System.out.printf("Your rented car:\n%s\n", car.name());
                            Company rentedCompany = dbHelper.getCompany(car.companyId());
                            System.out.printf("Company:\n%s\n", rentedCompany.name());
                        }
                    break;
            }
        } while (true);
    }

    static void addCompanyMode() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        dbHelper.addNewCompany(companyName);
        System.out.println("The company was created!");
    }

    static void showCompanyMode() {
        List<Company> companies = dbHelper.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }
        System.out.println("Choose a company:");
        AtomicInteger id = new AtomicInteger();
        companies.stream().peek(s -> id.addAndGet(1))
                .forEach(s -> System.out.printf("%d. %s\n", id.get(), s.name()));
        System.out.println("0. Back");

        int chosen = Integer.parseInt(scanner.nextLine());
        if (chosen == 0) {
            return;
        }
        System.out.println();
        Company company = companies.get(chosen - 1);
        if (currentUser != null) {
            rentCarMode(company);
        } else {
            carMode(company);
        }
    }

    static void carMode(Company company) {
        System.out.println();
        System.out.printf("'%s' company", company.name());
        do {
            System.out.println();
            System.out.println("""
                    1. Car list
                    2. Create a car
                    0. Back""");
            int mode = Integer.parseInt(scanner.nextLine());
            if (mode == 0) {
                break;
            }
            System.out.println();
            switch (mode) {
                case 1: {
                    List<Car> cars = dbHelper.getAllCars(company);
                    if (cars.isEmpty()) {
                        System.out.println("The car list is empty!");
                        break;
                    }
                    System.out.println("Car list:");
                    AtomicInteger id = new AtomicInteger();
                    cars.stream()
                            .peek(s -> id.addAndGet(1))
                            .forEach(s -> System.out.printf("%d. %s\n", id.get(), s.name()));
                }
                break;
                case 2: {
                    System.out.println("Enter the car name:");
                    String name = scanner.nextLine();
                    dbHelper.addNewCar(name, company);
                    System.out.println("The car was added!");
                }
                break;
            }
        } while (true);
    }

    static void rentCarMode(Company company) {
        List<Car> cars = dbHelper.getAvailableCars(company);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            return;
        }
        System.out.println("Choose a car:");
        AtomicInteger id = new AtomicInteger();
        cars.stream()
                .peek(s -> id.addAndGet(1))
                .forEach(s -> System.out.printf("%d. %s\n", id.get(), s.name()));
        System.out.println("0. Back");

        int mode = Integer.parseInt(scanner.nextLine());
        if (mode == 0) {
            return;
        }
        System.out.println();
        Car car = cars.get(mode - 1);
        currentUser = dbHelper.rentACar(currentUser, car);
        System.out.printf("You rented '%s'\n", car.name());
    }

    static String getDbFileName(String[] args) {
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