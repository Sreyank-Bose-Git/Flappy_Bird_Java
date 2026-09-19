import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bird {

    BufferedImage birdImage;

    GameScreen gameScreen;
    public int posY = 50;

    public int jumpHeight = 5;

    public float fallHeight = 0;
    public float fallHeightAcc = 0.000005f;

    Rectangle collider = new Rectangle();

    public Bird(GameScreen gameScreen) {

        collider.x = (gameScreen.screenWidth / 2) - (gameScreen.tileSize / 2) + 5;
        collider.y = posY + 5;
        collider.width = gameScreen.tileSize - 10;
        collider.height = gameScreen.tileSize - 10;

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
                if(fallHeight < 3) {
                    fallHeight += fallHeight + fallHeightAcc;
                }
                posY = (int) (posY + fallHeight);
            }

            collider.y = posY;
            checkCollision();
        }
    }

    public void draw(Graphics2D g2d) {

        if(gameScreen.gameState == 50) {
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
