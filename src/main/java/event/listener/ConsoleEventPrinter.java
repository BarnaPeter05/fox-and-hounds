package event.listener;

import event.GameEvent;
import event.GameEventListener;

public class ConsoleEventPrinter implements GameEventListener {

    @Override
    public void onEvent(GameEvent event) {
        System.out.println(event.message());
    }
}
