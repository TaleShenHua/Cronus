package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_repair")
public class TaskItemRepair extends Countable<PrepareAnvilEvent> {

    private Block anvil;
    private Location location;
    private ItemStack result;

    public TaskItemRepair(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        result = data.containsKey("result") ? BukkitParser.toItemStack(data.get("result")) : null;
        anvil = data.containsKey("anvil") ? BukkitParser.toBlock(data.get("anvil")) : null;
        location = data.containsKey("location") ? BukkitParser.toLocation(data.get("location")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PrepareAnvilEvent e) {
        return (result == null || result.isItem(e.getResult())) && (location == null || (e.getInventory().getLocation() != null && location.inSelect(e.getInventory().getLocation()))) && (anvil == null || (e.getInventory().getLocation() != null && anvil.isSelect(e.getInventory().getLocation().getBlock())));
    }

    @Override
    public String toString() {
        return "TaskItemRepair{" +
                "anvil=" + anvil +
                ", result=" + result +
                ", location=" + location +
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
