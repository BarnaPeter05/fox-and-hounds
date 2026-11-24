package command;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.GamePhase;
import model.GameState;

/**
 * Egy parancssoros parancs: a szintaxisát (regex), azt, hogy mely játékfázisban érhető el,
 * és a végrehajtását is magában foglalja (GoF Command + szövegfeldolgozás).
 */
public abstract class Command {

    private final Pattern pattern;
    private final String name;
    protected final Set<GamePhase> allowedPhases;

    /**
     * @param name          a parancs neve (súgóhoz és azonosításhoz)
     * @param regex         a parancs szintaxisa; kis- és nagybetűre nem érzékeny, az egész bemenetre illeszkedik
     * @param allowedPhases azok a fázisok, amelyekben a parancs elérhető
     */
    protected Command(String name, String regex, Set<GamePhase> allowedPhases) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A parancs neve nem lehet üres.");
        }
        if (regex == null || allowedPhases == null) {
            throw new IllegalArgumentException("A regex és az engedélyezett fázisok nem lehetnek null.");
        }
        this.name = name;
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        this.allowedPhases = Set.copyOf(allowedPhases);
    }

    /**
     * A (levágott szélű) bemenetre illesztett Matcher. A matches() hívása a hívó dolga:
     * ha igaz, a Matcher csoportjai az argumentumokat tartalmazzák, és átadható az apply-nak.
     */
    public Matcher match(String input) {
        return pattern.matcher(input == null ? "" : input.trim());
    }

    public boolean isApplicableInPhase(GamePhase phase) {
        return allowedPhases.contains(phase);
    }

    /** Végrehajtás: a már illeszkedett Matcher csoportjai alapján módosítja a játékállapotot. */
    public abstract void apply(GameState state, Matcher matcher);

    /** A súgóban megjelenő szintaxis. Alapértelmezetten a név, az argumentumos parancsok felülírják. */
    public String getSyntax() {
        return name;
    }

    public String getName() {
        return name;
    }
}
