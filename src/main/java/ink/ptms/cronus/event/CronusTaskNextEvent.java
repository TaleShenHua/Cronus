package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusTaskNextEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Quest quest;
    private QuestStage questStage;
    private QuestTask questTask;
    private boolean cancelled;

    public CronusTaskNextEvent(Player who, Quest quest, QuestStage questStage, QuestTask questTask) {
        super(who);
        this.quest = quest;
        this.questStage = questStage;
        this.questTask = questTask;
    }

    public static CronusTaskNextEvent call(Player who, Quest quest, QuestStage questStage, QuestTask questTask) {
        CronusTaskNextEvent event = new CronusTaskNextEvent(who, quest, questStage, questTask);
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
