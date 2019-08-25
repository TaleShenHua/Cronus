package ink.ptms.cronus;

import com.google.gson.Gson;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestBook;
import ink.ptms.cronus.internal.QuestStage;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Catchers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-23 18:06
 */
@CronusPlugin.Version(5.04)
public class Cronus extends CronusPlugin implements PluginMessageListener {

    @TInject
    private static Cronus inst;
    @TInject(state = TInject.State.LOADING, init = "init", active = "active", cancel = "cancel")
    private static CronusService cronusService;
    @TInject(state = TInject.State.LOADING, init = "init", active = "start")
    private static CronusLoader cronusLoader;
    private static CronusVersion cronusVersion;
    @TInject(value = "config.yml")
    private static TConfig conf;
    @TInject
    private static TLogger logger;

    @Override
    public void onLoading() {
        cronusVersion = CronusVersion.fromString(this.getDescription().getVersion());
    }

    @Override
    public void onStarting() {
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "modugui", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "modugui");
        try (InputStreamReader inputStreamReader = new InputStreamReader(inst.getResource("motd.txt"), StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            bufferedReader.lines().forEach(l -> Bukkit.getConsoleSender().sendMessage(Strings.replaceWithOrder(l, inst.getDescription().getVersion())));
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void onStopping() {
        Catchers.getPlayerdata().clear();
    }

    public static void reloadQuest() {
        cronusLoader.start();
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static Cronus getInst() {
        return inst;
    }

    public static CronusLoader getCronusLoader() {
        return cronusLoader;
    }

    public static CronusService getCronusService() {
        return cronusService;
    }

    public static CronusVersion getCronusVersion() {
        return cronusVersion;
    }

    public static TConfig getConf() {
        return conf;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("modugui")) {
            try {
                String cmd = new String(message, "UTF-8");
                if (cmd.equals("@getQuests")) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cmd", "quest");
                    Map<String, Object> mainQuests = new HashMap<>();
                    map.put("mainquests", mainQuests);
                    DataPlayer playerData = CronusAPI.getData(player);
                    for (String mainId : cronusService.getRegisteredQuestBook().keySet()) {
                        Map<String, Object> mainQuest = new HashMap<>();
                        QuestBook main = cronusService.getRegisteredQuestBook().get(mainId);
                        Map<String, Object> quests = new HashMap<>();
                        for (String questId : cronusService.getRegisteredQuest().keySet()) {
                            Quest quest = cronusService.getRegisteredQuest().get(questId);
                            for (String tag : quest.getBookTag()) {
                                if (main.getList().containsKey(tag)) {
                                    Map<String, Object> questMap = new HashMap<>();
                                    questMap.put("QuestName", quest.getDisplay());
                                    Map<String, Object> questStag = new HashMap<>();
                                    for(QuestStage stage : quest.getStage()){
                                        System.out.println(stage);
                                    }
                                    questMap.put("QuestStage", questStag);
                                    quests.put(questId, questMap);
                                }
                            }
                        }
                        mainQuest.put("MainName", main.getDisplay());
                        mainQuest.put("MainQuests", quests);
                        mainQuests.put(mainId, mainQuest);
                    }
                    try {
                        player.sendPluginMessage(this, "modugui", ("@" + new Gson().toJson(map)).getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
