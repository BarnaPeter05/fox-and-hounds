import businesslogic.BoardService;
import config.GameConfig;
import event.GameEvent;
import event.GameEventPublisher;
import event.GameEventType;
import event.listener.AuditLoggerListener;
import event.listener.ConsoleEventPrinter;
import event.listener.ErrorCounter;

public class Main {

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
        System.out.println("Róka lépései: " + boardService.determineFoxPossibleMoves());
        System.out.println("Kutyák lépései: " + boardService.determineHoundsPossibleMoves());
        System.out.println("Hibák száma: " + errorCounter.getErrorCount());
    }
}