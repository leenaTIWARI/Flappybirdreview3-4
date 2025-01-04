import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBirdGame extends JPanel implements ActionListener, KeyListener {
    // Game constants
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BIRD_SIZE = 20;
    private static final int PIPE_WIDTH = 60;
    private static final int PIPE_GAP = 150;
    private static final int PIPE_SPEED = 5;

    // Game variables
    private int birdY;
    private int birdVelocity;
    private final int gravity = 1;

    private final ArrayList<Rectangle> pipes = new ArrayList<>();
    private int score;
    private boolean gameOver;

    private final Timer timer;

    public FlappyBirdGame() {
        // Initialize game variables
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        gameOver = false;

        // Set up panel properties
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        // Generate initial pipes
        generatePipe();

        // Initialize the game timer
        timer = new Timer(20, this);
        timer.start();
    }

    private void generatePipe() {
        try {
            int pipeHeight = new Random().nextInt(HEIGHT - PIPE_GAP - 100) + 50;
            pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, pipeHeight)); // Top pipe
            pipes.add(new Rectangle(WIDTH, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_GAP)); // Bottom pipe
        } catch (IllegalArgumentException e) {
            System.err.println("Error generating pipe: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            // Draw bird
            g.setColor(Color.YELLOW);
            g.fillRect(100, birdY, BIRD_SIZE, BIRD_SIZE);

            // Draw pipes
            g.setColor(Color.GREEN);
            for (Rectangle pipe : pipes) {
                g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
            }

            // Draw score
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, 10, 30);

            // Game over message
            if (gameOver) {
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString("Game Over", WIDTH / 3, HEIGHT / 2);
                g.setFont(new Font("Arial", Font.PLAIN, 24));
                g.drawString("Press R to Restart", WIDTH / 3, HEIGHT / 2 + 50);
            }
        } catch (Exception e) {
            System.err.println("Error during rendering: " + e.getMessage());
        }
    }

    private void updatePipes() {
        try {
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= PIPE_SPEED;

                // Remove pipes that are off-screen
                if (pipe.x + PIPE_WIDTH < 0) {
                    pipes.remove(pipe);
                    if (i % 2 == 0) {
                        score++; // Increment score for every passed pipe pair
                    }
                }
            }

            // Generate new pipes
            if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < WIDTH - 300) {
                generatePipe();
            }
        } catch (Exception e) {
            System.err.println("Error updating pipes: " + e.getMessage());
        }
    }

    private boolean checkCollision() {
        try {
            Rectangle bird = new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE);
            for (Rectangle pipe : pipes) {
                if (bird.intersects(pipe)) {
                    return true;
                }
            }
            return birdY <= 0 || birdY >= HEIGHT - BIRD_SIZE;
        } catch (Exception e) {
            System.err.println("Error checking collision: " + e.getMessage());
            return true; // Return true to stop the game in case of error
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (!gameOver) {
                birdVelocity += gravity;
                birdY += birdVelocity;

                updatePipes();

                if (checkCollision()) {
                    gameOver = true;
                }
            }
            repaint();
        } catch (Exception ex) {
            System.err.println("Error in game loop: " + ex.getMessage());
            timer.stop(); // Stop the game if a critical error occurs
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                birdVelocity = -10;
            }
            if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
                restartGame();
            }
        } catch (Exception ex) {
            System.err.println("Error handling key press: " + ex.getMessage());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void restartGame() {
        try {
            birdY = HEIGHT / 2;
            birdVelocity = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            generatePipe();
        } catch (Exception e) {
            System.err.println("Error restarting game: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("Flappy Bird");
            FlappyBirdGame game = new FlappyBirdGame();

            frame.add(game);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error initializing game: " + e.getMessage());
        }
    }
}
