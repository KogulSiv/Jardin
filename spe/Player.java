import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


public abstract class Player {

    private int id;
    private boolean alive;
    private List<Integer> usedBoxesId; //Les id des emplacements utilisés par le joueur
    private String name;
    private int numberOfSabotages; //Nombre de sabotages
    private double totalGain; //Gain totale
    private int strategyId;

    public Player(int id, String name) {
        this.id = id;
        this.alive = true;
        this.usedBoxesId = new ArrayList<>();
        this.name = name;
        this.numberOfSabotages =  0;
        this.totalGain = 0;
    }

    public Player() {
        this(0, "Jack");
    }

    public void setStrategyId(int id) {
        strategyId = id;
    }

    public int getStratId() {
        return strategyId;
    }

    public int getId() {
        return id; 
    }

    public boolean getAlive() {
        return alive;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfSabotages() {
        return numberOfSabotages;
    }

    public double getTotalGain() {
        return totalGain;
    }

    public void addGain(double gain) {
        this.totalGain += gain;
    }

    public void kill(Game game) {
        alive = false;
        for (int boxId : usedBoxesId) {
            game.addFreeBox(boxId);
        }
    }

    public double calculeGainPoly(double maxGain, double opti) {
        //return usedBoxesId.size();

        double alpha = -(maxGain/(opti*opti));
        
        int x = usedBoxesId.size();

        return alpha*(x*x - 2*opti*x);

    }

    public double calculeGain(Game game) {
        return calculeGainPoly(game.getMaxGain(), game.getOpti());
    }

    public int getPlayerPlantsState(Game game) {
        double gain = calculeGain(game);
        double gainMaxi = game.getMaxGain();

        if (gain >= gainMaxi/2) {
            return 1;
        } else if (gain >= gainMaxi/4) {
            return 2;
        } else if (gain >= gainMaxi/6) {
            return 3;
        } else {
            return 4;
        }
    }

    public void setPlayerPlantsState(Game game) {
        int state = getPlayerPlantsState(game);

        int j;
        int i;
        for (int id : usedBoxesId) {
            j = id%game.getBoxHeight();
            i = (int) ((id - j)/game.getBoxHeight());
            game.setBox(i, j, state);
        }
    }

    public void incNumberOfSabotages() {
        this.numberOfSabotages += 1;
    }
    
    public abstract void makeMove(Game game);


//
//Méthodes sur la liste des box occupés
//

    public void addBox(int boxId) {
        this.usedBoxesId.add(boxId);
    }

    public int getNumberOfUsedBoxes() {
        return usedBoxesId.size();
    }

    public int getBoxIdAt(int index) {
        return usedBoxesId.get(index);
    }

    public void removeUsedBox(int index) {
        usedBoxesId.remove(index);

    }

    public boolean inBoard() {
        return usedBoxesId.size() != 0;
    }

//
//Actions basiques
//

public boolean randomlyPlant(Game game) {
    int numberFree = game.getNumberOfFreeBoxes();
    if (numberFree == 0) {
        return false;
    }
    int boxId = getRandomIndex(game.getNumberOfFreeBoxes());
    game.plantInBox(boxId, this);
    return true;
}

public boolean randomlyPlantIntelligent(Game game) {
    if (calculeGain(game) < 0) {
        return false;
    }

    int numberFree = game.getNumberOfFreeBoxes();
    if (numberFree == 0) {
        return false;
    }
    int boxId = getRandomIndex(game.getNumberOfFreeBoxes());
    game.plantInBox(boxId, this);
    return true;
}

public boolean randomlySabotage(Game game) {
    int players = game.getNumberOfPlayersInBoard();

    if (game.getAllianceStrat()) {
        boolean cond = (game.getNumberOfStratInBoard() > 1) || (game.getNumberOfStratInBoard() == 1 && !game.isInBoardStrat(strategyId));
        if (!cond) {
            return false;
        }
    }
    
    Player playerToKill;
    if (players > 1 || (players == 1 && !this.inBoard())) {
        int playerIdInBoard = getRandomIndex(players);
        playerToKill = game.getPlayer(game.getPlayerInBoard(playerIdInBoard));
        while ((game.getAllianceStrat() && playerToKill.getStratId() == getStratId()) || game.getPlayerInBoard(playerIdInBoard) == getId()) {
            playerIdInBoard = getRandomIndex(players);
            playerToKill = game.getPlayer(game.getPlayerInBoard(playerIdInBoard));
        }

        //Player playerToKill = game.getPlayer(game.getPlayerInBoard(playerIdInBoard));
        int boxIndex = getRandomIndex(playerToKill.getNumberOfUsedBoxes());
        game.sabotageBox(boxIndex, playerToKill, this.id);
        this.numberOfSabotages += 1;
        return true;
    } else {
        return false;
    }
}

//
//Méthodes utilitaires
//

    public int getRandomIndex(int size) {
        int min = 0;
        int max = size;
        Random random = new Random();
		return random.nextInt(max + min) + min;  
    }


    public static Comparator<Player> ComparatorSabotage = new Comparator<Player>() {

        @Override
        public int compare(Player p1, Player p2) {
            return (int) (p2.getNumberOfSabotages() - p1.getNumberOfSabotages()); //Ordre décroissant
        }
    };

    public static Comparator<Player> ComparatorGain = new Comparator<Player>() {

        @Override
        public int compare(Player p1, Player p2) {
            double g1 = p1.getTotalGain();
            double g2 = p2.getTotalGain();

            if (g1 > g2) {
                return -1;
            } else if (g1 < g2) {
                return 1;
            } else {
                return 0;
            }
            //return (int) (p2.getTotalGain() - p1.getTotalGain()); //Ordre décroissant
        }
    };
}