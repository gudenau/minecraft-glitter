package net.gudenau.minecraft.glitter.common.block;

import net.gudenau.minecraft.glitter.common.block.entity.GlitterBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GlitterBlock extends Block implements BlockEntityProvider {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public GlitterBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext verticalEntityPosition_1) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new GlitterBlockEntity();
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean activate(
            BlockState state,
            World world,
            BlockPos position,
            PlayerEntity player,
            Hand hand,
            BlockHitResult blockHitResult
    ) {
        if (world.isClient) {
            return true;
        }

        GlitterBlockEntity entity = (GlitterBlockEntity) world.getBlockEntity(position);

        if (entity != null) {
            if (player.isSneaking()) {
                entity.pop();
            } else {
                ItemStack stack = player.getStackInHand(hand);
                if (!stack.isEmpty()) {
                    entity.push(stack);
                }
            }
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public void onBlockRemoved(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean boolean_1) {
        if (oldState.getBlock() != newState.getBlock()) {
            BlockEntity rawEntity = world.getBlockEntity(pos);
            if (rawEntity instanceof GlitterBlockEntity) {
                GlitterBlockEntity entity = (GlitterBlockEntity) rawEntity;
                for (ItemStack dye : entity.getDyes()) {
                    ItemScatterer.spawn(
                            world,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            dye
                    );
                }
                if(!entity.getItem().isEmpty()){
                    ItemScatterer.spawn(
                            world,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            entity.getItem()
                    );
                }
            }

            super.onBlockRemoved(oldState, world, pos, newState, boolean_1);
            world.removeBlockEntity(pos);
        }
    }
}
