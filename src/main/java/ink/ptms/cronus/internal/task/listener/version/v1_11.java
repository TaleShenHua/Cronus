package ink.ptms.cronus.internal.task.listener.version;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.task.player.TaskPlayerResurrect;
import io.izzel.taboolib.module.inject.TListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

/**
 * @Author 坏黑
 * @Since 2019-07-24 16:31
 */
@TListener(version = ">11000")
public class v1_11 implements Listener {

    @EventHandler
    public void e(EntityResurrectEvent e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerResurrect.class);
        }
    }

}
