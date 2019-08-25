package ink.ptms.cronus.internal.task.special;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.util.Sxpression;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.util.lite.Numbers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-07 19:50
 */
public abstract class Uncountable<C extends Event> extends QuestTask<C> {

    protected Sxpression total;

    public Uncountable(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        total = data.containsKey("total") ? new Sxpression(data.get("total")) : null;
    }

    @Override
    public boolean isCompleted(DataQuest dataQuest) {
        return dataQuest.getDataStage().getInt(getId() + ".complete") > 0 || (total == null || total.isSelect(dataQuest.getDataStage().getInt(getId() + ".total")));
    }

    @Override
    public void next(Player player, DataQuest dataQuest, C event) {
        double v = Numbers.format(dataQuest.getDataStage().getDouble(getId() + ".total") + getCount(player, dataQuest, event));
        dataQuest.getDataStage().set(getId() + ".total", Utils.isInt(v) ? NumberConversions.toInt(v) : v);
    }

    @Override
    public void complete(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".complete", 1);
    }

    @Override
    public void reset(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".complete", 0);
        dataQuest.getDataStage().set(getId() + ".count", 0);
    }

    public double getTotal(DataQuest dataQuest) {
        return dataQuest.getDataStage().getDouble(getId() + ".total");
    }

    abstract public double getCount(Player player, DataQuest dataQuest, C event);

    public Sxpression getTotal() {
        return total;
    }
}
