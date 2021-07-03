package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    private static boolean isFarmer = false;

    @Inject(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;offset(Lnet/minecraft/util/Direction;)Lnet/minecraft/util/math/BlockPos;", shift = At.Shift.AFTER))
    private void saveFarmerForLater(ItemUseContext context, CallbackInfoReturnable<ActionResult> cir) {
        if(context.getPlayer() != null && ClassPowerTypes.BETTER_BONE_MEAL.isActive(context.getPlayer())) {
            isFarmer = true;
        }
    }

    @Inject(method = "onItemUse", at = @At("RETURN"))
    private void removeSavedFarmer(ItemUseContext context, CallbackInfoReturnable<ActionResult> cir) {
        isFarmer = false;
    }

    @Inject(method = "applyBonemeal(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;shrink(I)V"))
    private static void applyAdditionalFarmerBoneMeal(ItemStack stack, World world, BlockPos pos, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if(isFarmer) {
            BlockState blockState = world.getBlockState(pos);
            IGrowable fertilizable = (IGrowable) blockState.getBlock();
            if (fertilizable.canUseBonemeal(world, world.rand, pos, blockState)) {
                fertilizable.grow((ServerWorld)world, world.rand, pos, blockState);
            }
        }
    }
}
