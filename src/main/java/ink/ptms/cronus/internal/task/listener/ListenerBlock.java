package ink.ptms.cronus.internal.task.listener;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.task.block.TaskBlockBreak;
import ink.ptms.cronus.internal.task.block.TaskBlockInteract;
import ink.ptms.cronus.internal.task.block.TaskBlockPlace;
import io.izzel.taboolib.module.inject.TListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:24
 */
@TListener
public class ListenerBlock implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(BlockBreakEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskBlockBreak.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(BlockPlaceEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskBlockPlace.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            CronusAPI.stageHandle(e.getPlayer(), e, TaskBlockInteract.class);
        }
    }
}
