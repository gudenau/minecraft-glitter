package net.gudenau.minecraft.glitter.common.init;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.gudenau.minecraft.glitter.common.block.entity.GlitterBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixers.Schemas;
import net.minecraft.datafixers.TypeReferences;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockEntities {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final BlockEntityType<GlitterBlockEntity> GLITTER = create("glitter", BlockEntityType.Builder.create(GlitterBlockEntity::new));

    private static <T extends BlockEntity> BlockEntityType<T> create(String name, BlockEntityType.Builder<T> builder) {
        return Registry.register(
                Registry.BLOCK_ENTITY,
                new Identifier("gud_glitter", name),
                builder.build(null)
        );
    }

    public static void init(){}
}
