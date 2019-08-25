package ink.ptms.cronus.uranus.event;

import ink.ptms.cronus.uranus.program.Program;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.script.CompiledScript;
import javax.script.SimpleBindings;

public class UranusScriptEvalEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Program program;
    private SimpleBindings bindings;
    private String script;
    private CompiledScript compiledScript;

    public UranusScriptEvalEvent(Program program, SimpleBindings bindings, String script, CompiledScript compiledScript) {
        this.program = program;
        this.bindings = bindings;
        this.script = script;
        this.compiledScript = compiledScript;
    }

    public UranusScriptEvalEvent call() {
        Bukkit.getPluginManager().callEvent(this);
        return this;
    }

    public Program getProgram() {
        return program;
    }

    public SimpleBindings getBindings() {
        return bindings;
    }

    public void setBindings(SimpleBindings bindings) {
        this.bindings = bindings;
    }

    public String getScript() {
        return script;
    }

    public CompiledScript getCompiledScript() {
        return compiledScript;
    }

    public void setCompiledScript(CompiledScript compiledScript) {
        this.compiledScript = compiledScript;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
