package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.QuestTask;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusInitQuestTaskEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private QuestTask quest;

    public CronusInitQuestTaskEvent(QuestTask quest) {
        this.quest = quest;
    }

    public static CronusInitQuestTaskEvent call(QuestTask quest) {
        CronusInitQuestTaskEvent event = new CronusInitQuestTaskEvent(quest);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public QuestTask getQuestStage() {
        return quest;
    }
}
