public class Box {
    private int state; //0 inoccupé, 1 planté (etat parfait), 2 planté (état bon), 3 planté (état moyen), 4 planté (état mauvais), 5 saboté
    private int playerId;
    private int sabotedPlayerId;

    public Box(int state, int playerId) {
        this.state = state; 
        this.playerId = playerId;
        this.sabotedPlayerId = 0;
    }

    public Box() {
        this(0, 0);
    }

    public int getState() {
        return state;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getSabotedPlayerId() {
        return sabotedPlayerId;
    }

    public void set (int state, int playerId) {
        this.state = state;
        this.playerId = playerId;
    }

    public void set (int state, int playerId, int sabotedPlayerId) {
        this.state = state;
        this.playerId = playerId;
        this.sabotedPlayerId = sabotedPlayerId;
    }
}