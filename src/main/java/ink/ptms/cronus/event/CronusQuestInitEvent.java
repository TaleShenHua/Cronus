package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.Quest;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusQuestInitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Quest quest;

    public CronusQuestInitEvent(Quest quest) {
        this.quest = quest;
    }

    public static CronusQuestInitEvent call(Quest quest) {
        CronusQuestInitEvent event = new CronusQuestInitEvent(quest);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Quest getQuest() {
        return quest;
    }
}
