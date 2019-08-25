package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Arrays;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_craft")
public class TaskItemCraft extends Countable<CraftItemEvent> {

    private ItemStack result;
    private ItemStack matrix;

    public TaskItemCraft(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        result = data.containsKey("result") ? BukkitParser.toItemStack(data.get("result")) : null;
        matrix = data.containsKey("matrix") ? BukkitParser.toItemStack(data.get("matrix")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, CraftItemEvent e) {
        return (result == null || result.isItem(e.getInventory().getResult())) && (matrix == null || Arrays.stream(e.getInventory().getMatrix()).anyMatch(matrix::isItem));
    }

    @Override
    public String toString() {
        return "TaskItemCraft{" +
                "result=" + result +
                ", matrix=" + matrix +
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
