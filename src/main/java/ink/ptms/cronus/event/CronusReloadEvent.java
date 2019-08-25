package ink.ptms.cronus.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusReloadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static CronusReloadEvent call() {
        CronusReloadEvent event = new CronusReloadEvent();
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
