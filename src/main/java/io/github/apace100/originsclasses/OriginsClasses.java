package io.github.apace100.originsclasses;

import io.github.apace100.originsclasses.condition.ClassesBlockConditions;
import io.github.apace100.originsclasses.effect.StealthEffect;
import io.github.apace100.originsclasses.events.ClientEvents;
import io.github.apace100.originsclasses.events.CommonEvents;
import io.github.apace100.originsclasses.networking.ModPacketsS2C;
import io.github.apace100.originsclasses.power.ClassesPowerFactories;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(OriginsClasses.MODID)
public class OriginsClasses {

	public static final String MODID = "origins-classes";

	// Mixin Save States :) Very useful, not hacky :)
	public static boolean isClericEnchanting;

	public OriginsClasses() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::effects);
		bus.addListener(this::common);
		ClassesPowerFactories.register();
		ClassesBlockConditions.register();
		CommonEvents.setup();
		if (FMLEnvironment.dist.isClient()) {
			ClientEvents.setup();
		}
	}

	private void common(FMLCommonSetupEvent e) {
		ModPacketsS2C.register();
	}

	private void effects(RegistryEvent.Register<Effect> e) {
		e.getRegistry().register(StealthEffect.INSTANCE.setRegistryName( new ResourceLocation(MODID, "stealth")));
	}
}
