package carsharing;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String dbFileName = getDbFileName(args);
        H2DatabaseHelper dbHelper = new H2DatabaseHelper(dbFileName);
        Scanner scanner = new Scanner(System.in);

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
                    case 1: {
                        List<Company> companies = dbHelper.getAllCompanies();
                        if (companies.isEmpty()) {
                            System.out.println("The company list is empty!");
                        } else {
                            AtomicInteger id = new AtomicInteger();
                            companies.stream().peek(s -> id.addAndGet(1))
                                    .forEach(s -> System.out.printf("%d. %s", id.get(), s.name()));
                        }
                    }
                    break;
                    case 2: {
                        System.out.println("Enter the company name:");
                        String companyName = scanner.nextLine();
                        dbHelper.addNewCompany(companyName);
                    }
                    break;
                }

            } while (true);
        } while (true);
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