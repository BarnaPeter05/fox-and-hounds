package command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import event.GameEvent;
import event.GameEventPublisher;
import event.GameEventType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import model.GamePhase;
import model.GameState;
import org.junit.jupiter.api.Test;

class HelpTest {

    @Test
    void helpListsOnlyCommandsOfTheCurrentPhase() {
        List<GameEvent> events = new ArrayList<>();
        GameEventPublisher publisher = new GameEventPublisher();
        publisher.subscribe(events::add);
        List<Command> commands = new ArrayList<>();
        commands.add(new Exit());
        commands.add(new StubCommand("editonly", "editonly", Set.of(GamePhase.EDIT)));
        commands.add(new StubCommand("playonly", "playonly", Set.of(GamePhase.PLAYING)));
        Help help = new Help(publisher, () -> commands);
        GameState state = new GameState();

        help.apply(state, help.match("help"));

        assertEquals(1, events.size());
        assertEquals(GameEventType.INFO, events.get(0).type());
        String text = events.get(0).message();
        assertTrue(text.contains("exit"));
        assertTrue(text.contains("editonly"));
        assertFalse(text.contains("playonly"));
    }

    @Test
    void helpMatchesOnlyItsOwnSyntaxInEveryPhase() {
        Help help = new Help(new GameEventPublisher(), List::of);

        assertTrue(help.match("Help").matches());
        assertFalse(help.match("help me").matches());
        for (GamePhase phase : GamePhase.values()) {
            assertTrue(help.isApplicableInPhase(phase), phase.name());
        }
    }
}
