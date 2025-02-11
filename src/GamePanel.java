import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

class GamePanel extends JPanel implements ActionListener {
    private Game game;
    // Game state
    private int currentRound = 1;
    private int currentPlayerIndex = 0;
    private int currentRoll = 1;
    private final int maxRolls = 3;

    // GUI components
    private JLabel roundLabel;
    private JLabel currentPlayerLabel;
    private JLabel comboLabel;
    private JPanel scoreboardPanel;
    private JButton rollButton;
    private JButton contButton;
    private JCheckBox[] lockCheckBoxes;

    // Custom dice animation panel
    private DiceAnimationPanel diceAnimationPanel;

    public GamePanel(Game game) {
        this.game = game;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        initComponents();
        updateDisplay();
    }

    private void initComponents() {
        // --- Top Panel: Header ---
        JPanel headerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        roundLabel = new JLabel();
        roundLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        comboLabel = new JLabel("Combo: ");
        comboLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(roundLabel);
        headerPanel.add(currentPlayerLabel);
        headerPanel.add(comboLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Center Panel: Dice Animation & Locks ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Dice animation panel (remains unchanged in functionality)
        diceAnimationPanel = new DiceAnimationPanel();
        centerPanel.add(diceAnimationPanel);

        // Panel for dice locks (checkboxes)
        JPanel lockPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        lockCheckBoxes = new JCheckBox[5];
        for (int i = 0; i < 5; i++) {
            lockCheckBoxes[i] = new JCheckBox();
            lockCheckBoxes[i].setPreferredSize(new Dimension(50, 50));
            final int index = i;
            lockCheckBoxes[i].addActionListener(e -> {
                // Toggle the lock state on the corresponding die.
                game.dice.toggleDieLock(index);
            });
            lockPanel.add(lockCheckBoxes[i]);
        }
        centerPanel.add(lockPanel);
        add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel: Control Buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rollButton = new JButton("Roll");
        contButton = new JButton("Cont");
        rollButton.addActionListener(this);
        contButton.addActionListener(this);
        bottomPanel.add(rollButton);
        bottomPanel.add(contButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- East Panel: Scoreboard ---
        scoreboardPanel = new JPanel();
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
        scoreboardPanel.setBorder(BorderFactory.createTitledBorder("Scoreboard"));
        // Setting a fixed width; height adjusts automatically.
        scoreboardPanel.setPreferredSize(new Dimension(200, 0));
        updateScoreboard();
        add(scoreboardPanel, BorderLayout.EAST);
    }

    // Update header labels and clear the dice animation at the start of each turn.
    private void updateDisplay() {
        roundLabel.setText("Round: " + currentRound + " / " + game.getTurns());
        currentPlayerLabel.setText("Current Player: " + game.getPlayers().get(currentPlayerIndex).getName());
        diceAnimationPanel.clearDice();
        comboLabel.setText("Combo: ");
    }

    // Update the scoreboard with each player's current score.
    private void updateScoreboard() {
        scoreboardPanel.removeAll();
        for (Player p : game.getPlayers()) {
            JLabel scoreLabel = new JLabel(p.getName() + ": " + p.getScore());
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            scoreboardPanel.add(scoreLabel);
        }
        scoreboardPanel.revalidate();
        scoreboardPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == rollButton) {
            if (currentRoll <= maxRolls) {
                // Roll the dice and animate.
                int[] dice = game.roll();
                diceAnimationPanel.animateRoll(dice);
                // Update combo info (logic stub).
                String combo = game.getCombo();
                comboLabel.setText("Combo: " + combo);
                currentRoll++;
            } else {
                JOptionPane.showMessageDialog(this, "Maximum rolls reached. Click 'Cont' to continue.");
            }
        } else if (src == contButton) {
            // End current player's turn and update their score.
            String combo = game.getCombo();
            int scoreToAdd = getScoreForCombo(combo);
            game.getPlayers().get(currentPlayerIndex).addScore(scoreToAdd);
            JOptionPane.showMessageDialog(this, game.getPlayers().get(currentPlayerIndex).getName()
                    + " scored " + scoreToAdd + " points for " + combo);
            game.dice.unlockAllDie();
            // Reset checkboxes.
            for (JCheckBox cb : lockCheckBoxes) {
                cb.setSelected(false);
            }
            nextTurn();
            updateScoreboard();
        }
    }

    // Determine score based on the combo string.
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

    // Manage turn progression.
    private void nextTurn() {
        currentRoll = 1;
        currentPlayerIndex++;
        if (currentPlayerIndex >= game.getPlayers().size()) {
            currentPlayerIndex = 0;
            currentRound++;
            if (currentRound > game.getTurns()) {
                String winner = determineWinner();
                JOptionPane.showMessageDialog(this, "Game Over! Winner: " + winner);
                rollButton.setEnabled(false);
                contButton.setEnabled(false);
                return;
            }
        }
        updateDisplay();
    }

    // Determine the winning player.
    private String determineWinner() {
        Player winner = game.getPlayers().get(0);
        for (Player p : game.getPlayers()) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        return winner.getName() + " with " + winner.getScore() + " points";
    }

    //////////////////////////////////////////////////////////////////////////////
    // Inner class for animating 5 dice.
    static class DiceAnimationPanel extends JPanel {
        private int[] finalDiceValues = new int[]{1, 1, 1, 1, 1};
        private int animationFrame = 0;
        private Timer timer;
        private final int TOTAL_FRAMES = 16;
        private boolean animating = false;

        public DiceAnimationPanel() {
            setPreferredSize(new Dimension(600, 150));
        }

        // Clears the dice display.
        public void clearDice() {
            animating = false;
            repaint();
        }

        // Animate the roll using the given dice values.
        public void animateRoll(int[] diceValues) {
            this.finalDiceValues = diceValues;
            animationFrame = 0;
            animating = true;
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            timer = new Timer(150, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animationFrame++;
                    if (animationFrame >= TOTAL_FRAMES) {
                        animating = false;
                        timer.stop();
                    }
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int dieSize = 100;
            int gap = 20;
            int startX = 20;
            int y = 20;  // vertical offset for drawing dice
            for (int i = 0; i < 5; i++) {
                int x = startX + i * (dieSize + gap);
                if (animating) {
                    // Cycle through dice faces during animation.
                    int spinningFace = (animationFrame % 6) + 1;
                    drawDie(g, spinningFace, x, y, dieSize);
                } else {
                    // Show the final value.
                    drawDie(g, finalDiceValues[i], x, y, dieSize);
                }
            }
        }

        // Draw a single die with pips based on the die value.
        private void drawDie(Graphics g, int value, int x, int y, int size) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, size, size);
            switch (value) {
                case 1:
                    g.fillOval(x + size/2 - 7, y + size/2 - 7, 15, 15);
                    break;
                case 2:
                    g.fillOval(x + size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    break;
                case 3:
                    g.fillOval(x + size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + size/2 - 7, y + size/2 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    break;
                case 4:
                    g.fillOval(x + size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    break;
                case 5:
                    g.fillOval(x + size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + size/2 - 7, y + size/2 - 7, 15, 15);
                    g.fillOval(x + size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    break;
                case 6:
                    g.fillOval(x + size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + size/4 - 7, y + size/2 - 7, 15, 15);
                    g.fillOval(x + size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + size/4 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + size/2 - 7, 15, 15);
                    g.fillOval(x + 3*size/4 - 7, y + 3*size/4 - 7, 15, 15);
                    break;
            }
        }
    }
}
