package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_furnace")
public class TaskItemFurnace extends Countable<FurnaceSmeltEvent> {

    private ItemStack source;
    private ItemStack result;

    public TaskItemFurnace(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        source = data.containsKey("source") ? BukkitParser.toItemStack(data.get("source")) : null;
        result = data.containsKey("result") ? BukkitParser.toItemStack(data.get("result")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, FurnaceSmeltEvent e) {
        return (source == null || source.isItem(e.getSource())) && (result == null || result.isItem(e.getResult()));
    }

    @Override
    public String toString() {
        return "TaskItemFurnace{" +
                "source=" + source +
                ", result=" + result +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
