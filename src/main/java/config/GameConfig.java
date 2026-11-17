package config;

/** A játék beállításai. Singleton, a Sudoku GameConfig mintájára. */
public final class GameConfig {

    private static class Holder {
        private static final GameConfig INSTANCE = new GameConfig();
    }

    private final int boardSize = 8;
    private final int numberOfHounds = 4;
    private final String starterMapPath = "map/starter.txt";

    private GameConfig() {
    }

    public static GameConfig getInstance() {
        return Holder.INSTANCE;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getNumberOfHounds() {
        return numberOfHounds;
    }

    public String getStarterMapPath() {
        return starterMapPath;
    }
}
