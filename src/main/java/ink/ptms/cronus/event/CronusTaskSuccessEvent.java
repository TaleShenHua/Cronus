package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusTaskSuccessEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Quest quest;
    private QuestStage questStage;
    private QuestTask questTask;

    public CronusTaskSuccessEvent(Player who, Quest quest, QuestStage questStage, QuestTask questTask) {
        super(who);
        this.quest = quest;
        this.questStage = questStage;
        this.questTask = questTask;
    }

    public static CronusTaskSuccessEvent call(Player who, Quest quest, QuestStage questStage, QuestTask questTask) {
        CronusTaskSuccessEvent event = new CronusTaskSuccessEvent(who, quest, questStage, questTask);
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

    public QuestTask getQuestTask() {
        return questTask;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
