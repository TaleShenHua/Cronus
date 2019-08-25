package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.Quest;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusInitQuestEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Quest quest;

    public CronusInitQuestEvent(Quest quest) {
        this.quest = quest;
    }

    public static CronusInitQuestEvent call(Quest quest) {
        CronusInitQuestEvent event = new CronusInitQuestEvent(quest);
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
