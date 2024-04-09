package carsharing;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static  H2DatabaseHelper dbHelper;
    static  Scanner scanner;
    public static void main(String[] args) {
        // write your code here
        String dbFileName = getDbFileName(args);
        dbHelper = new H2DatabaseHelper(dbFileName);
        scanner = new Scanner(System.in);

        do {
            System.out.println("""
                1. Log in as a manager
                0. Exit""");
            int mode = Integer.parseInt(scanner.nextLine());
            if (mode == 0) {
                break;
            }
            do {
                System.out.println();
                System.out.println("""
                    1. Company list
                    2. Create a company
                    0. Back""");
                mode = Integer.parseInt(scanner.nextLine());
                if (mode == 0) {
                    break;
                }
                System.out.println();
                switch (mode) {
                    case 1:
                        companyMode();
                        break;
                    case 2:
                       addCompanyMode();
                        break;
                }
            } while (true);
        } while (true);
    }

    static void addCompanyMode() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        dbHelper.addNewCompany(companyName);
        System.out.println("The company was created!");
    }

    static void companyMode() {
        List<Company> companies = dbHelper.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }
        System.out.println("Choose the company:");
        AtomicInteger id = new AtomicInteger();
        companies.stream().peek(s -> id.addAndGet(1))
                .forEach(s -> System.out.printf("%d. %s\n", id.get(), s.name()));
        System.out.println("0. Back");

        int chosen = Integer.parseInt(scanner.nextLine());
        if (chosen == 0) {
            return;
        }
        Company company = companies.get(chosen - 1);
        carMode(company);
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