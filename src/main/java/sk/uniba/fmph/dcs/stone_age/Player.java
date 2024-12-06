package sk.uniba.fmph.dcs.stone_age;

public class Player {
    private PlayerOrder playerOrder;
    private InterfacePlayerBoardGameBoard playerBoard;
    public Player(PlayerOrder playerOrder, InterfacePlayerBoardGameBoard playerBoard){
        this.playerOrder = playerOrder;
        this.playerBoard = playerBoard;
    }
    public PlayerOrder getPlayerOrder() {
        return playerOrder;
    }
    public InterfacePlayerBoardGameBoard getPlayerBoard() {
        return playerBoard;
    }
}