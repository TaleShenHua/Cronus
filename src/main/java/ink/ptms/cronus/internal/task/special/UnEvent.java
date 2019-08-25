package ink.ptms.cronus.internal.task.special;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.event.EventPeriod;
import ink.ptms.cronus.internal.QuestTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-20 13:47
 */
public abstract class UnEvent extends QuestTask<EventPeriod> {

    public UnEvent(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean isCompleted(DataQuest dataQuest) {
        return dataQuest.getDataStage().getInt(getId() + ".complete") > 0;
    }

    @Override
    public void next(Player player, DataQuest dataQuest, EventPeriod event) {
        dataQuest.getDataStage().set(getId() + ".complete", 1);
    }

    @Override
    public void complete(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".complete", 1);
    }

    @Override
    public void reset(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".complete", 0);
    }
}
