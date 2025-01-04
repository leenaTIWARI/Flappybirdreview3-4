public class Bird {
    private static final int SIZE = 20;
    private int x, y;
    private int velocity = 0;
    private final int gravity = 1;

    public Bird(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void flap() {
        velocity = -10; // Flap upward
    }

    public void update(int panelHeight) {
        velocity += gravity;
        y += velocity;

        // Validate bird stays within boundaries
        if (y < 0) {
            y = 0; // Prevent going above the screen
            velocity = 0;
        } else if (y > panelHeight - SIZE) {
            y = panelHeight - SIZE; // Prevent falling below the screen
            velocity = 0;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}
public class Pipe {
    private static final int WIDTH = 60;
    private static final int MIN_HEIGHT = 50;
    private static final int MAX_HEIGHT = 300; // Arbitrary max pipe height
    private static final int GAP = 150; // Gap between top and bottom pipes

    private int x, height;
    private boolean passed = false;

    public Pipe(int startX, int panelHeight) {
        this.x = startX;

        // Validate random pipe height within allowable range
        int maxPipeHeight = panelHeight - GAP - MIN_HEIGHT;
        this.height = Math.max(MIN_HEIGHT, new Random().nextInt(maxPipeHeight));
    }

    public void update() {
        x -= 5; // Move pipe left
    }

    public boolean isOffScreen() {
        return x + WIDTH < 0; // Validate if pipe is off-screen
    }

    public Rectangle getTopBounds() {
        return new Rectangle(x, 0, WIDTH, height);
    }

    public Rectangle getBottomBounds(int panelHeight) {
        return new Rectangle(x, height + GAP, WIDTH, panelHeight - height - GAP);
    }
}
public class CollisionValidator {
    public static boolean isBirdColliding(Bird bird, Pipe pipe, int panelHeight) {
        Rectangle birdBounds = bird.getBounds();
        return birdBounds.intersects(pipe.getTopBounds()) || birdBounds.intersects(pipe.getBottomBounds(panelHeight));
    }

    public static boolean isBirdOutOfBounds(Bird bird, int panelHeight) {
        Rectangle birdBounds = bird.getBounds();
        return birdBounds.y < 0 || birdBounds.y > panelHeight;
    }
}
@Override
public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
        bird.flap(); // Validate bird flaps only during gameplay
    } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
        restartGame(); // Validate restart only when game is over
    }
}
private void restartGame() {
    bird = new Bird(100, HEIGHT / 2); // Reset bird position
    pipes.clear(); // Clear existing pipes
    score = 0; // Reset score
    gameOver = false; // Reset game state
    generatePipe(); // Generate initial pipes
}
private void updateScore() {
    for (Pipe pipe : pipes) {
        if (!pipe.isPassed() && pipe.getTopBounds().x + Pipe.WIDTH < bird.getBounds().x) {
            score++; // Increment score
            pipe.setPassed(true); // Mark pipe as passed
        }
    }
}
private void updateGame() {
    if (!gameOver) {
        bird.update(HEIGHT); // Validate bird movement

        // Update pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.update();

            if (CollisionValidator.isBirdColliding(bird, pipe, HEIGHT)) {
                gameOver = true; // Validate collisions
            }

            if (pipe.isOffScreen()) {
                pipes.remove(i); // Validate pipe removal
                i--;
            }
        }

        // Check for out-of-bounds bird
        if (CollisionValidator.isBirdOutOfBounds(bird, HEIGHT)) {
            gameOver = true;
        }

        // Generate new pipes
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getTopBounds().x < WIDTH - 300) {
            generatePipe();
        }

        // Update score
        updateScore();
    }
}
