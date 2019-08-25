package ink.ptms.cronus.internal.task.listener;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.task.player.*;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerAttack;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDamaged;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDeath;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerKill;
import ink.ptms.cronus.internal.task.player.total.TaskPlayerExp;
import io.izzel.taboolib.common.event.PlayerJumpEvent;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.lite.Servers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

/**
 * @Author 坏黑
 * @Since 2019-05-31 14:50
 */
@TListener
public class ListenerPlayer implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerTeleportEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerTeleport.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerFishEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerFish.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerEditBookEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerBook.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerBucketEmptyEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerBucketEmpty.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerBucketFillEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerBucketFill.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(AsyncPlayerChatEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerChat.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerCommandPreprocessEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerCommand.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerShearEntityEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerShear.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerJumpEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerJump.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerExpChangeEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerExp.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PotionSplashEvent e) {
        if (e.getPotion().getShooter() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getPotion().getShooter(), e, TaskPlayerThrowPotion.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(ExpBottleEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity().getShooter(), e, TaskPlayerThrowXpBottle.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityTameEvent e) {
        if (e.getOwner() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getOwner(), e, TaskPlayerTame.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerChat.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity().getShooter(), e, TaskPlayerShoot.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerDamaged.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {
            CronusAPI.stageHandle(e.getEntity().getKiller(), e, TaskPlayerKill.class);
        }
        // 宠物击杀
        if (e.getEntity().getKiller() instanceof Tameable && ((Tameable) e.getEntity().getKiller()).getOwner() instanceof Player) {
            CronusAPI.stageHandle((Player) ((Tameable) e.getEntity().getKiller()).getOwner(), e, TaskPlayerKill.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle(e.getEntity(), e, TaskPlayerDeath.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityPortalEnterEvent  e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerPortalEnter.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityPortalExitEvent  e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerPortalExit.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(VehicleEnterEvent e) {
        if (e.getEntered() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntered(), e, TaskPlayerVehicleEnter.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(VehicleExitEvent e) {
        if (e.getExited() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getExited(), e, TaskPlayerVehicleExit.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.MERCHANT && e.getRawSlot() == 2 && !Items.isNull(e.getCurrentItem())) {
            CronusAPI.stageHandle((Player) e.getWhoClicked(), e, TaskPlayerTrade.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType().name().endsWith("_PLATE")) {
            CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerPressurePlate.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityBlockFormEvent e) {
        if (e.getEntity() instanceof Player && e.getBlock().getType() == Material.ICE) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerWalkFrost.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(HorseJumpEvent e) {
        for (Entity entity: e.getEntity().getPassengers()) {
            if (entity instanceof Player) {
                CronusAPI.stageHandle((Player) entity, e, TaskPlayerJumpHorse.class);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityDamageByEntityEvent e) {
        LivingEntity attacker = Servers.getLivingAttackerInDamageEvent(e);
        if (attacker instanceof Player) {
            CronusAPI.stageHandle((Player) attacker, e, TaskPlayerAttack.class);
        }
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerDamaged.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            // 异步计算
            Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), () -> {
                CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerWalk.class, TaskPlayerSwim.class, TaskPlayerRide.class, TaskPlayerElytra.class, TaskPlayerLeash.class);
            });
        }
    }
}
