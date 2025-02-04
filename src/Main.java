import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Main class for the Poker Dice Swing application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().createAndShowGUI();
        });
    }

    // Main frame and CardLayout pages
    private JFrame frame;
    private JPanel pages;
    private CardLayout cardLayout;

    // Setup page components
    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private JSpinner roundsSpinner;

    // Game instance (will be created when the game starts)
    private Game game;

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
        JPanel setupPage = new JPanel(new GridLayout(1,2));
        JPanel inputPanel = new JPanel(new GridLayout(3, 1));

        // --- Player List Panel ---
        JPanel playerListPanel = new JPanel();
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        JScrollPane scrollPane = new JScrollPane(playerList);

        JButton addPlayerButton = new JButton("Add Player");
        addPlayerButton.addActionListener(e -> {
            if (playerListModel.size() < 6) {
                String name = JOptionPane.showInputDialog(frame, "Enter player name:");
                if (name != null && !name.trim().isEmpty()) {
                    playerListModel.addElement(name.trim());
                } else {
                    JOptionPane.showMessageDialog(frame, "Enter a valid name.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Max players reached.");
            }
        });

        // --- Rounds Spinner ---
        roundsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));

        // --- Start Game Button ---
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            if (playerListModel.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter at least 1 player.");
            } else {
                // Create game with chosen rounds and add players.
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

        // Layout the setup page
        playerListPanel.add(scrollPane);
        inputPanel.add(addPlayerButton);
        inputPanel.add(roundsSpinner);
        inputPanel.add(startButton);
        setupPage.add(inputPanel);
        setupPage.add(playerListPanel);

        return setupPage;
    }
}

/**
 * The panel that contains the game loop logic, now integrated with Swing controls.
 */
class GamePanel extends JPanel implements ActionListener {
    private Game game;

    // Game state
    private int currentRound = 1;
    private int currentPlayerIndex = 0;
    private int currentRoll = 1;
    private final int maxRolls = 3;

    // GUI components for the game
    private JLabel roundLabel;
    private JLabel currentPlayerLabel;
    private JLabel diceLabel;
    private JLabel comboLabel;
    private JPanel scoreboardPanel;
    private JButton rollButton;
    private JButton contButton;
    private JCheckBox[] lockCheckBoxes; // One for each die

    public GamePanel(Game game) {
        this.game = game;
        setLayout(new BorderLayout());
        initComponents();
        updateDisplay();
    }

    private void initComponents() {
        // Top panel shows round and current player info
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        roundLabel = new JLabel();
        currentPlayerLabel = new JLabel();
        topPanel.add(roundLabel);
        topPanel.add(currentPlayerLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center panel displays dice roll and combo result
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        diceLabel = new JLabel("Dice: ");
        diceLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        centerPanel.add(diceLabel);

        // Panel for dice locks (checkboxes for each die)
        JPanel lockPanel = new JPanel();
        lockCheckBoxes = new JCheckBox[5];
        for (int i = 0; i < 5; i++) {
            lockCheckBoxes[i] = new JCheckBox("Lock " + (i + 1));
            final int index = i;
            lockCheckBoxes[i].addActionListener(e -> {
                // Toggle lock in the game’s Dice object when checkbox state changes.
                game.dice.toggleDieLock(index);
            });
            lockPanel.add(lockCheckBoxes[i]);
        }
        centerPanel.add(lockPanel);

        comboLabel = new JLabel("Combo: ");
        centerPanel.add(comboLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with control buttons and scoreboard
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Control buttons
        JPanel controlPanel = new JPanel();
        rollButton = new JButton("Roll");
        contButton = new JButton("Cont");
        rollButton.addActionListener(this);
        contButton.addActionListener(this);
        controlPanel.add(rollButton);
        controlPanel.add(contButton);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        // Scoreboard on the side
        scoreboardPanel = new JPanel();
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
        updateScoreboard();
        bottomPanel.add(scoreboardPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Update labels for round, current player, and dice
    private void updateDisplay() {
        roundLabel.setText("Round: " + currentRound + " / " + game.getTurns());
        currentPlayerLabel.setText("Current Player: " + game.getPlayers().get(currentPlayerIndex).getName());
        // Initially, no dice roll has happened.
        diceLabel.setText("Dice: [?, ?, ?, ?, ?]");
        comboLabel.setText("Combo: ");
    }

    // Update the scoreboard with players' scores
    private void updateScoreboard() {
        scoreboardPanel.removeAll();
        for (Player p : game.getPlayers()) {
            scoreboardPanel.add(new JLabel(p.getName() + ": " + p.getScore()));
        }
        scoreboardPanel.revalidate();
        scoreboardPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == rollButton) {
            if (currentRoll <= maxRolls) {
                int[] dice = game.roll();
                diceLabel.setText("Dice: " + arrayToString(dice));
                // Get combo result (stub – replace with your own logic)
                String combo = game.getCombo();
                comboLabel.setText("Combo: " + combo);
                currentRoll++;
            } else {
                JOptionPane.showMessageDialog(this, "Maximum rolls reached. Click 'Cont' to continue.");
            }
        } else if (src == contButton) {
            // End current player's turn
            String combo = game.getCombo();
            int scoreToAdd = getScoreForCombo(combo);
            game.getPlayers().get(currentPlayerIndex).addScore(scoreToAdd);
            JOptionPane.showMessageDialog(this, game.getPlayers().get(currentPlayerIndex).getName()
                    + " scored " + scoreToAdd + " points for " + combo);
            game.dice.unlockAllDie();
            // Reset checkboxes
            for (JCheckBox cb : lockCheckBoxes) {
                cb.setSelected(false);
            }
            // Prepare for next turn
            nextTurn();
            updateScoreboard();
        }
    }

    // Simple scoring based on the combo string.
    private int getScoreForCombo(String combo) {
        return switch (combo) {
            case "Five of a Kind" -> 100;
            case "Straight" -> 80;
            case "Four of a Kind" -> 50;
            case "Full House" -> 30;
            case "Three of a Kind" -> 20;
            case "Two Pairs" -> 10;
            case "One Pair" -> 5;
            default -> 0;
        };
    }

    // Proceed to the next player's turn or next round.
    private void nextTurn() {
        currentRoll = 1;
        currentPlayerIndex++;
        if (currentPlayerIndex >= game.getPlayers().size()) {
            currentPlayerIndex = 0;
            currentRound++;
            if (currentRound > game.getTurns()) {
                // Game over!
                String winner = determineWinner();
                JOptionPane.showMessageDialog(this, "Game Over! Winner: " + winner);
                rollButton.setEnabled(false);
                contButton.setEnabled(false);
                return;
            }
        }
        updateDisplay();
    }

    private String determineWinner() {
        Player winner = game.getPlayers().getFirst();
        for (Player p : game.getPlayers()) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        return winner.getName() + " with " + winner.getScore() + " points";
    }

    // Utility to display an int array nicely.
    private String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}