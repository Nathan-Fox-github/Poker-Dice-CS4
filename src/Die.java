/**
 * The Die class represents a single die with a fixed number of sides (6 by default).
 * It can be rolled to generate a random value between 1 and 6.
 */
public class Die {
    private final int sides;       // Number of sides on the die (fixed at 6)
    private int currentValue;      // The current value showing on the die

    /**
     * Constructs a new Die object with 6 sides.
     * Initializes the current value with a random number between 1 and 6.
     */
    public Die() {
        sides = 6;
        currentValue = (int)(Math.random() * sides) + 1;
    }

    /**
     * Rolls the die to generate a new random value between 1 and 6.
     * Updates and returns the new current value.
     *
     * @return The result of the die roll (an integer from 1 to 6).
     */
    public int roll() {
        currentValue = (int)(Math.random() * sides) + 1;
        return currentValue;
    }

    /**
     * Retrieves the current face value of the die without rolling it.
     *
     * @return The current value showing on the die.
     */
    public int getCurrentValue() {
        return currentValue;
    }
}
