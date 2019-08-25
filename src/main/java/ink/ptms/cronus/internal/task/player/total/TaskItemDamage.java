package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Uncountable;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_damage")
public class TaskItemDamage extends Uncountable<PlayerItemDamageEvent> {

    private ItemStack item;
    private Sxpression damage;

    public TaskItemDamage(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        damage = data.containsKey("damage") ? new Sxpression(data.get("damage")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerItemDamageEvent e) {
        return (item == null || item.isItem(e.getItem())) && (damage == null || damage.isSelect(getCount(player, dataQuest, e)));
    }

    @Override
    public double getCount(Player player, DataQuest dataQuest, PlayerItemDamageEvent e) {
        return e.getDamage();
    }

    @Override
    public String toString() {
        return "TaskItemDamage{" +
                "item=" + item +
                ", damage=" + damage +
                ", total=" + total +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", status='" + status + '\'' +
                ", action=" + action +
                '}';
    }
}
