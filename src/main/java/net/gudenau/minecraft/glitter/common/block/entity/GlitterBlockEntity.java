package net.gudenau.minecraft.glitter.common.block.entity;

import net.fabricmc.fabric.block.entity.ClientSerializable;
import net.gudenau.minecraft.glitter.common.init.BlockEntities;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateClientPacket;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Tickable;

import java.util.LinkedList;
import java.util.List;

public class GlitterBlockEntity extends BlockEntity implements Tickable, ClientSerializable {
    private List<ItemStack> dyes = new LinkedList<>();
    private ItemStack item = ItemStack.EMPTY;
    private ItemStack result = ItemStack.EMPTY;
    private int progress;

    public GlitterBlockEntity() {
        super(BlockEntities.GLITTER);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        dyes.clear();
        deserialize(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        serialize(tag);

        return tag;
    }

    private void serialize(CompoundTag tag) {
        ListTag tagList = new ListTag();
        for(ItemStack stack : dyes){
            tagList.add(stack.toTag(new CompoundTag()));
        }
        tag.put("dyes", tagList);
        tag.put("item", item.toTag(new CompoundTag()));
        tag.put("result", result.toTag(new CompoundTag()));
        tag.putInt("progress", progress);
    }

    private void deserialize(CompoundTag tag) {
        ListTag tagList = tag.getList("dyes", 10);
        for(int i = 0; i < tagList.size(); ++i) {
            dyes.add(ItemStack.fromTag(tagList.getCompoundTag(i)));
        }
        item = ItemStack.fromTag(tag.getCompound("item"));
        result = ItemStack.fromTag(tag.getCompound("result"));
        progress = tag.getInt("progress");
    }

    public void pop() {
        if(!result.isEmpty()){
            spawn(result);
            result = ItemStack.EMPTY;
            markDirty();
            return;
        }

        if(!item.isEmpty()){
            return;
        }

        if(!dyes.isEmpty()){
            spawn(dyes.remove(dyes.size() - 1));
            markDirty();
        }

        System.out.println("Current dyes:");
        for(ItemStack stack : dyes){
            if(stack.isEmpty()){
                break;
            }
            System.out.printf("    %s\n", stack.toString());
        }
    }

    private void spawn(ItemStack stack) {
        ItemEntity entity = new ItemEntity(
                world,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                stack
        );
        entity.addVelocity(0, 0.1, 0);
        world.spawnEntity(entity);
    }

    public void push(ItemStack stack) {
        if(!result.isEmpty()){
            spawn(result);
            result = ItemStack.EMPTY;
            markDirty();
            return;
        }

        if(!item.isEmpty()){
            return;
        }

        Item item = stack.getItem();
        if(stack.hasEnchantmentGlint() && !dyes.isEmpty()){
            this.item = stack.copy();
            stack.setAmount(0);
            markDirty();
        }else if(item instanceof DyeItem){
            ItemStack ourStack = stack.copy();
            stack.subtractAmount(1);

            ourStack.setAmount(1);
            dyes.add(ourStack);
            markDirty();
        }
    }

/*    @Override
    public void markDirty() {
        super.markDirty();
        world.fireWorldEvent(1035, pos, 0);
    }*/

    public List<ItemStack> getDyes() {
        return dyes;
    }

//    @Override
//    public BlockEntityUpdateClientPacket toUpdatePacket() {
//        return new BlockEntityUpdateClientPacket(pos, 9, toInitialChunkDataTag());
//    }
//
//    @Override
//    public CompoundTag toInitialChunkDataTag() {
//        return this.toTag(new CompoundTag());
//    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void tick() {
        if(!item.isEmpty()){
            if(progress == 100){
                float red = 0;
                float green = 0;
                float blue = 0;

                for(ItemStack dye : dyes){
                    float[] colors = ((DyeItem)dye.getItem()).getColor().getColorComponents();
                    red += colors[0];
                    green += colors[1];
                    blue += colors[2];
                }

                red /= dyes.size();
                green /= dyes.size();
                blue /= dyes.size();

                red *= 255;
                green *= 255;
                blue *= 255;

                int color = (((int)red) << 16) | (((int)green) << 8) | ((int)blue);

                CompoundTag tag = item.getTag();
                if(tag == null){
                    tag = new CompoundTag();
                    item.setTag(tag);
                }

                CompoundTag gudTag;
                if(tag.containsKey("gud")){
                    gudTag = tag.getCompound("gud");
                }else{
                    gudTag = new CompoundTag();
                    tag.put("gud", gudTag);
                }

                System.out.println(Integer.toHexString(color));
                gudTag.putInt("glitter", color);

                result = item;
                item = ItemStack.EMPTY;
                dyes.clear();
                progress = 0;
            }else {
                progress++;
            }
            markDirty();
        }
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(new CompoundTag());
    }

    public ItemStack getResult() {
        return result;
    }
}
