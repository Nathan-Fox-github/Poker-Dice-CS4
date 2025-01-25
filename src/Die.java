public class Die {
    private final int sides;
    private int currentValue;

    public Die(int sides) {
        this.sides = sides;
        currentValue = (int)(Math.random() * sides) + 1;
    }

    public Die() {
        sides = 6;
        currentValue = (int)(Math.random() * sides) + 1;
    }

    public int roll() {
        currentValue = (int)(Math.random() * sides) + 1;
        return currentValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }
}
