package io.github.apace100.originsclasses.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodStats.class)
public abstract class HungerManagerMixin {

    @Shadow public abstract void addStats(int foodLevelIn, float foodSaturationModifier);

    @Inject(method = "consume", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FoodStats;addStats(IF)V", shift = At.Shift.AFTER))
    private void addFoodBonus(Item item, ItemStack stack, CallbackInfo ci) {
        if(stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            if(tag.contains("FoodBonus")) {
                int foodBonus = tag.getInt("FoodBonus");
                float saturationBonus = (float)foodBonus * 0.2F;
                this.addStats(foodBonus, saturationBonus);
            }
        }
    }
}
