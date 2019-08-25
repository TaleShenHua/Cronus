package ink.ptms.cronus.service.dialog.api;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.event.CronusDialogEvalEvent;
import ink.ptms.cronus.service.dialog.DialogDisplay;
import ink.ptms.cronus.service.dialog.DialogPack;
import io.izzel.taboolib.module.inject.PlayerContainer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;
import java.util.UUID;

/**
 * @Author 坏黑
 * @Since 2019-08-03 0:07
 */
public abstract class DisplayBase extends DialogDisplay {

    @PlayerContainer
    private static Map<String, ReplyMap> map = Maps.newHashMap();
    private static Map<String, Entity> pe = Maps.newHashMap();

    abstract public String getName();

    abstract public void open(Player player, DialogPack dialogPack);

    abstract public void preReply(Player player, DialogPack replyPack, String id, int index);

    abstract public void postReply(Player player, DialogPack dialogPack, ReplyMap replyMap, int index);

    public boolean isHide(Player player, DialogPack dialogPack) {
        return String.valueOf(dialogPack.getConfig().get("item")).equalsIgnoreCase("air") || dialogPack.getConfig().containsKey("hide");
    }

    public void preDialog(Player player, Reply reply) {
    }

    public void preEffect(Player player, Reply reply) {
    }

    public Result eval(Player player, Reply reply) {
        DialogPack pack = reply.getDialogPackOrigin().getPack(player, null);
        if (pack == null) {
            return Result.NONE;
        }
        CronusDialogEvalEvent event = CronusDialogEvalEvent.call(pack, player);
        if (event.isCancelled()) {
            return Result.CANCELLED;
        }
        // 打开新的对话
        if (event.getPack().getDialog() != null) {
            player.setMetadata("cronus:ignore_close", new FixedMetadataValue(Cronus.getInst(), 1));
            preDialog(player, reply);
            display(player, pe.get(player.getName()), event.getPack().getDialog());
            return Result.DIALOG;
        }
        // 执行点击动作
        else if (event.getPack().getEffect() != null) {
            player.setMetadata("cronus:ignore_close", new FixedMetadataValue(Cronus.getInst(), 1));
            preEffect(player, reply);
            event.getPack().effectEval(player);
            return Result.EFFECT;
        }
        return Result.NONE;
    }

    public void close(Player player) {
        pe.remove(player.getName());
        ReplyMap replyMap = resetDialog(player);
        if (replyMap != null) {
            if (player.hasMetadata("cronus:ignore_close")) {
                player.removeMetadata("cronus:ignore_close", Cronus.getInst());
            } else {
                replyMap.getCurrent().getParent().closeEval(player);
            }
        }
    }

    @Override
    public void display(Player player, Entity entity, DialogPack dialogPack) {
        pe.put(player.getName(), entity);
        ReplyMap replyMap = new ReplyMap(dialogPack);
        // 基本界面
        try {
            open(player, dialogPack);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 条件
        for (int i = 0; i < dialogPack.getReply().size(); i++) {
            DialogPack reply = dialogPack.getReply().get(i).getPack(player, null);
            if (!isHide(player, reply)) {
                replyMap.getReply().add(new Reply(UUID.randomUUID().toString(), reply, dialogPack.getReply().get(i)));
            }
        }
        // 物品
        int index = 0;
        for (; index < replyMap.getReply().size(); index++) {
            try {
                preReply(player, replyMap.getReply().get(index).getDialogPack(), replyMap.getReply().get(index).getId(), index);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        try {
            postReply(player, dialogPack, replyMap, index);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 开始动作
        dialogPack.getParent().openEval(player);
        // 重置数据
        map.put(player.getName(), replyMap);
    }

    public static boolean hasDialog(Player player) {
        return map.containsKey(player.getName());
    }

    public static ReplyMap resetDialog(Player player) {
        return map.remove(player.getName());
    }

    public static DialogPack getCurrent(Player player) {
        ReplyMap replyMap = map.get(player.getName());
        return replyMap == null ? null : replyMap.getCurrent();
    }

    public static Reply getReplyById(Player player, String id) {
        ReplyMap replyMap = map.get(player.getName());
        return replyMap == null ? null : replyMap.getReply().stream().filter(reply -> reply.getId().equals(id)).findFirst().orElse(null);
    }

    public static Entity getInEntity(Player player) {
        return pe.get(player.getName());
    }

    public static Map<String, ReplyMap> getMap() {
        return map;
    }
}
