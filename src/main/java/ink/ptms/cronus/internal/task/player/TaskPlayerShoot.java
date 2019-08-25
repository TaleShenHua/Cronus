package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_shoot")
public class TaskPlayerShoot extends Countable<EntityShootBowEvent> {

    private String projectile;
    private ItemStack bow;
    private Sxpression force;

    public TaskPlayerShoot(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        bow = data.containsKey("bow") ? BukkitParser.toItemStack(data.get("bow")) : null;
        force = data.containsKey("force") ? new Sxpression(data.get("force")) : null;
        projectile = data.containsKey("projectile") ? String.valueOf(data.get("projectile")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityShootBowEvent e) {
        return (bow == null || bow.isItem(e.getBow())) && (force == null || force.isSelect(e.getForce())) && (projectile == null || entitySelector.isSelect(e.getEntity(), projectile));
    }

    @Override
    public String toString() {
        return "TaskPlayerShoot{" +
                "bow=" + bow +
                ", projectile=" + projectile +
                ", force=" + force +
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
