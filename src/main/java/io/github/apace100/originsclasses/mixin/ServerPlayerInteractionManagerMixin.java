package io.github.apace100.originsclasses.mixin;

import io.github.apace100.origins.registry.ModComponents;
import io.github.apace100.originsclasses.ducks.SneakingStateSavingManager;
import io.github.apace100.originsclasses.networking.ModPackets;
import io.github.apace100.originsclasses.power.MultiMinePower;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin implements SneakingStateSavingManager {

    @Shadow public ServerWorld world;
    @Shadow public ServerPlayerEntity player;
    @Shadow public abstract void func_229860_a_(BlockPos pos, CPlayerDiggingPacket.Action action, String reason);

    private BlockState justMinedBlockState;
    private boolean performingMultiMine = false;
    private boolean wasSneakingWhenStarted = false;

    @Inject(method = "func_225416_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onBlockClicked(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V", ordinal = 0))
    private void saveSneakingState(BlockPos pos, CPlayerDiggingPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci) {
        wasSneakingWhenStarted = player.isSneaking();
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBoolean(!wasSneakingWhenStarted);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ModPackets.MULTI_MINING, data);
    }

    @Inject(method = "func_229860_a_", at = @At("HEAD"))
    private void saveBlockStateForMultiMine(BlockPos pos, CPlayerDiggingPacket.Action action, String reason, CallbackInfo ci) {
        justMinedBlockState = world.getBlockState(pos);
    }

    @Inject(method = "func_229860_a_", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/play/ServerPlayNetHandler;sendPacket(Lnet/minecraft/network/IPacket;)V", ordinal = 0))
    private void multiMinePower(BlockPos pos, CPlayerDiggingPacket.Action action, String reason, CallbackInfo ci) {
        if(!wasSneakingWhenStarted && !performingMultiMine) {
            performingMultiMine = true;
            ModComponents.ORIGIN.get(player).getPowers(MultiMinePower.class).forEach(mmp -> {
                if(mmp.isBlockStateAffected(justMinedBlockState)) {
                    ItemStack tool = player.getHeldItemMainhand().copy();
                    for(BlockPos bp : mmp.getAffectedBlocks(justMinedBlockState, pos)) {
                        func_229860_a_(bp, action, reason);
                        if(!player.getHeldItemMainhand().isItemEqualIgnoreDurability(tool)) {
                            break;
                        }
                    }
                }
            });
            performingMultiMine = false;
        }
    }

    public boolean wasSneakingWhenBlockBreakingStarted() {
        return wasSneakingWhenStarted;
    }
}
