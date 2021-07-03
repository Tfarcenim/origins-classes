package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractFurnaceTileEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableTileEntity {

    private static PlayerEntity playerTakingStacks;

    protected AbstractFurnaceBlockEntityMixin(TileEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(method = "unlockRecipes", at = @At("HEAD"))
    private void savePlayerForLater(PlayerEntity player, CallbackInfo ci) {
        if(getType() == TileEntityType.SMOKER) {
            playerTakingStacks = player;
        }
    }

    @Redirect(method = "func_235642_a_", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/AbstractCookingRecipe;getExperience()F"))
    private static float modifyExperienceGain(AbstractCookingRecipe abstractCookingRecipe) {
        float regularXp = abstractCookingRecipe.getExperience();
        if(playerTakingStacks != null) {
            if(ClassPowerTypes.MORE_SMOKER_XP.isActive(playerTakingStacks)) {
                return regularXp * 2F;
            }
        }
        return regularXp;
    }

    @Inject(method = "grantStoredRecipeExperience", at = @At("TAIL"))
    private void resetPlayerTakingStacks(World world, Vector3d vec3d, CallbackInfoReturnable<List<IRecipe<?>>> cir) {
        playerTakingStacks = null;
    }
}
