import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private ArrayList<Player> players;
    private final Dice dice;
    private final Scanner sc;
    private final int turns;

    public Game(){
        players = new ArrayList<>();
        dice = new Dice();
        sc = new Scanner(System.in);
        turns = 10;
    }

    public Game(int turns){
        players = new ArrayList<>();
        dice = new Dice();
        sc = new Scanner(System.in);
        this.turns = turns;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void run() {
        boolean playing = true;
        int round = 1;

        System.out.println("Welcome to Poker Dice!");
        System.out.println("---------------------------");
        System.out.println("Commands:");
        System.out.println("'Roll' - Rolls the dice");
        System.out.println("'1-5'  - toggle locked die");
        System.out.println("'Cont' - End your turn");
        System.out.println("---------------------------");

        for(int i = 0; i < turns; i++) {
            System.out.println("Player Scores (round " + round + "/" + turns + "):");
            for(Player player: players) {
                System.out.println(player.getName() + " - " + player.getScore());
            }
            System.out.println("---------------------------");

            for(Player currentPlayer: players) {
                int rollCount = 1;

                System.out.println("It is " + currentPlayer.getName() + "'s turn:");
                System.out.println("Dice: " + Arrays.toString(dice.roll()) + " " + dice.getCombo());

                while(rollCount < 3 && playing) {
                    String input = sc.nextLine();

                    switch (input) {
                        case "STOP":
                            playing = false;
                            break;
                        case "Roll":
                            rollCount++;
                            System.out.println(Arrays.toString(dice.roll()) + " " + dice.getCombo());
                            break;
                        case "Cont":
                            rollCount = 3;
                            break;
                        default:
                            int index = Integer.parseInt(input, 10);
                            dice.toggleDieLock(index-1);
                            System.out.println("Toggled die " + index + " lock");
                            break;
                    }
                }
                if(!playing) break;

                String combo = dice.getCombo();
                switch (combo) {
                    case ("Five of a Kind") -> currentPlayer.addScore(100);
                    case ("Four of a Kind") -> currentPlayer.addScore(50);
                    case ("Full House") -> currentPlayer.addScore(30);
                    case ("Three of a Kind") -> currentPlayer.addScore(20);
                    case ("Two Pairs") -> currentPlayer.addScore(10);
                    case ("One Pair") -> currentPlayer.addScore(5);
                    default -> {}
                }
                dice.unlockAllDie();
            }
            round++;
            System.out.println("---------------------------");
        }

        System.out.println("Final Scores:");
        Player winner = players.getFirst();
        for(Player player: players) {
            if(player.getScore() > winner.getScore()) winner = player;
            System.out.println(player.getName() + " - " + player.getScore());
        }

        System.out.println("\nThe winner is " + winner.getName() + " with " + winner.getScore() + " points!");
    }
}
