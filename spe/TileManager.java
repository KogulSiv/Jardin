import java.io.IOException;
import javax.imageio.*;
import java.awt.*;


public class TileManager {
    GamePanel gp;
    Tile[] tile;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[12];
        setTileImages();
    }

    public void setTileImages() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/img/grass2.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlower.png"));
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlower2.png"));
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlower3.png"));
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlower4.png"));
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/img/sabotagedGrass.png"));
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/img/grass2Shine.png"));
            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlowerShine.png"));
            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlower2Shine.png"));
            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlower3Shine.png"));
            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getResourceAsStream("/img/grassWithFlower4Shine.png"));
            tile[11] = new Tile();
            tile[11].image = ImageIO.read(getClass().getResourceAsStream("/img/sabotagedGrassShine.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // à droite => coord + (taille/2, taille/4)
    //en bas ==> coord + (-taille/2, taille/4)
    public int getScreenX(int idWidth, int idHeight) {
        return gp.width/2 + (idWidth - idHeight)*gp.tailleEnJeu/2 - gp.cameraX;
    }

    public int getScreenY(int idWidth, int idHeight) {
        return  (idWidth + idHeight)*gp.tailleEnJeu/4 - gp.cameraY;
    }


    public int getMouseAt(int mouseX, int mouseY) {
        int left = gp.getCameraLeftLimit();
        int up = gp.getCameraUpperLimit();

        int row = left + (int) Math.ceil((2*mouseX/gp.tailleEnJeu));
        int column = up + (int) Math.ceil((4*mouseY/gp.tailleEnJeu));

        int X = (row + column)/2;
        int Y = (row - column)/2;

        return 0;
    }

    public void draw (Graphics2D g2d) { 
        Box[][] map = gp.game.getBoxes();

        int vStart = gp.getCameraUpperLimit();
        int vEnd = Math.min(gp.tilesParHeight + gp.tilesParWidth - 1, 2 + vStart + (int) Math.ceil(4*gp.height/gp.tailleEnJeu));
        int leftLimit = gp.getCameraLeftLimit();
        int rightLimit = Math.min(gp.tilesParHeight - 1, 2 + leftLimit + (int) Math.ceil(2*gp.width/gp.tailleEnJeu));
        int minU = 0;
        int maxU = 0;
        int epsilon = 1;
        if (vStart <= (gp.tilesParHeight + gp.tilesParWidth)/2 - 1) {
            minU = -vStart;
            maxU = vStart;
        } else {
            minU = -(gp.tilesParHeight - 1) + (vStart - ((gp.tilesParHeight + gp.tilesParWidth)/2 - 1));
            maxU = -minU;
            epsilon = -1;
        }

        int x;
        int y;
        for (int v = vStart; v < vEnd ; v++) { //X + Y => Invariant de ligne
            for (int u = minU; u <= maxU; u+=2 ) { //X - Y => Invariant de colonne
                if (u > rightLimit) {
                    break;
                } else if (u < leftLimit) {
                    continue;
                }
                x = (u+v)/2;
                y = (v-u)/2;

                g2d.drawImage(tile[map[x][y].getState()].image, getScreenX(x, y), getScreenY(x, y), gp.tailleEnJeu, gp.tailleEnJeu, null);
            }

            minU -= epsilon;
            maxU += epsilon;

            if (maxU == gp.tilesParHeight) {
                minU = -(gp.tilesParHeight - 2);
                maxU = gp.tilesParHeight - 2;
                epsilon = -1;
            }
        } 
    }
}
