import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private Player player1;
    private Player player2;
    private boolean[] pressedKeys;
    private ArrayList<Chip> chips;
    private Color currentColor;
    private Chip[][] board;
    private JTextField textField;
    private String winnerStr;

    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/board.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player1 = new Player("src/redPlayer.png");
        player2 = new Player("src/yellowPlayer.png");
        chips = new ArrayList<>();
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above
        currentColor = Color.RED;
        board = new Chip[6][7];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = new Chip(Color.blue);
            }
        }
        textField = new JTextField();
        winnerStr = "";
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.drawImage(player1.getPlayerImage(), 800, 10, null);
        g.drawImage(player2.getPlayerImage(), 800, 250, null);

        for (int i = 0; i < chips.size(); i++) {
            Chip coin = chips.get(i);
            g.drawImage(coin.getImage(), coin.getxCoord(), coin.getyCoord(), null); // draw Coin
        }

        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("Player 1 score: " + player1.getScore(), 800, 230);
        g.drawString("Player 2 score: " + player2.getScore(), 800, 480);

        textField.setLocation(600, 550);
        textField.setVisible(false);
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
        if (e.getKeyCode() == 10) {
            textField.setVisible(false);
            winnerStr = textField.getText();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();

            int xcount = 0;
            int yCount = 0;
            int x = 0;
            int num = 0;
            for (int i = 111; i < 780; i += 111) {
                if (mouseClickLocation.getX() <= i) {
                    x = i - 80 - (num * 5) + 1;
                    break;
                }
                num++;
                xcount++;
            }
            int y = 0;
            int num2 = 0;
            for (int j = 81; j < 490; j += 71) {
                if (mouseClickLocation.getY() <= j) {
                    y = j - 70 + num2;
                    break;
                }
                num2 += 6;
                yCount++;
            }
            Chip chip = new Chip(x, y, currentColor);
            boolean empty = true;
            for (int i = 0; i < chips.size(); i++) {
                if (chips.get(i).chipRect().equals(chip.chipRect())) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                if (currentColor == Color.RED) {
                    currentColor = Color.YELLOW;
                } else {
                    currentColor = Color.RED;
                }
                chips.add(chip);
                board[xcount][yCount] = chip;
            }
        }
        for (int r = 0; r < board.length; r++) {
            if (r == 0) {
                for (int c = 0; c < board[r].length; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 3; c < board[r].length; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c - 1].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c - 2].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c - 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
            }
            if (r == 1) {
                for (int c = 0; c < board[r].length; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 3; c < board[r].length; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c - 1].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c - 2].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c - 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
            }
            if (r == 2) {
                for (int c = 0; c < board[r].length; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 3; c < board[r].length; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r + 1][c - 1].getColor2()) && board[r][c].getColor2().equals(board[r + 2][c - 2].getColor2()) && board[r][c].getColor2().equals(board[r + 3][c - 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
            }
            if (r == 3) {
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
            }
            if (r == 4) {
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
            }
            if (r == 5) {
                for (int c = 0; c < 4; c++) {
                    if (!board[r][c].getColor2().equals("blue") && board[r][c].getColor2().equals(board[r][c + 1].getColor2()) && board[r][c].getColor2().equals(board[r][c + 2].getColor2()) && board[r][c].getColor2().equals(board[r][c + 3].getColor2())) {
                        if (currentColor == Color.RED) {
                            player2.increaseScore();
                        } else {
                            player1.increaseScore();
                        }
                        reset();
                        textField.setVisible(true);
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void reset() {
        chips.clear();
        board = new Chip[6][7];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = new Chip(Color.blue);
            }
        }
    }
}