import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersGame extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    private int playerX = 375;
    private int playerY = 500;

    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Missile> missiles = new ArrayList<>();

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    private Random random = new Random();
    private Timer timer;

    public SpaceInvadersGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        initGame();
    }

    private void initGame() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                enemies.add(new Enemy(col * 60 + 50, row * 50 + 50));
            }
        }

        timer = new Timer(60, this); // check this area for slowing the enemies
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        movePlayer();
        moveEnemies();

        for (Missile missile : missiles) {
            missile.move();
        }

        checkCollisions();

        repaint();
    }

    private void movePlayer() {
        if (leftPressed && playerX > 0) {
            playerX -= 10;
        }
        if (rightPressed && playerX < WIDTH - 40) {
            playerX += 10;
        }
        if (spacePressed) {
            missiles.add(new Missile(playerX + 18, playerY));
            spacePressed = false;
        }
    }

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            enemy.move();

            if (random.nextInt(100) < 2) {
                missiles.add(new Missile(enemy.getX() + 9, enemy.getY() + 20));
            }
        }
    }

    private void checkCollisions() {
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        ArrayList<Missile> missilesToRemove = new ArrayList<>();

        for (Enemy enemy : enemies) {
            Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), 40, 30);

            for (Missile missile : missiles) {
                Rectangle missileRect = new Rectangle(missile.getX(), missile.getY(), 5, 10);

                if (missileRect.intersects(enemyRect)) {
                    enemiesToRemove.add(enemy);
                    missilesToRemove.add(missile);
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
        missiles.removeAll(missilesToRemove);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.fillRect(playerX, playerY, 40, 20);

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        for (Missile missile : missiles) {
            missile.draw(g);
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Improved Space Invaders");
        SpaceInvadersGame game = new SpaceInvadersGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class Enemy {
        private int x, y;
        private int xSpeed = 1;
        private int ySpeed = 20;
        private boolean movingRight = true;

        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            x += xSpeed;

            if (x >= WIDTH - 40 || x <= 0) {
                movingRight = !movingRight;
                y += ySpeed;
                xSpeed *= -1; // Reverse the horizontal movement direction
            }
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, 40, 30);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }


    class Missile {
        private int x, y;

        public Missile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move() {
            y -= 10;
        }

        public void draw(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(x, y, 5, 10);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
