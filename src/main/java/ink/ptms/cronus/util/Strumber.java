package ink.ptms.cronus.util;

import ink.ptms.cronus.internal.variable.VariableType;

/**
 * StringAndNumber
 *
 * @Author 坏黑
 * @Since 2019-05-29 21:43
 */
public class Strumber {

    private NumberType type;
    private Number number;
    private String source;

    public Strumber(long number) {
        this.number = number;
        this.type = NumberType.INT;
    }

    public Strumber(double number) {
        this.number = number;
        this.type = NumberType.DOUBLE;
    }

    public Strumber(String source) {
        this.source = source;
        try {
            this.number = Double.parseDouble(this.source);
            this.type = Utils.isInt(this.number.doubleValue()) ? NumberType.INT : NumberType.DOUBLE;
        } catch (Throwable ignored) {
            this.type = NumberType.STRING;
        }
    }

    public Strumber add(String v) {
        Strumber numberFormat = new Strumber(v);
        if (isNumber() && numberFormat.isNumber()) {
            this.number = this.number.doubleValue() + numberFormat.getNumber().doubleValue();
            this.type = Utils.isInt(this.number.doubleValue()) ? NumberType.INT : NumberType.DOUBLE;
        } else {
            this.source += numberFormat.getSource();
            this.type = NumberType.STRING;
        }
        return this;
    }

    public Strumber subtract(String v) {
        Strumber numberFormat = new Strumber(v);
        if (isNumber() && numberFormat.isNumber()) {
            this.number = this.number.doubleValue() - numberFormat.getNumber().doubleValue();
            this.type = Utils.isInt(this.number.doubleValue()) ? NumberType.INT : NumberType.DOUBLE;
        }
        return this;
    }

    public Object get() {
        switch (type) {
            case INT:
                return number.longValue();
            case DOUBLE:
                return number.doubleValue();
            default:
                return source;
        }
    }

    public boolean isNumber() {
        return type == NumberType.INT || type == NumberType.DOUBLE;
    }

    public Number getNumber() {
        return number;
    }

    public NumberType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public enum NumberType {

        DOUBLE, INT, STRING;

        public VariableType toVariableType() {
            try {
                return VariableType.valueOf(name());
            } catch (Throwable ignored) {
                return VariableType.STRING;
            }
        }
    }

    @Override
    public String toString() {
        return "StringNumber{" +
                "type=" + type +
                ", number=" + number +
                ", source='" + source + '\'' +
                '}';
    }
}
