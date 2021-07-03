package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.networking.ModPackets;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
    @Inject(method = "displayMerchantGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/merchant/villager/VillagerEntity;openMerchantContainer(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/text/ITextComponent;I)V", shift = At.Shift.AFTER))
    private void sendTraderType(PlayerEntity customer, CallbackInfo ci) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBoolean(false);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(customer, ModPackets.TRADER_TYPE, data);
    }
}
