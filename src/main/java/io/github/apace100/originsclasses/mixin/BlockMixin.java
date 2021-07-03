package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.MelonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Random;


@Mixin(Block.class)
public abstract class BlockMixin {

    @Shadow public static void spawnDrops(BlockState state, World worldIn, BlockPos pos, @Nullable TileEntity tileEntityIn, Entity entityIn, ItemStack stack) {
        throw new RuntimeException("oops");
    }

    @Inject(method = "harvestBlock", at = @At("TAIL"))
    private void dropAdditionalCrops(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if(state.getBlock() instanceof CropsBlock || state.getBlock() instanceof MelonBlock) {
            if(player != null && ClassPowerTypes.MORE_CROP_DROPS.isActive(player) && new Random().nextInt(10) < 3) {
                spawnDrops(state, world, pos, blockEntity, player, stack);
            }
        }
    }

    @ModifyConstant(method = "harvestBlock", constant = @Constant(floatValue = 0.005F))
    private float preventBlockMiningExhaustion(float exhaustion, World world, PlayerEntity playerEntity) {
        if(ClassPowerTypes.NO_MINING_EXHAUSTION.isActive(playerEntity)) {
            return 0F;
        }
        return exhaustion;
    }
}
