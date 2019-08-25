package ink.ptms.cronus.service.dialog;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-10 17:50
 */
public abstract class DialogDisplay {

    abstract public void display(Player player, Entity entity, DialogPack dialogPack);

}
