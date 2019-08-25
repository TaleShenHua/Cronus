package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.RegainReason;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Uncountable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_regain")
public class TaskPlayerRegain extends Uncountable<EntityRegainHealthEvent> {

    private RegainReason reason;
    private Sxpression health;

    public TaskPlayerRegain(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        reason = data.containsKey("reason") ? BukkitParser.toRegainReason(data.get("reason")) : null;
        health = data.containsKey("health") ? new Sxpression(data.get("health")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityRegainHealthEvent e) {
        return (reason == null || reason.isSelect(e.getRegainReason())) && (health == null || health.isSelect(getCount(player, dataQuest, e)));
    }

    @Override
    public double getCount(Player player, DataQuest dataQuest, EntityRegainHealthEvent e) {
        return e.getAmount();
    }

    @Override
    public String toString() {
        return "TaskPlayerRegain{" +
                "reason=" + reason +
                ", health=" + health +
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
