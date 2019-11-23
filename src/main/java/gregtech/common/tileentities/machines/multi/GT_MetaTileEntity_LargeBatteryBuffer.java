package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine.isValidForLowGravity;

public class GT_MetaTileEntity_LargeBatteryBuffer extends GT_MetaTileEntity_MultiBlockBase {

    public int mMaxAmperrage = 64;
    public long mEUStored = 0;
    public long mMaxEUStorage = 9000000000000000L;

    public GT_MetaTileEntity_LargeBatteryBuffer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeBatteryBuffer(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeBatteryBuffer(this.mName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Advanced Processing Array"};
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[] {
                    Textures.BlockIcons.casingTexturePages[1][50],
                    new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE
                            : Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[1][50] };
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "AdvancedProcessingArray.png");
    }


    public boolean isCorrectMachinePart(ItemStack aStack) {
            return true;

    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        int tElapsedAmpers = mMaxAmperrage;
        for(GT_MetaTileEntity_Hatch_Energy aHatch: mEnergyHatches){
            if(tElapsedAmpers<=0)
                continue;
            if((mMaxEUStorage>(mEUStored+tElapsedAmpers>=aHatch.maxAmperesIn()?aHatch.getMaxInput():aHatch.maxEUInput()*tElapsedAmpers))&&aHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tElapsedAmpers>=aHatch.maxAmperesIn()?aHatch.getMaxInput():aHatch.maxEUInput()*tElapsedAmpers, false)){
               // System.out.println("inputing "+(tElapsedAmpers>=aHatch.maxAmperesIn()?aHatch.maxAmperesIn():tElapsedAmpers)+" ampers "+(tElapsedAmpers>=aHatch.maxAmperesIn()?aHatch.getMaxInput():aHatch.maxEUInput()*tElapsedAmpers+"EU"));
                mEUStored += tElapsedAmpers>=aHatch.maxAmperesIn()?aHatch.getMaxInput():aHatch.maxEUInput()*tElapsedAmpers;
                tElapsedAmpers -= tElapsedAmpers>=aHatch.maxAmperesIn()?aHatch.maxAmperesIn():tElapsedAmpers;
               // System.out.println("stored EU "+mEUStored);
            }

        }
        tElapsedAmpers = mMaxAmperrage;
        for(GT_MetaTileEntity_Hatch_Dynamo aDynamo: mDynamoHatches){
            if(tElapsedAmpers<=0)
                continue;
            if((mEUStored>(tElapsedAmpers>=aDynamo.maxAmperesOut()?aDynamo.getMaxOutput():aDynamo.maxEUOutput()*tElapsedAmpers))&&aDynamo.getBaseMetaTileEntity().increaseStoredEnergyUnits(tElapsedAmpers>=aDynamo.maxAmperesOut()?aDynamo.getMaxOutput():aDynamo.maxEUOutput()*tElapsedAmpers,false)){
               // System.out.println("outputing "+( tElapsedAmpers>=aDynamo.maxAmperesOut()?aDynamo.maxAmperesOut():tElapsedAmpers)+" ampers and "+(tElapsedAmpers>=aDynamo.maxAmperesOut()?aDynamo.getMaxOutput():aDynamo.maxEUOutput()*tElapsedAmpers)+" EU");
                mEUStored -= tElapsedAmpers>=aDynamo.maxAmperesOut()?aDynamo.getMaxOutput():aDynamo.maxEUOutput()*tElapsedAmpers;
                tElapsedAmpers -= tElapsedAmpers>=aDynamo.maxAmperesOut()?aDynamo.maxAmperesOut():tElapsedAmpers;
               // System.out.println("stored EU "+mEUStored);
            }
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);


    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        System.out.println("storedEU "+mEUStored+" " + aSide);
        GT_Utility.sendChatToPlayer(aPlayer,""+aSide);
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    public boolean checkRecipe(ItemStack aStack) {
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = 20;
        mEUt = 10;
        return true;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return addDynamoToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityOffset(0,1,0),48)&&
                addMaintenanceToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityOffset(0,2,0),48)&&
                addEnergyInputToMachineList(getBaseMetaTileEntity().getIGregTechTileEntityOffset(0,-1,0),48);
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

}
