package io.github.apace100.originsclasses.events;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class ClientEvents {

    public static void setup() {
        MinecraftForge.EVENT_BUS.addListener(ClientEvents::tooltips);
    }

    private static void tooltips(ItemTooltipEvent e) {
        ItemStack stack = e.getItemStack();
        List<ITextComponent> tooltip = e.getToolTip();
        if (!stack.isEmpty()) {
            CompoundNBT tag = stack.getTag();
            if (tag != null) {
                if (tag.contains("FoodBonus")) {
                    int bonus = tag.getInt("FoodBonus");
                    tooltip.add(new TranslationTextComponent("origins-classes.food_bonus", bonus).mergeStyle(TextFormatting.GRAY));
                }
                if (tag.contains("MiningSpeedMultiplier")) {
                    int bonusInt = Math.round((tag.getFloat("MiningSpeedMultiplier") - 1F) * 100);
                    String bonus = bonusInt > 0 ? ("+" + bonusInt + "%") : (bonusInt + "%");
                    tooltip.add(new TranslationTextComponent("origins-classes.mining_speed_bonus", bonus).mergeStyle(TextFormatting.BLUE));
                }
                if (tag.contains("IsExtendedByCleric")) {
                    tooltip.add(new TranslationTextComponent("origins-classes.longer_potions").mergeStyle(TextFormatting.GOLD));
                }
            }
        }
    }
}
