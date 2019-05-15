package net.gudenau.minecraft.glitter.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;

public class Items {
    public static BlockItem GLITTER = register(Blocks.GLITTER);

    private static BlockItem register(Block block){
        BlockItem blockItem = new BlockItem(
                block,
                new Item.Settings().itemGroup(ItemGroup.DECORATIONS)
        );
        blockItem.registerBlockItemMap(Item.BLOCK_ITEM_MAP, blockItem);
        return Registry.register(
                Registry.ITEM,
                Registry.BLOCK.getId(block),
                blockItem
        );
    }

    public static void init() {}
}
