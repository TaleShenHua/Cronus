package ink.ptms.cronus.service.effectitem;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.event.CronusReloadServiceEvent;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.Files;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author 坏黑
 * @Since 2019-07-26 11:29
 */
@Auto
public class EffectItem implements Service, Listener {

    @TInject
    private static TLogger logger;
    private File folder;
    private List<EffectItemData> items = Lists.newArrayList();

    @Override
    public void active() {
        long time = System.currentTimeMillis();
        folder = new File(Cronus.getInst().getDataFolder(), "items");
        if (!folder.exists()) {
            Cronus.getInst().saveResource("items/def.yml", true);
        }
        items.clear();
        load(folder);
        logger.info(items.size() + " Item Loaded. (" + (System.currentTimeMillis() - time + "ms)"));
        CronusReloadServiceEvent.call(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void e(PlayerItemConsumeEvent e) {
        eval(e.getPlayer(), EffectItemEvent.USE, e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void e(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.HAND) {
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                eval(e.getPlayer(), EffectItemEvent.LEFT_CLICK, e);
            } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                eval(e.getPlayer(), EffectItemEvent.RIGHT_CLICK, e);
            }
        }
    }

    public void eval(Player player, EffectItemEvent e, Cancellable c) {
        DataPlayer dataPlayer = CronusAPI.getData(player);
        for (EffectItemData item : items) {
            // 判断物品
            if (item.getEvent() == e && item.getItem() != null && item.getItem().isItem(player.getItemInHand())) {
                // 取消事件
                Optional.ofNullable(c).ifPresent(i -> i.setCancelled(true));
                // 条件判断
                if (item.getCondition() != null && !item.getCondition().check(player.getPlayer())) {
                    // 执行动作
                    Optional.ofNullable(item.getEffectCondition()).ifPresent(i -> i.eval(player));
                    return;
                }
                // 冷却判断
                if (item.getCooldown() > 0 && dataPlayer.getItemCooldown().getOrDefault(item.getId(), 0L) + item.getCooldown() > System.currentTimeMillis()) {
                    // 执行动作
                    Optional.ofNullable(item.getEffectCooldown()).ifPresent(i -> i.eval(player));
                    return;
                }
                // 执行动作
                Optional.ofNullable(item.getEffect()).ifPresent(i -> i.eval(player));
                // 冷却时间
                dataPlayer.getItemCooldown().put(item.getId(), System.currentTimeMillis());
                dataPlayer.push();
                return;
            }
        }
    }

    public void load(File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(this::load);
        } else {
            FileConfiguration load = Files.load(file);
            for (String id : load.getKeys(false)) {
                try {
                    items.add(new EffectItemData(load.getConfigurationSection(id)));
                } catch (Throwable t) {
                    logger.error("Item " + id + " failed to load.");
                    t.printStackTrace();
                }
            }
        }
    }

    public File getFolder() {
        return folder;
    }

    public List<EffectItemData> getItems() {
        return items;
    }
}
