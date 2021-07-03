package io.github.apace100.originsclasses.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CommonEvents {

    public static void setup() {
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::modifyMiningSpeed);
    }

    private static void modifyMiningSpeed(PlayerEvent.BreakSpeed e) {
        float originalSpeed = e.getOriginalSpeed();
        ItemStack stack = e.getPlayer().getHeldItemMainhand();
        if(stack.hasTag() && stack.getTag().contains("MiningSpeedMultiplier")) {
            e.setNewSpeed(originalSpeed * stack.getTag().getFloat("MiningSpeedMultiplier"));
        }
    }
}
