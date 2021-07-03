package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.networking.ModPacketsS2C;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.NPCMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NPCMerchant.class)
public class SimpleMerchantMixin {

    @Shadow @Final private PlayerEntity customer;

    @Redirect(method = "onTrade", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/MerchantOffer;increaseUses()V"))
    private void preventUseClientSide(MerchantOffer tradeOffer) {
        if(ModPacketsS2C.isWanderingTrader || !ClassPowerTypes.TRADE_AVAILABILITY.isActive(customer)) {
            tradeOffer.increaseUses();
        }
    }
}
