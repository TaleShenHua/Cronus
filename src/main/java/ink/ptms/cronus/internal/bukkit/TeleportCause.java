package ink.ptms.cronus.internal.bukkit;

import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-07 20:11
 */
public class TeleportCause extends EnumEntry<PlayerTeleportEvent.TeleportCause> {

    public TeleportCause(String in) {
        super(in);
    }

    @Override
    public Class origin() {
        return PlayerTeleportEvent.TeleportCause.class;
    }

    @Override
    public String toString() {
        return "TeleportCause{" +
                "data=" + data +
                '}';
    }
}
