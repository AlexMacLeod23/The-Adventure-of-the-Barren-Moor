package events;

import game.event.Event;
import game.event.Treasure;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventTest {

    @Test
    public void playEvent_shouldSetEventAsCompleted(){
        Event testEvent = new Treasure(0);
        assertFalse(testEvent.isCompleted());

        testEvent.play();
        assertTrue(testEvent.isCompleted());
    }
}
