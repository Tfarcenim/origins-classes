package io.github.apace100.originsclasses.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.apace100.originsclasses.power.ClassPowerTypes;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @ModifyVariable(method = "renderName", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/matrix/MatrixStack;push()V"), ordinal = 0)
    private boolean modifyUnsneakyState(boolean original, Entity entity, ITextComponent text, MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light) {
        if(ClassPowerTypes.SNEAKY.isActive(entity)) {
            return false;
        }
        return original;
    }
}
