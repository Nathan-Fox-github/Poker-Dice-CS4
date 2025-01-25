public class Dice {
    private final Die[] Dice;
    private final int SIZE;

    public Dice() {
        SIZE = 5;
        this.Dice = new Die[SIZE];

        Dice[0] = new Die();
        Dice[1] = new Die();
        Dice[2] = new Die();
        Dice[3] = new Die();
        Dice[4] = new Die();
    }

    public int[] roll(boolean[] exclude) {
        int[] outcome = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            if (!exclude[i]) outcome[i] = Dice[i].roll();
            else {
                outcome[i] = Dice[i].getCurrentValue();
            }
        }

        return outcome;
    }
}
