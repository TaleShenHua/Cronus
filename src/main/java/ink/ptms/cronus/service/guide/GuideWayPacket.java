package ink.ptms.cronus.service.guide;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.asm.AsmHandler;
import io.izzel.taboolib.module.lite.SimpleReflection;
import io.izzel.taboolib.module.packet.TPacketListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-29 19:35
 */
public class GuideWayPacket extends TPacketListener {

    private GuideWay service = Cronus.getCronusService().getService(GuideWay.class);

    @Override
    public boolean onSend(Player player, Object packet) {
        if (packet.getClass().getSimpleName().equals("PacketPlayOutSpawnEntity")) {
            SimpleReflection.checkAndSave(packet.getClass());
            try {
                Entity entity = AsmHandler.getImpl().getEntityByEntityId((int) SimpleReflection.getFieldValue(packet.getClass(), packet, "a"));
                if (entity != null && entity.hasMetadata("cronus_guide_owner")) {
                    return player.getName().equals(entity.getMetadata("cronus_guide_owner").get(0).asString());
                }
            } catch (Throwable ignore) {
            }
        }
        return true;
    }
}
