package ink.ptms.cronus.internal.event;

import ink.ptms.cronus.Ignore;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Ignore
public class EventPeriod extends Event {

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}