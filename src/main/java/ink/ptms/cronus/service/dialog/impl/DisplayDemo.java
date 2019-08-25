package ink.ptms.cronus.service.dialog.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.service.dialog.DialogPack;
import ink.ptms.cronus.service.dialog.api.DisplayBase;
import ink.ptms.cronus.service.dialog.api.Reply;
import ink.ptms.cronus.service.dialog.api.ReplyMap;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @Author 坏黑
 * @Since 2019-08-03 0:07
 */
public class DisplayDemo extends DisplayBase implements Listener {

    public DisplayDemo() {
        Bukkit.getPluginManager().registerEvents(this, Cronus.getInst());
    }

    @Override
    public String getName() {
        return "CRONUS_DEMO";
    }

    @Override
    public void open(Player player, DialogPack dialogPack) {
        TellrawJson.create()
                .newLine()
                .append("§7" + dialogPack.getParent().getTitle()).hoverText(String.join("\n", dialogPack.getText()))
                .send(player);
    }

    @Override
    public void preReply(Player player, DialogPack replyPack, String id, int index) {
        TellrawJson.create()
                .append("§8" + (index + 1) + ". §f§o")
                .append(replyPack.getText().isEmpty() ? "..." : replyPack.getText().get(0)).hoverText(String.join("\n", replyPack.getText())).clickCommand("cronus_dialog_reply:" + id)
                .send(player);
    }

    @Override
    public void postReply(Player player, DialogPack dialogPack, ReplyMap replyMap, int index) {
        TellrawJson.create()
                .append("§8" + (index + 1) + ". §f§o")
                .append("再见").hoverText("...").clickCommand("cronus_dialog_reply:close")
                .send(player);
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        if (e.getMessage().startsWith("cronus_dialog_reply:")) {
            e.setCancelled(true);
            // 获取对话序号
            String id = e.getMessage().substring("cronus_dialog_reply:".length());
            if (id.equals("close") && hasDialog(e.getPlayer())) {
                close(e.getPlayer());
                return;
            }
            // 获取对话回复并执行动作
            Reply reply = getReplyById(e.getPlayer(), id);
            if (reply != null) {
                eval(e.getPlayer(), reply);
            }
        }
    }

    @Override
    public String toString() {
        return "DisplayDemo{}";
    }
}
