import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Game {
    static int ELIMINATION_TURNS = 5;

    private Box[][] boxes;
    private Player[] players;
    private List<Integer> freeBoxes; // Emplacements libres 
    private List<Integer> nextFreeBoxes; // Emplacements qui seront libres au prochain tour
    private List<Integer> inBoardPlayers; // Joueurs qui ont une plante dans le jeu
    private List<Player> playersSortedBySabotage; //Joueurs triés pr ordre décroissante de nb de sabotage
    private List<Player> playersSortedByGain; //Joueurs triés pas ordre décroissante de gains
    private int box_height;
    private int nbAlivePlayers;
    private int turns_before_elimination; //Nombre de tours avant élimination

    private double maxGain;
    private double opti;

    public Game(int number_of_players, int box_width, int box_height, double maxGain, double opti) {
        players = new Player[number_of_players];
        boxes = new Box[box_width][box_height];
        this.freeBoxes = new ArrayList<>();
        this.nextFreeBoxes = new ArrayList<>();
        this.inBoardPlayers = new ArrayList<>();
        this.playersSortedBySabotage = new ArrayList<>();
        this.playersSortedByGain = new ArrayList<>();
        this.box_height = box_height;
        this.nbAlivePlayers = number_of_players;
        this.turns_before_elimination = ELIMINATION_TURNS;
        this.maxGain = maxGain;
        this.opti = opti;

        for (int i = 0; i < number_of_players; i++) {
            players[i] = new Naive(i);
            playersSortedBySabotage.add(players[i]);
            playersSortedByGain.add(players[i]);
        }

        for (int i = 0; i < box_width; i++) {
            for (int j = 0; j < box_height; j++) {
                boxes[i][j] = new Box();
                freeBoxes.add(i*box_height + j);
            }
        }

    }

    public Game(Player[] players, int box_width, int box_height, double maxGain, double opti) {
        this.players = new Player[players.length];
        boxes = new Box[box_width][box_height];
        this.freeBoxes = new ArrayList<>();
        this.nextFreeBoxes = new ArrayList<>();
        this.inBoardPlayers = new ArrayList<>();
        this.playersSortedBySabotage = new ArrayList<>();
        this.playersSortedByGain = new ArrayList<>();
        this.box_height = box_height;
        this.nbAlivePlayers = players.length;
        this.turns_before_elimination = ELIMINATION_TURNS;
        this.maxGain = maxGain;
        this.opti = opti;

        for (int i = 0; i < players.length; i++) {
            this.players[i] = players[i];
            playersSortedBySabotage.add(players[i]);
            playersSortedByGain.add(players[i]);
        }

        for (int i = 0; i < box_width; i++) {
            for (int j = 0; j < box_height; j++) {
                boxes[i][j] = new Box();
                freeBoxes.add(i*box_height + j);
            }
        }
    }

    public Box[][] getBoxes() {
        return boxes;
    }

    public Player getPlayer(int playerId) {
        return players[playerId];
    }

    public boolean nextTurn() {
        for (Player player : players) {
            if (player.getAlive()) {
                player.makeMove(this);
            }
        }
        freeBoxes.addAll(nextFreeBoxes);
        nextFreeBoxes.clear();
        Collections.sort(playersSortedBySabotage, Player.ComparatorSabotage);


        for (Player player : playersSortedByGain) {
            player.addGain(player.calculeGain(this)*player.getNumberOfUsedBoxes());
        }
        Collections.sort(playersSortedByGain, Player.ComparatorGain);

        if (turns_before_elimination == 0) {
            Player playerToEliminate = playersSortedByGain.get(playersSortedByGain.size() - 1);
            
            //System.out.println("Player " + playerToEliminate.getId() + " (" + playerToEliminate.getName() + ") is eliminated with a gain of " + playerToEliminate.getTotalGain());
            System.out.println(playerToEliminate.getName() + ": " + playerToEliminate.getId());
            nbAlivePlayers -= 1;
            playerToEliminate.kill(this);
            playersSortedBySabotage.remove(playerToEliminate);
            playersSortedByGain.remove(playerToEliminate);
            turns_before_elimination = ELIMINATION_TURNS;
        } else {
            turns_before_elimination--;
        }

        for (Player p : playersSortedByGain) {
            p.setPlayerPlantsState(this);
        }

        if (nbAlivePlayers == 0) {
            System.out.println("Game over !");
            return false;
        }
        return true;

    }

    public boolean nextTurnSilent() {
        for (Player player : players) {
            if (player.getAlive()) {
                player.makeMove(this);
            }
        }
        freeBoxes.addAll(nextFreeBoxes);
        nextFreeBoxes.clear();
        Collections.sort(playersSortedBySabotage, Player.ComparatorSabotage);


        for (Player player : playersSortedByGain) {
            player.addGain(player.calculeGain(this));
        }
        Collections.sort(playersSortedByGain, Player.ComparatorGain);

        Player playerToEliminate = players[0];
        if (turns_before_elimination == 0) {
            playerToEliminate = playersSortedByGain.get(playersSortedByGain.size() - 1);
            
            //System.out.println("Player " + playerToEliminate.getId() + " (" + playerToEliminate.getName() + ") is eliminated with a gain of " + playerToEliminate.getTotalGain());
            //System.out.println(playerToEliminate.getName());
            nbAlivePlayers -= 1;
            playerToEliminate.kill(this);
            playersSortedBySabotage.remove(playerToEliminate);
            playersSortedByGain.remove(playerToEliminate);
            turns_before_elimination = ELIMINATION_TURNS;
        } else {
            turns_before_elimination--;
        }

        if (nbAlivePlayers == 0) {
            System.out.println(playerToEliminate.getName());
            //System.out.println("Game over !");
            return false;
        }
        return true;

    }

    public void playSilently() {
        boolean toGo = true;

        while (toGo) {
            toGo = this.nextTurnSilent();
        }
    }

    public int getNumberOfFreeBoxes() {
        return freeBoxes.size();
    }

    public void addFreeBox(int boxId) {
        freeBoxes.add(boxId);
        int j = boxId%box_height;
        int i = (int) ((boxId - j)/box_height);

        boxes[i][j].set(0, 0);
    }

    public int getNumberOfPlayersInBoard() {
        return inBoardPlayers.size();
    }

    public boolean isInBoard(int playerId) {
        return inBoardPlayers.contains(playerId);  
    }

    public int getPlayerInBoard(int playerId) {
        return inBoardPlayers.get(playerId);
    }

    public int getPosition(Player player) {
        return playersSortedByGain.lastIndexOf(player);
    }

    //public int getBestSabotagerAt(int position) {
    //    return playersSortedBySabotage.get(position);
    //}

    public int getNumberOfPlayersAlive() {
        return nbAlivePlayers;
    }

    public Player getPlayerSortedBySabotageAt(int index) {
        return playersSortedBySabotage.get(index);
    }

    public Player getPlayerSortedByGainAt(int index) {
        return playersSortedByGain.get(index);
    }

    public double getMaxGain() {
        return maxGain;
    }

    public double getOpti() {
        return opti;
    }

    public int getBoxHeight() {
        return box_height;
    }

    public void plantInBox(int boxPosition, Player player) {
        if (!player.inBoard()) {
            inBoardPlayers.add(player.getId());
        }
        player.addBox(freeBoxes.get(boxPosition));
        int j = freeBoxes.get(boxPosition)%box_height;
        int i = (int) ((freeBoxes.get(boxPosition) - j)/box_height);        

        this.boxes[i][j].set(1, player.getId());
        freeBoxes.remove(boxPosition);
    }

    public void sabotageBox(int boxIndex, Player sabotedPlayer, int sabotingPlayerId) {

        int boxPosition = sabotedPlayer.getBoxIdAt(boxIndex);
        int j = boxPosition%box_height;
        int i = (int) ((boxPosition - j)/box_height);

        this.boxes[i][j].set(5, 0, sabotingPlayerId);
        sabotedPlayer.removeUsedBox(boxIndex);
        if (!sabotedPlayer.inBoard()) {
            inBoardPlayers.remove(new Integer(sabotedPlayer.getId()));
        }
        nextFreeBoxes.add(boxPosition);
    }

    public void setBox(int boxI, int boxJ, int state) {
        this.boxes[boxI][boxJ].set(state, this.boxes[boxI][boxJ].getPlayerId());
    }

}