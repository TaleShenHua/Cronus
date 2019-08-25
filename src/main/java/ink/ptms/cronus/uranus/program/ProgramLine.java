package ink.ptms.cronus.uranus.program;

import ink.ptms.cronus.uranus.program.effect.Effect;

/**
 * @Author 坏黑
 * @Since 2019-05-11 11:23
 */
public class ProgramLine {

    private ProgramLine next;
    private Effect effect;

    public ProgramLine(Effect effect) {
        this.effect = effect;
    }

    public boolean hasNext() {
        return next != null;
    }

    public ProgramLine getNext() {
        return next;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setNext(ProgramLine next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "ProgramLine{" +
                "next=" + next +
                ", effect=" + effect +
                '}';
    }
}
