package ink.ptms.cronus.internal.asm;

import io.izzel.taboolib.module.inject.TInject;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-05-30 22:25
 */
public abstract class AsmHandler {

    @TInject(asm = "ink.ptms.cronus.internal.asm.AsmHandlerImpl")
    private static AsmHandler impl;

    abstract public Entity getEntityByEntityId(int id);

    abstract public ItemStack[] getRecipe(Inventory inventory);

    public static AsmHandler getImpl() {
        return impl;
    }
}
