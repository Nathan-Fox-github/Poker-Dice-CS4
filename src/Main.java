public class Main {
    public static void main(String[] args) {
        Game game = new Game();

        game.addPlayer(new Player("Ishan"));
        game.addPlayer(new Player("Nathan"));

        game.run();
    }
}