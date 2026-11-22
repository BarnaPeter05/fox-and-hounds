import businesslogic.BoardService;
import config.GameConfig;
import event.GameEvent;
import event.GameEventPublisher;
import event.GameEventType;
import event.listener.AuditLoggerListener;
import event.listener.ConsoleEventPrinter;
import event.listener.ErrorCounter;
import java.util.List;
import model.Move;

public class Main {

    private static final int MAX_PLIES = 500;

    public static void main(String[] args) {
        GameConfig config = GameConfig.getInstance();

        GameEventPublisher publisher = new GameEventPublisher();
        ErrorCounter errorCounter = new ErrorCounter();
        publisher.subscribe(new ConsoleEventPrinter());
        publisher.subscribe(new AuditLoggerListener());
        publisher.subscribe(errorCounter);

        BoardService boardService = new BoardService(BoardService.getStarterBoard(config.getBoardSize()));

        publisher.publish(new GameEvent(GameEventType.GAME_STARTED,
                "Fox and Hounds - pályaméret: " + boardService.getBoard().getBoardSize()));
        System.out.println(boardService.getBoard());

        playRandomGame(boardService, publisher);

        System.out.println("Hibák száma: " + errorCounter.getErrorCount());
    }

    /** Ideiglenes: két véletlen játékos lejátszik egy teljes játékot. A 10. naptól a GameEngine veszi át. */
    private static void playRandomGame(BoardService service, GameEventPublisher publisher) {
        int plies = 0;
        while (!service.winFox() && !service.winHounds() && plies < MAX_PLIES) {
            boolean foxTurn = service.getBoard().isFoxOnMove();
            List<Move> moves = foxTurn
                    ? service.determineFoxPossibleMoves()
                    : service.determineHoundsPossibleMoves();
            if (moves.isEmpty()) {
                break;
            }
            if (foxTurn) {
                service.doARandomFoxMove();
            } else {
                service.doARandomHoundMove();
            }
            plies++;
            publisher.publish(new GameEvent(GameEventType.MOVE_DONE,
                    plies + ". lépés: " + (foxTurn ? "róka" : "kutya")));
            System.out.println(service.getBoard());
        }

        String result;
        if (service.winFox()) {
            result = "A róka nyert.";
        } else if (service.winHounds()) {
            result = "A kutyák nyertek.";
        } else {
            result = "A játék véget ért: nincs lehetséges lépés.";
        }
        publisher.publish(new GameEvent(GameEventType.GAME_ENDED, result));
    }
}