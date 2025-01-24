public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Die one = new Die();
        for(int i = 0; i < 10; i++) {
            System.out.println(one.roll());
        }
    }
}