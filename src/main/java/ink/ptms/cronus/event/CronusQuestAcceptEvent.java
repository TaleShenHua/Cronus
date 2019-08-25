package ink.ptms.cronus.event;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CronusQuestAcceptEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private Quest quest;

    public CronusQuestAcceptEvent(Player who, Quest quest) {
        super(who);
        this.quest = quest;
    }

    public static CronusQuestAcceptEvent call(Player who, Quest quest) {
        CronusQuestAcceptEvent event = new CronusQuestAcceptEvent(who, quest);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DataQuest getDataQuest() {
        return CronusAPI.getData(player).getQuest(quest.getId());
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
