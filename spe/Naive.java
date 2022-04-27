public class Naive extends Player {
    
    public Naive(int id) {
        super(id, "Naïve");
    }

    //Plante dans un emplacement libre au hasard
    //Si ce n'est pas possible sabote un joueur au hasard
    public void makeMove(Game game) {
        boolean planted = randomlyPlantIntelligent(game);
        if (!planted) {
            randomlySabotage(game);
        }
    }
}