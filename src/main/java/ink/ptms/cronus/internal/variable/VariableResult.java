package ink.ptms.cronus.internal.variable;

/**
 * @Author sky
 * @Since 2019-08-17 18:52
 */
public class VariableResult {

    private VariableType type;
    private Object value;

    public VariableResult(VariableType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public VariableType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
