package ink.ptms.cronus.internal.variable;

/**
 * @Author sky
 * @Since 2019-08-17 18:51
 */
public enum VariableType {

    INT, DOUBLE, STRING, LIST;

    public boolean isNumber() {
        return this == INT ||this == DOUBLE;
    }

}
