package ink.ptms.cronus.event;

import ink.ptms.cronus.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusReloadServiceEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Service service;

    public CronusReloadServiceEvent(Service service) {
        this.service = service;
    }

    public static CronusReloadServiceEvent call(Service service) {
        CronusReloadServiceEvent event = new CronusReloadServiceEvent(service);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public Service getService() {
        return service;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
