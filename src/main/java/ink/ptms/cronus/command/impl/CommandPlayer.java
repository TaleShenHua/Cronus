package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusVisibleToggleEvent;
import ink.ptms.cronus.internal.QuestBook;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

/**
 * @Author 坏黑
 * @Since 2019-06-09 23:57
 */
public class CommandPlayer {

    @TInject("commands.yml")
    static TConfig conf;
    
    @TInject
    static io.izzel.taboolib.module.command.lite.CommandBuilder logs = io.izzel.taboolib.module.command.lite.CommandBuilder.create("CronusQuestLogs", null)
            .execute((sender, args) -> {
                if (!conf.getBoolean("PlayerCommands.CronusQuestLogs", true)) {
                    sender.sendMessage(conf.getString("DisableMessage").equals("$spigot") ? SpigotConfig.unknownCommandMessage : conf.getStringColored("DisableMessage"));
                    return;
                }
                if (sender instanceof Player) {
                    CronusAPI.openQuestLogs((Player) sender);
                }
            });

    @TInject
    static io.izzel.taboolib.module.command.lite.CommandBuilder quit = io.izzel.taboolib.module.command.lite.CommandBuilder.create("CronusQuestQuit", null)
            .execute((sender, args) -> {
                if (!conf.getBoolean("PlayerCommands.CronusQuestQuit", true)) {
                    sender.sendMessage(conf.getString("DisableMessage").equals("$spigot") ? SpigotConfig.unknownCommandMessage : conf.getStringColored("DisableMessage"));
                    return;
                }
                if (sender instanceof Player && args.length > 0) {
                    DataPlayer dataPlayer = CronusAPI.getData((Player) sender);
                    DataQuest dataQuest = dataPlayer.getQuest(args[0]);
                    if (dataQuest != null && !dataPlayer.isQuestCompleted(args[0])) {
                        dataPlayer.failureQuest(dataQuest.getQuest());
                        dataPlayer.push();
                    }
                }
            });

    @TInject
    static io.izzel.taboolib.module.command.lite.CommandBuilder visible = io.izzel.taboolib.module.command.lite.CommandBuilder.create("CronusQuestVisible", null)
            .execute((sender, args) -> {
                if (!conf.getBoolean("PlayerCommands.CronusQuestVisible", true)) {
                    sender.sendMessage(conf.getString("DisableMessage").equals("$spigot") ? SpigotConfig.unknownCommandMessage : conf.getStringColored("DisableMessage"));
                    return;
                }
                if (sender instanceof Player && args.length > 0) {
                    DataPlayer dataPlayer = CronusAPI.getData((Player) sender);
                    if (dataPlayer.getQuestHide().contains(args[0])) {
                        dataPlayer.getQuestHide().remove(args[0]);
                    } else {
                        dataPlayer.getQuestHide().add(args[0]);
                    }
                    dataPlayer.push();
                    CronusVisibleToggleEvent.call((Player) sender);
                }
            });

}
