/**
 * The Player class represents a player in the game.
 * It stores the player's name and score, and provides methods to access and modify these attributes.
 */
public class Player {
    private int score;        // Holds the player's current score
    private final String name; // The player's name

    /**
     * Constructs a Player with the specified name and initializes the score to 0.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        score = 0;
    }

    /**
     * Retrieves the player's current score.
     *
     * @return The player's score as an integer.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retrieves the player's name.
     *
     * @return The player's name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds the specified number of points to the player's score.
     *
     * @param points The number of points to add (can be positive or negative).
     */
    public void addScore(int points) {
        score += points;
    }
}
