package ink.ptms.cronus.internal.condition.impl.hook;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.hook.HookSkript;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "skript", pattern = "sk(ript)? (?<script>.+)", example = "skript [skript]")
public class CondSkript extends Condition {

    private String origin;
    private ch.njol.skript.lang.Condition condition;

    @Override
    public void init(Matcher matcher, String text) {
        HookSkript.toggleCurrentEvent(true);
        try {
            origin = matcher.group("script");
            condition = ch.njol.skript.lang.Condition.parse(matcher.group("script"), null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        HookSkript.toggleCurrentEvent(false);
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return condition != null && condition.check(new PlayerCommandPreprocessEvent(player, ""));
    }

    @Override
    public String translate() {
            return TLocale.asString("translate-condition-skript", origin);
    }

    @Override
    public String toString() {
        return "CondSkript{" +
                "origin='" + origin + '\'' +
                ", condition=" + condition +
                '}';
    }
}
