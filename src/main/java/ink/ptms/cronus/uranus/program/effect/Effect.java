package ink.ptms.cronus.uranus.program.effect;

import ink.ptms.cronus.uranus.program.Program;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 坏黑
 * @Since 2019-05-11 11:20
 */
public abstract class Effect {

    abstract public String pattern();

    abstract public String getExample();

    abstract public void eval(Program program);

    private Pattern pattern;
    private String source;

    public void init() {
        pattern = Pattern.compile(pattern(), Pattern.CASE_INSENSITIVE);
    }

    public void match(Matcher matcher) {
    }

    public boolean parse(String in) {
        return pattern.matcher(in).find();
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getSource() {
        return source;
    }

    public Effect copy() {
        try {
            Effect effect = getClass().newInstance();
            effect.pattern = this.pattern;
            return effect;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    public Effect copy(String program) {
        Effect instance = copy();
        if (instance == null) {
            return null;
        }
        Matcher matcher = instance.getPattern().matcher(program);
        if (matcher.find()) {
            instance.match(matcher);
        }
        instance.source = program;
        return instance;
    }
}
