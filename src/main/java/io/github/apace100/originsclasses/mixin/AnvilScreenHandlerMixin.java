package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.util.IWorldPosCallable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RepairContainer.class)
public abstract class AnvilScreenHandlerMixin extends AbstractRepairContainer {

    public AnvilScreenHandlerMixin(ContainerType<?> type, int syncId, PlayerInventory playerInventory, IWorldPosCallable context) {
        super(type, syncId, playerInventory, context);
    }

    @ModifyConstant(method = "updateRepairOutput", constant = @Constant(intValue = 4, ordinal = 0))
    private int halfRepairMaterialCost(int original) {
        if(ClassPowerTypes.EFFICIENT_REPAIRS.isActive(field_234645_f_)) {
            return original / 2;
        }
        return original;
    }

    @ModifyConstant(method = "updateRepairOutput", constant = @Constant(intValue = 12, ordinal = 0))
    private int doubleCombineRepairDurabilityBonus(int original) {
        if(ClassPowerTypes.EFFICIENT_REPAIRS.isActive(field_234645_f_)) {
            return original * 12;
        }
        return original;
    }
}
