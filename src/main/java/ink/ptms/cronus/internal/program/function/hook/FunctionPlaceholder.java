package ink.ptms.cronus.internal.program.function.hook;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import io.izzel.taboolib.module.locale.TLocale;

/**
 * @Author 坏黑
 * @Since 2019-05-11 13:16
 */
@Auto
public class FunctionPlaceholder extends Function {

    @Override
    public boolean allowArguments() {
        return false;
    }

    @Override
    public String getName() {
        return "placeholder";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            return TLocale.Translate.setPlaceholders(((QuestProgram) program).getPlayer(), args[0]);
        }
        return "<Non-Quest>";
    }
}
