package xyz.perif;

public class PeripheralOccupiedException extends Exception {
    String message;
    public PeripheralOccupiedException (String s) {
        message = s;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
