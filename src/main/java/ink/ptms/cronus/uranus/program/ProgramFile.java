package ink.ptms.cronus.uranus.program;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @Author 坏黑
 * @Since 2019-05-11 11:41
 */
public class ProgramFile {

    private String name;
    private YamlConfiguration conf;
    private ProgramLine program;

    public ProgramFile(String name, YamlConfiguration conf, ProgramLine program) {
        this.name = name;
        this.conf = conf;
        this.program = program;
    }

    public Object eval(CommandSender sender, String... args) {
        return new Program().eval(this, sender, args);
    }

    public String getName() {
        return name;
    }

    public YamlConfiguration getConf() {
        return conf;
    }

    public ProgramLine getProgram() {
        return program;
    }
}
