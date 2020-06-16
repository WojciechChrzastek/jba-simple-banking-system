package banking;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DbHandler dbHandler = new DbHandler();

    public static void main(String[] args) {
        runApplication(args);
    }

    private static void runApplication(String[] args) {
//        String dbName = importDbFileName(args);
        String dbName = "test.db";
        setDb(dbName);
        Connection conn = dbHandler.connect(dbName);
        determineMainMenuAction(conn, takeInput("main"));
    }

    private static void setDb(String dbName) {
        dbHandler.createNewDatabase(dbName);
        dbHandler.createNewTable(dbName);
    }

//    private static String importDbFileName(String[] args) {
//        String fileName = "";
//        try {
//            if (args[0].equals("-fileName") && args.length >= 2) {
//                fileName = args[1];
//            } else {
//                System.out.println("No database name passed by command line argument.");
//            }
//        } catch (Exception e) {
//            System.out.println("No database name passed by command line argument.");
//        }
//        return fileName;
//    }

    private static String takeInput(String menu) {
        final String[] possibleActions = {"1", "2", "3", "4", "5", "0"};
        String action;
        boolean hasAction = false;

        do {
            printMenu(menu);
            action = scanner.next();
            if (Arrays.asList(possibleActions).contains(action)) {
                hasAction = true;

            } else {
                System.out.println("Provide valid input! (1 or 2 or 3 or 4 or 5 or 0)");
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
                            "2. Add income\n" +
                            "3. Do transfer\n" +
                            "4. Close account\n" +
                            "5. Log out\n" +
                            "0. Exit");
        }
    }

    private static void determineMainMenuAction(Connection conn, String action) {
        switch (action) {
            case "1": {
                createAnAccount(conn);
                break;
            }
            case "2": {
                logIntoAccount(conn);
                break;
            }
            case "0": {
                exit();
                break;
            }
        }
    }

    private static String createCardNumber() {
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

        return cardNumberWithoutChecksumString.concat(String.valueOf(checksum));
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

    private static void createAnAccount(Connection conn) {
        String cardNumber = createCardNumber();
        String pin = createPin();
        int balance = 0;

        Account account = new Account(cardNumber, pin, balance);

        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                cardNumber + "\n" +
                "Your card PIN:\n" +
                pin + "\n");

        dbHandler.insertNewCard(conn, String.valueOf(account.getCardNumber()), account.getPin());
        determineMainMenuAction(conn, takeInput("main"));
        dbHandler.closeConnection(conn);
    }

    private static void logIntoAccount(Connection conn) {
        System.out.println("\nEnter your card number: ");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN: ");
        String pin = scanner.next();

        if (dbHandler.hasCardPinMatch(conn, cardNumber, pin)) {
            System.out.println("\nYou have successfully logged in!\n");
            determineLoggedUserAction(conn, cardNumber, takeInput("user"));
        } else {
            System.out.println("\nWrong card number or PIN!\n");
            determineMainMenuAction(conn, takeInput("main"));
        }
    }

    private static void determineLoggedUserAction(Connection conn, String loggedCardNumber, String action) {
        switch (action) {
            case "1": {
                checkBalance(conn, loggedCardNumber);
                break;
            }
            case "2": {
                addIncome(conn, loggedCardNumber);
                break;
            }
            case "3": {
                doTransfer(conn, loggedCardNumber);
                break;
            }
            case "4": {
                closeAccount(conn, loggedCardNumber);
                break;
            }
            case "5": {
                logOut(conn);
                break;
            }
            case "0": {
                exit();
                break;
            }
        }
    }

    private static boolean isLuhnValid(String number) {
        int[] ints = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            ints[i] = Integer.parseInt(number.substring(i, i + 1));
        }
        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }
        int sum = 0;
        for (int i : ints) {
            sum += i;
        }
        return sum % 10 == 0;
    }

    private static void doTransfer(Connection conn, String loggedCardNumber) {
        String transferRecipientCardNumber;
        int transferAmount;
        System.out.println("\nTransfer");
        System.out.println("Enter card number: ");
        transferRecipientCardNumber = scanner.next();
        if (!transferRecipientCardNumber.equals(loggedCardNumber)) {
            if (isLuhnValid(transferRecipientCardNumber)) {
                if (dbHandler.hasCardNumber(conn, transferRecipientCardNumber)) {
                    System.out.println("Enter how much money you want to transfer: ");
                    transferAmount = scanner.nextInt();
                    dbHandler.updateBalance(conn, transferRecipientCardNumber, transferAmount);
                    dbHandler.updateBalance(conn, loggedCardNumber, -transferAmount);
                    System.out.println("Success!\n");
                } else {
                    System.out.println("Such a card does not exist.\n");
                }
            } else {
                System.out.println("Probably you made mistake in card number. Please try again!\n");
            }
        } else {
            System.out.println("You can't transfer money to the same account!\n");
        }
        determineLoggedUserAction(conn, loggedCardNumber, takeInput("user"));
    }

    private static void addIncome(Connection conn, String loggedCardNumber) {
        System.out.println("\nEnter income: ");
        int income = scanner.nextInt();
        dbHandler.updateBalance(conn, loggedCardNumber, income);
        System.out.println("Income was added!\n");
        determineLoggedUserAction(conn, loggedCardNumber, takeInput("user"));
    }

    private static void closeAccount(Connection conn, String loggedCardNumber) {
        dbHandler.deleteCard(conn, loggedCardNumber);
        System.out.println("\nThe account has been closed!\n");
        determineLoggedUserAction(conn, loggedCardNumber, takeInput("user"));
    }

    private static void checkBalance(Connection conn, String loggedCardNumber) {
        int balance = dbHandler.checkBalance(conn, loggedCardNumber);
        System.out.println("\nBalance: " + balance + "\n");
        determineLoggedUserAction(conn, loggedCardNumber, takeInput("user"));
    }

    private static void logOut(Connection conn) {
        System.out.println("\nYou have successfully logged out!\n");
        determineMainMenuAction(conn, takeInput("main"));
    }

    private static void exit() {
        scanner.close();
        System.out.println("\nBye!");
        System.exit(0);
    }
}
