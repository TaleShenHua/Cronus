package ink.ptms.cronus.service.selector;

import ink.ptms.cronus.service.selector.impl.Selector;

/**
 * @Author 坏黑
 * @Since 2019-08-09 13:10
 */
public class EntityCache {

    private String fullyName;
    private Selector selector;

    public EntityCache(String fullyName, Selector selector) {
        this.fullyName = fullyName;
        this.selector = selector;
    }

    public String getFullyName() {
        return fullyName;
    }

    public Selector getSelector() {
        return selector;
    }
}
