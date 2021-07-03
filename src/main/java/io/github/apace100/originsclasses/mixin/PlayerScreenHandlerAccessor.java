package io.github.apace100.originsclasses.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerContainer.class)
public interface PlayerScreenHandlerAccessor {

    @Accessor
    PlayerEntity getPlayer();
}
