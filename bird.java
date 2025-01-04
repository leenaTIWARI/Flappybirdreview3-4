import java.awt.*;

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
        velocity = -10;
    }

    public void update() {
        velocity += gravity;
        y += velocity;
    }

    public boolean checkBounds(int panelHeight) {
        return y < 0 || y > panelHeight - SIZE;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
}
