package net.gudenau.minecraft.glitter.common.block.entity;

import net.fabricmc.fabric.block.entity.ClientSerializable;
import net.gudenau.minecraft.glitter.common.init.BlockEntities;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.util.Tickable;

import java.util.LinkedList;
import java.util.List;

public class GlitterBlockEntity extends BlockEntity implements Tickable, ClientSerializable {
    private List<ItemStack> dyes = new LinkedList<>();
    private ItemStack item = ItemStack.EMPTY;
    private int progress;

    public GlitterBlockEntity() {
        super(BlockEntities.GLITTER);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        deserialize(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return serialize(super.toTag(tag));
    }

    private CompoundTag serialize(CompoundTag tag) {
        ListTag tagList = new ListTag();
        for (ItemStack stack : dyes) {
            tagList.add(stack.toTag(new CompoundTag()));
        }
        tag.put("dyes", tagList);
        tag.put("item", item.toTag(new CompoundTag()));
        tag.putInt("progress", progress);
        return tag;
    }

    private void deserialize(CompoundTag tag) {
        dyes.clear();
        ListTag tagList = tag.getList("dyes", 10);
        for (int i = 0; i < tagList.size(); ++i) {
            dyes.add(ItemStack.fromTag(tagList.getCompoundTag(i)));
        }
        item = ItemStack.fromTag(tag.getCompound("item"));
        progress = tag.getInt("progress");
    }

    public void pop() {
        if (!item.isEmpty()) {
            return;
        }

        if (!dyes.isEmpty()) {
            ItemScatterer.spawn(
                    world,
                    pos.getX(),
                    pos.getY() + 0.3125,
                    pos.getZ(),
                    dyes.remove(dyes.size() - 1)
            );
            markDirty();
        }
    }

    public void push(ItemStack stack) {
        if (!item.isEmpty()) {
            return;
        }

        Item item = stack.getItem();
        if (stack.hasEnchantmentGlint() && !dyes.isEmpty()) {
            this.item = stack.copy();
            stack.setAmount(0);
            markDirty();
        } else if (item instanceof DyeItem) {
            ItemStack ourStack = stack.copy();
            stack.subtractAmount(1);

            ourStack.setAmount(1);
            dyes.add(ourStack);
            markDirty();
        }
    }

    public List<ItemStack> getDyes() {
        return dyes;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void tick() {
        if (!item.isEmpty()) {
            if (progress == 100) {
                float red = 0;
                float green = 0;
                float blue = 0;

                for (ItemStack dye : dyes) {
                    float[] colors = ((DyeItem) dye.getItem()).getColor().getColorComponents();
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

                int color = (((int) red) << 16) | (((int) green) << 8) | ((int) blue);

                CompoundTag tag = item.getTag();
                if (tag == null) {
                    tag = new CompoundTag();
                    item.setTag(tag);
                }

                CompoundTag gudTag;
                if (tag.containsKey("gud")) {
                    gudTag = tag.getCompound("gud");
                } else {
                    gudTag = new CompoundTag();
                    tag.put("gud", gudTag);
                }

                gudTag.putInt("glitter", color);

                if(!world.isClient) {
                    ItemScatterer.spawn(
                            world,
                            pos.getX(),
                            pos.getY() + 0.3125,
                            pos.getZ(),
                            item
                    );
                }

                item = ItemStack.EMPTY;
                dyes.clear();
                progress = 0;
                markDirty();
            } else {
                progress++;
                super.markDirty();
            }
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

    @Override
    public void markDirty() {
        super.markDirty();
        world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
    }

    public int getProgress() {
        return progress;
    }
}
