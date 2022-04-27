import java.awt.event.*;

public class KeyHandler implements KeyListener{

    public boolean haut, bas, gauche, droite, J, J1, K, L, C, LDone, T, esc;

    @Override
    public void keyTyped(KeyEvent e) {        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            haut = true;
        }

        if (code == KeyEvent.VK_LEFT) {
            gauche = true;
        }

        if (code == KeyEvent.VK_DOWN) {
            bas = true;
        }

        if (code == KeyEvent.VK_RIGHT) {
            droite = true;
        }

        if (code == KeyEvent.VK_J) {
            if (!J1) {
                J = true;
            } else {
                J1 = false;
                J = false;
            }
        }

        if (code == KeyEvent.VK_K) {
            K = true;
        }

        if (!LDone && code == KeyEvent.VK_L) {
            L = true;
            LDone = true;
        }

        if (code == KeyEvent.VK_T) {
            T = true;
        }

        if (code == KeyEvent.VK_C) {
            C = true;
        }

        if (code == KeyEvent.VK_ESCAPE) {
            esc = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            haut = false;
        }

        if (code == KeyEvent.VK_LEFT) {
            gauche = false;
        }

        if (code == KeyEvent.VK_DOWN) {
            bas = false;
        }

        if (code == KeyEvent.VK_RIGHT) {
            droite = false;
        }

        if (code == KeyEvent.VK_J) {
            if (!J1 && J) {
                J1 = true;
            }           
        }

        if (code == KeyEvent.VK_K) {
            K = false;
        }

        if (code == KeyEvent.VK_L) {
            L = false;
            LDone = false;
        }

        if (code == KeyEvent.VK_T) {
            T = false;
        }

        if (code == KeyEvent.VK_C) {
            C = false;
        }

        if (code == KeyEvent.VK_ESCAPE) {
            esc = false;
        }
        
    }
    
}
