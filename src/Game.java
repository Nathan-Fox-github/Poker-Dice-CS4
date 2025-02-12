import java.util.ArrayList;

/**
 * The Game class manages the overall game logic, including players,
 * dice rolls, and the number of turns in the game.
 */
public class Game {
    private final ArrayList<Player> players; // List to store all players in the game
    public final Dice dice;                 // Dice object used for rolling in the game
    private final int turns;               // Total number of turns for the game

    /**
     * Constructs a new Game instance with the specified number of turns.
     * @param turns The number of turns each player will have.
     */
    public Game(int turns){
        players = new ArrayList<>();
        dice = new Dice();
        this.turns = turns;
    }

    /**
     * Adds a player to the game.
     * @param player The player to be added to the player list.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Rolls the dice and returns the result as an array of integers.
     * @return An array of integers representing the dice roll outcomes.
     */
    public int[] roll() {
        return dice.roll();
    }

    /**
     * Retrieves the current dice combination (combo) based on the dice values.
     * This could represent combinations like pairs, straights, etc.
     * @return A string representing the current combo.
     */
    public String getCombo() {
        return dice.getCombo();
    }

    /**
     * Gets the total number of turns set for the game.
     * @return The total number of turns.
     */
    public int getTurns() {
        return turns;
    }

    /**
     * Retrieves the list of players participating in the game.
     * @return An ArrayList of Player objects.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }
}
