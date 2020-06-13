package banking;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Set<Account> accountsSet = new HashSet<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        runApplication();
        scanner.close();
    }

    private static void runApplication() {
        determineMainMenuAction(takeInput("main"));
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

    private static void createAnAccount() {
        Random random = new Random();
        String pin = "";
        String cardNumberString = "";
        long cardNumber = 400000L;
        int balance = 0;
        StringBuilder pinBuilder = new StringBuilder();
        StringBuilder cardNumberBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            pin = pinBuilder.append(random.nextInt(10)).toString();
        }

        for (int i = 0; i < 10; i++) {
            cardNumberString = cardNumberBuilder.append(random.nextInt(10)).toString();
        }

        cardNumber = Long.parseLong(String.valueOf(cardNumber).concat(cardNumberString));

        Account account = new Account(cardNumber, pin, balance);
        accountsSet.add(account);

        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                cardNumber + "\n" +
                "Your card PIN:\n" +
                pin + "\n");
        determineMainMenuAction(takeInput("main"));
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
        System.out.println("\nBye!");
        System.exit(0);
    }
}
