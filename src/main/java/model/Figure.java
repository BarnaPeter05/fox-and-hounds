package model;

/** A játék bábuinak közös ősosztálya: a bábu helyét tárolja. */
public abstract class Figure {

    private Position position;

    protected Figure(Position position) {
        setPosition(position);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("A pozíció nem lehet null.");
        }
        this.position = position;
    }
}
