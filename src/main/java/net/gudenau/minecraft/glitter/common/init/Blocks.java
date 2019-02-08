package net.gudenau.minecraft.glitter.common.init;

import net.gudenau.minecraft.glitter.common.block.GlitterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {
    public static final GlitterBlock GLITTER = register("glitter", new GlitterBlock(Block.Settings.of(Material.STONE, MaterialColor.RED).strength(5.0F, 1200.0F)));

    private static <T extends Block> T register(String name, T block){
        Identifier identifier = new Identifier("gud_glitter", name);
        return Registry.register(Registry.BLOCK, identifier, block);
    }

    public static void init() {}
}
