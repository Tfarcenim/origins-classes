package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    @Inject(method = "dropLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generate(Lnet/minecraft/loot/LootContext;)Ljava/util/List;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void dropAdditionalRancherLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci, ResourceLocation identifier, LootTable lootTable, LootContext.Builder builder) {
        if(causedByPlayer && (Object)this instanceof AnimalEntity && ClassPowerTypes.MORE_ANIMAL_LOOT.isActive(source.getTrueSource())) {
            if(new Random().nextInt(10) < 3) {
                lootTable.generate(builder.build(LootParameterSets.ENTITY), ((LivingEntity)(Object)this)::entityDropItem);
            }
        }
    }
    
    @Inject(method = "addPotionEffect", at = @At("RETURN"))
    private void addStatusEffect(EffectInstance effect, CallbackInfoReturnable<Boolean> ci) {
        if (ci.getReturnValue() && !effect.isAmbient()) {
            if(ClassPowerTypes.TAMED_POTION_DIFFUSAL.isActive(this)) {
                world.getEntitiesWithinAABB(TameableEntity.class, getBoundingBox().grow(8F, 2F, 8F).grow(-8f, -2F, -8F),
                        e -> e.getOwner() == (Object) this).forEach(e -> e.addPotionEffect(effect));
            }
        }
    }
}
