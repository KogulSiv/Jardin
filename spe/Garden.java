public class Garden {

    static int NUMBER_OF_PLAYERS = 100;
    static int BOX_WIDTH = 50;
    static int BOX_HEIGHT = 50;
    static double MAX_GAIN = 20;
    static double OPTI = 10;
    
    public static void main(String[] args) {
        Player[] players = new Player[100];
        for (int i = 0; i <100; i++) {
            if (i%5 == 0) {
                players[i] = new Revolutionnaire(i, 0.8);
                //players[i] = new Naive(i);
            } else if (i%5 == 1) {
                players[i] = new Saboteur(i, 15);
            } else if (i%5 == 2) {
                players[i] = new Revolutionnaire(i, 0.8);
                //players[i] = new Naive(i);
            } else if (i%5 == 3){
                players[i] = new Revolutionnaire(i, 0.8);
                //players[i] = new Naive(i);
            } else {
                players[i] = new Revolutionnaire(i, 0.8);
                //players[i] = new Naive(i);
            }
        }
        Game game = new Game(players, BOX_WIDTH, BOX_HEIGHT, MAX_GAIN, OPTI);

        GameScreen screen = new GameScreen(game);
        screen.display();
    }
}

//Pour une proportion fixée et strat fixée, tracer le nb de strat gagnantes contre saboteur(k) sur X nb de partie en fonction de (k, opti)