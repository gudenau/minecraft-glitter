package net.gudenau.minecraft.glitter.client.block.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gudenau.minecraft.glitter.common.block.entity.GlitterBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;

import java.util.List;

@Environment(EnvType.CLIENT)
public class GlitterBlockEntityRenderer extends BlockEntityRenderer<GlitterBlockEntity> {
    @Override
    public void render(
            GlitterBlockEntity entity,
            double x,
            double y,
            double z,
            float delta,
            int breakProgress
    ) {
        super.render(entity, x, y, z, delta, breakProgress);

        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        if(entity != null){
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.5, y + 1, z + 0.5);

            double angle = (System.currentTimeMillis() / 50.0) % 360;

            { // Render item
                ItemStack item = entity.getItem();

                if(!item.isEmpty()){
                    GlStateManager.pushMatrix();
                    GlStateManager.rotated(45 - angle * 2, 0, 1, 0);
                    GlStateManager.scaled(0.25, 0.25, 0.25);
                    itemRenderer.renderItem(item, ModelTransformation.Type.FIXED);
                    GlStateManager.popMatrix();
                }
            }

            { // Render dyes
                List<ItemStack> dyes = entity.getDyes();
                GlStateManager.pushMatrix();
                GlStateManager.rotated(angle, 0, 1, 0);
                double progress = (100 - entity.getProgress()) / 100.0;
                GlStateManager.translated(0, -0.19 * progress, 0);
                GlStateManager.scaled(0.14, 0.14, 0.14);

                int i = 0;
                double rads = Math.toRadians(360d / dyes.size());
                double r = 3 * progress;
                for (ItemStack stack : dyes) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translated(
                            Math.cos(rads * i) * r,
                            0,
                            Math.sin(rads * i) * r
                    );
                    GlStateManager.rotated(angle, 0, 1, 0);
                    itemRenderer.renderItem(stack, ModelTransformation.Type.FIXED);
                    GlStateManager.popMatrix();
                    i++;
                }
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
        }
    }
}
