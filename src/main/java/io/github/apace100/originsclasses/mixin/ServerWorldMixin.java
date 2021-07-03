package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.effect.StealthEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "playEvent", at = @At("HEAD"), cancellable = true)
    private void cancelSyncingStealthEvents(PlayerEntity player, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if(player != null && player.isPotionActive(StealthEffect.INSTANCE)) {
            ci.cancel();
        }
    }
}
