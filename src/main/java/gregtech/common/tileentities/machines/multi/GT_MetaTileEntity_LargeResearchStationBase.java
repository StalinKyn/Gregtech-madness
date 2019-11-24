package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IDataDevice;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_Data_Packet;
import gregtech.api.util.*;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Commutator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.regex.Pattern;

public abstract class GT_MetaTileEntity_LargeResearchStationBase extends GT_MetaTileEntity_DataWorkerBase {

    Object[] mRequest = new Object[3];
    int mPassedIterations = 0;
    int mTargetIterationsCount = 0;


    public GT_MetaTileEntity_Hatch_InputBus mScanningHatch = null;
    public GT_MetaTileEntity_Commutator aCommutator = null;

    GT_Recipe.GT_Recipe_ResearchStation currentRecipe = null;
    GT_Recipe.GT_Recipe_ResearchStation prevRecipe = null;

    public GT_MetaTileEntity_LargeResearchStationBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeResearchStationBase(String aName) {
        super(aName);
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "AssemblyLine.png");
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if(currentRecipe!=prevRecipe){
            mPassedIterations = 0;
            mTargetIterationsCount = currentRecipe.mMinIterationsCount+getBaseMetaTileEntity().getRandomNumber(currentRecipe.mMaxIterationsCount-currentRecipe.mMinIterationsCount);
        }
        if(!GT_Recipe.GT_Recipe_ResearchStation.checkInputs(true,false, getStoredFluids(), getStoredInputs(),currentRecipe)){
            mRequest = null;
            return false;
        }
        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = currentRecipe.mSingleResearchTime;
        mEUt = currentRecipe.mEUt;
        return true;
    }

    public boolean requestComputation(int aDuration, int aComputation){
        mRequest[0] = aDuration;
        mRequest[1] = aComputation;
        mRequest[2] = this;
        return true;
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

    public Object[] getRequest(){
        return mRequest;
    }

    @Override
    protected boolean maintainMachine() {
        if((mScanningHatch != null)&&mScanningHatch.mInventory[0]!=null)
        currentRecipe = GT_Recipe.GT_Recipe_ResearchStation.findRecipe(mScanningHatch.mInventory);
        if(currentRecipe!=null){
            if(GT_Recipe.GT_Recipe_ResearchStation.checkInputs(false,false, getStoredFluids(), getStoredInputs(),currentRecipe)){
                requestComputation(currentRecipe.mSingleResearchTime,currentRecipe.mComputation);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        maintainMachine();
        return false;
    }

    @Override
    protected void endProcess() {
        mPassedIterations++;
        prevRecipe = currentRecipe;
        currentRecipe = null;
        if(prevRecipe.mMinIterationsCount > mPassedIterations)
            return;
        if(mPassedIterations>=mTargetIterationsCount){
            saveData(prevRecipe.mTargetRecipe.mOutputs[0]);
            mPassedIterations = 0;
            mTargetIterationsCount = currentRecipe.mMinIterationsCount+getBaseMetaTileEntity().getRandomNumber(currentRecipe.mMaxIterationsCount-currentRecipe.mMinIterationsCount);
            return;
        }

    }

    public boolean addScanningHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mScanningHatch = (GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean saveData(ItemStack aStack){
        System.out.println("Successed at "+mPassedIterations);
        aCommutator.saveData(aStack);
        return true;
    }


}
