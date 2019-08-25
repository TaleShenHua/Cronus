package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusVisibleToggleEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public CronusVisibleToggleEvent(Player who) {
        super(who);
    }

    public static CronusVisibleToggleEvent call(Player who) {
        CronusVisibleToggleEvent event = new CronusVisibleToggleEvent(who);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DataPlayer getDataPlayer() {
        return CronusAPI.getData(player);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
