package io.github.apace100.originsclasses.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.WorkbenchContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorkbenchContainer.class)
public interface CraftingScreenHandlerAccessor {

    @Accessor
    PlayerEntity getPlayer();
}
