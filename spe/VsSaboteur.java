public class VsSaboteur {

    static int BOX_WIDTH = 50;
    static int BOX_HEIGHT = 50;
    static int propSaboteur = 5; // 1/propSaboteur saboteurs
    static double MAX_GAIN = 20;
    static double PROP = 0.5;
    static int NB_GAME = 80;
    //static double OPTI = 10;
    
    public static void main(String[] args) {
        System.out.println(propSaboteur);
        System.out.println(NB_GAME);
        System.out.println("Justicier[" + PROP*100 +"%]#"+(propSaboteur-1));
        System.out.println("Saboteur[k]#1");
        System.out.println("START");
        Player[] players = new Player[100];
        for (int opti = 8; opti < 25; opti+=2) {
            for (int k = opti-5; k < opti+6; k++) {
                System.out.println(opti + "#" + k);


                for (int i = 0; i < NB_GAME; i++) {
                    players = new Player[100];
                    for (int id = 0; id < 100; id++) {
                        if (id%propSaboteur == 0) {
                            players[id] = new Saboteur(id, k);
                        } else {
                            players[id] = new Justicier(id, k);
                        }
                    }

                    Game game = new Game(players, BOX_WIDTH, BOX_HEIGHT, MAX_GAIN, opti, true);
                    game.playSilently();
                }

                // for (int i = 0; i < NB_GAME; i++) {
                //     Game game = new Game(players, BOX_WIDTH, BOX_HEIGHT, MAX_GAIN, opti, true);
                //     game.playSilently();
                // }
            }
        }
    }
}

//Pour une proportion fixée et strat fixée, tracer le nb de strat gagnantes contre saboteur(k) sur X nb de partie en fonction de (k, opti)