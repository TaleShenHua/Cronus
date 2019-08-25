package ink.ptms.cronus;

import com.google.common.collect.Maps;
import ink.ptms.cronus.database.Database;
import ink.ptms.cronus.database.DatabaseType;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.item.ItemStorage;
import ink.ptms.cronus.database.data.item.ItemStorageCronus;
import ink.ptms.cronus.database.data.item.hook.StorageAsgard;
import ink.ptms.cronus.database.data.item.hook.StoragePurtmars;
import ink.ptms.cronus.database.impl.DatabaseSQL;
import ink.ptms.cronus.database.impl.DatabaseYAML;
import ink.ptms.cronus.event.CronusDataPullEvent;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestBook;
import ink.ptms.cronus.internal.condition.ConditionCache;
import ink.ptms.cronus.internal.task.TaskCache;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.TabooLibLoader;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author 坏黑
 * @Since 2019-05-28 23:35
 */
public class CronusService {

    @TInject
    private static TLogger logger;
    @TInject("SacredItem")
    private static boolean asgardHooked;
    @TInject("PurtmarsItem")
    private static boolean purtmarsHooked;
    private DatabaseType databaseType;
    private Map<String, Service> services = Maps.newHashMap();
    private Map<String, Quest> registeredQuest = Maps.newHashMap();
    private Map<String, QuestBook> registeredQuestBook = Maps.newHashMap();
    private Map<String, TaskCache> registeredTask = Maps.newHashMap();
    private Map<String, ConditionCache> registeredCondition = Maps.newHashMap();
    private Map<String, DataPlayer> playerData = Maps.newConcurrentMap();
    private ExecutorService executorService = Executors.newFixedThreadPool(16);

    void init() {
        // 数据储存
        databaseType = Cronus.getConf().getString("Database.type").equalsIgnoreCase("SQL") ? DatabaseType.SQL : DatabaseType.YAML;
        if (databaseType == DatabaseType.SQL) {
            services.put("Database", new DatabaseSQL());
            logger.info("Database Using SQL.");
        } else {
            services.put("Database", new DatabaseYAML());
            logger.info("Database Using YAML.");
        }
        // 物品储存
        if (purtmarsHooked) {
            services.put("ItemStorage", new StoragePurtmars());
            logger.info("Hooked Purtmars.");
        } else if (asgardHooked) {
            services.put("ItemStorage", new StorageAsgard());
            logger.info("Hooked Asgard.");
        } else {
            services.put("ItemStorage", new ItemStorageCronus());
        }
        // 引路
        TabooLibLoader.getPluginClasses(Cronus.getInst()).ifPresent(classes -> {
            classes.stream().filter(pClass -> Service.class.isAssignableFrom(pClass) && pClass.isAnnotationPresent(Auto.class)).forEach(pClass -> {
                try {
                    Service service = (Service) pClass.newInstance();
                    if (service instanceof Listener) {
                        Bukkit.getPluginManager().registerEvents((Listener) service, Cronus.getInst());
                    }
                    services.put(pClass.getSimpleName(), service);
                } catch (Throwable t) {
                    logger.info("Service " + pClass.getSimpleName() + " initialization failed.");
                    t.printStackTrace();
                }
            });
        });
        logger.info(services.size() + " Service Registered.");
        // 加载
        services.values().forEach(Service::init);
        // 下载数据
        Bukkit.getOnlinePlayers().forEach(this::refreshData);
    }

    void active() {
        // 运行
        services.values().forEach(Service::active);
    }

    void cancel() {
        // 上传数据
        playerData.values().forEach(DataPlayer::push);
        // 卸载
        services.values().forEach(Service::cancel);
        executorService.shutdown();
    }

    public void async(Runnable runnable) {
        executorService.submit(runnable);
    }

    public void refreshData(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), () -> {
            CronusDataPullEvent.call(player, Cronus.getCronusService().getPlayerData().computeIfAbsent(player.getName(), p -> Cronus.getCronusService().getDatabase().download(player)));
        });
    }

    public <T> T getService(Class<? extends T> clazz) {
        return (T) services.get(clazz.getSimpleName());
    }

    public <T> T getService(String name, Class<? extends T> clazz) {
        return (T) services.get(name);
    }

    public Service getService(String name) {
        return services.get(name);
    }

    public Database getDatabase() {
        return (Database) getService("Database");
    }

    public ItemStorage getItemStorage() {
        return (ItemStorage) getService("ItemStorage");
    }

    public boolean isNonHooked() {
        return !purtmarsHooked && !asgardHooked;
    }

    public boolean isPurtmarsHooked() {
        return purtmarsHooked;
    }

    public boolean isAsgardHooked() {
        return asgardHooked;
    }

    public Map<String, Quest> getRegisteredQuest() {
        return registeredQuest;
    }

    public Map<String, QuestBook> getRegisteredQuestBook() {
        return registeredQuestBook;
    }

    public Map<String, TaskCache> getRegisteredTask() {
        return registeredTask;
    }

    public Map<String, ConditionCache> getRegisteredCondition() {
        return registeredCondition;
    }

    public Map<String, DataPlayer> getPlayerData() {
        return playerData;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public Map<String, Service> getServices() {
        return services;
    }
}
