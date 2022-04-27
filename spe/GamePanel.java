import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.io.IOException;
import java.awt.image.BufferedImage;



public class GamePanel extends JPanel implements Runnable {
    final int tailleReel = 100;
    int zoom = 1;
    public int tailleEnJeu = tailleReel*zoom;

    public final int width = 1400;
    public final int height = 700;


    public final int playState = 0;
    public final int tileInfoState = 1;
    public final int rankingState = 2;
    public int currentState;

    public int clickedX;
    public int clickedY;

    final Font font = new Font("Arial", Font.PLAIN, 25);

    TileManager tileManager = new TileManager(this);
    Thread gameThread;
    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler(this);

    int FPS = 60;
    public int cameraX = tailleEnJeu;
    public int cameraY = 0;
    int v = 4;

    public Game game;
    public int tilesParWidth;
    public int tilesParHeight;
    private boolean toGo;
    private int kTicks;

    private int offsetRanking;
    private int offsetTick;


    public GamePanel(Game game) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        this.setFocusable(true);

        this.game = game;
        this.tilesParWidth = game.getBoxes().length;
        this.tilesParHeight = this.tilesParWidth;
        this.toGo = true;
        this.kTicks = 0;
        this.currentState = this.playState;
        this.clickedX = 0;
        this.clickedY = 0;
        this.offsetRanking = 0;
        this.offsetTick = 0;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; //En ns
        double nextDrawTime = System.nanoTime() + drawInterval;
        double remainingTime = 0;

        while(gameThread != null) {
            if (currentState == playState) { 
                update();
            } else if (currentState == tileInfoState) {
                updateTileInfo();
            } else if (currentState == rankingState) {
                updateRanking();
            }
            repaint();

            try {
                remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000; //En ms

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        boolean cond = true;
        int N = 6;
        if (keyHandler.haut) {
            cond = true;
            if (cameraY <= tilesParHeight*tailleEnJeu/4) {
                if (cameraX < 2*tailleEnJeu) {
                    cond = cameraX - width/2 > -2*cameraY - N*tailleEnJeu;
                } else {
                    cond = cameraX - width/2 < 2*cameraY;
                }
            }

            if (cameraY > 0 && cond) {
                cameraY -= v;
            }
        }

        if (keyHandler.bas) {
            cond = true;
            if (cameraY >= tilesParHeight*tailleEnJeu/4) {
                if (cameraX < 2*tailleEnJeu) {
                    cond = cameraX - (width/2 - tilesParWidth*tailleEnJeu/2) > 2*(cameraY - tilesParHeight*tailleEnJeu/4) + N*tailleEnJeu;
                } else {
                    cond = cameraX - (width/2 + tilesParWidth*tailleEnJeu/2) < -2*(cameraY - tilesParHeight*tailleEnJeu/4) - N*tailleEnJeu;
                }
            }

            if ((cameraY + height < (tilesParWidth + tilesParHeight)*tailleEnJeu/4 + tailleEnJeu) && cond) {
                cameraY += v;
            }
        }

        if (keyHandler.gauche) {
            cond = true;
            if (cameraX < 2*tailleEnJeu) {
                if (cameraY + height <= tilesParHeight*tailleEnJeu/4) {
                    cond = cameraX - width/2 > -2*cameraY - N*tailleEnJeu;
                } else {
                    cond = cameraX - (width/2 - tilesParWidth*tailleEnJeu/2) > 2*(cameraY - tilesParHeight*tailleEnJeu/4) + N*tailleEnJeu;
                }
            }

            if ((cameraX - width/2 > -(tilesParHeight)*tailleEnJeu/2) && cond) {
                cameraX -= v;
            }
        }

        if (keyHandler.droite) {
            cond = true;
            if (cameraX >= 2*tailleEnJeu) {
                if (cameraY <= tilesParHeight*tailleEnJeu/4) {
                    cond = cameraX - width/2 < 2*cameraY;
                } else {
                    cond = cameraX - (width/2 + tilesParWidth*tailleEnJeu/2) < -2*(cameraY - tilesParHeight*tailleEnJeu/4) - N*tailleEnJeu;
                }
            }
            if ((cameraX + width/2 < (tilesParWidth)*tailleEnJeu/2 + tailleEnJeu) && cond) {
                cameraX += v;
            }
        }

        if (toGo && keyHandler.J) {
            if (kTicks == 0) {
                toGo = game.nextTurn();
                kTicks = 6;
            } else {
                kTicks--;
            }  
        }

        if (toGo && keyHandler.K) {
            if (kTicks == 0) {
                toGo = game.nextTurn();
                kTicks = 6;
            } else {
                kTicks--;
            }  
        }

        if (toGo && keyHandler.L) {
            toGo = game.nextTurn();
            keyHandler.LDone = true;
            keyHandler.L = false;
        }

        if (keyHandler.T) {
            if (this.tailleEnJeu == 100) {
                this.tailleEnJeu = 50;
            } else if (this.tailleEnJeu == 50) {
                this.tailleEnJeu = 25;
            } else if (this.tailleEnJeu == 25) {
                this.tailleEnJeu = 200;
            } else if (this.tailleEnJeu == 200) {
                this.tailleEnJeu = 100;
            }
            keyHandler.T = false;
        }

        if (keyHandler.C) {
            this.currentState = this.rankingState;
        }
    }

    public void updateTileInfo() {
        if (keyHandler.esc) {
            this.currentState = this.playState;
        }
    }

    public void updateRanking() {
        if (keyHandler.esc) {
            this.currentState = this.playState;
        }

        if (keyHandler.bas && offsetRanking < game.getNumberOfPlayersAlive() - 1) {
            if (this.offsetTick == 0) {
                offsetTick = 5;
                offsetRanking++;
            } else {
                offsetTick--;
            }          
        }

        if (keyHandler.haut && offsetRanking != 0) {
            if (this.offsetTick == 0) {
                offsetTick = 5;
                offsetRanking--;
            } else {
                offsetTick--;
            }          
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        //double start = System.nanoTime();
        tileManager.draw(g2d);
        //System.out.println((System.nanoTime() - start)/1000000000);
        if (currentState == tileInfoState) {
            drawInfoCase(g2d);
        } else if (currentState == rankingState) {
            drawRanking(g2d);
        }

    }

    public void drawInfoCase(Graphics2D g2d) {
        int width = 800;
        int height = 70;
        int x = (this.width - width)/2;
        int y = (this.height - height)/2;

        Color c = new Color(0, 0, 0, 210);
        g2d.setColor(c);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);

        g2d.setFont(font);
        Box clickedBox = game.getBoxes()[clickedX][clickedY];
        int state = clickedBox.getState();
        if (state == 6) {
            g2d.drawString("Case libre", x+18, y+42);
        } else if (state < 11) {
            int playerId = clickedBox.getPlayerId();
            g2d.drawString("Case occupée par le joueur numéro "  + playerId + "(" + game.getPlayer(playerId).getName() + ")", x+18, y+30);
            g2d.drawString("Gains : "  + game.getPlayer(playerId).calculeGain(game), x+18, y+55);
        } else {
            int playerId = clickedBox.getSabotedPlayerId();
            g2d.drawString("Case sabotée par le joueur numéro "  + playerId + "(" + game.getPlayer(playerId).getName() + ")", x+18, y+42);
        }
        
    }

    public void drawRanking(Graphics2D g2d) {
        int width = 800;
        int height = 590;
        int x = (this.width - width)/2;
        int y = (this.height - height)/2;

        Color c = new Color(0, 0, 0, 210);
        g2d.setColor(c);
        g2d.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);

       
        c = new Color(120, 120, 120);
        g2d.setColor(c);
        g2d.fillRoundRect(x+width - 28, y + 18, 14, height - 32, 15, 15);

        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setFont(font);
        g2d.drawString("ID", x+28, y+40);
        g2d.drawString("Stratégie", x+150, y+40);
        g2d.drawString("Gains", x+480, y+40);


        c = new Color(120, 120, 120);
        g2d.setColor(c);
        for (int i = 1; i < 14; i++) {
            g2d.fillRoundRect(x + 15, y + 15 + 40*(i), width - 50, 30, 20, 20);
        }


        c = new Color(255, 255, 255);
        g2d.setColor(c);
        g2d.setFont(font);
        Player currentPlayer;
        for (int i = 0; i < 13; i++) {
            if (i + offsetRanking >= game.getNumberOfPlayersAlive()) {
                break;
            }
            currentPlayer = game.getPlayerSortedByGainAt(i + offsetRanking);
            g2d.drawString(Integer.toString(currentPlayer.getId()), x+28, y + 38 + 40*(i+1));
            g2d.drawString(currentPlayer.getName(), x+150, y + 38 + 40*(i+1));
            g2d.drawString(Double.toString(currentPlayer.getTotalGain()), x+480, y + 38 + 40*(i+1));
        }
        //g2d.fillRoundRect(x + 15, y + 40, width - 50, 30, 20, 20);
        //g2d.fillRoundRect(x + 15, y + 80, width - 50, 30, 20, 20);
    }

    public int getCameraUpperLimit() {
        return Math.max(0, (int) Math.ceil(4*cameraY/tailleEnJeu) - 2);
    }

    public int getCameraLeftLimit() {
        return  Math.max(-(tilesParHeight - 1), (int) Math.ceil(2*(cameraX-tailleEnJeu)/tailleEnJeu) - (int) Math.ceil(width/tailleEnJeu));
    }
}
