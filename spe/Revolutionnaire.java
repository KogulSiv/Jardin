public class Revolutionnaire extends Player {
    //private double percent; //Compris entre 0 et 1
    private int k;

    public Revolutionnaire(int id, int k) {
        super(id, "Revolutionnaire["+k+"]");
        this.k = k;
        setStrategyId(4);
        //setStrategyId(0);
    }

    public boolean sabotage(Game game) {
        int nbAlivePlayers = game.getNumberOfPlayersAlive();
        Player currentPlayerToSabotage;

        if (game.getAllianceStrat()) {
            boolean cond = (game.getNumberOfStratInBoard() > 1) || (game.getNumberOfStratInBoard() == 1 && !game.isInBoardStrat(getStratId()));
            if (!cond) {
                return false;
            }
        }

        for (int i = 0; i < nbAlivePlayers; i++) {
            currentPlayerToSabotage = game.getPlayerSortedByGainAt(i);
            if ((game.getAllianceStrat() && currentPlayerToSabotage.getStratId() != getStratId()) && currentPlayerToSabotage.getId() != this.getId() && currentPlayerToSabotage.getNumberOfUsedBoxes() != 0) {
                int boxIndex = getRandomIndex(currentPlayerToSabotage.getNumberOfUsedBoxes());
                game.sabotageBox(boxIndex, currentPlayerToSabotage, getId());
                this.incNumberOfSabotages();
                return true;
            }
        }

        return false;
    }

    //Sabote le joueur le mieux classé de la partie s'il fait partie des (1-percent) premiers pourcents du classement
    //Sinon, plante au hasard
    public void makeMove(Game game) {

        boolean madeMove = false; 
        if (this.getNumberOfUsedBoxes() >= k) {
            madeMove = sabotage(game);
            if (!madeMove) {
                randomlyPlant(game);
            }
        } else {
                madeMove = randomlyPlant(game);
            if (!madeMove)  {
                sabotage(game);
            }
        }  
    }
}