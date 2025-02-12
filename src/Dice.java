/**
 * The Dice class represents a collection of 5 dice.
 * It handles rolling, locking/unlocking dice, and determining dice combinations (e.g., pairs, full house).
 */
public class Dice {
    private final Die[] Dice;     // Array of Die objects representing the dice
    private int[] values;         // Array storing the current values of each die
    private boolean[] exclude;    // Boolean array to track which dice are "locked" (excluded from rolling)
    private final int SIZE;       // Number of dice (fixed at 5)

    /**
     * Constructs a Dice object with 5 individual dice.
     * Initializes all dice with random values and sets all dice to be rollable (unlocked).
     */
    public Dice() {
        SIZE = 5;
        this.Dice = new Die[SIZE];
        values = new int[SIZE];
        exclude = new boolean[SIZE];

        // Initialize each die
        Dice[0] = new Die();
        Dice[1] = new Die();
        Dice[2] = new Die();
        Dice[3] = new Die();
        Dice[4] = new Die();

        // Store the initial random values from each die
        values[0] = Dice[0].getCurrentValue();
        values[1] = Dice[1].getCurrentValue();
        values[2] = Dice[2].getCurrentValue();
        values[3] = Dice[3].getCurrentValue();
        values[4] = Dice[4].getCurrentValue();
    }

    /**
     * Rolls all unlocked dice and returns the updated values.
     * Locked dice retain their previous values.
     *
     * @return An array containing the current values of all dice after rolling.
     */
    public int[] roll() {
        int[] outcome = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            if (!exclude[i]) outcome[i] = Dice[i].roll();
            else outcome[i] = Dice[i].getCurrentValue();
        }

        values = outcome;
        return outcome;
    }

    /**
     * Toggles the lock state of a die at the specified index.
     * Locked dice are excluded from future rolls until unlocked.
     *
     * @param index The index of the die to lock/unlock (0 to 4).
     */
    public void toggleDieLock(int index) {
        exclude[index] = !exclude[index];
    }

    /**
     * Unlocks all dice, allowing them to be rolled again.
     */
    public void unlockAllDie() {
        exclude = new boolean[SIZE];
    }

    /**
     * Counts the occurrences of each face value (1 through 6) among the dice.
     *
     * @return An array where the index represents the die face (0 = 1, 1 = 2, ..., 5 = 6),
     *         and the value at each index is the count of that face.
     */
    private int[] getCounts() {
        int[] counts = new int[6];
        for (int value : values) {
            counts[value - 1]++;
        }
        return counts;
    }

    /**
     * Determines the highest-ranking combination (e.g., pair, straight, full house) based on the current dice values.
     *
     * @return A string representing the best combination found.
     */
    public String getCombo() {
        int[] counts = getCounts();
        int highestCombo = 0;

        for (int i = 0; i < counts.length; i++) {
            if (counts[i] == 5) highestCombo = 7;  // Five of a Kind
            else if (counts[i] == 4) highestCombo = Math.max(highestCombo, 6);  // Four of a Kind
            else if (counts[i] == 3) {
                for (int count : counts) {
                    if (count == 2) highestCombo = Math.max(highestCombo, 5);  // Full House
                }
                highestCombo = Math.max(highestCombo, 3);  // Three of a Kind
            }
            else if (counts[i] == 2) {
                for (int j = i + 1; j < counts.length; j++) {
                    if (counts[j] == 2) highestCombo = Math.max(highestCombo, 2);  // Two Pairs
                }
                highestCombo = Math.max(highestCombo, 1);  // One Pair
            }
            else if (counts[i] == 1) {  // Check for a straight (sequence of consecutive numbers)
                int seqCount = 1;
                for (int j = i + 1; j < counts.length; j++) {
                    if (counts[j] == 1) seqCount++;
                    else break;
                }
                if (seqCount == 5) highestCombo = Math.max(highestCombo, 4);  // Straight
            }
        }

        // Mapping combination rankings to their string descriptions
        String[] combos = new String[] {
                "Nothing",
                "One Pair",
                "Two Pairs",
                "Three of a Kind",
                "Straight",
                "Full House",
                "Four of a Kind",
                "Five of a Kind"
        };

        return combos[highestCombo];
    }
}
