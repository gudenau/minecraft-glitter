package net.gudenau.minecraft.glitter.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.ItemDynamicRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resource.ResourceReloadListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements ResourceReloadListener {
    @Shadow @Final
    private TextureManager textureManager;

    @Shadow
    private void renderItemModel(BakedModel bakedModel_1, ItemStack itemStack_1){}

    @Shadow
    public static void renderGlint(TextureManager textureManager_1, Runnable runnable_1, int int_1){}

    @Shadow
    private void renderModelWithTint(BakedModel bakedModel_1, int int_1){}

    /**
     * @author gudenau
     */
    @Overwrite
    public void renderItemAndGlow(ItemStack stack, BakedModel model) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            if (model.isBuiltin()) {
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                ItemDynamicRenderer.INSTANCE.render(stack);
            } else {
                this.renderItemModel(model, stack);
                if (stack.hasEnchantmentGlint()) {
                    renderGlint(this.textureManager, () -> {
                        int color = 0xFF8040CC;
                        CompoundTag tag = stack.getTag();
                        if(tag != null && tag.containsKey("gud")){
                            CompoundTag gudTag = tag.getCompound("gud");
                            if(gudTag.containsKey("glitter")){
                                color = gudTag.getInt("glitter") | 0xFF000000;
                            }
                        }
                        this.renderModelWithTint(model, color);
                    }, 8);
                }
            }

            GlStateManager.popMatrix();
        }
    }

    /*
    @Shadow
    private void renderModelWithTint(BakedModel bakedModel_1, int int_1){}

    @ModifyArg(
            method = "renderItemAndGlow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGlint(Lnet/minecraft/client/texture/TextureManager;Ljava/lang/Runnable;I)V"
            ),
            index = 1
    )
    private Runnable renderGlintColor(TextureManager textureManager, Runnable runnable, int scale){
        return ()->{
            int color = 0xFF8040CC;
            CompoundTag tag = stack.getTag();
            if(tag != null && tag.containsKey("gud")){
                CompoundTag gudTag = tag.getCompound("gud");
                if(gudTag.containsKey("glitter")){
                    color = gudTag.getInt("glitter") | 0xFF000000;
                }
            }
            this.renderModelWithTint(model, color);
        };
    }
    */
}
