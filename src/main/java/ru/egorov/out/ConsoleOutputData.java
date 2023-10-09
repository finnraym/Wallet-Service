package ru.egorov.out;

public class ConsoleOutputData implements OutputData {
    @Override
    public void output(Object data) {
        System.out.println(data.toString());
    }

    @Override
    public void errOutput(Object data) {
        System.err.println(data.toString());
    }
}
