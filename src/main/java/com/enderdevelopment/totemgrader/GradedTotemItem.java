package com.enderdevelopment.totemgrader;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.List;

public final class GradedTotemItem extends Item {
    public static final String USES_KEY = "graded_uses";

    public GradedTotemItem(Settings settings) {
        super(settings);
    }

    public static int uses(ItemStack stack) {
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component == null) return 2;
        return Math.clamp(component.copyNbt().getInt(USES_KEY).orElse(2), 2, 10);
    }

    public static void setUses(ItemStack stack, int uses) {
        uses = Math.clamp(uses, 2, 10);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(itemNbt(uses)));
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.translatable("item.totemgrader.graded_totem").append(" (" + uses + " uses)").formatted(Formatting.RED));
    }

    private static net.minecraft.nbt.NbtCompound itemNbt(int uses) {
        net.minecraft.nbt.NbtCompound nbt = new net.minecraft.nbt.NbtCompound();
        nbt.putInt(USES_KEY, uses);
        return nbt;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item.totemgrader.graded_totem").append(" (" + uses(stack) + " uses)").formatted(Formatting.RED);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, net.minecraft.client.item.TooltipType type) {
        tooltip.add(Text.literal("Uses: " + uses(stack) + "/10").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Progressive red eye shimmer").formatted(Formatting.DARK_RED));
    }
}
