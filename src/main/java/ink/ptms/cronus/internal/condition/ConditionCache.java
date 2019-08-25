package ink.ptms.cronus.internal.condition;

import java.util.regex.Pattern;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:22
 */
public class ConditionCache {

    private Pattern pattern;
    private Class<? extends Condition> condition;

    public ConditionCache(String pattern, Class<? extends Condition> condition) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        this.condition = condition;
    }

    public Condition instance() throws Exception {
        return condition.newInstance();
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Class<? extends Condition> getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "ConditionCache{" +
                "pattern=" + pattern +
                ", condition=" + condition +
                '}';
    }
}
