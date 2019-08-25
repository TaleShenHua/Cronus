package ink.ptms.cronus.internal.condition.impl.argument;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.Numbers;
import io.izzel.taboolib.util.lite.Scripts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "script", pattern = "(script|js) (?<script>.+)", example = "script [script]")
public class CondScript extends Condition {

    private String origin;
    private CompiledScript script;

    @Override
    public void init(Matcher matcher, String text) {
        origin = matcher.group("script");
        script = Scripts.compile(matcher.group("script"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("server", Bukkit.getServer());
        bindings.put("plugin", Cronus.getInst());
        bindings.put("player", player);
        bindings.put("event", event);
        bindings.put("quest", quest);
        try {
            return Numbers.getBoolean(String.valueOf(this.script.eval(bindings)));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String translate() {
        return TLocale.asString("translate-condition-script", origin);
    }

    @Override
    public String toString() {
        return "CondScript{" +
                "origin='" + origin + '\'' +
                ", script=" + script +
                '}';
    }
}
