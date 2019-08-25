package ink.ptms.cronus.uranus.program;

import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @Author 坏黑
 * @Since 2019-05-11 11:19
 */
public class Program {

    @TInject
    protected static TLogger logger;
    protected YamlConfiguration data = new YamlConfiguration();
    protected ProgramFile programFile;
    protected CommandSender sender;
    protected String[] args;
    protected Object result;
    protected int line;

    public Object eval(ProgramFile programFile, CommandSender sender, String... args) {
        if (programFile.getProgram() == null) {
            return null;
        }
        this.programFile = programFile;
        this.sender = sender;
        this.args = args;
        this.line = 1;
        // 执行程序
        ProgramLine index = programFile.getProgram();
        if (index.getEffect() != null) {
            eval(programFile, index);
        }
        // 循环检测
        while (index.hasNext()) {
            if ((index = index.getNext()) != null && index.getEffect() != null) {
                eval(programFile, index);
            }
        }
        return result;
    }

    private void eval(ProgramFile programFile, ProgramLine index) {
        this.line++;
        try {
            index.getEffect().eval(this);
        } catch (Throwable t) {
            logger.error("程序 " + programFile.getName() + ":" + line + " 执行时出现异常: " + t.getMessage());
        }
    }

    public YamlConfiguration getData() {
        return data;
    }

    public ProgramFile getProgramFile() {
        return programFile;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
