package model;

/** Egy lépés: melyik bábu melyik irányba lép. */
public final class Move {

    private final Figure mover;
    private final Direction direction;

    public Move(Figure mover, Direction direction) {
        if (mover == null || direction == null) {
            throw new IllegalArgumentException("A bábu és az irány nem lehet null.");
        }
        this.mover = mover;
        this.direction = direction;
    }

    public Figure getMover() {
        return mover;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return mover.getClass().getSimpleName() + mover.getPosition() + " " + direction;
    }
}
