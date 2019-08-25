package ink.ptms.cronus.internal.task.block;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "block_break")
public class TaskBlockBreak extends Countable<BlockBreakEvent> {

    private Block block;
    private Location location;

    public TaskBlockBreak(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        block = data.containsKey("block") ? BukkitParser.toBlock(data.get("block")) : null;
        location = data.containsKey("location") ? BukkitParser.toLocation(data.get("location")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, BlockBreakEvent e) {
        return (block == null || block.isSelect(e.getBlock())) && (location == null || location.inSelect(e.getBlock().getLocation()));
    }

    @Override
    public String toString() {
        return "TaskBlockBreak{" +
                "block=" + block +
                ", location=" + location +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
