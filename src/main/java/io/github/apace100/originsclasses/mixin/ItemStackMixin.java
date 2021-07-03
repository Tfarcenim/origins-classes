package io.github.apace100.originsclasses.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	
	@Shadow public abstract boolean hasTag();
	
	@Shadow public abstract CompoundNBT getTag();
	
	@Inject(method = "getDisplayName", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
	private void getExtendedName(CallbackInfoReturnable<ITextComponent> cir) {
		if (hasTag() && getTag().contains("OriginalName")) {
			cir.setReturnValue(ITextComponent.Serializer.getComponentFromJson(getTag().getString("OriginalName")));
		}
	}
}
