public class Dice {
    private final Die[] Dice;
    private int[] values;
    private final int SIZE;

    public Dice() {
        SIZE = 5;
        this.Dice = new Die[SIZE];
        values = new int[SIZE];

        Dice[0] = new Die();
        Dice[1] = new Die();
        Dice[2] = new Die();
        Dice[3] = new Die();
        Dice[4] = new Die();

        values[0] = Dice[0].getCurrentValue();
        values[1] = Dice[1].getCurrentValue();
        values[2] = Dice[2].getCurrentValue();
        values[3] = Dice[3].getCurrentValue();
        values[4] = Dice[4].getCurrentValue();
    }

    public int[] roll(boolean[] exclude) {
        int[] outcome = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            if (!exclude[i]) outcome[i] = Dice[i].roll();
            else {
                outcome[i] = Dice[i].getCurrentValue();
            }
        }

        values = outcome;
        return outcome;
    }

    private int[] getCounts() {
        int[] counts = new int[6];
        for(int value: values) {
            counts[value - 1]++;
        }
        return counts;
    }

    public String getCombo() {
        int[] counts = getCounts();
        int highestCombo = 0;

        for(int i = 0; i < counts.length; i++) {
            if(counts[i] == 5) highestCombo = 7; //Five of a Kind
            else if(counts[i] == 4) highestCombo = Math.max(highestCombo, 6); //Four of a Kind
            else if(counts[i] == 3) {
                for (int count : counts) {
                    if (count == 2) highestCombo = Math.max(highestCombo, 5); //Full House
                }
                highestCombo = Math.max(highestCombo, 3); //Three of a Kind
            }
            else if(counts[i] == 2) {
                for(int j = i + 1; j < counts.length; j++) {
                    if(counts[j] == 2) highestCombo = Math.max(highestCombo, 2); //Two Pairs
                }
                highestCombo = Math.max(highestCombo, 1); //One Pair
            }
            else if (counts[i] == 1) {
                int seqCount = 1;
                for(int j = i + 1; j < counts.length; j++) {
                    if(counts[j] == 1) seqCount++;
                    else break;
                }
                if(seqCount == 5) highestCombo = Math.max(highestCombo, 4); //Straight
            }
        }

        String[] combos = new String[] {
                "Nothing",
                "One Pair",
                "Two Pairs",
                "Three of a Kind",
                "Straight",
                "Full House",
                "Four of a Kind",
                "Five of a Kind"
        };

        return combos[highestCombo];
    }
}
