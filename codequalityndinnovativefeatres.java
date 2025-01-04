public interface GameObject {
    void update();
    void draw(Graphics g);
    Rectangle getBounds();
}
public class PowerUp {
    private int x, y;
    private boolean active;
    
    public PowerUp(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.active = true;
    }

    public void update() {
        x -= 5;
        if (x < 0) {
            active = false;  // Deactivate power-up when off-screen
        }
    }

    public void activate(Bird bird) {
        // Power-up effect: e.g., make bird invincible for a short time
        bird.activateInvincibility();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }

    public boolean isActive() {
        return active;
    }
}
public class DifficultyManager {
    private int score;
    private int pipeSpeed;

    public DifficultyManager() {
        this.score = 0;
        this.pipeSpeed = 5;
    }

    public void updateDifficulty() {
        if (score > 50) {
            pipeSpeed = 7;  // Increase pipe speed as score increases
        } else if (score > 100) {
            pipeSpeed = 10;
        }
    }

    public void incrementScore() {
        score++;
        updateDifficulty();
    }

    public int getPipeSpeed() {
        return pipeSpeed;
    }
}
public class Background {
    private int x;
    private final Image backgroundImage;

    public Background(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
        this.x = 0;
    }

    public void update() {
        x -= 2;  // Move background left
        if (x <= -WIDTH) {
            x = 0;  // Reset background position for continuous scrolling
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, x, 0, null);
        g.drawImage(backgroundImage, x + WIDTH, 0, null);  // Repeat background
    }
}
public class MultiplayerGamePanel extends GamePanel {
    private Bird secondBird;

    public MultiplayerGamePanel() {
        super();
        secondBird = new Bird(200, HEIGHT / 2);  // Start second bird at a different position
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bird.flap();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            secondBird.flap();
        }
    }

    @Override
    public void updateGame() {
        super.updateGame();  // Update main bird

        if (!gameOver) {
            secondBird.update(HEIGHT);  // Update second bird
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw second bird
        g.setColor(Color.GREEN);
        g.fillRect(secondBird.getBounds().x, secondBird.getBounds().y, BIRD_SIZE, BIRD_SIZE);
    }
}
import javax.sound.sampled.*;

public class SoundManager {
    private Clip flapSound;
    private Clip collisionSound;
    private Clip backgroundMusic;

    public SoundManager() {
        loadSounds();
    }

    private void loadSounds() {
        try {
            flapSound = AudioSystem.getClip();
            flapSound.open(AudioSystem.getAudioInputStream(getClass().getResource("flap.wav")));

            collisionSound = AudioSystem.getClip();
            collisionSound.open(AudioSystem.getAudioInputStream(getClass().getResource("collision.wav")));

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(AudioSystem.getAudioInputStream(getClass().getResource("background_music.wav")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playFlap() {
        flapSound.setFramePosition(0);
        flapSound.start();
    }

    public void playCollision() {
        collisionSound.setFramePosition(0);
        collisionSound.start();
    }

    public void playBackgroundMusic() {
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
