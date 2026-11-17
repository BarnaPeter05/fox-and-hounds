package event;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameEventPublisherTest {

    @Mock
    private GameEventListener listener;

    @Test
    void subscribedListenerReceivesEvent() {
        GameEventPublisher publisher = new GameEventPublisher();
        GameEvent event = new GameEvent(GameEventType.MOVE_DONE, "A róka lépett.");
        publisher.subscribe(listener);

        publisher.publish(event);

        verify(listener).onEvent(event);
    }

    @Test
    void unsubscribedListenerDoesNotReceiveEvent() {
        GameEventPublisher publisher = new GameEventPublisher();
        GameEvent event = new GameEvent(GameEventType.MOVE_DONE, "A róka lépett.");
        publisher.subscribe(listener);
        publisher.unSubscribe(listener);

        publisher.publish(event);

        verify(listener, never()).onEvent(event);
    }
}
