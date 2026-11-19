package model;

/** A játék életciklusának fázisai. A parancsok ezek alapján dönthetik el, hogy elérhetők-e. */
public enum GamePhase {

    /** A pálya szerkesztése: bábuk felvétele, mozgatása. */
    EDIT,

    /** A pálya elkészült, de a játékos még nem választott oldalt. */
    HAVE_BOARD,

    /** A játékos kiválasztotta az oldalát (róka vagy kutyák). */
    HAVE_SIDE,

    /** A felváltva lépéses játék folyamatban van. */
    PLAYING,

    /** A játék véget ért: a róka kijutott, vagy a kutyák bekerítették. */
    GAME_ENDED,

    /** Elmentett játék betöltése után ebbe az állapotba kerül a program. */
    SAVED_GAME
}
