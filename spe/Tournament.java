public class Tournament {

    static int BOX_WIDTH = 120;
    static int BOX_HEIGHT = 120;
    static double MAX_GAIN = 20;
    static double OPTI = 10;

    static int REPRESENTATIVES = 100;
    static int NUMBER_STRATEGY = 10;

    public static void main(String[] args) {

        System.out.println(NUMBER_STRATEGY);
        System.out.println(REPRESENTATIVES*NUMBER_STRATEGY);
        System.out.println(REPRESENTATIVES); //NAIVE
        System.out.println(3*REPRESENTATIVES); //SABOTEUR
        System.out.println(2*REPRESENTATIVES); //DETERMINISTE
        System.out.println(2*REPRESENTATIVES); //JUSTICIER
        System.out.println(2*REPRESENTATIVES); //REVOLUTIONNAIRE
        
        Player[] players = new Player[REPRESENTATIVES*NUMBER_STRATEGY];
        for (int id = 0; id < REPRESENTATIVES*NUMBER_STRATEGY; id++) {
            if (id%NUMBER_STRATEGY == 0) {
                players[id] = new Naive(id);
            } else if (id%NUMBER_STRATEGY == 1) {
                players[id] = new Justicier(id, (int)OPTI);
            } else if (id%NUMBER_STRATEGY == 2) {
                players[id] = new Revolutionnaire(id, (int)OPTI);
            } else if (id%NUMBER_STRATEGY == 3) {
                players[id] = new Saboteur(id, (int)OPTI - 1);
            } else if (id%NUMBER_STRATEGY == 4) {
                players[id] = new Justicier(id, (int) OPTI+1);
            } else if (id%NUMBER_STRATEGY == 5) {
                players[id] = new Revolutionnaire(id, (int) OPTI+1);
            } else if (id%NUMBER_STRATEGY == 6) {
                players[id] = new Deterministe(id, 1, 1);
            } else if (id%NUMBER_STRATEGY == 7) {
                players[id] = new Saboteur(id, (int)OPTI+1);
            } else if (id%NUMBER_STRATEGY == 8) {
                players[id] = new Deterministe(id, 5, 3);
            } else if (id%NUMBER_STRATEGY == 9) {
                players[id] = new Saboteur(id, (int)OPTI);
            }
        }

        Game game = new Game(players, BOX_WIDTH, BOX_HEIGHT, MAX_GAIN, OPTI, false);

        GameScreen screen = new GameScreen(game);
        screen.display();
    }
}