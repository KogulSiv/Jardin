import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;

public class MouseHandler extends MouseAdapter {
    GamePanel gp;
    int oldX;
    int oldY;
    int oldState;
    int X;
    int Y;
    BufferedImage image;

    public MouseHandler(GamePanel gp) {
        super();
        this.gp = gp;
        this.oldX = 0;
        this.oldY = 0;
        this.oldState = 0;
        this.X = -1;
        this.Y = -1;
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/img/shine.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Click !");
        if (gp.currentState == gp.playState && this.X != -1) {
            gp.clickedX = this.X;
            gp.clickedY = this.Y;
            gp.currentState = gp.tileInfoState;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gp.currentState == gp.playState) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            //int column = gp.getCameraLeftLimit() + (int) Math.ceil((2*mouseX/gp.tailleEnJeu)) + 1;
            //int row = gp.getCameraUpperLimit() + (int) Math.ceil((4*mouseY/gp.tailleEnJeu)) ;

            int row = (int) Math.ceil(4*(gp.cameraY+mouseY)/gp.tailleEnJeu);//
            int column = (int) Math.ceil(2*(gp.cameraX-gp.tailleEnJeu+mouseX-gp.width/2)/gp.tailleEnJeu) + 1;// - (int) Math.ceil(gp.width/gp.tailleEnJeu) + 1;

            int X = (row + column)/2;
            int Y = (row - column)/2;
            //System.out.println(row + "     " + column);


            if (X >= 0 && Y >= 0 && X < gp.tilesParWidth && Y < gp.tilesParHeight) {
                this.X = X;
                this.Y = Y;
                gp.game.setBox(oldX, oldY, oldState);
                oldState = gp.game.getBoxes()[X][Y].getState();
                oldX = X;
                oldY = Y;
                gp.game.setBox(X, Y, oldState+6);
            } else {
                this.X = -1;
                this.Y = -1;
                gp.game.setBox(oldX, oldY, oldState);
            }
        }
    }

    public void draw (Graphics2D g2d) {
        if (this.X != -1) {
            AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2d.setComposite(alcom);
            int screenX = gp.width/2 + (X - Y)*gp.tailleEnJeu/2 - gp.cameraX;
            int screenY = (X + Y)*gp.tailleEnJeu/4 - gp.cameraY;
            g2d.drawImage(image, screenX, screenY, gp.tailleEnJeu, gp.tailleEnJeu, null);
        }
    }
}
