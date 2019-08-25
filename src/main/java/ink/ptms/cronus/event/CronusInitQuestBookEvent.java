package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.QuestBook;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusInitQuestBookEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private QuestBook quest;

    public CronusInitQuestBookEvent(QuestBook quest) {
        this.quest = quest;
    }

    public static CronusInitQuestBookEvent call(QuestBook quest) {
        CronusInitQuestBookEvent event = new CronusInitQuestBookEvent(quest);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public QuestBook getQuestStage() {
        return quest;
    }
}
