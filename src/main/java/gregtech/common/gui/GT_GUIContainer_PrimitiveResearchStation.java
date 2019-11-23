package gregtech.common.gui;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.gui.GT_GUIContainer_BasicTank;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_GUIContainer_PrimitiveResearchStation extends GT_GUIContainerMetaTile_Machine {

    public GT_GUIContainer_PrimitiveResearchStation(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
       super(new GT_Container_PrimitiveResearchStation(aInventoryPlayer,aTileEntity),"gregtech:textures/gui/basicmachines/PrimitiveResearchStation.png");
    }

    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
