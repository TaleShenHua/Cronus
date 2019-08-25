package ink.ptms.cronus.service.dialog.api;

import ink.ptms.cronus.service.dialog.DialogPack;

/**
 * @Author 坏黑
 * @Since 2019-08-03 0:33
 */
public class Reply {

    private String id;
    private DialogPack dialogPack;
    private DialogPack dialogPackOrigin;

    public Reply(String id, DialogPack dialogPack, DialogPack dialogPackOrigin) {
        this.id = id;
        this.dialogPack = dialogPack;
        this.dialogPackOrigin = dialogPackOrigin;
    }

    public String getId() {
        return id;
    }

    public DialogPack getDialogPack() {
        return dialogPack;
    }

    public DialogPack getDialogPackOrigin() {
        return dialogPackOrigin;
    }
}
