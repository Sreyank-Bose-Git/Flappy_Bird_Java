import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bird {

    BufferedImage birdImage;

    GameScreen gameScreen;
    public int posY = 50;

    public int jumpHeight = 5;
    public boolean showHitbox = false;

    public float fallHeight = 0;
    public float fallHeightAcc = 0.000005f;

    public int maxGravity = 3;

    Rectangle collider = new Rectangle();

    public Bird(GameScreen gameScreen) {

        collider.x = (gameScreen.screenWidth / 2) - (gameScreen.tileSize / 2) + 10;
        collider.y = posY + 15;
        collider.width = gameScreen.tileSize - 20;
        collider.height = gameScreen.tileSize - 30;

        this.gameScreen = gameScreen;

        try {
            birdImage = ImageIO.read(getClass().getResourceAsStream("/bird.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {

        if(gameScreen.gameState == 50) {
            if(gameScreen.keyHandler.movingUp) {
                posY -= jumpHeight;
                fallHeight = 0.5f;
            }
            else {
                if(fallHeight < maxGravity) {
                    fallHeight += fallHeight + fallHeightAcc;
                }
                posY = (int) (posY + fallHeight);
            }

            collider.y = posY + 15;
            checkCollision();
        }
    }

    public void draw(Graphics2D g2d) {

        if(gameScreen.gameState == 50) {

            if(showHitbox) {
                g2d.setColor(Color.RED);
                g2d.drawRect(collider.x, collider.y, collider.width, collider.height);

                g2d.setColor(Color.BLUE);
                g2d.drawRect(
                        gameScreen.obstacleManager.colliders[0].x,
                        gameScreen.obstacleManager.colliders[0].y,
                        gameScreen.obstacleManager.colliders[0].width,
                        gameScreen.obstacleManager.colliders[0].height
                );
            }

            g2d.drawImage(birdImage, (gameScreen.screenWidth / 2) - (gameScreen.tileSize / 2), posY, gameScreen.tileSize, gameScreen.tileSize, null);
        }
    }
    public void checkCollision() {

        for(int i = 0; i < gameScreen.obstacleManager.boxes.length; i++) {
            gameScreen.obstacleManager.colliders[0].height = gameScreen.obstacleManager.boxes[i];
            gameScreen.obstacleManager.colliders[1].height = gameScreen.screenHeight - (gameScreen.obstacleManager.boxes[i] + gameScreen.obstacleManager.spaceHeight);

            gameScreen.obstacleManager.colliders[1].y = gameScreen.obstacleManager.boxes[i] + gameScreen.obstacleManager.spaceHeight;

            gameScreen.obstacleManager.colliders[0].x = gameScreen.obstacleManager.x + (gameScreen.obstacleManager.gap * i);
            gameScreen.obstacleManager.colliders[1].x = gameScreen.obstacleManager.x + (gameScreen.obstacleManager.gap * i);

            if(collider.intersects(gameScreen.obstacleManager.colliders[0]) ||
               collider.intersects(gameScreen.obstacleManager.colliders[1])) {

                gameScreen.gameState = 100;
            }
            else if(posY >= gameScreen.screenHeight) {
                gameScreen.gameState = 100;
            }
            else if(posY <= 0) {
                gameScreen.gameState = 100;
            }
        }
    }
}
