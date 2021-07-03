package io.github.apace100.originsclasses.mixin;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import io.github.apace100.originsclasses.power.CraftAmountPower;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorkbenchContainer.class)
public class CraftingScreenHandlerMixin {

    //todo, event?
    @Inject(method = "updateCraftingResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftResultInventory;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void modifyCraftingResult(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftResultInventory resultInventory, CallbackInfo ci, ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        if(itemStack.getItem().isFood() && ClassPowerTypes.BETTER_CRAFTED_FOOD.isActive(player)) {
            Food food = itemStack.getItem().getFood();
            int foodBonus = (int)Math.ceil((float)food.getHealing() / 3F);
            if(foodBonus < 1) {
                foodBonus = 1;
            }
            itemStack.getOrCreateTag().putInt("FoodBonus", foodBonus);
        }
        if(ClassPowerTypes.QUALITY_EQUIPMENT.isActive(player) && isEquipment(itemStack)) {
            addQualityAttribute(itemStack);
        }
        /*if(ClassPowerTypes.MORE_PLANKS_FROM_LOGS.isActive(player)) {
            if(itemStack.getItem().isIn(ItemTags.PLANKS) && itemStack.getCount() == 4) {
                itemStack.setCount(6);
            }
        }*/
        int baseValue = itemStack.getCount();
        int newValue = (int) OriginComponent.modify(player, CraftAmountPower.class, baseValue, (p -> p.doesApply(itemStack)));
        if(newValue != baseValue) {
            itemStack.setCount(newValue < 0 ? 0 : Math.min(newValue, itemStack.getMaxStackSize()));
        }
    }

    private static void addQualityAttribute(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof ArmorItem) {
            EquipmentSlotType slot = ((ArmorItem)item).getEquipmentSlot();
            stack.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Blacksmith quality", 0.25D, AttributeModifier.Operation.ADDITION), slot);
        } else if(item instanceof SwordItem || item instanceof ShootableItem) {
            stack.addAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier("Blacksmith quality", 0.5D, AttributeModifier.Operation.ADDITION), EquipmentSlotType.MAINHAND);
        } else if(item instanceof ToolItem || item instanceof ShearsItem) {
            stack.getOrCreateTag().putFloat("MiningSpeedMultiplier", 1.05F);
        } else if(item instanceof ShieldItem) {
            stack.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("Blacksmith quality", 0.1D, AttributeModifier.Operation.ADDITION), EquipmentSlotType.OFFHAND);
        }
    }

    private static boolean isEquipment(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof ArmorItem)
            return true;
        if(item instanceof ToolItem)
            return true;
        if(item instanceof ShootableItem)
            return true;
        return item instanceof ShieldItem;
    }
}
