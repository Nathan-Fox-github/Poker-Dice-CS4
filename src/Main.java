import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
//        Player playerOne = new Player("Ishan");
        Dice dice = new Dice();

        System.out.println(Arrays.toString(dice.roll(new boolean[5])) + " has the combo: " + dice.getCombo());
        System.out.println(Arrays.toString(dice.roll(new boolean[5])) + " has the combo: " + dice.getCombo());
        System.out.println(Arrays.toString(dice.roll(new boolean[5])) + " has the combo: " + dice.getCombo());
        System.out.println(Arrays.toString(dice.roll(new boolean[5])) + " has the combo: " + dice.getCombo());
    }
}