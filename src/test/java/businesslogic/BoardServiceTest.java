package businesslogic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Fox;
import model.Hound;
import model.Move;
import model.Position;
import org.junit.jupiter.api.Test;

class BoardServiceTest {

    @Test
    void constructorRejectsNullBoard() {
        assertThrows(IllegalArgumentException.class, () -> new BoardService(null));
    }

    @Test
    void getBoardReturnsTheGivenBoard() {
        Board board = BoardService.getStarterBoard(8);

        assertSame(board, new BoardService(board).getBoard());
    }

    @Test
    void emptyBoardHasNoFigures() {
        Board board = BoardService.getEmptyBoard(8);

        assertEquals(8, board.getBoardSize());
        assertNull(board.getFoxPosition());
        assertTrue(board.getHoundsPositions().isEmpty());
    }

    @Test
    void starterBoardOfEightHasFourHoundsAndBottomLeftFox() {
        Board board = BoardService.getStarterBoard(8);

        assertEquals(8, board.getBoardSize());
        assertEquals(new Position(7, 0), board.getFoxPosition());
        assertEquals(List.of(new Position(0, 1), new Position(0, 3), new Position(0, 5), new Position(0, 7)),
                board.getHoundsPositions());
        assertTrue(board.isFoxOnMove());
    }

    @Test
    void starterBoardFiguresStandOnDarkSquares() {
        for (int n = 4; n <= 10; n++) {
            Board board = BoardService.getStarterBoard(n);

            Position fox = board.getFoxPosition();
            assertEquals(n - 1, fox.getRow());
            assertTrue(BoardService.isDark(fox.getRow(), fox.getCol()), "róka, n=" + n);
            for (Position hound : board.getHoundsPositions()) {
                assertEquals(0, hound.getRow());
                assertTrue(BoardService.isDark(hound.getRow(), hound.getCol()), "kutya, n=" + n);
            }
        }
    }

    @Test
    void tooSmallBoardIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> BoardService.getEmptyBoard(3));
        assertThrows(IllegalArgumentException.class, () -> BoardService.getStarterBoard(3));
    }

    @Test
    void fitsChecksBoardBoundaries() {
        BoardService service = new BoardService(BoardService.getStarterBoard(8));

        assertTrue(service.fits(new Position(0, 0)));
        assertTrue(service.fits(new Position(7, 7)));
        assertFalse(service.fits(new Position(-1, 0)));
        assertFalse(service.fits(new Position(0, -1)));
        assertFalse(service.fits(new Position(8, 0)));
        assertFalse(service.fits(new Position(0, 8)));
    }

    @Test
    void isEmptyRecognizesFoxAndHounds() {
        BoardService service = new BoardService(BoardService.getStarterBoard(8));

        assertFalse(service.isEmpty(new Position(7, 0)));
        assertFalse(service.isEmpty(new Position(0, 3)));
        assertTrue(service.isEmpty(new Position(3, 4)));
    }

    @Test
    void isEmptyWorksOnBoardWithoutFox() {
        BoardService service = new BoardService(BoardService.getEmptyBoard(8));

        assertTrue(service.isEmpty(new Position(7, 0)));
    }

    private static Board board(Position fox, Position... hounds) {
        return Board.builder()
                .withBoardSize(8)
                .withFoxPosition(fox)
                .withHoundsPositions(List.of(hounds))
                .build();
    }

    private static List<String> names(List<Move> moves) {
        List<String> result = new ArrayList<>();
        for (Move move : moves) {
            result.add(move.toString());
        }
        return result;
    }

    @Test
    void foxInCenterHasFourMoves() {
        BoardService service = new BoardService(board(new Position(4, 3)));

        assertEquals(List.of("Fox(4,3) NORTHWEST", "Fox(4,3) NORTHEAST", "Fox(4,3) SOUTHWEST", "Fox(4,3) SOUTHEAST"),
                names(service.determineFoxPossibleMoves()));
    }

    @Test
    void foxInCornerAndOnEdgeHasFewerMoves() {
        BoardService corner = new BoardService(BoardService.getStarterBoard(8));
        BoardService edge = new BoardService(board(new Position(4, 7)));

        assertEquals(List.of("Fox(7,0) NORTHEAST"), names(corner.determineFoxPossibleMoves()));
        assertEquals(List.of("Fox(4,7) NORTHWEST", "Fox(4,7) SOUTHWEST"), names(edge.determineFoxPossibleMoves()));
    }

    @Test
    void foxCannotStepOntoHounds() {
        BoardService service = new BoardService(board(new Position(7, 0), new Position(6, 1)));

        assertTrue(service.determineFoxPossibleMoves().isEmpty());
    }

    @Test
    void foxMovesAreEmptyWithoutFox() {
        BoardService service = new BoardService(BoardService.getEmptyBoard(8));

        assertTrue(service.determineFoxPossibleMoves().isEmpty());
    }

    @Test
    void foxMoveCarriesAFoxFigure() {
        BoardService service = new BoardService(BoardService.getStarterBoard(8));

        assertInstanceOf(Fox.class, service.determineFoxPossibleMoves().get(0).getMover());
    }

    @Test
    void houndsOnStarterBoardHaveSevenMoves() {
        BoardService service = new BoardService(BoardService.getStarterBoard(8));
        List<Move> moves = service.determineHoundsPossibleMoves();

        assertEquals(7, moves.size());
        assertEquals("Hound(0,1) SOUTHWEST", moves.get(0).toString());
        assertEquals("Hound(0,7) SOUTHWEST", moves.get(6).toString());
        assertInstanceOf(Hound.class, moves.get(0).getMover());
    }

    @Test
    void houndsOnlyMoveDownwardAndNotOntoFigures() {
        BoardService service = new BoardService(board(new Position(1, 2), new Position(0, 1), new Position(7, 2)));

        assertEquals(List.of("Hound(0,1) SOUTHWEST"), names(service.determineHoundsPossibleMoves()));
    }

    @Test
    void houndBlockedByOtherHoundsHasNoMove() {
        Board board = board(null, new Position(3, 2), new Position(4, 1), new Position(4, 3));
        BoardService service = new BoardService(board);

        assertEquals(List.of("Hound(4,1) SOUTHWEST", "Hound(4,1) SOUTHEAST",
                "Hound(4,3) SOUTHWEST", "Hound(4,3) SOUTHEAST"), names(service.determineHoundsPossibleMoves()));
    }
}