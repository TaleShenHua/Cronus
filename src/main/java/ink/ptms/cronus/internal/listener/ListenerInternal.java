package ink.ptms.cronus.internal.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import io.izzel.taboolib.module.inject.PlayerContainer;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.inject.TSchedule;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-07-24 12:14
 */
@TListener
public class ListenerInternal implements Listener {

    @PlayerContainer
    private static Map<String, List<PlayerLeashEntityEvent>> leashed = Maps.newHashMap();

    @TSchedule(period = 100, async = true)
    private static void c() {
        leashed.values().forEach(list -> list.stream().filter(e -> !e.getEntity().isValid() || !isLeashed(e.getEntity())).forEach(list::remove));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerLeashEntityEvent e) {
        leashed.computeIfAbsent(e.getPlayer().getName(), n -> Lists.newCopyOnWriteArrayList()).add(e);
    }

    @EventHandler
    public void e(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Firework && e.getDamager().hasMetadata("no_damage")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void e(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/cronus") && !e.getPlayer().hasPermission("*")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§f§lCronus §7§lby.§r§7坏黑");
            e.getPlayer().sendMessage("§8插件版本: §7" + Cronus.getCronusVersion().toString());
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
        }
    }

    public static List<Entity> getLeashedEntity(Player player) {
        List<PlayerLeashEntityEvent> list = leashed.get(player.getName());
        if (list != null) {
            return list.stream().filter(e -> e.getEntity().isValid() && getLeashHolder(e.getEntity()).equals(player)).map(PlayerLeashEntityEvent::getEntity).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public static boolean isLeashed(Entity entity) {
        return entity instanceof LivingEntity && ((LivingEntity) entity).isLeashed();
    }

    public static Entity getLeashHolder(Entity entity) {
        return isLeashed(entity) ? ((LivingEntity) entity).getLeashHolder() : null;
    }
}
