package event.listener;

import event.GameEvent;
import event.GameEventListener;
import event.GameEventType;

public class ErrorCounter implements GameEventListener {

    private int errorCount = 0;

    @Override
    public void onEvent(GameEvent event) {
        if (event.type() == GameEventType.ERROR || event.type() == GameEventType.INVALID_MOVE) {
            errorCount++;
        }
    }

    public int getErrorCount() {
        return errorCount;
    }
}
