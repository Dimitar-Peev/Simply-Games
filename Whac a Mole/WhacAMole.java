import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 650; //50 for the text panel on top

    JFrame frame = new JFrame("Mario: Whac A Mole");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[] board = new JButton[9];
    JButton resetButton = new JButton("Reset");
    ImageIcon moleIcon;
    ImageIcon plantIcon;

    JButton currMoleTile;
    JButton[] currPlantTiles = new JButton[2];

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;
    int score = 0;
    int highScore = 0;
    boolean gameOver = false;

    WhacAMole() {
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        updateScoreLabel();
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        textPanel.add(resetButton, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        // boardPanel.setBackground(Color.black);
        frame.add(boardPanel);

        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            // tile.setIcon(plantIcon);

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;
                        if (score > highScore) {
                            highScore = score;
                        }
                        currMoleTile.setIcon(null);
                        currMoleTile = null;
                        updateScoreLabel();
                    }
                    else if (isPlantTile(tile)) {
                        gameOver = true;
                        updateScoreLabel();
                        setMoleTimer.stop();
                        setPlantTimer.stop();
                        for (int i = 0; i < board.length; i++) {
                            board[i].setEnabled(false);
                        }
                    }
                }
            });
        }

        setMoleTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //remove icon from current tile
                if (currMoleTile != null) {
                    currMoleTile.setIcon(null);
                    currMoleTile = null;
                }

                JButton tile = getAvailableTile();
                currMoleTile = tile;
                currMoleTile.setIcon(moleIcon);
            }
        });

        setPlantTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < currPlantTiles.length; i++) {
                    if (currPlantTiles[i] != null) {
                        currPlantTiles[i].setIcon(null);
                        currPlantTiles[i] = null;
                    }
                }

                for (int i = 0; i < currPlantTiles.length; i++) {
                    JButton tile = getAvailableTile();
                    currPlantTiles[i] = tile;
                    tile.setIcon(plantIcon);
                }
            }
        });

        setMoleTimer.start();
        setPlantTimer.start();
        frame.setVisible(true);
    }

    private JButton getAvailableTile() {
        while (true) {
            JButton tile = board[random.nextInt(board.length)];
            if (tile != currMoleTile && !isPlantTile(tile)) {
                return tile;
            }
        }
    }

    private boolean isPlantTile(JButton tile) {
        for (JButton plantTile : currPlantTiles) {
            if (plantTile == tile) {
                return true;
            }
        }
        return false;
    }

    private void updateScoreLabel() {
        String status = gameOver ? "Game Over! " : "";
        textLabel.setText(status + "Score: " + score + "  Best: " + highScore);
    }

    private void resetGame() {
        setMoleTimer.stop();
        setPlantTimer.stop();

        for (JButton tile : board) {
            tile.setIcon(null);
            tile.setEnabled(true);
        }
        currMoleTile = null;
        currPlantTiles = new JButton[currPlantTiles.length];
        score = 0;
        gameOver = false;
        updateScoreLabel();

        setMoleTimer.start();
        setPlantTimer.start();
    }
}
