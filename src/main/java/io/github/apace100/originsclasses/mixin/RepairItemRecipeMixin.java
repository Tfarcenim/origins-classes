package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {

    @ModifyConstant(method = "getCraftingResult", constant = @Constant(intValue = 5, ordinal = 0))
    private int doubleRepairDurabilityBonus(int original, CraftingInventory inventory) {
        Container handler = ((CraftingInventoryAccessor)inventory).getEventHandler();
        PlayerEntity player = null;
        if(handler instanceof WorkbenchContainer) {
            player = ((CraftingScreenHandlerAccessor)handler).getPlayer();
        } else if(handler instanceof PlayerContainer) {
            player = ((PlayerScreenHandlerAccessor)handler).getPlayer();
        }
        if(player != null && ClassPowerTypes.EFFICIENT_REPAIRS.isActive(player)) {
            return original * 3;
        }
        return original;
    }
}
