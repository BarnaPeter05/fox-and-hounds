package command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.GamePhase;
import model.GameState;
import org.junit.jupiter.api.Test;

class ExitTest {

    @Test
    void exitRequestsProgramExit() {
        Exit exit = new Exit();
        GameState state = new GameState();

        exit.apply(state, exit.match("exit"));

        assertTrue(state.isExitRequested());
    }

    @Test
    void exitMatchesOnlyItsOwnSyntax() {
        Exit exit = new Exit();

        assertTrue(exit.match("EXIT").matches());
        assertFalse(exit.match("exit now").matches());
        assertFalse(exit.match("quit").matches());
    }

    @Test
    void exitIsAvailableInEveryPhase() {
        Exit exit = new Exit();

        for (GamePhase phase : GamePhase.values()) {
            assertTrue(exit.isApplicableInPhase(phase), phase.name());
        }
    }
}
