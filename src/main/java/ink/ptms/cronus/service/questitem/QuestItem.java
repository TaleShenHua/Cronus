package ink.ptms.cronus.service.questitem;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.event.CronusReloadServiceEvent;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-07-23 14:10
 */
@Auto
public class QuestItem implements Service, Listener {

    private List<QuestItemData> items = Lists.newArrayList();

    @Override
    public void init() {
        ConfigurationSection questItem = Cronus.getConf().getConfigurationSection("QuestItem");
        if (questItem != null) {
            questItem.getKeys(false).forEach(id -> items.add(new QuestItemData(questItem.getConfigurationSection(id))));
        }
        CronusReloadServiceEvent.call(this);
    }

    @Override
    public void cancel() {

    }

    @TSchedule(period = 100)
    static void format() {
        QuestItem questItem = Cronus.getCronusService().getService(QuestItem.class);
        Bukkit.getOnlinePlayers().forEach(questItem::format);
    }

    @EventHandler
    public void e(PlayerJoinEvent e) {
        format(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void e(ItemSpawnEvent e) {
        ItemStack dropItem = e.getEntity().getItemStack();
        items.stream().filter(item -> item.getItem().isSimilar(dropItem)).map(item -> true).forEach(e::setCancelled);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void e(PlayerDropItemEvent e) {
        ItemStack dropItem = e.getItemDrop().getItemStack();
        items.stream().filter(item -> item.getItem().isSimilar(dropItem)).map(item -> true).forEach(e::setCancelled);
    }

    @EventHandler
    public void e(PlayerInteractEvent e) {
        items.stream().filter(item -> item.getSlot() == e.getPlayer().getInventory().getHeldItemSlot() && item.getItem().isSimilar(e.getPlayer().getItemInHand())).findFirst().ifPresent(item -> {
            e.setCancelled(true);
            // 如果启用则执行动作
            if (item.isEnable()) {
                item.getEffect().eval(e.getPlayer());
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void e(InventoryClickEvent e) {
        for (QuestItemData item : items) {
            // 键盘移动
            if (e.getClick() == ClickType.NUMBER_KEY && item.getItem().isSimilar(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()))) {
                e.setCancelled(true);
            }
            // 鼠标点击
            else if (item.getItem().isSimilar(e.getCurrentItem())) {
                e.setCancelled(true);
                // 如果启用则执行动作
                if (item.isEnable()) {
                    item.getEffect().eval((Player) e.getWhoClicked());
                }
            }
        }
    }

    public void format(Player player) {
        for (QuestItemData item : items) {
            ItemStack inventoryItem = player.getInventory().getItem(item.getSlot());
            if (item.isEnable() && Items.isNull(inventoryItem)) {
                player.getInventory().setItem(item.getSlot(), item.getItem());
            } else if (!item.isEnable() && item.getItem().isSimilar(inventoryItem)) {
                player.getInventory().setItem(item.getSlot(), null);
            }
        }
    }

    public List<QuestItemData> getItems() {
        return items;
    }
}
