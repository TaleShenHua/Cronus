package ink.ptms.cronus.internal.bukkit;

import org.bukkit.event.entity.EntityRegainHealthEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-07 20:11
 */
public class RegainReason extends EnumEntry<EntityRegainHealthEvent.RegainReason> {

    public RegainReason(String in) {
        super(in);
    }

    @Override
    public Class origin() {
        return EntityRegainHealthEvent.RegainReason.class;
    }

    @Override
    public String toString() {
        return "RegainReason{" +
                "data=" + data +
                '}';
    }
}
