package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BoardTest {

    private static Board.Builder validBuilder() {
        return Board.builder()
                .withBoardSize(8)
                .withFoxPosition(new Position(7, 0))
                .withHoundsPositions(List.of(new Position(0, 1), new Position(0, 3)))
                .withFoxOnMove(true);
    }

    @Test
    void gettersReturnBuilderValues() {
        Board board = validBuilder().build();

        assertEquals(8, board.getBoardSize());
        assertEquals(new Position(7, 0), board.getFoxPosition());
        assertEquals(List.of(new Position(0, 1), new Position(0, 3)), board.getHoundsPositions());
        assertTrue(board.isFoxOnMove());
    }

    @Test
    void externalListChangeDoesNotAffectBoard() {
        List<Position> hounds = new ArrayList<>();
        hounds.add(new Position(0, 1));
        Board board = validBuilder().withHoundsPositions(hounds).build();

        hounds.add(new Position(0, 3));
        hounds.clear();

        assertEquals(List.of(new Position(0, 1)), board.getHoundsPositions());
    }

    @Test
    void returnedListCannotBeModified() {
        List<Position> returned = validBuilder().build().getHoundsPositions();

        assertThrows(UnsupportedOperationException.class, () -> returned.add(new Position(0, 5)));
    }

    @Test
    void withMethodsReturnNewBoardAndKeepOldOne() {
        Board original = validBuilder().build();

        Board moved = original.withFoxPosition(new Position(6, 1)).withFoxOnMove(false);

        assertEquals(new Position(7, 0), original.getFoxPosition());
        assertTrue(original.isFoxOnMove());
        assertEquals(new Position(6, 1), moved.getFoxPosition());
        assertFalse(moved.isFoxOnMove());
        assertEquals(original.getHoundsPositions(), moved.getHoundsPositions());
    }

    @Test
    void withHoundsPositionsReplacesHounds() {
        Board board = validBuilder().build().withHoundsPositions(List.of(new Position(1, 2)));

        assertEquals(List.of(new Position(1, 2)), board.getHoundsPositions());
    }

    @Test
    void emptyBoardMayHaveNoFoxAndNoHounds() {
        Board board = Board.builder().withBoardSize(8).withFoxPosition(null).build();

        assertNull(board.getFoxPosition());
        assertTrue(board.getHoundsPositions().isEmpty());
    }

    @Test
    void invalidBuilderValuesAreRejected() {
        assertThrows(IllegalStateException.class, () -> validBuilder().withBoardSize(0).build());
        assertThrows(IllegalStateException.class, () -> validBuilder().withHoundsPositions(null).build());
    }

    @Test
    void equalsAndHashCodeWork() {
        Board a = validBuilder().build();
        Board b = validBuilder().build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, validBuilder().withFoxOnMove(false).build());
    }
}
