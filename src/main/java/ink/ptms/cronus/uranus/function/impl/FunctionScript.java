package ink.ptms.cronus.uranus.function.impl;

import com.google.common.collect.Maps;
import ink.ptms.cronus.uranus.Uranus;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.event.UranusScriptEvalEvent;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import io.izzel.taboolib.util.lite.Scripts;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionScript extends Function {

    @TInject
    private static TLogger logger;
    private Map<String, CompiledScript> scripts = Maps.newHashMap();

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "script";
    }

    @Override
    public Object eval(Program program, String... args) {
        try {
            SimpleBindings bindings = new SimpleBindings();
            bindings.put("sender", program.getSender());
            bindings.put("player", program.getSender());
            bindings.put("server", Uranus.getInst().getServer());
            bindings.put("bukkit", Uranus.getInst().getServer());
            bindings.put("plugin", Uranus.getInst());
            UranusScriptEvalEvent event = new UranusScriptEvalEvent(program, bindings, args[0], scripts.computeIfAbsent(args[0], s -> Scripts.compile(args[0]))).call();
            return event.getCompiledScript().eval(event.getBindings());
        } catch (ScriptException e) {
            logger.error("脚本执行出错: " + e.getMessage());
        }
        return "<Null.Script>";
    }
}
