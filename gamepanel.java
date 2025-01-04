import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final Bird bird;
    private final ArrayList<Pipe> pipes;
    private final Timer timer;

    private int score = 0;
    private boolean gameOver = false;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);

        bird = new Bird(100, HEIGHT / 2);
        pipes = new ArrayList<>();
        generatePipe();

        timer = new Timer(20, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void generatePipe() {
        pipes.add(new Pipe(WIDTH, HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw bird
        bird.draw(g);

        // Draw pipes
        for (Pipe pipe : pipes) {
            pipe.draw(g, HEIGHT);
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 10, 30);

        // Draw game over message
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game Over", WIDTH / 3, HEIGHT / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Press R to Restart", WIDTH / 3, HEIGHT / 2 + 50);
        }
    }

    private void updateGame() {
        if (!gameOver) {
            bird.update();

            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                pipe.update();

                if (pipe.isOffScreen()) {
                    pipes.remove(pipe);
                }

                if (pipe.isPassed(100)) {
                    score++;
                }

                if (bird.getBounds().intersects(pipe.getTopBounds()) || bird.getBounds().intersects(pipe.getBottomBounds(HEIGHT))) {
                    gameOver = true;
                }
            }

            if (bird.checkBounds(HEIGHT)) {
                gameOver = true;
            }

            if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getTopBounds().x < WIDTH - 300) {
                generatePipe();
            }
        }
    }

    private void restartGame() {
        bird.update();
        pipes.clear();
        score = 0;
        gameOver = false;
        generatePipe();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            bird.flap();
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
