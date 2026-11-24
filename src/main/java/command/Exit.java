package command;

import java.util.EnumSet;
import java.util.regex.Matcher;
import model.GamePhase;
import model.GameState;

/** Kilépés a programból. Minden fázisban elérhető. */
public class Exit extends Command {

    public Exit() {
        super("exit", "exit", EnumSet.allOf(GamePhase.class));
    }

    @Override
    public void apply(GameState state, Matcher matcher) {
        state.setExitRequested(true);
    }
}
