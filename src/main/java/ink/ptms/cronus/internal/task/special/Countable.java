package ink.ptms.cronus.internal.task.special;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.service.selector.EntitySelector;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-07 19:50
 */
public abstract class Countable<C extends Event> extends QuestTask<C> {

    protected int count;
    protected EntitySelector entitySelector;

    public Countable(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        count = NumberConversions.toInt(data.getOrDefault("count", 1));
        entitySelector = Cronus.getCronusService().getService(EntitySelector.class);
    }

    @Override
    public boolean isCompleted(DataQuest dataQuest) {
        return dataQuest.getDataStage().getInt(getId() + ".count") >= count;
    }

    @Override
    public void next(Player player, DataQuest dataQuest, C event) {
        dataQuest.getDataStage().set(getId() + ".count", dataQuest.getDataStage().getInt(getId() + ".count") + 1);
    }

    @Override
    public void complete(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".count", count);
    }

    @Override
    public void reset(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".count", 0);
    }

    public int getCount(DataQuest dataQuest) {
        return dataQuest.getDataStage().getInt(getId() + ".count");
    }

    public int getCountMax() {
        return count;
    }
}
