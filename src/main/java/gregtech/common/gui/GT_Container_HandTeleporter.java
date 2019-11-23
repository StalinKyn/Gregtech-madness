package gregtech.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GT_Container_HandTeleporter
        extends Container {
    ItemStack telepoter;

    public GT_Container_HandTeleporter(InventoryPlayer inventoryPlayer) {
        telepoter = inventoryPlayer.getCurrentItem();
    }

    @Override
    public boolean canInteractWith(EntityPlayer p) {
        return (telepoter != null) && (telepoter.stackSize > 0);
    }
}
