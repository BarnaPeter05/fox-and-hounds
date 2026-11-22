package businesslogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Board;
import model.Direction;
import model.Fox;
import model.Hound;
import model.Move;
import model.Position;

/**
 * A játékszabályok és a pálya életciklusa. A Board immutable, ezért a későbbi lépések
 * (6-8. nap) mindig új Boardot hoznak létre, és a {@code board} mezőt cserélik le.
 */
public class BoardService {

    private static final int MIN_BOARD_SIZE = 4;
    private static final List<Direction> HOUND_DIRECTIONS = List.of(Direction.SOUTHWEST, Direction.SOUTHEAST);

    private final Random random;
    private Board board;

    public BoardService(Board board) {
        this(board, new Random());
    }

    /** A Random kívülről adható, így a véletlen lépések tesztelhetők (pl. fix vagy seedelt generátorral). */
    public BoardService(Board board, Random random) {
        if (board == null || random == null) {
            throw new IllegalArgumentException("A pálya és a véletlengenerátor nem lehet null.");
        }
        this.board = board;
        this.random = random;
    }

    public Board getBoard() {
        return board;
    }

    /** Üres pálya: nincs rajta se róka, se kutya (a róka helye null). A szerkesztő (EDIT) fázis kezdőpontja. */
    public static Board getEmptyBoard(int n) {
        checkSize(n);
        return Board.builder()
                .withBoardSize(n)
                .withFoxPosition(null)
                .withHoundsPositions(List.of())
                .withFoxOnMove(true)
                .build();
    }

    /**
     * Alaphelyzet: a kutyák a 0. sor sötét mezőin, a róka az alsó sor első sötét mezőjén.
     * A róka lép először.
     */
    public static Board getStarterBoard(int n) {
        checkSize(n);
        List<Position> hounds = new ArrayList<>();
        for (int col = 0; col < n; col++) {
            if (isDark(0, col)) {
                hounds.add(new Position(0, col));
            }
        }
        int foxCol = isDark(n - 1, 0) ? 0 : 1;
        return Board.builder()
                .withBoardSize(n)
                .withFoxPosition(new Position(n - 1, foxCol))
                .withHoundsPositions(hounds)
                .withFoxOnMove(true)
                .build();
    }

    /**
     * Végrehajt egy lépést: új Board készül (a régi változatlan marad), a mozgó fél helye megváltozik,
     * a sor a másik félre kerül, és a {@code board} mező az új példányra cserélődik.
     *
     * @throws IllegalArgumentException ha a lépés null, nem az következik, aki lépne, vagy szabálytalan
     */
    public void doMove(Move move) {
        if (move == null) {
            throw new IllegalArgumentException("A lépés nem lehet null.");
        }
        boolean foxMove = move.getMover() instanceof Fox;
        if (foxMove != board.isFoxOnMove()) {
            throw new IllegalArgumentException("Most nem ez a fél következik: " + move);
        }
        List<Move> legalMoves = foxMove ? determineFoxPossibleMoves() : determineHoundsPossibleMoves();
        if (!isLegal(move, legalMoves)) {
            throw new IllegalArgumentException("Szabálytalan lépés: " + move);
        }
        Position from = move.getMover().getPosition();
        Position to = new Position(from, move.getDirection());
        if (foxMove) {
            board = board.withFoxPosition(to).withFoxOnMove(false);
        } else {
            List<Position> hounds = new ArrayList<>(board.getHoundsPositions());
            hounds.set(hounds.indexOf(from), to);
            board = board.withHoundsPositions(hounds).withFoxOnMove(true);
        }
    }

    /** A róka nyert, ha elérte a 0. sort. */
    public boolean winFox() {
        Position fox = board.getFoxPosition();
        return fox != null && fox.getRow() == 0;
    }

    /** A kutyák nyertek, ha a róka nem tud lépni (és nem ért el a 0. sorba). */
    public boolean winHounds() {
        return board.getFoxPosition() != null && !winFox() && determineFoxPossibleMoves().isEmpty();
    }

    /** Véletlenszerű szabályos lépés a rókának. */
    public void doARandomFoxMove() {
        List<Move> moves = determineFoxPossibleMoves();
        if (moves.isEmpty()) {
            throw new IllegalStateException("A rókának nincs lehetséges lépése.");
        }
        doMove(moves.get(random.nextInt(moves.size())));
    }

    /** Véletlenszerű szabályos lépés egy kutyának. */
    public void doARandomHoundMove() {
        List<Move> moves = determineHoundsPossibleMoves();
        if (moves.isEmpty()) {
            throw new IllegalStateException("A kutyáknak nincs lehetséges lépése.");
        }
        doMove(moves.get(random.nextInt(moves.size())));
    }

    /** A róka lehetséges lépései: mind a 4 átlós irány, ha a cél a pályán belül van és üres. */
    public List<Move> determineFoxPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        Position foxPosition = board.getFoxPosition();
        if (foxPosition == null) {
            return moves;
        }
        Fox fox = new Fox(foxPosition);
        for (Direction direction : Direction.values()) {
            if (canStep(foxPosition, direction)) {
                moves.add(new Move(fox, direction));
            }
        }
        return moves;
    }

    /** A kutyák lehetséges lépései: minden kutya csak lefelé (DL, DK) léphet, üres, pályán belüli mezőre. */
    public List<Move> determineHoundsPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        for (Position houndPosition : board.getHoundsPositions()) {
            Hound hound = new Hound(houndPosition);
            for (Direction direction : HOUND_DIRECTIONS) {
                if (canStep(houndPosition, direction)) {
                    moves.add(new Move(hound, direction));
                }
            }
        }
        return moves;
    }

    /** Csak a sötét mezők játszhatók: (sor + oszlop) páratlan. */
    static boolean isDark(int row, int col) {
        return (row + col) % 2 == 1;
    }

    /** A pálya határán belül van-e a pozíció. (A PDF szerint privát, a teszteléshez csomag-szintű.) */
    boolean fits(Position p) {
        int size = board.getBoardSize();
        return p.getRow() >= 0 && p.getRow() < size && p.getCol() >= 0 && p.getCol() < size;
    }

    /** Nincs-e a mezőn bábu. A határellenőrzést a fits végzi. (Ez is csomag-szintű a teszteléshez.) */
    boolean isEmpty(Position p) {
        return !p.equals(board.getFoxPosition()) && !board.getHoundsPositions().contains(p);
    }

    private static boolean isLegal(Move move, List<Move> legalMoves) {
        for (Move legal : legalMoves) {
            if (legal.getMover().getClass() == move.getMover().getClass()
                    && legal.getMover().getPosition().equals(move.getMover().getPosition())
                    && legal.getDirection() == move.getDirection()) {
                return true;
            }
        }
        return false;
    }

    private boolean canStep(Position from, Direction direction) {
        Position target = new Position(from, direction);
        return fits(target) && isEmpty(target);
    }

    private static void checkSize(int n) {
        if (n < MIN_BOARD_SIZE) {
            throw new IllegalArgumentException("A pálya legalább " + MIN_BOARD_SIZE + " széles kell legyen: " + n);
        }
    }
}