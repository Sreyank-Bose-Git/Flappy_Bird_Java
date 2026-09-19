import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel implements Runnable{

    public final int tileSize = 48;
    public final int maxScreenCols = 16;
    public final int maxScreenRows = 12;

    final int screenWidth = tileSize * maxScreenCols;
    final int screenHeight = tileSize * maxScreenRows;

    long drawInterval;
    int FPS = 60;
    boolean like_game = false;


    KeyHandler keyHandler;
    Bird bird;
    ObstacleManager obstacleManager;
    UserInterface UI;

    Thread gameThread;
    public boolean gameRunning = false;

    public int gameState = 0;

    public GameScreen() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        keyHandler = new KeyHandler();
        this.addKeyListener(keyHandler);

        bird = new Bird(this);
        obstacleManager = new ObstacleManager(this);
        UI = new UserInterface(this);

        gameThread = new Thread(this);
    }

    public void startGame() {

        gameRunning = true;
        gameThread.start();
    }

    @Override
    public void run() {

        drawInterval = 1000000000 / FPS;
        long startTime;
        long nextDrawTime;

        long beginTime = System.nanoTime();
        int frames = 0;

        while (gameRunning) {

            startTime = System.nanoTime();
            nextDrawTime = startTime + drawInterval;

            update();
            repaint();

            frames++;

            if (System.nanoTime() - beginTime >= 1000000000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                beginTime = System.nanoTime();
            }

            long timeRemaining = nextDrawTime - System.nanoTime();

            if (timeRemaining > 0) {
                try {
                    Thread.sleep(timeRemaining / 1_000_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public void update() {

        bird.update();

        obstacleManager.update();

        UI.update();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        obstacleManager.draw(g2d);

        bird.draw(g2d);

        UI.draw(g2d);
    }
}
