package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExpBottleEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_throw_xp_bottle")
public class TaskPlayerThrowXpBottle extends Countable<ExpBottleEvent> {

    private String bottle;

    public TaskPlayerThrowXpBottle(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        bottle = data.containsKey("bottle") ? String.valueOf(data.get("bottle")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, ExpBottleEvent e) {
        return (bottle == null || entitySelector.isSelect(e.getEntity(), bottle));
    }

    @Override
    public String toString() {
        return "TaskPlayerThrowXpBottle{" +
                "bottle=" + bottle +
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
