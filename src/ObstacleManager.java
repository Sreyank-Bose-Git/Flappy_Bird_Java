import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class ObstacleManager {

    Random rand = new Random();

    int[] boxes = new int[4];

    BufferedImage skyImage;

    Rectangle[] colliders = new Rectangle[2];

    int o;
    int b;

    int spawn;
    int x;

    int skyX;
    int skySpeed;

    int spaceHeight;
    int width;

    boolean newSky = false;
    boolean oldSky = true;

    int gap;
    int moveSpeed;

    GameScreen gameScreen;

    public ObstacleManager(GameScreen gameScreen) {

        this.gameScreen = gameScreen;

        colliders[0] = new Rectangle();
        colliders[1] = new Rectangle();

        colliders[0].y = 0;

        gap = gameScreen.tileSize * 10;

        spawn = gameScreen.tileSize * 20;
        x = spawn;

        skyX = 0;
        skySpeed = 2;

        spaceHeight = gameScreen.tileSize * 3;
        width = gameScreen.tileSize * 2;

        colliders[0].width = width;
        colliders[1].width = width;

        moveSpeed = 4;

        o = gameScreen.tileSize * 3;
        b = gameScreen.tileSize * 7;

        for(int i = 0; i < boxes.length; i++) {
            boxes[i] = rand.nextInt(o, b);
        }

        try {
            skyImage = ImageIO.read(getClass().getResourceAsStream("/sky.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {

        if(gameScreen.gameState == 50) {

            x -= moveSpeed;
            skyX -= skySpeed;

            if(x <= -width) {
                x += gap;
                newBox();
            }

            // Reset after one complete image has moved off-screen
            if(skyX <= -1700) {
                skyX = 0;
            }
        }
    }
    void newBox() {

        boxes[0] = 0;

        for(int i = 0; i < boxes.length - 1; i++) {
            boxes[i] = boxes[i + 1];
        }

        boxes[boxes.length - 1] = rand.nextInt(o, b);
    }

    public void draw(Graphics2D g2d) {

        if(gameScreen.gameState == 50) {

            // Sky
            g2d.drawImage(skyImage, skyX, -180, null);
            g2d.drawImage(skyImage, skyX + 1700, -180, null);

            // Obstacles
            g2d.setColor(Color.RED);

            for(int j = 0; j < boxes.length; j++) {

                g2d.fillRect(
                        x + (j * gap),
                        0,
                        width,
                        boxes[j]
                );

                g2d.fillRect(
                        x + (j * gap),
                        boxes[j] + spaceHeight,
                        width,
                        gameScreen.screenHeight - (boxes[j] + spaceHeight)
                );
            }
        }
    }
}
