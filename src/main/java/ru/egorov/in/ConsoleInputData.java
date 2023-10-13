package ru.egorov.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The type Console input data.
 */
public class ConsoleInputData implements InputData {

    private final BufferedReader reader;

    /**
     * Instantiates a new Console input data.
     */
    public ConsoleInputData() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String input() {
        String readData;
        try {
            readData = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readData;
    }

    @Override
    public void closeInput() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
