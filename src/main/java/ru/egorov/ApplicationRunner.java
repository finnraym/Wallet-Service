package ru.egorov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.egorov.config.DatabaseConfiguration;
import ru.egorov.controller.MainController;
import ru.egorov.exception.*;
import ru.egorov.in.InputData;
import ru.egorov.model.Player;
import ru.egorov.model.Transaction;
import ru.egorov.out.OutputData;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * The type Application runner.
 */
public class ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ApplicationRunner.class);
    private static MainController controller;
    private static ProcessStage currentStage;

    /**
     * Run the application.
     */
    public static void run() {
        ApplicationContext.loadContext();
        DatabaseConfiguration.migration();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        outputData.output("Migrations completed successfully!");
        controller = (MainController) ApplicationContext.getBean("controller");
        currentStage = ProcessStage.SECURITY;
        outputData.output("Welcome to the account management application for players!\n");

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (currentStage) {
                    case SECURITY -> securityProcess(inputData, outputData);
                    case MAIN_MENU -> menuProcess(inputData, outputData);
                    case DEBIT_PROCESS -> debitProcess(inputData, outputData);
                    case CREDIT_PROCESS -> creditProcess(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AuthorizeException |
                    RegisterException |
                    TransactionAlreadyExistsException |
                    TransactionOperationException |
                    NotValidArgumentException e) {
                log.warn(e.getMessage());
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                outputData.errOutput("Unknown error. More details " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeInput();
    }

    private static void creditProcess(InputData inputData, OutputData outputData) {
        double amount = transactionProcess(inputData, outputData);
        controller.creditTransaction(BigDecimal.valueOf(amount), UUID.randomUUID(), ApplicationContext.getAuthorizePlayer());
        outputData.output("The transaction was completed successfully.\n");
        currentStage = ProcessStage.MAIN_MENU;
    }

    private static void debitProcess(InputData inputData, OutputData outputData) {
        double amount = transactionProcess(inputData, outputData);
        controller.debitTransaction(BigDecimal.valueOf(amount), UUID.randomUUID(), ApplicationContext.getAuthorizePlayer());
        outputData.output("The transaction was completed successfully.\n");
        currentStage = ProcessStage.MAIN_MENU;
    }

    private static double transactionProcess(InputData inputData, OutputData outputData) {
        final String msg = "Enter the transaction amount. If you want to cancel, then write the word \"Cancel\":";
        outputData.output(msg);
        String request = inputData.input().toString();
        if (request.equalsIgnoreCase("cancel")) {
            currentStage = ProcessStage.MAIN_MENU;
            throw new TransactionOperationException("Cancel the transaction.");
        }
        double amount = 0;
        try {
            amount = Double.parseDouble(request);
        } catch (NumberFormatException e) {
            outputData.errOutput("Wrong format. Please enter a number.\n");
        }
        return amount;
    }

    private static void menuProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                Choose an action:
                View your balance - 1
                View transaction history - 2
                Withdraw funds - 3
                Top up funds - 4
                Log out of account - 5
                Quit the application - 0
                """;
        while (true) {
            outputData.output(menu);
            String input = inputData.input().toString();
            if (input.equals("1")) {
                BigDecimal balance = controller.showBalance(ApplicationContext.getAuthorizePlayer());
                String response = "Your balance: " + balance.toString();
                outputData.output(response);
            } else if (input.equals("2")) {
                List<Transaction> history = controller.showTransactionsHistory(ApplicationContext.getAuthorizePlayer());
                if (history == null || history.isEmpty()) {
                    outputData.output("You have not completed any transactions yet.\n");
                } else {
                    outputData.output("Your transaction history: ");
                    for (Transaction transaction : history) {
                        outputData.output(
                                "Transaction ID - " + transaction.getTransactionIdentifier() +
                                        ", transaction type - " + transaction.getType() +
                                        ", balance before - " + transaction.getBalanceBefore() +
                                        ", transaction amount - " + transaction.getAmount() +
                                        ", balance after - " + transaction.getBalanceAfter()
                        );
                    }
                }
            } else if (input.equals("3")) {
                currentStage = ProcessStage.DEBIT_PROCESS;
                break;
            } else if (input.equals("4")) {
                currentStage = ProcessStage.CREDIT_PROCESS;
                break;
            } else if (input.equals("5")) {
                ApplicationContext.cleanAuthorizePlayer();
                currentStage = ProcessStage.SECURITY;
                break;
            } else if (input.equals("0")) {
                ApplicationContext.cleanAuthorizePlayer();
                currentStage = ProcessStage.EXIT;
                break;
            } else {
                outputData.output("Unknown command, try again.\n");
            }
        }
    }

    private static void securityProcess(InputData inputData, OutputData outputData) {
        final String firstMessage = "Please register or log in to the application.";
        final String menu = """
                Enter one number without spaces or other symbols:
                Register - 1
                Login - 2
                Exit the application - 3
                """;

        outputData.output(firstMessage);
        while(true) {
            outputData.output(menu);
            Object input = inputData.input();
            if (input.toString().equals("1")) {
                final String loginMsg = "Enter login:";
                outputData.output(loginMsg);
                String login = inputData.input().toString();
                final String passMsg = "Enter password. The password cannot be empty and must be between 4 and 32 characters long:";
                outputData.output(passMsg);
                String password = inputData.input().toString();

                Player registeredPlayer = controller.register(login, password);
                ApplicationContext.loadAuthorizePlayer(registeredPlayer);
                currentStage = ProcessStage.MAIN_MENU;
                break;
            } else if (input.toString().equals("2")) {
                final String loginMsg = "Enter login:";
                outputData.output(loginMsg);
                String login = inputData.input().toString();
                final String passMsg = "Enter password:";
                outputData.output(passMsg);
                String password = inputData.input().toString();

                Player authorizedPlayer = controller.authorize(login, password);
                ApplicationContext.loadAuthorizePlayer(authorizedPlayer);
                currentStage = ProcessStage.MAIN_MENU;
                break;
            } else if (input.toString().equals("3")) {
                currentStage = ProcessStage.EXIT;
                break;
            } else {
                outputData.output("Unknown command, try again.");
            }
        }
    }

    private static void exitProcess(OutputData outputData) {
        final String message = "Goodbye!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizePlayer();
    }
    private enum ProcessStage {
        /**
         * Security process stage.
         */
        SECURITY,
        /**
         * Main menu process stage.
         */
        MAIN_MENU,
        /**
         * Debit process process stage.
         */
        DEBIT_PROCESS,
        /**
         * Credit process process stage.
         */
        CREDIT_PROCESS,
        /**
         * Exit process stage.
         */
        EXIT
    }
}
