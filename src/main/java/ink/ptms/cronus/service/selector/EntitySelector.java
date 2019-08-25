package ink.ptms.cronus.service.selector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.event.CronusReloadServiceEvent;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.service.selector.impl.Selector;
import ink.ptms.cronus.service.selector.impl.SelectorCitizens;
import ink.ptms.cronus.service.selector.impl.SelectorMythicMobs;
import ink.ptms.cronus.service.selector.impl.SelectorShopkeepers;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.module.lite.SimpleI18n;
import io.izzel.taboolib.util.ArrayUtil;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-10 16:36
 */
@Auto
public class EntitySelector implements Service {

    private List<Selector> selectors = Lists.newArrayList();
    private Map<String, EntityCache> entityCache = Maps.newHashMap();
    private Map<String, ink.ptms.cronus.internal.bukkit.Entity> entityMap = Maps.newHashMap();

    @Override
    public void init() {
        selectors.add(new SelectorCitizens());
        selectors.add(new SelectorMythicMobs());
        selectors.add(new SelectorShopkeepers());
        selectors.forEach(Selector::init);
        CronusReloadServiceEvent.call(this);
    }

    @Override
    public void cancel() {
    }

    public boolean isSelect(Entity entity, String in) {
        if (in == null) {
            return false;
        }
        EntityCache entityCache = this.entityCache.get(in);
        if (entityCache != null) {
            return entityCache.getSelector().isHooked() && entityCache.getSelector().isSelect(entity, entityCache.getFullyName());
        }
        String[] v = in.split("=");
        if (v.length > 1) {
            for (Selector selector : selectors) {
                if (selector.match(v[0])) {
                    entityCache = this.entityCache.computeIfAbsent(in, i -> new EntityCache(ArrayUtil.arrayJoin(v, 1), selector));
                    return entityCache.getSelector().isHooked() && entityCache.getSelector().isSelect(entity, entityCache.getFullyName());
                }
            }
        }
        return entityMap.computeIfAbsent(in, i -> BukkitParser.toEntity(in)).isSelect(entity);
    }

    public String getSelectDisplay(String in) {
        String[] v = in.split("=");
        if (v.length > 1) {
            for (Selector selector : selectors) {
                if (selector.match(v[0])) {
                    return selector.isHooked() ? selector.getDisplay(ArrayUtil.arrayJoin(v, 1)) : "No Hooked";
                }
            }
        }
        return in;
    }

    public String fromEntity(Entity entity) {
        for (Selector selector : selectors) {
            if (selector.isHooked()) {
                String fromEntity = selector.fromEntity(entity);
                if (fromEntity != null) {
                    return selector.getPrefix()[0] + "=" + fromEntity;
                }
            }
        }
        return "type=" + entity.getType() + ",name=" + SimpleI18n.getName(entity);
    }
}
