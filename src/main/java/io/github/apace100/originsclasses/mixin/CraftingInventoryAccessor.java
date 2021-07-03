package io.github.apace100.originsclasses.mixin;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CraftingInventory.class)
public interface CraftingInventoryAccessor {

    @Accessor
    Container getEventHandler();
}
