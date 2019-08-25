package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.listener.ListenerInternal;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_leash")
public class TaskPlayerLeash extends Countable<PlayerMoveEvent> {

    private String entity;
    private ItemStack item;

    public TaskPlayerLeash(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        entity = data.containsKey("entity") ? String.valueOf(data.get("entity")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerMoveEvent e) {
        return (item == null || item.isItem(Utils.NonNull(Utils.getUsingItem(e.getPlayer(), MaterialControl.LEAD.parseMaterial())))) && (entity == null || ListenerInternal.getLeashedEntity(player).stream().allMatch(i -> entitySelector.isSelect(i, entity)));
    }

    @Override
    public String toString() {
        return "TaskPlayerLeash{" +
                "entity=" + entity +
                ", item=" + item +
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
