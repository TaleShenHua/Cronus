package ink.ptms.cronus.event;

import ink.ptms.cronus.database.data.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusDataPushEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private DataPlayer dataPlayer;

    public CronusDataPushEvent(Player who, DataPlayer dataPlayer) {
        super(who);
        this.dataPlayer = dataPlayer;
    }

    public static CronusDataPushEvent call(Player who, DataPlayer dataPlayer) {
        CronusDataPushEvent event = new CronusDataPushEvent(who, dataPlayer);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DataPlayer getDataPlayer() {
        return dataPlayer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
