package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogGroup;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusInitDialogEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private DialogGroup dialogGroup;

    public CronusInitDialogEvent(DialogGroup dialogGroup) {
        this.dialogGroup = dialogGroup;
    }

    public static CronusInitDialogEvent call(DialogGroup dialogGroup) {
        CronusInitDialogEvent event = new CronusInitDialogEvent(dialogGroup);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public DialogGroup getDialogGroup() {
        return dialogGroup;
    }
}
