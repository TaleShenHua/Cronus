package ink.ptms.cronus.internal.bukkit.parser;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.bukkit.*;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.serialize.TSerializerElementGeneral;
import org.bukkit.Bukkit;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;

/**
 * @Author 坏黑
 * @Since 2019-05-23 22:43
 */
public class BukkitParser {

    @TInject
    static TLogger logger;

    public static BlockFace toBlockFace(Object str) {
        return new BlockFace(String.valueOf(str));
    }

    public static FishState toFishState(Object str) {
        return new FishState(String.valueOf(str));
    }

    public static DamageCause toDamageCause(Object str) {
        return new DamageCause(String.valueOf(str));
    }

    public static Enchantment toEnchantment(Object str) {
        return new Enchantment(String.valueOf(str));
    }

    public static RegainReason toRegainReason(Object str) {
        return new RegainReason(String.valueOf(str));
    }

    public static PotionEffect toPotionEffect(Object str) {
        return new PotionEffect(String.valueOf(str));
    }

    public static TeleportCause toTeleportCause(Object str) {
        return new TeleportCause(String.valueOf(str));
    }

    public static Block toBlock(Object str) {
        return new Block(String.valueOf(str));
    }

    public static Entity toEntity(Object str) {
        return new Entity(String.valueOf(str));
    }

    public static Location toLocation(org.bukkit.Location in) {
        return new Location(Location.Mode.POINT, null, new org.bukkit.Location[] {in});
    }

    public static Location toLocation(Object in) {
        String str = String.valueOf(in);
        // 区域
        // world:0,80,0~0,90,0
        if (str.contains(":") && str.contains("~")) {
            String[] area = str.split("~");
            try {
                return new Location(Location.Mode.AREA, new org.bukkit.Location[] {toBukkitLocation(area[0].replace(":", ",")), toBukkitLocation(area[0].split(":")[0] + "," + area[1])}, null);
            } catch (Throwable ignored) {
                return new Location(Location.Mode.AREA, null, null);
            }
        }
        // 范围
        // world:0,80,0 r:10
        else if (str.contains("r:")) {
            String[] range = str.split("r:");
            return new Location(Location.Mode.RANGE, new org.bukkit.Location[] {toBukkitLocation(range[0].replace(":", ","))}, NumberConversions.toInt(range[1]));
        }
        // 单项
        // world,0,80,0;world,0,90,0
        else {
            return new Location(Location.Mode.POINT, null, Arrays.stream(str.split(";")).map(BukkitParser::toBukkitLocation).toArray(org.bukkit.Location[]::new));
        }
    }

    public static String fromItemStack(org.bukkit.inventory.ItemStack item) {
        return "bukkit:" + TSerializerElementGeneral.ITEM_STACK.getSerializer().write(item);
    }

    public static ItemStack toItemStack(Object in) {
        // bukkit item
        if (String.valueOf(in).startsWith("bukkit:")) {
            return new ItemStack((org.bukkit.inventory.ItemStack) TSerializerElementGeneral.ITEM_STACK.getSerializer().read(String.valueOf(in).substring("bukkit:".length())));
        }
        // cronus saved item
        if (Cronus.getCronusService().getItemStorage().getItem(String.valueOf(in)) != null) {
            return new ItemStack(Cronus.getCronusService().getItemStorage().getItem(String.valueOf(in)));
        }
        // cronus item
        String type = null;
        String name = null;
        String lore = null;
        int damage = -1;
        int amount = 1;
        for (String v : String.valueOf(in).split("[,;]")) {
            if (v.toLowerCase().startsWith("type=")) {
                type = v.substring("type=".length());
            } else if (v.toLowerCase().startsWith("t=")) {
                type = v.substring("t=".length());
            } else if (v.toLowerCase().startsWith("name=")) {
                name = v.substring("name=".length());
            } else if (v.toLowerCase().startsWith("n=")) {
                name = v.substring("n=".length());
            } else if (v.toLowerCase().startsWith("lore=")) {
                lore = v.substring("lore=".length());
            } else if (v.toLowerCase().startsWith("l=")) {
                lore = v.substring("l=".length());
            } else if (v.toLowerCase().startsWith("damage=")) {
                damage = NumberConversions.toInt(v.substring("damage=".length()));
            } else if (v.toLowerCase().startsWith("d=")) {
                damage = NumberConversions.toInt(v.substring("d=".length()));
            } else if (v.toLowerCase().startsWith("amount=")) {
                amount = NumberConversions.toInt(v.substring("amount=".length()));
            } else if (v.toLowerCase().startsWith("a=")) {
                amount = NumberConversions.toInt(v.substring("a=".length()));
            } else {
                type = v;
            }
        }
        return new ItemStack(type, name, lore, damage, amount);
    }

    public static org.bukkit.Location toBukkitLocation(Object in) {
        String[] v = String.valueOf(in).trim().split(",");
        return new org.bukkit.Location(
                v.length > 0 ? Bukkit.getWorld(v[0]) : Bukkit.getWorlds().iterator().next(),
                v.length > 1 ? NumberConversions.toDouble(v[1]) : 0,
                v.length > 2 ? NumberConversions.toDouble(v[2]) : 0,
                v.length > 3 ? NumberConversions.toDouble(v[3]) : 0,
                v.length > 4 ? NumberConversions.toFloat(v[4]) : 0,
                v.length > 5 ? NumberConversions.toFloat(v[5]) : 0);
    }
}
