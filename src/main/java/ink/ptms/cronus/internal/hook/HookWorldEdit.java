package ink.ptms.cronus.internal.hook;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.WorldData;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author 坏黑
 * @Since 2019-06-19 15:17
 */
public class HookWorldEdit {

    @TInject
    private static TLogger logger;
    @TInject("WorldEdit")
    private static WorldEditPlugin plugin;

    public static void pasteSchematic(Player player, String name, Location location, boolean ignoreAir) {
        try {
            BukkitPlayer bukkitPlayer = plugin.wrapPlayer(player);
            LocalConfiguration config = WorldEdit.getInstance().getConfiguration();
            File dir = WorldEdit.getInstance().getWorkingDirectoryFile(config.saveDir);
            File f = WorldEdit.getInstance().getSafeOpenFile(bukkitPlayer, dir, name, "schematic", "schematic");
            if (!f.exists()) {
                logger.error("Schematic " + name + " does not exist!");
            } else {
                ClipboardFormat format = ClipboardFormat.findByAlias("schematic");
                if (format == null) {
                    logger.error("Unknown schematic format: schematic");
                } else {
                    try (Closer closer = Closer.create()) {
                        WorldData worldData = bukkitPlayer.getWorld().getWorldData();
                        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(player.getWorld()), -1);
                        FileInputStream fis = closer.register(new FileInputStream(f));
                        BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
                        ClipboardReader reader = format.getReader(bis);
                        Clipboard clipboard = reader.read(worldData);
                        Operation operation = new ClipboardHolder(clipboard, worldData)
                                .createPaste(editSession, worldData)
                                .to(new Vector(location.getX(), location.getY(), location.getZ()))
                                .ignoreAirBlocks(ignoreAir)
                                .build();
                        Operations.complete(operation);
                    } catch (IOException t) {
                        logger.error("Schematic could not read or it does not exist: " + t.getMessage());
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
