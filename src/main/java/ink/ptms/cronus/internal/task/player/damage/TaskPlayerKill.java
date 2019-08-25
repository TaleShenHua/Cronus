package ink.ptms.cronus.internal.task.player.damage;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.DamageCause;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.service.selector.EntitySelector;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_kill")
public class TaskPlayerKill extends Countable<EntityDeathEvent> {

    private String victim;
    private ItemStack weapon;
    private DamageCause cause;

    public TaskPlayerKill(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        victim = data.containsKey("victim") ? String.valueOf(data.get("victim")) : null;
        weapon = data.containsKey("weapon") ? BukkitParser.toItemStack(data.get("weapon")) : null;
        cause = data.containsKey("cause") ? BukkitParser.toDamageCause(data.get("cause")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityDeathEvent e) {
        return (weapon == null || weapon.isItem(player.getItemInHand())) && (victim == null || Cronus.getCronusService().getService(EntitySelector.class).isSelect(e.getEntity(), victim)) && (cause == null || cause.isSelect(e.getEntity().getLastDamageCause().getCause()));
    }

    @Override
    public String toString() {
        return "TaskPlayerKill{" +
                "victim=" + victim +
                ", weapon=" + weapon +
                ", cause=" + cause +
                ", count=" + count +
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
