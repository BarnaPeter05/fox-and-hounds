import config.GameConfig;
import event.GameEvent;
import event.GameEventPublisher;
import event.GameEventType;
import event.listener.AuditLoggerListener;
import event.listener.ConsoleEventPrinter;
import event.listener.ErrorCounter;
import model.Board;
import model.Position;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        GameConfig config = GameConfig.getInstance();

        GameEventPublisher publisher = new GameEventPublisher();
        ErrorCounter errorCounter = new ErrorCounter();
        publisher.subscribe(new ConsoleEventPrinter());
        publisher.subscribe(new AuditLoggerListener());
        publisher.subscribe(errorCounter);

        Board board = Board.builder()
                .withBoardSize(config.getBoardSize())
                .withFoxPosition(new Position(config.getBoardSize() - 1, 0))
                .withHoundsPositions(List.of(new Position(0, 1), new Position(0, 3)))
                .withFoxOnMove(true)
                .build();

        publisher.publish(new GameEvent(GameEventType.GAME_STARTED,
                "Fox and Hounds - pályaméret: " + board.getBoardSize() + ", róka: " + board.getFoxPosition()));
        System.out.println("Hibák száma: " + errorCounter.getErrorCount());
    }
}
