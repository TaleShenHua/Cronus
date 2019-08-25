package ink.ptms.cronus.service.effectitem;

/**
 * @Author 坏黑
 * @Since 2019-07-26 11:31
 */
public enum EffectItemEvent {

    USE, LEFT_CLICK, RIGHT_CLICK, NONE;

    public static EffectItemEvent fromString(String in) {
        try {
            return EffectItemEvent.valueOf(in.toUpperCase());
        } catch (Throwable ignored) {
            return NONE;
        }
    }

}
