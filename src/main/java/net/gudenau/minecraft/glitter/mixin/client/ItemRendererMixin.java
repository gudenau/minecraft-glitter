package net.gudenau.minecraft.glitter.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resource.ResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements ResourceReloadListener {
    private final ThreadLocal<ItemStack> renderStack = new ThreadLocal<>();
    
    @Inject(method = "renderItemAndGlow", at = @At("HEAD"))
    public void renderItemAndGlow(ItemStack stack, BakedModel model, CallbackInfo info){
        renderStack.set(stack);
    }
    
    @ModifyArg(method = "method_4015", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/item/ItemRenderer;renderModelWithTint(Lnet/minecraft/client/render/model/BakedModel;I)V"
    ), index = 1)
    public int itemColor(int originalColor){
        ItemStack stack = renderStack.get();
        int color = originalColor; //0xFF8040CC;
        if(stack != null){
            CompoundTag tag = stack.getTag();
            if(tag != null && tag.containsKey("gud")){
                CompoundTag gudTag = tag.getCompound("gud");
                if(gudTag.containsKey("glitter")){
                    color = gudTag.getInt("glitter") | 0xFF000000;
                }
            }
        }
        return color;
    }
}
