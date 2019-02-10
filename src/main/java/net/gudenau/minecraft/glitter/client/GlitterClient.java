package net.gudenau.minecraft.glitter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.gudenau.minecraft.glitter.client.block.renderer.GlitterBlockEntityRenderer;
import net.gudenau.minecraft.glitter.common.block.entity.GlitterBlockEntity;

@Environment(EnvType.CLIENT)
public class GlitterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(
                GlitterBlockEntity.class,
                new GlitterBlockEntityRenderer()
        );
    }
}
