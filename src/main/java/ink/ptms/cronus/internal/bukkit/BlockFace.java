package ink.ptms.cronus.internal.bukkit;

/**
 * @Author 坏黑
 * @Since 2019-06-07 20:11
 */
public class BlockFace extends EnumEntry<org.bukkit.block.BlockFace> {

    public BlockFace(String in) {
        super(in);
    }

    @Override
    public Class origin() {
        return org.bukkit.block.BlockFace.class;
    }

    @Override
    public String toString() {
        return "BlockFace{" +
                "data=" + data +
                '}';
    }
}
