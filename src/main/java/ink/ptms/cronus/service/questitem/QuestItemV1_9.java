package ink.ptms.cronus.service.questitem;

import ink.ptms.cronus.Cronus;
import io.izzel.taboolib.module.inject.TListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * @Author 坏黑
 * @Since 2019-07-23 14:51
 */
@TListener(version = ">10900")
public class QuestItemV1_9 implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void e(PlayerSwapHandItemsEvent e) {
        QuestItem questItem = Cronus.getCronusService().getService(QuestItem.class);
        questItem.getItems().stream().filter(item -> item.getItem().isSimilar(e.getOffHandItem()) || item.getItem().isSimilar(e.getMainHandItem())).map(item -> true).forEach(e::setCancelled);
    }

}
