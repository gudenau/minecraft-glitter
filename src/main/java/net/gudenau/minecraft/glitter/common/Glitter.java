package net.gudenau.minecraft.glitter.common;

import net.fabricmc.api.ModInitializer;
import net.gudenau.minecraft.glitter.common.init.BlockEntities;
import net.gudenau.minecraft.glitter.common.init.Blocks;
import net.gudenau.minecraft.glitter.common.init.Items;

public class Glitter implements ModInitializer {
    @Override
    public void onInitialize() {
        Blocks.init();
        BlockEntities.init();
        Items.init();
    }
}
