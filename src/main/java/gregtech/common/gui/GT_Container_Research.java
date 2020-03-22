package gregtech.common.gui;

import gregtech.api.enums.ItemList;
import gregtech.api.gui.GT_Container;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Container_Research extends Container {

    public GT_Container_Research(InventoryPlayer aPlayerInventory, ItemStack aDisplayeStack, int slotX, int sloY){
        addSlotToContainer(new GT_Slot_Holo(aPlayerInventory,1,slotX,sloY,false,false,0, aDisplayeStack));


    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
