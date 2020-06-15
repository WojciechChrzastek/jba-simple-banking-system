package banking;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Set<Account> accountsSet = new HashSet<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DbHandler dbHandler = new DbHandler();
    private static String dbName;


    public static void main(String[] args) {
        runApplication(args);
    }

    private static void runApplication(String[] args) {
        setDb(args);
        determineMainMenuAction(takeInput("main"));
    }

    private static void setDb(String[] args) {
        dbName = importDbFileName(args);
        dbHandler.createNewDatabase(dbName);
        dbHandler.createNewTable(dbName);
    }

    private static String importDbFileName(String[] args) {
        String fileName = "";
        try {
            if (args[0].equals("-fileName") && args.length >= 2) {
                fileName = args[1];
            } else {
                System.out.println("No database name passed by command line argument.");
            }
        } catch (Exception e) {
            System.out.println("No database name passed by command line argument.");
        }
        return fileName;
    }

    private static String takeInput(String menu) {
        final String[] possibleActions = {"1", "2", "0"};
        String action;
        boolean hasAction = false;

        do {
            printMenu(menu);
            action = scanner.next();
            if (Arrays.asList(possibleActions).contains(action)) {
                hasAction = true;

            } else {
                System.out.println("Provide valid input! (1 or 2 or 3)");
            }
        }
        while (!(hasAction));
        return action;
    }

    private static void printMenu(String menu) {
        if (menu.equals("main")) {
            System.out.println(
                    "1. Create an account\n" +
                            "2. Log into account\n" +
                            "0. Exit");
        } else if (menu.equals("user")) {
            System.out.println(
                    "1. Balance\n" +
                            "2. Log out\n" +
                            "0. Exit");
        }
    }

    private static void determineMainMenuAction(String action) {
        switch (action) {
            case "1": {
                createAnAccount();
                break;
            }
            case "2": {
                logIntoAccount();
                break;
            }
            case "0": {
                exit();
                break;
            }
        }
    }

    private static long createCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int[] cardNumberWithoutChecksum = {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 6; i < 15; i++) {
            cardNumberWithoutChecksum[i] = random.nextInt(10);
        }

        int checksum = 0;
        int sum = 0;

        String cardNumberWithoutChecksumString = "";

        for (int i : cardNumberWithoutChecksum) {
            cardNumberWithoutChecksumString = sb.append(i).toString();
        }

        for (int i = 1; i <= cardNumberWithoutChecksum.length; i++) {
            if (i % 2 != 0) {
                cardNumberWithoutChecksum[i - 1] *= 2;
            }
        }

        for (int i = 1; i <= cardNumberWithoutChecksum.length; i++) {
            if (cardNumberWithoutChecksum[i - 1] > 9) {
                cardNumberWithoutChecksum[i - 1] -= 9;
            }
        }

        for (int i : cardNumberWithoutChecksum) {
            sum += i;
        }

        while (!((sum + checksum) % 10 == 0)) {
            checksum++;
        }

        return Long.parseLong(cardNumberWithoutChecksumString.concat(String.valueOf(checksum)));
    }

    public static String createPin() {
        Random random = new Random();
        String pin = "";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            pin = sb.append(random.nextInt(10)).toString();
        }
        return pin;
    }

    private static void createAnAccount() {
        Connection conn = dbHandler.connect(dbName);

        long cardNumber = createCardNumber();
        String pin = createPin();
        int balance = 0;

        Account account = new Account(cardNumber, pin, balance);
        accountsSet.add(account);

        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                cardNumber + "\n" +
                "Your card PIN:\n" +
                pin + "\n");

        dbHandler.insert(conn, String.valueOf(account.getCardNumber()), account.getPin());
        determineMainMenuAction(takeInput("main"));
        dbHandler.closeConnection(conn);
    }

    private static void logIntoAccount() {
        System.out.println("\nEnter your card number: ");
        long cardNumber = scanner.nextLong();
        System.out.println("Enter your PIN: ");
        String pin = scanner.next();
        Account loggedAccount = findAccount(cardNumber, pin);
        System.out.println("\nYou have successfully logged in!\n");
        determineLoggedUserAction(loggedAccount, takeInput("user"));
    }

    private static Account findAccount(long cardNumber, String pin) {
        for (Account account : Main.accountsSet) {
            long cn = account.getCardNumber();
            String p = account.getPin();
            if (cn == cardNumber && p.equals(pin)) {
                return account;
            } else {
                System.out.println("\nWrong card number or PIN!\n");
                determineMainMenuAction(takeInput("main"));
            }
        }
        return null;
    }

    private static void determineLoggedUserAction(Account account, String action) {
        switch (action) {
            case "1": {
                checkBalance(account);
                break;
            }
            case "2": {
                logOut();
                break;
            }
            case "0": {
                exit();
                break;
            }
        }
    }

    private static void checkBalance(Account account) {
        System.out.println("\nBalance: " + account.getBalance() + "\n");
        determineLoggedUserAction(account, takeInput("user"));
    }

    private static void logOut() {
        System.out.println("\nYou have successfully logged out!\n");
        determineMainMenuAction(takeInput("main"));
    }

    private static void exit() {
        scanner.close();
        System.out.println("\nBye!");
        System.exit(0);
    }
}
