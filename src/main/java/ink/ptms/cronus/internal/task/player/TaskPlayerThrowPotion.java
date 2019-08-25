package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.PotionEffect;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_throw_potion")
public class TaskPlayerThrowPotion extends Countable<PotionSplashEvent> {

    private PotionEffect effect;
    private String affect;

    public TaskPlayerThrowPotion(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        effect = data.containsKey("effect") ? BukkitParser.toPotionEffect(data.get("effect")) : null;
        affect = data.containsKey("affect") ? String.valueOf(data.get("affect")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PotionSplashEvent e) {
        return (affect == null || e.getAffectedEntities().stream().anyMatch(a -> entitySelector.isSelect(a, affect))) && (effect == null || e.getPotion().getEffects().stream().anyMatch(p -> effect.isSelect(p.getType(), p.getAmplifier())));
    }

    @Override
    public String toString() {
        return "TaskPlayerThrowPotion{" +
                "effect=" + effect +
                ", affect=" + affect +
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
