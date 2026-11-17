package model;

import java.util.List;
import java.util.Objects;

/**
 * A játéktábla változtathatatlan (immutable) pillanatképe. Felépítése a Sudoku MapVO-t követi:
 * privát konstruktor, Builder, és "with..." metódusok az új példány előállításához.
 */
public final class Board {

    private final int boardSize;
    private final Position foxPosition;
    private final List<Position> houndsPositions;
    private final boolean foxOnMove;

    private Board(Builder builder) {
        this.boardSize = builder.boardSize;
        this.foxPosition = builder.foxPosition;
        // Védekező másolat: a Position immutable, így a lista másolása elég.
        this.houndsPositions = List.copyOf(builder.houndsPositions);
        this.foxOnMove = builder.foxOnMove;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getBoardSize() {
        return boardSize;
    }

    /** A róka helye; üres pályán null lehet. */
    public Position getFoxPosition() {
        return foxPosition;
    }

    /** Módosíthatatlan lista. */
    public List<Position> getHoundsPositions() {
        return houndsPositions;
    }

    public boolean isFoxOnMove() {
        return foxOnMove;
    }

    public Board withFoxPosition(Position newFoxPosition) {
        return toBuilder().withFoxPosition(newFoxPosition).build();
    }

    public Board withHoundsPositions(List<Position> newHoundsPositions) {
        return toBuilder().withHoundsPositions(newHoundsPositions).build();
    }

    public Board withFoxOnMove(boolean newFoxOnMove) {
        return toBuilder().withFoxOnMove(newFoxOnMove).build();
    }

    private Builder toBuilder() {
        return builder()
                .withBoardSize(boardSize)
                .withFoxPosition(foxPosition)
                .withHoundsPositions(houndsPositions)
                .withFoxOnMove(foxOnMove);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board board = (Board) o;
        return boardSize == board.boardSize
                && foxOnMove == board.foxOnMove
                && Objects.equals(foxPosition, board.foxPosition)
                && houndsPositions.equals(board.houndsPositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardSize, foxPosition, houndsPositions, foxOnMove);
    }

    public static final class Builder {

        private int boardSize;
        private Position foxPosition;
        private List<Position> houndsPositions = List.of();
        private boolean foxOnMove = true;

        public Builder withBoardSize(int boardSize) {
            this.boardSize = boardSize;
            return this;
        }

        public Builder withFoxPosition(Position foxPosition) {
            this.foxPosition = foxPosition;
            return this;
        }

        public Builder withHoundsPositions(List<Position> houndsPositions) {
            this.houndsPositions = houndsPositions;
            return this;
        }

        public Builder withFoxOnMove(boolean foxOnMove) {
            this.foxOnMove = foxOnMove;
            return this;
        }

        public Board build() {
            if (boardSize <= 0) {
                throw new IllegalStateException("A pályaméret pozitív kell legyen.");
            }
            if (houndsPositions == null) {
                throw new IllegalStateException("A kutyák listája nem lehet null.");
            }
            return new Board(this);
        }
    }
}
