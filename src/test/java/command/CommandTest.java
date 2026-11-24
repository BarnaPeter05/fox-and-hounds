package command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.regex.Matcher;
import model.GamePhase;
import model.GameState;
import org.junit.jupiter.api.Test;

class CommandTest {

    private static StubCommand moveCommand() {
        return new StubCommand("move", "move (\\d+) (\\d+)", Set.of(GamePhase.PLAYING));
    }

    @Test
    void matchExtractsArgumentGroups() {
        Matcher matcher = moveCommand().match("move 3 4");

        assertTrue(matcher.matches());
        assertEquals("3", matcher.group(1));
        assertEquals("4", matcher.group(2));
    }

    @Test
    void matchIgnoresCaseAndSurroundingWhitespace() {
        assertTrue(moveCommand().match("   MOVE 3 4  ").matches());
    }

    @Test
    void matchRejectsWrongSyntax() {
        StubCommand command = moveCommand();

        assertFalse(command.match("move x 4").matches());
        assertFalse(command.match("move 3").matches());
        assertFalse(command.match("move 3 4 5").matches());
        assertFalse(command.match(null).matches());
    }

    @Test
    void isApplicableOnlyInAllowedPhases() {
        StubCommand command = moveCommand();

        assertTrue(command.isApplicableInPhase(GamePhase.PLAYING));
        assertFalse(command.isApplicableInPhase(GamePhase.EDIT));
        assertFalse(command.isApplicableInPhase(GamePhase.GAME_ENDED));
    }

    @Test
    void syntaxDefaultsToName() {
        StubCommand command = moveCommand();

        assertEquals("move", command.getName());
        assertEquals("move", command.getSyntax());
    }

    @Test
    void applyIsPolymorphic() {
        StubCommand command = moveCommand();
        Command asBase = command;

        asBase.apply(new GameState(), command.match("move 1 2"));

        assertEquals(1, command.getApplyCount());
    }

    @Test
    void invalidConstructorArgumentsAreRejected() {
        Set<GamePhase> phases = Set.of(GamePhase.EDIT);

        assertThrows(IllegalArgumentException.class, () -> new StubCommand(" ", "x", phases));
        assertThrows(IllegalArgumentException.class, () -> new StubCommand(null, "x", phases));
        assertThrows(IllegalArgumentException.class, () -> new StubCommand("x", null, phases));
        assertThrows(IllegalArgumentException.class, () -> new StubCommand("x", "x", null));
    }
}
