import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    ArrayList<Player> players;
    Dice dice;
    Scanner sc;

    public Game(){
        players = new ArrayList<>();
        dice = new Dice();
        sc = new Scanner(System.in);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void run() {
        boolean playing = true;
        System.out.println("Welcome to Poker Dice!");
        System.out.println("---------------------------");
        System.out.println("Commands:");
        System.out.println("'Roll' - Rolls the dice");
        System.out.println("'1-5'  - toggle locked die");
        System.out.println("---------------------------");

        while(playing) {
            for(Player currentPlayer: players) {
                System.out.println("It is " + currentPlayer.getName() + "'s turn:");
                String input = sc.nextLine();

                if(input.equals("STOP")) playing = false;
            }
        }
    }
}
