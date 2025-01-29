import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // Create the main frame
        JFrame frame = new JFrame("Poker Dice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Player list
        JPanel playerListPanel = new JPanel();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> playerList = new JList<>(listModel);
        JButton addPlayerButton = new JButton("Add Player");

        addPlayerButton.addActionListener(e -> {
            if(listModel.size() < 6) {
                String name = JOptionPane.showInputDialog(frame, "Enter player name:");
                if(name != null && !name.isEmpty()) {
                    listModel.addElement(name.trim());
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Enter a valid name.");
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, "Max players has been reached.");
            }
        });

        playerListPanel.setLayout(new BorderLayout());
        playerListPanel.add(playerList, BorderLayout.CENTER);
        playerListPanel.add(addPlayerButton, BorderLayout.SOUTH);

        // Rounds Input
        JSpinner rounds = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));

        // Start game
        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            if(listModel.isEmpty()) JOptionPane.showMessageDialog(frame, "Enter at least 1 player.");
            else {
                Game game = new Game((int) rounds.getValue());
                for(int i = 0; i < listModel.size(); i++) {
                    game.addPlayer(new Player(listModel.get(i)));
                }

                game.run();
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(playerListPanel, BorderLayout.CENTER);
        frame.add(start, BorderLayout.SOUTH);
        frame.add(rounds, BorderLayout.NORTH);

//        frame.pack();
        frame.setVisible(true);
    }
}
