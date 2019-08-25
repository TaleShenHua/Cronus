package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Enchantment;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_enchant")
public class TaskItemEnchant extends Countable<EnchantItemEvent> {

    private ItemStack item;
    private Enchantment enchant;
    private Location enchantBlock;

    public TaskItemEnchant(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        enchant = data.containsKey("enchant") ? BukkitParser.toEnchantment(data.get("enchant")) : null;
        enchantBlock = data.containsKey("enchant-block") ? BukkitParser.toLocation(data.get("enchant-block")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EnchantItemEvent e) {
        return (enchantBlock == null || enchantBlock.inSelect(e.getEnchantBlock().getLocation())) && (item == null || item.isItem(e.getItem())) && (enchant == null || e.getEnchantsToAdd().entrySet().stream().anyMatch(a -> enchant.isSelect(a.getKey(), a.getValue())));
    }

    @Override
    public String toString() {
        return "TaskItemEnchant{" +
                "item=" + item +
                ", enchant=" + enchant +
                ", enchantBlock=" + enchantBlock +
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
