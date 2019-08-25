package ink.ptms.cronus.internal.asm;

import com.google.common.collect.Lists;
import io.izzel.taboolib.Version;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftInventoryMerchant;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-30 22:26
 */
public class AsmHandlerImpl extends AsmHandler {

    @Override
    public Entity getEntityByEntityId(int id) {
        if (Version.isAfter(Version.v1_14)) {
            for (World world : Bukkit.getWorlds()) {
                Object entity = ((org.bukkit.craftbukkit.v1_14_R1.CraftWorld) world).getHandle().getEntity(id);
                return entity != null ? ((net.minecraft.server.v1_14_R1.Entity) entity).getBukkitEntity() : null;
            }
        }
        for (World world : Bukkit.getWorlds()) {
            for (Object entity : Lists.newArrayList(((CraftWorld) world).getHandle().entityList)) {
                if (((net.minecraft.server.v1_13_R2.Entity) entity).getId() == id) {
                    return ((net.minecraft.server.v1_13_R2.Entity) entity).getBukkitEntity();
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack[] getRecipe(Inventory inventory) {
        Object selectedRecipe;
        try {
            selectedRecipe = ((CraftInventoryMerchant) inventory).getSelectedRecipe();
        } catch (Throwable ignored) {
            return null;
        }
        Object ingredients = ((MerchantRecipe) selectedRecipe).getIngredients();
        return new ItemStack[] {
                ((ItemStack) ((List) ingredients).get(0)).clone(),
                ((List) ingredients).size() > 1 ? ((ItemStack) ((List) ingredients).get(1)).clone() : null,
                (((MerchantRecipe) selectedRecipe).getResult()).clone()
        };
    }
}
