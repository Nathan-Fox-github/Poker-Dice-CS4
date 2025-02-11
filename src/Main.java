import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().createAndShowGUI();
        });
    }

    private JFrame frame;
    private JPanel pages;
    private CardLayout cardLayout;
    private Game game;
    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private JSpinner roundsSpinner;

    private void createAndShowGUI() {
        frame = new JFrame("Poker Dice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        // Set up CardLayout for pages
        cardLayout = new CardLayout();
        pages = new JPanel(cardLayout);

        // Create the setup and game pages
        JPanel setupPage = createSetupPage();
        JPanel gamePage = new JPanel(new BorderLayout());  // placeholder; will be replaced with GamePanel later

        pages.add(setupPage, "Setup");
        pages.add(gamePage, "Game");

        frame.setLayout(new BorderLayout());
        frame.add(pages, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createSetupPage() {
        // Use a BorderLayout with padding for a modern look
        JPanel setupPage = new JPanel(new BorderLayout(10, 10));
        setupPage.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Header ---
        JLabel headerLabel = new JLabel("Game Setup", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        setupPage.add(headerLabel, BorderLayout.NORTH);

        // --- Center Panel: Divided into Players and Game Options ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Left Panel: Players
        JPanel playersPanel = new JPanel(new BorderLayout(5, 5));
        playersPanel.setBorder(BorderFactory.createTitledBorder("Players"));

        // Panel for adding a new player
        JPanel addPlayerPanel = new JPanel(new BorderLayout(5, 5));
        JTextField playerNameField = new JTextField();
        JButton addPlayerButton = new JButton("Add Player");
        addPlayerButton.addActionListener(e -> {
            String name = playerNameField.getText().trim();
            if (!name.isEmpty() && playerListModel.getSize() < 6) {
                playerListModel.addElement(name);
                playerNameField.setText("");
            } else if (playerListModel.getSize() >= 6) {
                JOptionPane.showMessageDialog(frame, "Max players reached.");
            } else {
                JOptionPane.showMessageDialog(frame, "Enter a valid name.");
            }
        });
        addPlayerPanel.add(playerNameField, BorderLayout.CENTER);
        addPlayerPanel.add(addPlayerButton, BorderLayout.EAST);
        playersPanel.add(addPlayerPanel, BorderLayout.NORTH);

        // The list of players in a scroll pane
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        JScrollPane playerScrollPane = new JScrollPane(playerList);
        playerScrollPane.setPreferredSize(new Dimension(200, 150));
        playersPanel.add(playerScrollPane, BorderLayout.CENTER);

        // Right Panel: Game Options
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Game Options"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel roundsLabel = new JLabel("Number of Rounds:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        optionsPanel.add(roundsLabel, gbc);

        roundsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
        gbc.gridx = 1;
        optionsPanel.add(roundsSpinner, gbc);

        // Add both panels to the center panel
        centerPanel.add(playersPanel);
        centerPanel.add(optionsPanel);
        setupPage.add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel: Start Game Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            if (playerListModel.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter at least 1 player.");
            } else {
                int rounds = (Integer) roundsSpinner.getValue();
                game = new Game(rounds);
                for (int i = 0; i < playerListModel.size(); i++) {
                    game.addPlayer(new Player(playerListModel.get(i)));
                }
                // Create the GamePanel (which contains the game loop logic)
                GamePanel gamePanel = new GamePanel(game);
                // Replace the placeholder game page with our game panel
                pages.add(gamePanel, "Game");
                cardLayout.show(pages, "Game");
            }
        });
        bottomPanel.add(startButton);
        setupPage.add(bottomPanel, BorderLayout.SOUTH);

        return setupPage;
    }
}
