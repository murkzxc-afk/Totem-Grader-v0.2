package com.enderdevelopment.totemgrader;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public final class UpgradeScreenHandler extends ScreenHandler {
    private final Inventory ingredients;
    private boolean updatingResult;

    public UpgradeScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4));
    }

    public UpgradeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(TotemGraderMod.UPGRADE_SCREEN_HANDLER, syncId);
        checkSize(inventory, 4);
        this.ingredients = inventory;
        inventory.onOpen(playerInventory.player);

        addSlot(new Slot(inventory, 0, 27, 35) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.isOf(net.minecraft.item.Items.TOTEM_OF_UNDYING) || stack.isOf(TotemGraderMod.GRADED_TOTEM);
            }
        });
        addSlot(new Slot(inventory, 1, 71, 35) {
            @Override public boolean canInsert(ItemStack stack) { return stack.isOf(TotemGraderMod.TOTEM_GRADER); }
        });
        addSlot(new Slot(inventory, 2, 115, 35) {
            @Override public boolean canInsert(ItemStack stack) { return stack.isOf(net.minecraft.item.Items.NETHERITE_INGOT); }
        });
        addSlot(new Slot(inventory, 3, 152, 35) {
            @Override public boolean canInsert(ItemStack stack) { return false; }
            @Override public boolean canTakeItems(PlayerEntity player) { return hasValidRecipe(); }
            @Override public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                finishUpgrade();
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        updateResult();
    }

    private void addPlayerInventory(PlayerInventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory inventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }

    private boolean hasValidRecipe() {
        ItemStack base = ingredients.getStack(0);
        ItemStack grader = ingredients.getStack(1);
        ItemStack netherite = ingredients.getStack(2);
        return !base.isEmpty() && !grader.isEmpty() && !netherite.isEmpty()
            && grader.isOf(TotemGraderMod.TOTEM_GRADER)
            && netherite.isOf(net.minecraft.item.Items.NETHERITE_INGOT)
            && (base.isOf(net.minecraft.item.Items.TOTEM_OF_UNDYING)
                || (base.isOf(TotemGraderMod.GRADED_TOTEM) && GradedTotemItem.uses(base) < 10));
    }

    private void updateResult() {
        if (updatingResult) return;
        updatingResult = true;
        ItemStack result = ItemStack.EMPTY;
        if (hasValidRecipe()) {
            ItemStack base = ingredients.getStack(0);
            int uses = base.isOf(TotemGraderMod.GRADED_TOTEM) ? GradedTotemItem.uses(base) + 1 : 2;
            result = new ItemStack(TotemGraderMod.GRADED_TOTEM);
            GradedTotemItem.setUses(result, uses);
        }
        ingredients.setStack(3, result);
        updatingResult = false;
    }

    private void finishUpgrade() {
        if (!hasValidRecipe()) return;
        ingredients.getStack(0).decrement(1);
        ingredients.getStack(1).decrement(1);
        ingredients.getStack(2).decrement(1);
        updateResult();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        updateResult();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (!slot.hasStack()) return empty;
        ItemStack original = slot.getStack();
        ItemStack copy = original.copy();
        if (slotIndex == 3) {
            if (!insertItem(original, 4, 40, true)) return empty;
            slot.onTakeItem(player, original);
        } else if (slotIndex < 4) {
            if (!insertItem(original, 4, 40, true)) return empty;
        } else if (original.isOf(net.minecraft.item.Items.TOTEM_OF_UNDYING) || original.isOf(TotemGraderMod.GRADED_TOTEM)) {
            if (!insertItem(original, 0, 1, false)) return empty;
        } else if (original.isOf(TotemGraderMod.TOTEM_GRADER)) {
            if (!insertItem(original, 1, 2, false)) return empty;
        } else if (original.isOf(net.minecraft.item.Items.NETHERITE_INGOT)) {
            if (!insertItem(original, 2, 3, false)) return empty;
        } else {
            return empty;
        }
        if (original.isEmpty()) slot.setStack(ItemStack.EMPTY);
        else slot.markDirty();
        return copy;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return ingredients.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        for (int i = 0; i < 3; i++) {
            ItemStack stack = ingredients.removeStack(i);
            if (!stack.isEmpty()) player.getInventory().offerOrDrop(stack);
        }
    }

    public Text statusText() {
        ItemStack base = ingredients.getStack(0);
        if (base.isOf(TotemGraderMod.GRADED_TOTEM) && GradedTotemItem.uses(base) >= 10) {
            return Text.literal("You hit the limit!");
        }
        return Text.literal("Add totem, grader, and netherite");
    }
}
