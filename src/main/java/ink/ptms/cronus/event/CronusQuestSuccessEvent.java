package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusQuestSuccessEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Quest quest;

    public CronusQuestSuccessEvent(Player who, Quest quest) {
        super(who);
        this.quest = quest;
    }

    public static CronusQuestSuccessEvent call(Player who, Quest quest) {
        CronusQuestSuccessEvent event = new CronusQuestSuccessEvent(who, quest);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DataQuest getDataQuest() {
        return CronusAPI.getData(player).getQuest(quest.getId());
    }

    public Quest getQuest() {
        return quest;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
