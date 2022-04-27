public class Revolutionnaire extends Player {
    private double percent; //Compris entre 0 et 1

    public Revolutionnaire(int id, double percent) {
        super(id, "Revolutionnaire["+percent*100+"%]");
        this.percent = percent;
    }

    public boolean sabotage(Game game) {
        int nbAlivePlayers = game.getNumberOfPlayersAlive();
        Player currentPlayerToSabotage;

        for (int i = 0; i < nbAlivePlayers; i++) {
            currentPlayerToSabotage = game.getPlayerSortedByGainAt(i);
            if (currentPlayerToSabotage.getId() != this.getId() && currentPlayerToSabotage.getNumberOfUsedBoxes() != 0) {
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
        int position = game.getPosition(this);
        if (position < (1-percent)*game.getNumberOfPlayersAlive()) {
            madeMove = sabotage(game);
            if (!madeMove) {
                randomlyPlantIntelligent(game);
            }
        } else {
                madeMove = randomlyPlantIntelligent(game);
            if (!madeMove)  {
                sabotage(game);
            }
        }  
    }
}