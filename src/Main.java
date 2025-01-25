import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Player playerOne = new Player("Ishan");
        Dice dice = new Dice();

        boolean[] keep = new boolean[] {false, false, false, true, true};
        System.out.println(playerOne.getName() + " made a roll 1 of: " + Arrays.toString(dice.roll(keep)));
        System.out.println(playerOne.getName() + " made a roll 2 of: " + Arrays.toString(dice.roll(keep)));
        System.out.println(playerOne.getName() + " made a roll 3 of: " + Arrays.toString(dice.roll(keep)));
    }
}