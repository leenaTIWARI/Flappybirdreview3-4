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

    // Game variables
    private int birdY = HEIGHT / 2;
    private int birdVelocity = 0;
    private final int gravity = 1;

    private final ArrayList<Rectangle> pipes = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;

    private final Timer timer = new Timer(20, this);

    public FlappyBirdGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);

        addKeyListener(this);
        setFocusable(true);
        generatePipe();

        timer.start();
    }

    private void generatePipe() {
        int pipeHeight = new Random().nextInt(HEIGHT - PIPE_GAP - 100) + 50;
        pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, pipeHeight)); // Top pipe
        pipes.add(new Rectangle(WIDTH, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_GAP)); // Bottom pipe
    }

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

    private void updatePipes() {
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle pipe = pipes.get(i);
            pipe.x -= 5;

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
    }

    private boolean checkCollision() {
        Rectangle bird = new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE);
        for (Rectangle pipe : pipes) {
            if (bird.intersects(pipe)) {
                return true;
            }
        }
        return birdY <= 0 || birdY >= HEIGHT - BIRD_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdVelocity += gravity;
            birdY += birdVelocity;

            updatePipes();

            if (checkCollision()) {
                gameOver = true;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            birdVelocity = -10;
        }
        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void restartGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        generatePipe();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBirdGame game = new FlappyBirdGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
