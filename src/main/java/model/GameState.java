package model;

import businesslogic.BoardService;

/**
 * A játékmenet változó (mutable) állapota. A parancsok ezt módosítják, a GameEngine pedig ez alapján
 * dönti el, melyik parancs érhető el. A BoardService mező a 6. napon kerül ide.
 */
public class GameState {

    private GamePhase phase = GamePhase.EDIT;
    private int size = -1;
    private BoardService boardService;
    private Boolean humanPlaysWithFox = null;
    private boolean loadedAGame = false;
    private Boolean foxIsOnMove = null;
    private boolean exitRequested = false;

    public GamePhase getPhase() {
        return phase;
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }
    public BoardService getBoardService() {
        return boardService;
    }
    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /** True, ha az ember a rókával játszik; false, ha a kutyákkal; null, ha még nem választott. */
    public Boolean getHumanPlaysWithFox() {
        return humanPlaysWithFox;
    }

    public void setHumanPlaysWithFox(Boolean humanPlaysWithFox) {
        this.humanPlaysWithFox = humanPlaysWithFox;
    }

    public boolean isLoadedAGame() {
        return loadedAGame;
    }

    public void setLoadedAGame(boolean loadedAGame) {
        this.loadedAGame = loadedAGame;
    }

    /** True, ha a róka következik; null, amíg a játék nem indult el. */
    public Boolean getFoxIsOnMove() {
        return foxIsOnMove;
    }

    public void setFoxIsOnMove(Boolean foxIsOnMove) {
        this.foxIsOnMove = foxIsOnMove;
    }

    public boolean isExitRequested() {
        return exitRequested;
    }

    public void setExitRequested(boolean exitRequested) {
        this.exitRequested = exitRequested;
    }
}
