import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBirdWithEvents extends JPanel implements ActionListener, KeyListener {
    // Constants
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
    private int score = 0;
    private boolean gameOver = false;

    // Timer for game loop
    private final Timer timer;

    public FlappyBirdWithEvents() {
        // Initialize game variables
        birdY = HEIGHT / 2;
        birdVelocity = 0;

        // Panel setup
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        setFocusable(true);
        addKeyListener(this);

        // Initialize timer for game updates
        timer = new Timer(20, this); // Calls actionPerformed() every 20 ms
        timer.start();

        // Generate the first set of pipes
        generatePipe();
    }

    // Generate new pipes
    private void generatePipe() {
        int pipeHeight = new Random().nextInt(HEIGHT - PIPE_GAP - 100) + 50;
        pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, pipeHeight)); // Top pipe
        pipes.add(new Rectangle(WIDTH, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_GAP)); // Bottom pipe
    }

    // Game rendering
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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
    }

    // Update game state
    private void updateGame() {
        if (!gameOver) {
            birdVelocity += gravity;
            birdY += birdVelocity;

            // Move pipes
            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= PIPE_SPEED;

                // Remove pipes that go off-screen
                if (pipe.x + PIPE_WIDTH < 0) {
                    pipes.remove(i);
                    i--;
                }

                // Check if bird passes a pipe
                if (pipe.x + PIPE_WIDTH == 100 && pipe.y == 0) {
                    score++;
                }
            }

            // Generate new pipes
            if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < WIDTH - 300) {
                generatePipe();
            }

            // Collision detection
            for (Rectangle pipe : pipes) {
                if (new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE).intersects(pipe)) {
                    gameOver = true;
                }
            }

            // Check if bird hits boundaries
            if (birdY < 0 || birdY > HEIGHT - BIRD_SIZE) {
                gameOver = true;
            }
        }
    }

    // Restart game
    private void restartGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        generatePipe();
    }

    // Event listener for game loop
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint(); // Triggers paintComponent()
    }

    // Key pressed event
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            birdVelocity = -10; // Flap upwards
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame(); // Restart game
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // Main method to run the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBirdWithEvents game = new FlappyBirdWithEvents();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
