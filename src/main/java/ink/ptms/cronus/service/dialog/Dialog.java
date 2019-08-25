package ink.ptms.cronus.service.dialog;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.event.CronusDialogInteractEvent;
import ink.ptms.cronus.event.CronusReloadEvent;
import ink.ptms.cronus.event.CronusReloadServiceEvent;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.service.dialog.api.DisplayBase;
import ink.ptms.cronus.service.dialog.impl.DisplayDemo;
import ink.ptms.cronus.service.dialog.impl.DisplayMenu;
import ink.ptms.cronus.service.dialog.impl.DisplayMod;
import ink.ptms.cronus.service.selector.EntitySelector;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.Files;
import io.izzel.taboolib.util.lite.cooldown.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-10 18:14
 */
@Auto
public class Dialog implements Service, Listener {

    @TInject
    private static TLogger logger;
    @TInject
    private static Cooldown cooldown = new Cooldown("cronus|dialog", 200);

    private File folder;
    private List<DialogGroup> dialogs = Lists.newArrayList();
    private List<DisplayBase> registeredBase = Lists.newArrayList();
    private boolean init;

    @Override
    public void init() {
        if (!init) {
            init = true;
            registeredBase.add(new DisplayDemo());
            registeredBase.add(new DisplayMenu());
            registeredBase.add(new DisplayMod());
        }
    }

    @Override
    public void cancel() {
    }

    @EventHandler
    public void e(CronusReloadEvent e) {
        long time = System.currentTimeMillis();
        folder = new File(Cronus.getInst().getDataFolder(), "dialogs");
        if (!folder.exists()) {
            Cronus.getInst().saveResource("dialogs/def.yml", true);
        }
        dialogs.clear();
        loadDialog(folder);
        logger.info(dialogs.size() + " Dialog Loaded. (" + (System.currentTimeMillis() - time + "ms)"));
        CronusReloadServiceEvent.call(this);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void e(PlayerInteractEntityEvent e) {
        if (cooldown.isCooldown(e.getPlayer().getName(), 0)) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), () -> {
            EntitySelector selector = Cronus.getCronusService().getService(EntitySelector.class);
            for (DialogGroup dialog : dialogs) {
                if (selector.isSelect(e.getRightClicked(), dialog.getTarget()) && (dialog.getCondition() == null || dialog.getCondition().check(e.getPlayer()))) {
                    CronusDialogInteractEvent dialogEvent = CronusDialogInteractEvent.call(dialog.getDialog(), e.getRightClicked(), e.getPlayer());
                    if (!dialogEvent.isCancelled()) {
                        try {
                            dialog.getDialog().display(e.getPlayer(), e.getRightClicked());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    return;
                }
            }
        });
    }

    public String getDialogStyle() {
        return Cronus.getConf().getString("Settings.dialog-style", "cronus_menu");
    }

    public DisplayBase getDisplay(String id) {
        return registeredBase.stream().filter(b -> b.getName().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public void registerDisplay(Class<? extends DisplayBase> base) throws IllegalAccessException, InstantiationException {
        registeredBase.add(base.newInstance());
    }

    public void registerDisplay(DisplayBase base) {
        for (DisplayBase registered : registeredBase) {
            if (registered.getName().equalsIgnoreCase(base.getName())) {
                throw new IllegalStateException(base.getName() + " already registered.");
            }
        }
        registeredBase.add(base);
    }

    public void loadDialog(File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(this::loadDialog);
        } else {
            YamlConfiguration yaml = Files.loadYaml(file);
            for (String id : yaml.getKeys(false)) {
                ConfigurationSection config = yaml.getConfigurationSection(id);
                try {
                    if (config.getKeys(false).isEmpty()) {
                        logger.error("Dialog " + id + " is empty.");
                    } else {
                        dialogs.add(new DialogGroup(config));
                    }
                } catch (Throwable t) {
                    logger.error("Dialog " + id + " failed to load.");
                    t.printStackTrace();
                }
            }
        }
    }

    public DialogGroup getDialog(String id) {
        return dialogs.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public List<DialogGroup> getDialogs() {
        return dialogs;
    }

    public File getFolder() {
        return folder;
    }
}
