package com.enderdevelopment.totemgrader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class SemiGradeTableBlock extends Block {
    public SemiGradeTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(createFactory());
        }
        return ActionResult.SUCCESS;
    }

    private NamedScreenHandlerFactory createFactory() {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new UpgradeScreenHandler(syncId, inventory), Text.translatable("container.totemgrader.semi_grade_table"));
    }
}
