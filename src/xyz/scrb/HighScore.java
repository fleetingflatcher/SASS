package xyz.scrb;

public abstract class HighScore {
    private final Process owner;
    private final int value;

    public HighScore(Process owner, int value) {
        this.owner = owner;
        this.value = value;
    }

    public Process getOwner() {
        return owner;
    }

    public int getValue() {
        return value;
    }
}