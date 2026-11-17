package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FigureTest {

    @Test
    void foxAndHoundAreFigures() {
        Figure fox = new Fox(new Position(7, 0));
        Figure hound = new Hound(new Position(0, 1));

        assertInstanceOf(Fox.class, fox);
        assertInstanceOf(Hound.class, hound);
        assertEquals(new Position(7, 0), fox.getPosition());
        assertEquals(new Position(0, 1), hound.getPosition());
    }

    @Test
    void setPositionChangesPosition() {
        Fox fox = new Fox(new Position(7, 0));
        fox.setPosition(new Position(6, 1));

        assertEquals(new Position(6, 1), fox.getPosition());
    }

    @Test
    void nullPositionIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Fox(null));

        Hound hound = new Hound(new Position(0, 1));
        assertThrows(IllegalArgumentException.class, () -> hound.setPosition(null));
    }
}
