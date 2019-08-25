package ink.ptms.cronus.internal.hook;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.variables.SerializedVariable;
import ch.njol.skript.variables.Variables;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Base64;

/**
 * @Author 坏黑
 * @Since 2019-06-17 23:44
 */
public class HookSkript {

    private static String currentEventName;
    private static Class<? extends Event>[] currentEvents;

    public static Object getVariable(String name) {
        return Variables.getVariable(name.toLowerCase(), null, false);
    }

    public static void setVariable(String name, Object value) {
        Variables.setVariable(name.toLowerCase(), value, null, false);
    }

    public static String serialize(Object variable) {
        SerializedVariable.Value value = Classes.serialize(variable);
        return value.type + ":" + Base64.getEncoder().encodeToString(value.data);
    }

    public static Object deserialize(String in) {
        String[] value = in.split(":");
        return Classes.deserialize(value[0], Base64.getDecoder().decode(value[1]));
    }

    public static void toggleCurrentEvent(boolean var) {
        if (var) {
            currentEventName = ScriptLoader.getCurrentEventName();
            currentEvents = ScriptLoader.getCurrentEvents();
            ScriptLoader.setCurrentEvent("command", PlayerCommandPreprocessEvent.class);
        } else {
            ScriptLoader.setCurrentEvent(currentEventName, currentEvents);
        }
    }
}
