package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.networking.ModPackets;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderEntity.class)
public class WanderingTraderEntityMixin {

    @Inject(method = "getEntityInteractionResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/merchant/villager/WanderingTraderEntity;openMerchantContainer(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/text/ITextComponent;I)V", shift = At.Shift.AFTER))
    private void sendTraderType(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBoolean(true);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ModPackets.TRADER_TYPE, data);
    }
}
