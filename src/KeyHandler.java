import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean movingUp;
    public int keyCode = 13231;
    public int jumpKey = KeyEvent.VK_UP;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();

        if(e.getKeyCode() == jumpKey){
            movingUp = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyCode = 123452;

        if(e.getKeyCode() == jumpKey){
            movingUp = false;
        }
    }
}
