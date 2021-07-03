package io.github.apace100.originsclasses.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public abstract class DontOverwriteAttrModsMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundNBT;getList(Ljava/lang/String;I)Lnet/minecraft/nbt/ListNBT;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, method = "getAttributeModifiers")
    private void addAttributeModifiersFromItem(EquipmentSlotType slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> info, Multimap<Attribute, AttributeModifier> multimap) {
        multimap.putAll(((ItemStack)(Object)this).getItem().getAttributeModifiers(slot));
    }
}
