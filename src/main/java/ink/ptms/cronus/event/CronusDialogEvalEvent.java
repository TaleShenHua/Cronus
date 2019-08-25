package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogPack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusDialogEvalEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private DialogPack pack;
    private boolean cancelled;

    public CronusDialogEvalEvent(Player who, DialogPack pack) {
        super(who);
        this.pack = pack;
    }

    public static CronusDialogEvalEvent call(DialogPack pack, Player player) {
        CronusDialogEvalEvent event = new CronusDialogEvalEvent(player, pack);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DialogPack getPack() {
        return pack;
    }

    public void setPack(DialogPack pack) {
        this.pack = pack;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
