package ink.ptms.cronus.internal.bukkit;

import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-07 20:11
 */
public class DamageCause extends EnumEntry<EntityDamageEvent.DamageCause> {

    public DamageCause(String in) {
        super(in);
    }

    @Override
    public Class origin() {
        return EntityDamageEvent.DamageCause.class;
    }

    @Override
    public String toString() {
        return "DamageCause{" +
                "data=" + data +
                '}';
    }
}
