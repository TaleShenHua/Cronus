package ink.ptms.cronus.service.questitem;

import ink.ptms.cronus.internal.program.QuestEffect;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

/**
 * @Author 坏黑
 * @Since 2019-07-23 14:55
 */
public class QuestItemData {

    private ConfigurationSection conf;
    private boolean enable;
    private QuestEffect effect;
    private ItemStack item;
    private int slot;

    public QuestItemData(ConfigurationSection conf) {
        this.conf = conf;
        this.enable = conf.getBoolean("enable");
        this.item = Items.loadItem(conf);
        this.slot = NumberConversions.toInt(conf.getName());
        this.effect = new QuestEffect(conf.getStringList("effect"));
    }

    public ConfigurationSection getConf() {
        return conf;
    }

    public boolean isEnable() {
        return enable;
    }

    public QuestEffect getEffect() {
        return effect;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
