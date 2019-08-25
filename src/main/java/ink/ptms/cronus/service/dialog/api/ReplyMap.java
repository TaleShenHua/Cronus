package ink.ptms.cronus.service.dialog.api;

import com.google.common.collect.Lists;
import ink.ptms.cronus.service.dialog.DialogPack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-08-03 0:45
 */
public class ReplyMap {

    private DialogPack current;
    private List<Reply> reply;

    public ReplyMap(DialogPack current) {
        this.current = current;
        this.reply = Lists.newArrayList();
    }

    public DialogPack getCurrent() {
        return current;
    }

    public List<Reply> getReply() {
        return reply;
    }
}
