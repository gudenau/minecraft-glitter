package net.gudenau.minecraft.glitter.common.block;

import net.gudenau.minecraft.glitter.common.block.entity.GlitterBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
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
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, VerticalEntityPosition verticalEntityPosition_1) {
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
        if(world.isClient){
            return true;
        }

        GlitterBlockEntity entity = (GlitterBlockEntity) world.getBlockEntity(position);

        if(entity != null) {
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
}
