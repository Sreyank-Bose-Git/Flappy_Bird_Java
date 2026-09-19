import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class UserInterface {

    GameScreen gameScreen;
    Font arial_25;
    Font arial_100;
    Font arial_50;

    // Settings
    final int total_options = 10;
    int option_selected = 0;
    String[] options = new String[total_options];
    double number = 0;
    boolean decimal = false;
    int dVal = 10;

    boolean waitingForKey = false;

    Timer writingTimer;
    boolean allowWriting = false;

    public int points = 0;
    int centre;

    public UserInterface(GameScreen gameScreen) {
        arial_25 = new Font("Arial", Font.PLAIN, 25);
        arial_100 = new Font("Arial", Font.PLAIN, 100);
        arial_50 = new Font("Arial", Font.PLAIN, 50);

        this.gameScreen = gameScreen;

        centre = (gameScreen.screenWidth / 2) - (gameScreen.tileSize / 2);

        for(int i = 0; i < total_options; i++) {
            options[i] = "";
        }
        options[0] = "Maximum FPS: " + gameScreen.FPS;
        options[1] = "Gravity: " + gameScreen.bird.fallHeightAcc;
        options[2] = "Jump Key: " + KeyEvent.getKeyText(gameScreen.keyHandler.jumpKey);

        writingTimer = new Timer(500, (e) -> {
            allowWriting = true;
        });

        writingTimer.start();
    }

    public void update() {

        if(gameScreen.gameState == 0) {
            if(gameScreen.keyHandler.keyCode == KeyEvent.VK_SPACE) {
                gameScreen.gameState = 50;
                gameScreen.obstacleManager.x = gameScreen.obstacleManager.spawn;
                //gameScreen.obstacleManager.skyX = -100;
            }
            else if(gameScreen.keyHandler.keyCode == KeyEvent.VK_BACK_SPACE) {
                gameScreen.gameState = 25;
            }
        }
        else if(gameScreen.gameState == 25) {
            if((gameScreen.keyHandler.keyCode == KeyEvent.VK_DOWN && option_selected != total_options && options[option_selected + 1] != "") && allowWriting) {
                option_selected++;
                allowWriting = false;
            }
            else if((gameScreen.keyHandler.keyCode == KeyEvent.VK_UP && option_selected != 0 && options[option_selected - 1] != "") && allowWriting) {
                option_selected--;
                allowWriting = false;
            }
            else if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_ESCAPE) {
                gameScreen.gameState = 0;
                allowWriting = false;
            }
            else if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_ENTER) {
                System.out.println("Option is Selected");

                if(option_selected == 2) {
                    waitingForKey = true;
                }
                else {
                    number = 0;
                    decimal = false;
                    dVal = 10;
                }

                gameScreen.gameState = 26;
                allowWriting = false;
            }
        }
        else if(gameScreen.gameState == 26) {
            if(allowWriting && waitingForKey && gameScreen.keyHandler.keyCode != KeyEvent.VK_ENTER) {
                if(option_selected == 2) {
                    gameScreen.keyHandler.jumpKey = gameScreen.keyHandler.keyCode;
                    options[option_selected] = "Jump Key: " + KeyEvent.getKeyText(gameScreen.keyHandler.jumpKey);
                    gameScreen.gameState = 25;
                    waitingForKey = false;
                }
            }
            else if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_PERIOD) {
                decimal = true;
                allowWriting = false;
            }
            else if (allowWriting && ((gameScreen.keyHandler.keyCode >= KeyEvent.VK_0 && gameScreen.keyHandler.keyCode <= KeyEvent.VK_9) ||
                    (gameScreen.keyHandler.keyCode >= KeyEvent.VK_NUMPAD0 && gameScreen.keyHandler.keyCode <= KeyEvent.VK_NUMPAD9))) {

                if(decimal) {
                    number += (gameScreen.keyHandler.keyCode - KeyEvent.VK_0) / (double) dVal;
                    dVal *= 10;
                }
                else {
                    number = (number * 10) + (gameScreen.keyHandler.keyCode - KeyEvent.VK_0);
                }
                allowWriting = false;
            }
            else if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_ESCAPE) {
                gameScreen.gameState = 25;
                allowWriting = false;
            }
            else if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_BACK_SPACE) {
                if (decimal) {

                    dVal /= 10;

                    number = Math.floor(number * dVal) / dVal;

                    if (dVal == 10) {
                        decimal = false;
                    }

                } else {
                    number = Math.floor(number / 10);
                }

                allowWriting = false;
            }
            else if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_ENTER) {

                if (option_selected == 0) {
                    gameScreen.FPS = (int) number;
                    gameScreen.drawInterval = 1000000000 / gameScreen.FPS;
                    options[option_selected] = "Maximum FPS: " + gameScreen.FPS;
                }
                gameScreen.gameState = 25;
                allowWriting = false;
            }
        }
        else if(gameScreen.gameState == 100) {
            if(gameScreen.keyHandler.keyCode == KeyEvent.VK_ESCAPE) {
                gameScreen.gameState = 0;
            }
        }
        else if(gameScreen.gameState == 101) {
            if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_ESCAPE) {
                gameScreen.gameState = 50;
                allowWriting = false;
            }
        }
        else if(gameScreen.gameState == 50) {
            int x = gameScreen.obstacleManager.x;

            for(int i = 0; i < gameScreen.obstacleManager.boxes.length; i++) {
                if(x + (gameScreen.obstacleManager.gap * i) == centre) {
                    points++;
                }
            }

            if(allowWriting && gameScreen.keyHandler.keyCode == KeyEvent.VK_ESCAPE) {
                gameScreen.gameState = 101;
                allowWriting = false;
            }
        }
    }

    public void draw(Graphics2D g2d) {

        if(gameScreen.gameState == 0) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(arial_100);

            g2d.drawString("Flappy Bird", (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("Flappy Bird") / 2), gameScreen.tileSize * 2);

            g2d.setFont(arial_25);
            g2d.setColor(Color.blue);

            g2d.drawString("SpaceBar to Play", (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("SpaceBar to Play") / 2), gameScreen.tileSize * 5);

            g2d.setColor(Color.gray);
            g2d.drawString("Backspace for Settings", (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("Backspace for Settings") / 2), gameScreen.tileSize * 7);
        }
        else if(gameScreen.gameState == 25) {
            g2d.setColor(Color.blue);
            g2d.setFont(arial_100);
            g2d.drawString("Settings", (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("Settings") / 2), gameScreen.tileSize * 2);

            g2d.setFont(arial_25);
            g2d.setColor(Color.white);

            for(int i = 0; i < total_options; i++) {
                g2d.drawString(options[i], (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth(options[i]) / 2), (gameScreen.tileSize * 2 * i) + gameScreen.tileSize * 4);
            }

            g2d.setColor(Color.red);
            g2d.drawRect(10, (option_selected * gameScreen.tileSize * 2) + (gameScreen.tileSize * 4) - 24, gameScreen.screenWidth - 20, 30);
        }
        else if(gameScreen.gameState == 26) {
            g2d.setColor(Color.blue);
            g2d.setFont(arial_50);

            g2d.drawString(options[option_selected], (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth(options[option_selected]) / 2), gameScreen.tileSize * 2);

            g2d.setFont(arial_25);

            g2d.setColor(Color.gray);
            g2d.drawRect(10, (gameScreen.tileSize * 7) - 24, gameScreen.screenWidth - 20, 30);

            g2d.drawString("Value: " + number, gameScreen.tileSize * 6, gameScreen.tileSize * 7);
        }
        else if(gameScreen.gameState == 50) {
            g2d.setColor(Color.white);
            g2d.setFont(arial_25);
            g2d.drawString("Points: " + points, 10, 35);
        }
        else if(gameScreen.gameState == 100) {
            g2d.setColor(Color.white);
            g2d.setFont(arial_100);

            g2d.drawString("Game Over", (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("Game Over") / 2), gameScreen.screenHeight / 2);

            g2d.setFont(arial_25);

            g2d.drawString("Total Points: " + points, (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("Total Points: " + points) / 2), (gameScreen.screenHeight / 2) + gameScreen.tileSize * 2);
        }
        else if(gameScreen.gameState == 101) {
            g2d.setColor(Color.white);
            g2d.setFont(arial_100);

            g2d.drawString("Game Paused", (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("Game Paused") / 2), gameScreen.screenHeight / 2);

            g2d.setFont(arial_25);

            g2d.drawString("Current Points: " + points, (gameScreen.screenWidth / 2) - (g2d.getFontMetrics().stringWidth("Current Points: " + points) / 2), (gameScreen.screenHeight / 2) + gameScreen.tileSize * 2);
        }
    }
}
