package ink.ptms.cronus.internal.program;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:15
 */
public enum Action {

    ACCEPT, ACCEPT_FAIL, SUCCESS, FAILURE, COOLDOWN, RESTART, NEXT;

    public static Action fromName(String name) {
        try {
            return valueOf(name.toUpperCase().replace("-", "_"));
        } catch (Throwable ignored) {
        }
        return null;
    }
}
