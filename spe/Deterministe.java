public class Deterministe extends Player {
    private int plantRate;
    private int sabotageRate;
    private int currentState;

    public Deterministe(int id, int plantRate, int sabotageRate) {
        super(id, "Déterministe["+plantRate+","+sabotageRate+"]");
        this.plantRate = plantRate;
        this.sabotageRate = sabotageRate;
        this.currentState = 1;
        setStrategyId(2);
        //setStrategyId(0);
    }


    public void makeMove(Game game) {
        boolean madeMove;
        if (currentState <= plantRate) {
            madeMove = randomlyPlantIntelligent(game);
            if (!madeMove)  {
                //randomlySabotage(game);
            }
        } else {
            madeMove = randomlySabotage(game);
            if (!madeMove) {
                //randomlyPlantIntelligent(game);
            }

            if (currentState == plantRate+sabotageRate) {
                currentState = 0;
            }
        }
        currentState += 1;
    }
}