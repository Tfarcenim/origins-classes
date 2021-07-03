package io.github.apace100.originsclasses.mixin;

import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BreedGoal.class)
public class AnimalMateGoalMixin {
    @Shadow @Final protected AnimalEntity animal;

    @Shadow protected AnimalEntity targetMate;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/BreedGoal;spawnBaby()V"))
    private void produceAdditionalBaby(CallbackInfo ci) {
        if(ClassPowerTypes.TWIN_BREEDING.isActive(this.animal.getLoveCause())) {
            if(new Random().nextInt(5) == 0) {
                animal.spawnBabyAnimal((ServerWorld)animal.world, this.targetMate);
            }
        }
    }
}
