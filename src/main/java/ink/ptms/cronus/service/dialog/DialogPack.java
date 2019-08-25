package ink.ptms.cronus.service.dialog;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.NoneProgram;
import ink.ptms.cronus.internal.program.QuestEffect;
import ink.ptms.cronus.internal.program.QuestProgram;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-10 17:37
 */
public class DialogPack {

    private Map<String, Object> config;
    private DialogGroup parent;
    private DialogPack dialog;
    private QuestEffect effect;
    private List<String> text;
    private List<DialogPack> reply = Lists.newArrayList();
    private Condition condition;
    private DialogPack conditionDialog;
    private boolean quest;

    public DialogPack(DialogGroup parent, Map<String, Object> config) {
        this.config = config;
        this.parent = parent;
        this.text = TLocale.Translate.setColored((List) config.getOrDefault("text", Lists.newArrayList("-")));
        // 含有 effect 则视为结束语句
        if (config.containsKey("effect")) {
            this. effect = new QuestEffect((List) config.get("effect"));
        }
        // 含有 dialog 则视为对话跳转
        else if (config.containsKey("dialog")) {
            this.dialog = new DialogPack(parent, (Map) config.get("dialog"));
        }
        // 含有 reply 则视为对话选择
        else if (config.containsKey("reply")) {
            for (Object reply : (List) config.get("reply")) {
                if (reply instanceof Map) {
                    this.reply.add(new DialogPack(parent, (Map) reply));
                }
            }
        }
        // 条件
        Object condition = config.get("condition");
        if (condition instanceof Condition) {
            this.condition = (Condition) condition;
        } else if (condition instanceof String) {
            this.condition = ConditionParser.parse((String) condition);
        }
        if (config.containsKey("condition-dialog")) {
            conditionDialog = new DialogPack(parent, (Map) config.get("condition-dialog"));
        }
    }

    public DialogPack(DialogGroup parent, DialogPack dialog) {
        this.parent = parent;
        this.dialog = dialog;
    }

    public DialogPack(DialogGroup parent, QuestEffect effect) {
        this.parent = parent;
        this.effect = effect;
    }

    public void display(Player player, Entity entity) {
        parent.openEval(player);
        if (parent.getDisplay() != null) {
            parent.getDisplay().display(player, entity, this);
        }
    }

    public void effectEval(Player player) {
        new NoneProgram(player).eval(effect.getEffect());
    }

    public void effectEval(QuestProgram program) {
        effect.eval(program.getPlayer(), program.getDataQuest());
    }

    public DialogPack copy() {
        DialogPack dialogPack = new DialogPack(parent, dialog);
        dialogPack.text = Lists.newArrayList(text);
        dialogPack.reply = reply.stream().map(DialogPack::copy).collect(Collectors.toList());
        dialogPack.effect = effect;
        return dialogPack;
    }

    public DialogPack getPack(Player player, DataQuest dataQuest) {
        DialogPack pack = this;
        while (true) {
            if (pack.getCondition() == null || !pack.getCondition().check(player, dataQuest, null)) {
                return pack;
            }
            if ((pack = pack.getConditionDialog()) == null) {
                return pack;
            }
        }
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public DialogGroup getParent() {
        return parent;
    }

    public List<String> getText() {
        return text;
    }

    public List<DialogPack> getReply() {
        return reply;
    }

    public DialogPack getDialog() {
        return dialog;
    }

    public QuestEffect getEffect() {
        return effect;
    }

    public void setParent(DialogGroup parent) {
        this.parent = parent;
    }

    public void setDialog(DialogPack dialog) {
        this.dialog = dialog;
    }

    public void setEffect(QuestEffect effect) {
        this.effect = effect;
    }

    public boolean isQuest() {
        return quest;
    }

    public void setQuest(boolean quest) {
        this.quest = quest;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public DialogPack getConditionDialog() {
        return conditionDialog;
    }

    public void setConditionDialog(DialogPack conditionDialog) {
        this.conditionDialog = conditionDialog;
    }

    @Override
    public String toString() {
        return "DialogPack{" +
                "parent=" + parent.getId() +
                ", dialog=" + dialog +
                ", effect=" + effect +
                ", text=" + text +
                ", reply=" + reply +
                '}';
    }
}