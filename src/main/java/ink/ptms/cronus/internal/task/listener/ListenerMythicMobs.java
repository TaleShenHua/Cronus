package ink.ptms.cronus.internal.task.listener;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerKill;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.util.lite.Servers;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

/**
 * @Author 坏黑
 * @Since 2019-08-09 18:32
 */
public class ListenerMythicMobs implements Listener {

    @TInject("MythicMobs")
    private MythicMobs plugin;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityDeathEvent e) {
        EntityDamageEvent damageEvent = e.getEntity().getLastDamageCause();
        if (!(damageEvent instanceof EntityDamageByEntityEvent)) {
            return;
        }
        LivingEntity attacker = Servers.getLivingAttackerInDamageEvent((EntityDamageByEntityEvent) damageEvent);
        if (attacker == null) {
            return;
        }
        ActiveMob activeMob = plugin.getMobManager().getActiveMob(attacker.getUniqueId()).orElse(null);
        if (activeMob == null) {
            return;
        }
        UUID ownerUUID = activeMob.getOwner().orElse(null);
        if (ownerUUID == null) {
            return;
        }
        Player player = Bukkit.getPlayer(ownerUUID);
        if (player == null) {
            return;
        }
        CronusAPI.stageHandle(player, e, TaskPlayerKill.class);
    }

}
