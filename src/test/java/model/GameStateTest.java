package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import businesslogic.BoardService;
import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void defaultValuesAreCorrect() {
        GameState state = new GameState();

        assertEquals(GamePhase.EDIT, state.getPhase());
        assertEquals(-1, state.getSize());
        assertNull(state.getHumanPlaysWithFox());
        assertFalse(state.isLoadedAGame());
        assertNull(state.getFoxIsOnMove());
        assertFalse(state.isExitRequested());
        assertNull(state.getBoardService());
    }

    @Test
    void boardServiceCanBeSet() {
        GameState state = new GameState();
        BoardService service = new BoardService(BoardService.getStarterBoard(8));

        state.setBoardService(service);

        assertSame(service, state.getBoardService());
    }

    @Test
    void settersChangeValues() {
        GameState state = new GameState();

        state.setPhase(GamePhase.PLAYING);
        state.setSize(8);
        state.setHumanPlaysWithFox(true);
        state.setLoadedAGame(true);
        state.setFoxIsOnMove(false);
        state.setExitRequested(true);

        assertEquals(GamePhase.PLAYING, state.getPhase());
        assertEquals(8, state.getSize());
        assertTrue(state.getHumanPlaysWithFox());
        assertTrue(state.isLoadedAGame());
        assertFalse(state.getFoxIsOnMove());
        assertTrue(state.isExitRequested());
    }

    @Test
    void phasesAreInLifecycleOrder() {
        assertEquals(6, GamePhase.values().length);
        assertEquals(GamePhase.EDIT, GamePhase.values()[0]);
        assertEquals(GamePhase.HAVE_BOARD, GamePhase.valueOf("HAVE_BOARD"));
    }
}