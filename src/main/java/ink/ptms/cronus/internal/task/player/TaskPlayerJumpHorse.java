package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.util.Sxpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.HorseJumpEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_jump_horse")
public class TaskPlayerJumpHorse extends Countable<HorseJumpEvent> {

    private String horse;
    private Sxpression power;

    public TaskPlayerJumpHorse(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        horse = data.containsKey("horse") ? String.valueOf(data.get("horse")) : null;
        power = data.containsKey("power") ? new Sxpression(data.get("power")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, HorseJumpEvent e) {
        return (horse == null || entitySelector.isSelect(e.getEntity(), horse)) && (power == null || power.isSelect(e.getPower()));
    }

    @Override
    public String toString() {
        return "TaskPlayerJumpHorse{" +
                "horse=" + horse +
                ", power=" + power +
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
