package io.github.apace100.originsclasses.condition;

import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.originsclasses.OriginsClasses;
import io.github.apace100.originsclasses.data.ClassesDataTypes;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ClassesBlockConditions {

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new ConditionFactory<>(new ResourceLocation(OriginsClasses.MODID, "material"), new SerializableData()
            .add("material", ClassesDataTypes.MATERIAL),
            (SerializableData.Instance data, CachedBlockInfo block) -> block.getBlockState().getMaterial() == data.get("material")));
    }

    private static void register(ConditionFactory<CachedBlockInfo> conditionFactory) {
        Registry.register(ModRegistries.BLOCK_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
