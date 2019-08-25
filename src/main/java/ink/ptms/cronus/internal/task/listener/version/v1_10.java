package ink.ptms.cronus.internal.task.listener.version;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.task.player.TaskPlayerBreed;
import io.izzel.taboolib.module.inject.TListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-24 15:32
 */
@TListener(version = ">10900")
public class v1_10 implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityBreedEvent e) {
        if (e.getBreeder() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getBreeder(), e, TaskPlayerBreed.class);
        }
    }

}
