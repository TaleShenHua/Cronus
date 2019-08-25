package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogPack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusDialogInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private DialogPack pack;
    private Entity target;
    private boolean cancelled;

    public CronusDialogInteractEvent(Player who, DialogPack pack, Entity target) {
        super(who);
        this.pack = pack;
        this.target = target;
    }

    public static CronusDialogInteractEvent call(DialogPack pack, Entity target, Player player) {
        CronusDialogInteractEvent event = new CronusDialogInteractEvent(player, pack, target);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DialogPack getPack() {
        return pack;
    }

    public Entity getTarget() {
        return target;
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
