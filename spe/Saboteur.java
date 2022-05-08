public class Saboteur extends Player {
    private int nbPlantesMax;

    public Saboteur(int id, int nbPlantesMax) {
        super(id, "Saboteur["+nbPlantesMax+"]");
        this.nbPlantesMax = nbPlantesMax;
        setStrategyId(1);
        //setStrategyId(0);
    }

    //Plante dans un emplacement libre au hasard jusqu'à avoir nbPlantesMax
    //Ensuite sabote un joueur au hasard
    public void makeMove(Game game) {

        int nbPlantes = getNumberOfUsedBoxes();
        boolean madeMove;
        if (nbPlantes >= nbPlantesMax) {
            madeMove = randomlySabotage(game);
            if (!madeMove) {
                randomlyPlant(game);
            }
        } else {
            madeMove = randomlyPlant(game);
            if (!madeMove)  {
                randomlySabotage(game);
            }
        }
    }
}