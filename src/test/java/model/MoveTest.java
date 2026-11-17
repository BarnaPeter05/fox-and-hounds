package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MoveTest {

    @Test
    void moveStoresMoverAndDirection() {
        Fox fox = new Fox(new Position(7, 0));
        Move move = new Move(fox, Direction.NORTHEAST);

        assertSame(fox, move.getMover());
        assertEquals(Direction.NORTHEAST, move.getDirection());
    }

    @Test
    void nullArgumentsAreRejected() {
        Fox fox = new Fox(new Position(7, 0));

        assertThrows(IllegalArgumentException.class, () -> new Move(null, Direction.NORTHEAST));
        assertThrows(IllegalArgumentException.class, () -> new Move(fox, null));
    }
}
