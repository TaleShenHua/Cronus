package ink.ptms.cronus.service.dialog.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.service.dialog.DialogPack;
import ink.ptms.cronus.service.dialog.api.DisplayBase;
import ink.ptms.cronus.service.dialog.api.Reply;
import ink.ptms.cronus.service.dialog.api.ReplyMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.UnsupportedEncodingException;

public class DisplayMod extends DisplayBase implements PluginMessageListener {
    public DisplayMod() {
        Bukkit.getMessenger().registerIncomingPluginChannel(Cronus.getInst(), "MODU", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Cronus.getInst(), "MODU");
    }

    @Override
    public String getName() {
        return "CRONUS_MOD";
    }

    @Override
    public void open(Player player, DialogPack dialogPack) {
    }

    @Override
    public void preReply(Player player, DialogPack replyPack, String id, int index) {
    }

    @Override
    public void preEffect(Player player, Reply reply) {
        JsonObject json = new JsonObject();
        json.addProperty("c", "bqcv");
        json.addProperty("end", 1);
        try {
            player.sendPluginMessage(Cronus.getInst(), "MODU", ("@" + json.toString()).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void postReply(Player player, DialogPack dialogPack, ReplyMap replyMap, int index) {
        JsonObject json = new JsonObject();
        json.addProperty("c", "bqcv");
        json.add("info", toJsonObject(player, dialogPack, replyMap));
        try {
            player.sendPluginMessage(Cronus.getInst(), "MODU", ("@" + json.toString()).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        close(player);
    }

    public JsonObject toJsonObject(Player player, DialogPack dialogPack, ReplyMap replyMap) {
        Entity entity = getInEntity(player);
        JsonObject json = new JsonObject();
        json.addProperty("qe", entity == null ? "对话" : entity.getCustomName() == null ? entity.getName() : entity.getCustomName());
        json.addProperty("rep", String.join("", dialogPack.getText()));
        JsonArray array = new JsonArray();
        for (int i = 0; i < replyMap.getReply().size(); i++) {
            Reply op = replyMap.getReply().get(i);
            array.add(new JsonPrimitive(String.join("", op.getDialogPack().getText())));
        }
        json.add("ops", array);
        if (entity != null) {
            json.addProperty("npcUid", entity.getUniqueId().toString());
        }
        return json;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("MODU")) {
            try {
                String cmd = new String(message, "UTF-8");
                if (cmd.indexOf("@bqcv_op:") > -1) {
                    String id = cmd.split(":")[1];
                    ReplyMap replyMap = getMap().get(player.getName());
                    Reply reply = replyMap.getReply().get(Integer.parseInt(id) - 1);
                    if (reply != null) {
                        eval(player, reply);
                    }
                } else if (cmd.indexOf("@bqcv_end:t") > -1) {
                    close(player);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
