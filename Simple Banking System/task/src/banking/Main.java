package banking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static ArrayList<Account> accounts = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        runApplication();
        scanner.close();
    }

    private static void runApplication() {
        String action = takeInput("main");
        determineMainMenuAction(action);
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

    private static String takeInput(String menu) {
        final String[] possibleActions = {"1", "2", "0"};
        String action = "";
        boolean hasAction = false;

        do {
            printMenu(menu);
            action = scanner.next();
            if (((Arrays.asList(possibleActions).contains(action)))) {
                hasAction = true;

            } else {
                System.out.println("Provide valid input! (1 or 2 or 3)");
            }
        }
        while (!(hasAction));


        return action;
    }

    private static void determineMainMenuAction(String action) {
        switch (action) {
            case "1" : {
                createAnAccount();
                break;
            }
            case "2" : {
                logIntoAccount();
                break;
            }
            case "0" : {
                exit();
                break;
            }
        }
    }

    private static void determineLoggedUserAction(Account account, String action) {
        switch (action) {
            case "1" : {
                checkBalance(account);
                break;
            }
            case "2" : {
                logOut();
                break;
            }
            case "0" : {
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
//      Logout;
        System.out.println("You have successfully logged out!");
        determineMainMenuAction(takeInput("main"));
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
        accounts.add(account);

        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                cardNumber + "\n" +
                "Your card PIN:\n" +
                pin + "\n");
        determineMainMenuAction(takeInput("main"));;
    }

    private static void logIntoAccount() {
        System.out.println("\nEnter your card number: ");
        System.out.println("...");
        System.out.println("Enter your PIN: ");
        System.out.println("...");
        System.out.println("You have successfully logged in!");
        System.out.println("You have successfully logged out!\n");

        determineLoggedUserAction(accounts.get(0), takeInput("user"));
    }

    private static void exit() {
        System.out.println("\nBye!");
        System.exit(0);
    }
}
