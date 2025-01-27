public class Main {
    public static void main(String[] args) {
        Game game = new Game(3);

        game.addPlayer(new Player("Nathan"));
        game.addPlayer(new Player("Kira"));

        game.run();
    }
}