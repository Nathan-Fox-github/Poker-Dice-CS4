public class Player {
    private int score;
    private final String name;

    public Player() {
        name = "John Doe";
        score = 0;
    }

    public Player(String name) {
        this.name = name;
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void addScore(int points) {
        score += points;
    }
}
