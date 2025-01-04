import java.awt.*;
import java.util.Random;

public class Pipe {
    private static final int WIDTH = 60;
    private static final int GAP = 150;
    private int x, height;
    private boolean passed = false;

    public Pipe(int startX, int panelHeight) {
        this.x = startX;
        this.height = new Random().nextInt(panelHeight - GAP - 100) + 50;
    }

    public void update() {
        x -= 5;
    }

    public void draw(Graphics g, int panelHeight) {
        g.setColor(Color.GREEN);
        g.fillRect(x, 0, WIDTH, height); // Top pipe
        g.fillRect(x, height + GAP, WIDTH, panelHeight - height - GAP); // Bottom pipe
    }

    public boolean isOffScreen() {
        return x + WIDTH < 0;
    }

    public boolean isPassed(int birdX) {
        if (!passed && x + WIDTH < birdX) {
            passed = true;
            return true;
        }
        return false;
    }

    public Rectangle getTopBounds() {
        return new Rectangle(x, 0, WIDTH, height);
    }

    public Rectangle getBottomBounds(int panelHeight) {
        return new Rectangle(x, height + GAP, WIDTH, panelHeight - height - GAP);
    }
}
