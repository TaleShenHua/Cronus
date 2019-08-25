package ink.ptms.cronus.event;

import ink.ptms.cronus.internal.QuestStage;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusInitQuestStageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private QuestStage quest;

    public CronusInitQuestStageEvent(QuestStage quest) {
        this.quest = quest;
    }

    public static CronusInitQuestStageEvent call(QuestStage quest) {
        CronusInitQuestStageEvent event = new CronusInitQuestStageEvent(quest);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public QuestStage getQuestStage() {
        return quest;
    }
}
