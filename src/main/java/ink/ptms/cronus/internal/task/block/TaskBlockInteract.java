package ink.ptms.cronus.internal.task.block;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.BlockFace;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "block_interact")
public class TaskBlockInteract extends Countable<PlayerInteractEvent> {

    private Block block;
    private Location location;
    private BlockFace blockFace;
    private Action action;

    public TaskBlockInteract(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        block = data.containsKey("block") ? BukkitParser.toBlock(data.get("block")) : null;
        location = data.containsKey("location") ? BukkitParser.toLocation(data.get("location")) : null;
        blockFace = data.containsKey("block-face") ? BukkitParser.toBlockFace(data.get("block-face")) : null;
        action = data.containsKey("action") ? Action.parse(data.get("action")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerInteractEvent e) {
        return (action == null || action.isSelect(e.getAction())) && (block == null || block.isSelect(e.getClickedBlock())) && (location == null || location.inSelect(e.getClickedBlock().getLocation())) && (blockFace == null || blockFace.isSelect(e.getBlockFace()));
    }

    @Override
    public String toString() {
        return "TaskBlockInteract{" +
                "block=" + block +
                ", location=" + location +
                ", blockFace=" + blockFace +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }

    enum Action {

        LEFT, RIGHT;

        public boolean isSelect(org.bukkit.event.block.Action a) {
            return this == LEFT ? a == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK : a == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
        }

        public static Action parse(Object in) {
            try {
                return valueOf(String.valueOf(in).toUpperCase());
            } catch (Throwable ignored) {
            }
            return null;
        }
    }
}
