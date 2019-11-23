package gregtech.common.covers;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_Cover_Vent
        extends GT_CoverBehavior {
    private final int mEfficiency;
    private boolean isPumpMpde =false;

    public GT_Cover_Vent(int aEfficiency) {
        this.mEfficiency = aEfficiency;
    }

    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if(isPumpMpde){
            if(aTileEntity instanceof IGregTechTileEntity&&aTimer%10==0){
                IGregTechTileEntity tTileEntity = (IGregTechTileEntity)aTileEntity;
                tTileEntity.fill(ForgeDirection.getOrientation(aSide), Materials.Air.getGas(1000),true);
            }
        }
        else
        if ((aTileEntity instanceof IMachineProgress)) {
            if ((((IMachineProgress) aTileEntity).hasThingsToDo()) && (aCoverVariable != ((IMachineProgress) aTileEntity).getProgress()) &&
                    (!GT_Utility.hasBlockHitBox(aTileEntity.getWorld(), aTileEntity.getOffsetX(aSide, 1), aTileEntity.getOffsetY(aSide, 1), aTileEntity.getOffsetZ(aSide, 1)))) {
                ((IMachineProgress) aTileEntity).increaseProgress(this.mEfficiency);
            }
            return ((IMachineProgress) aTileEntity).getProgress();
        }
        return 0;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        isPumpMpde = !isPumpMpde;
        if(isPumpMpde){
            GT_Utility.sendChatToPlayer(aPlayer,"Enabled Pump Mode");
        }
        else{
            GT_Utility.sendChatToPlayer(aPlayer,"Enabled Efficiency-Increace Mode");
        }
        return super.onCoverScrewdriverclick(aSide, aCoverID, aCoverVariable, aTileEntity, aPlayer, aX, aY, aZ);
    }

    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 100;
    }
}
