package com.enderdevelopment.totemgrader;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.resource.featuretoggle.FeatureFlags;

public final class TotemGraderMod implements ModInitializer {
    public static final String MOD_ID = "totemgrader";
    public static final Item TOTEM_GRADER = registerItem("totem_grader", new Item(new Item.Settings()));
    public static final Item GRADED_TOTEM = registerItem("graded_totem", new GradedTotemItem(new Item.Settings().maxCount(1)));
    public static final Block SEMI_GRADE_TABLE = Registry.register(Registries.BLOCK, id("semi_grade_table"), new SemiGradeTableBlock(AbstractBlock.Settings.create().strength(2.5f).requiresTool()));
    public static final Item SEMI_GRADE_TABLE_ITEM = registerItem("semi_grade_table", new BlockItem(SEMI_GRADE_TABLE, new Item.Settings()));
    public static final net.minecraft.screen.ScreenHandlerType<UpgradeScreenHandler> UPGRADE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, id("upgrade"), new net.minecraft.screen.ScreenHandlerType<>(UpgradeScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    @Override
    public void onInitialize() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.getValue().getPath().equals("chests/end_city_treasure")) {
                tableBuilder.pool(net.minecraft.loot.LootPool.builder()
                    .rolls(net.minecraft.loot.provider.number.ConstantLootNumberProvider.create(1))
                    .with(net.minecraft.loot.entry.ItemEntry.builder(TOTEM_GRADER))
                    .conditionally(net.minecraft.loot.condition.RandomChanceLootCondition.builder(0.35f)));
            }
        });
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
