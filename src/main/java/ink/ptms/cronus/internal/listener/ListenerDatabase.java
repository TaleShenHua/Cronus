package ink.ptms.cronus.internal.listener;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.inject.TSchedule;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @Author 坏黑
 * @Since 2019-05-30 23:11
 */
@TListener
public class ListenerDatabase implements Listener {

    @TSchedule(delay = 200, period = 200)
    public static void upload() {
        Bukkit.getOnlinePlayers().forEach(p -> CronusAPI.getData(p).push());
    }

    @EventHandler
    public void e(PlayerJoinEvent e) {
        Cronus.getCronusService().refreshData(e.getPlayer());
    }

    @EventHandler
    public void e(PlayerQuitEvent e) {
        DataPlayer dataPlayer = Cronus.getCronusService().getPlayerData().remove(e.getPlayer().getName());
        if (dataPlayer != null) {
            dataPlayer.push();
        }
    }
}
