package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusStageFailureEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Quest quest;
    private QuestStage questStage;

    public CronusStageFailureEvent(Player who, Quest quest, QuestStage questStage) {
        super(who);
        this.quest = quest;
        this.questStage = questStage;
    }

    public static CronusStageFailureEvent call(Player who, Quest quest, QuestStage stage) {
        CronusStageFailureEvent event = new CronusStageFailureEvent(who, quest, stage);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DataQuest getDataQuest() {
        return CronusAPI.getData(player).getQuest(quest.getId());
    }

    public Quest getQuest() {
        return quest;
    }

    public QuestStage getQuestStage() {
        return questStage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
