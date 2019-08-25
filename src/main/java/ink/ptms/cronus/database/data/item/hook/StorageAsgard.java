package ink.ptms.cronus.database.data.item.hook;

import ink.ptms.cronus.database.data.item.ItemStorage;
import me.asgard.sacreditem.item.SacredItemManager;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-28 23:41
 */
public class StorageAsgard extends ItemStorage {

    @Override
    public ItemStack getItem(String name) {
        return SacredItemManager.getInstance().getItem(name);
    }

    @Override
    public void addItem(String name, ItemStack itemStack) {
    }

    @Override
    public void delItem(String name) {
    }

    @Override
    public List<String> getItems() {
        return SacredItemManager.getInstance().getItemList();
    }
}
