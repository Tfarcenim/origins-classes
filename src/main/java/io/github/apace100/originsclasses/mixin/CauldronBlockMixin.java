package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.stream.Collectors;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {


    //todo event

    @Shadow public abstract void setWaterLevel(World worldIn, BlockPos pos, BlockState state, int level);

    @Inject(method = "onBlockActivated", at = @At(value = "RETURN", ordinal = 9), cancellable = true)
    private void extendPotionDuration(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, CallbackInfoReturnable<ActionResultType> cir) {
        if(ClassPowerTypes.LONGER_POTIONS.isActive(player)) {
            ItemStack stack = player.getHeldItem(hand);
            int level = state.get(CauldronBlock.LEVEL);
            if(stack.getItem() instanceof PotionItem && level > 0 && (!stack.hasTag() || !stack.getTag().getBoolean("IsExtendedByCleric"))) {
                ItemStack extended = new ItemStack(stack.getItem());
                CompoundNBT tag = extended.getOrCreateTag().merge(stack.getTag());
                tag.putString("OriginalName", ITextComponent.Serializer.toJson(stack.getDisplayName()));
                tag.putBoolean("IsExtendedByCleric", true);
                PotionUtils.addPotionToItemStack(extended, Potions.EMPTY);
                Collection<EffectInstance> customPotion = (PotionUtils.getEffectsFromStack(stack).isEmpty() ? PotionUtils.getEffectsFromStack(stack) :
                        PotionUtils.getEffectsFromStack(stack)).stream()
                        .map(effect -> new EffectInstance(effect.getPotion(), effect.getDuration() * (effect.getPotion().isInstant() ? 1 : 2),
                                effect.getAmplifier(), effect.isAmbient(), effect.doesShowParticles(), effect.isShowIcon())).collect(Collectors.toList());;
                PotionUtils.appendEffects(extended, customPotion);
                tag.putInt("CustomPotionColor", PotionUtils.getPotionColorFromEffectList(customPotion));
                setWaterLevel(world, pos, state, level - 1);
                stack.shrink(1);
                player.addItemStackToInventory(extended);
                cir.setReturnValue(ActionResultType.SUCCESS);
            }
        }
    }
}
