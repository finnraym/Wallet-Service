package ru.egorov;

import ru.egorov.controller.MainController;
import ru.egorov.exception.*;
import ru.egorov.in.InputData;
import ru.egorov.model.Player;
import ru.egorov.model.Transaction;
import ru.egorov.out.OutputData;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ApplicationRunner {
    private static MainController controller;
    private static ProcessStage currentStage;

    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        controller = (MainController) ApplicationContext.getBean("controller");
        currentStage = ProcessStage.SECURITY;
        outputData.output("Добро пожаловать в приложение по управлению счетом для игроков!\n");

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
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                outputData.errOutput("Неизвестная ошибка. Подробнее " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeInput();
    }

    private static void creditProcess(InputData inputData, OutputData outputData) {
        double amount = transactionProcess(inputData, outputData);
        controller.creditTransaction(BigDecimal.valueOf(amount), UUID.randomUUID(), ApplicationContext.getAuthorizePlayer());
        outputData.output("Транзакция успешно проведена.\n");
        currentStage = ProcessStage.MAIN_MENU;
    }

    private static void debitProcess(InputData inputData, OutputData outputData) {
        double amount = transactionProcess(inputData, outputData);
        controller.debitTransaction(BigDecimal.valueOf(amount), UUID.randomUUID(), ApplicationContext.getAuthorizePlayer());
        outputData.output("Транзакция успешно проведена.\n");
        currentStage = ProcessStage.MAIN_MENU;
    }

    private static double transactionProcess(InputData inputData, OutputData outputData) {
        final String msg = "Введите сумму транзакции. Если хотите отменить, то напишите слово \"Отмена\":";
        outputData.output(msg);
        String request = inputData.input().toString();
        if (request.equalsIgnoreCase("отмена")) {
            currentStage = ProcessStage.MAIN_MENU;
            throw new TransactionOperationException("Отмена транзакции.");
        }
        double amount = 0;
        try {
            amount = Double.parseDouble(request);
        } catch (NumberFormatException e) {
            outputData.errOutput("Неверный формат. Пожалуйста введите число.\n");
        }
        return amount;
    }

    private static void menuProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                Выберите действие:
                Посмотреть свой баланс - 1
                Посмотреть историю транзакций - 2
                Снять средства - 3
                Пополнить средства - 4
                Выйти из аккаунта - 5
                Выйти из приложения - 0
                """;
        while (true) {
            outputData.output(menu);
            String input = inputData.input().toString();
            if (input.equals("1")) {
                BigDecimal balance = controller.showBalance(ApplicationContext.getAuthorizePlayer());
                String response = "Ваш баланс: " + balance.toString();
                outputData.output(response);
            } else if (input.equals("2")) {
                List<Transaction> history = controller.showTransactionsHistory(ApplicationContext.getAuthorizePlayer());
                if (history == null || history.isEmpty()) {
                    outputData.output("У вас пока не было проведенных транзакций.\n");
                } else {
                    outputData.output("История ваших транзакций:");
                    for (Transaction transaction : history) {
                        outputData.output(
                                "Идентификатор транзакции - " + transaction.getTransactionIdentifier() +
                                        ", тип транзакции - " + transaction.getType() +
                                        ", баланс до - " + transaction.getBalanceBefore() +
                                        ", значение транзакции - " + transaction.getAmount() +
                                        ", баланс после - " + transaction.getBalanceAfter()
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
                outputData.output("Неизвестная команда, попробуйте ещё раз.\n");
            }
        }
    }

    private static void securityProcess(InputData inputData, OutputData outputData) {
        final String firstMessage = "Пожалуйста, зарегистрируйтесь или авторизуйтесь в приложении.";
        final String menu = """
                Введите одно число без пробелов и других символов:
                Зарегистрироваться - 1
                Авторизоваться - 2
                Выйти из приложения - 3
                """;

        outputData.output(firstMessage);
        while(true) {
            outputData.output(menu);
            Object input = inputData.input();
            if (input.toString().equals("1")) {
                final String loginMsg = "Введите логин:";
                outputData.output(loginMsg);
                String login = inputData.input().toString();
                final String passMsg = "Введите пароль. Пароль не может быть пустым и должен быть длиной от 4 до 32 символов:";
                outputData.output(passMsg);
                String password = inputData.input().toString();

                Player registeredPlayer = controller.register(login, password);
                ApplicationContext.loadAuthorizePlayer(registeredPlayer);
                currentStage = ProcessStage.MAIN_MENU;
                break;
            } else if (input.toString().equals("2")) {
                final String loginMsg = "Введите логин:";
                outputData.output(loginMsg);
                String login = inputData.input().toString();
                final String passMsg = "Введите пароль:";
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
                outputData.output("Неизвестная команда, попробуйте ещё раз.");
            }
        }
    }

    private static void exitProcess(OutputData outputData) {
        final String message = "До свидания!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizePlayer();
    }
    private enum ProcessStage {
        SECURITY, MAIN_MENU, DEBIT_PROCESS, CREDIT_PROCESS, EXIT
    }
}
