package command;

import java.util.Set;
import java.util.regex.Matcher;
import model.GamePhase;
import model.GameState;

/** Tesztekhez használt egyszerű parancs, ami csak számolja a végrehajtásait. */
class StubCommand extends Command {

    private int applyCount = 0;

    StubCommand(String name, String regex, Set<GamePhase> phases) {
        super(name, regex, phases);
    }

    @Override
    public void apply(GameState state, Matcher matcher) {
        applyCount++;
    }

    int getApplyCount() {
        return applyCount;
    }
}
