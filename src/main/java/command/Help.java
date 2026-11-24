package command;

import event.GameEvent;
import event.GameEventPublisher;
import event.GameEventType;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import model.GamePhase;
import model.GameState;

/** Kiírja az aktuális fázisban elérhető parancsokat. Minden fázisban elérhető. */
public class Help extends Command {

    private final GameEventPublisher publisher;
    private final Supplier<List<Command>> commands;

    /**
     * @param publisher ezen keresztül jelenik meg a súgó (a konzolra az eseményfigyelő írja ki)
     * @param commands  az összes regisztrált parancs (a 10. naptól a CommandRegistry adja)
     */
    public Help(GameEventPublisher publisher, Supplier<List<Command>> commands) {
        super("help", "help", EnumSet.allOf(GamePhase.class));
        this.publisher = publisher;
        this.commands = commands;
    }

    @Override
    public void apply(GameState state, Matcher matcher) {
        StringBuilder text = new StringBuilder("Elérhető parancsok (" + state.getPhase() + "):");
        for (Command command : commands.get()) {
            if (command.isApplicableInPhase(state.getPhase())) {
                text.append("\n  ").append(command.getSyntax());
            }
        }
        publisher.publish(new GameEvent(GameEventType.INFO, text.toString()));
    }
}
