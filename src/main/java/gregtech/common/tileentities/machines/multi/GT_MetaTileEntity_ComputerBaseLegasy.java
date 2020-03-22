package gregtech.common.tileentities.machines.multi;

import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;


public abstract class GT_MetaTileEntity_ComputerBaseLegasy extends GT_MetaTileEntity_DataWorkerBaseLegasy {

    public long mCalculationPower = 0;
    public Object[] mRequest = null;

    public ArrayList<GT_MetaTileEntity_Hatch_CircuitAccess> mCircuitAccessHatches = new ArrayList<GT_MetaTileEntity_Hatch_CircuitAccess>();
    public ArrayList<GT_MetaTileEntity_Hatch_DataAccess> mDataAccessHatches = new ArrayList<>();

    public GT_MetaTileEntity_ComputerBaseLegasy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ComputerBaseLegasy(String aName) {
        super(aName);
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "AssemblyLine.png");
    }

    public boolean addCircuitAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_CircuitAccess) {
            ((GT_MetaTileEntity_Hatch_CircuitAccess) aMetaTileEntity).updateStats();
            if(mCircuitAccessHatches.contains(aMetaTileEntity))
                return true;
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
             mCircuitAccessHatches.add((GT_MetaTileEntity_Hatch_CircuitAccess) aMetaTileEntity);
            return true;
        }
        return false;
    }

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DataAccess) {
            ((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity).isComputerPart = true;
            if(mDataAccessHatches.contains(aMetaTileEntity))
                return true;
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            mDataAccessHatches.add((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if(mRequest==null||((int)mRequest[1]>getCalculationPower()))
            return false;
        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = (int)mRequest[0];
        ((GT_MetaTileEntity_LargeResearchStationBaseLegasy)mRequest[2]).checkRecipe(null);
        mRequest = null;
        return true;
    }

    public int getCalculationPower(){
        int power = 0;
        for(GT_MetaTileEntity_Hatch_CircuitAccess aHatch: mCircuitAccessHatches){
            power+=aHatch.getCalculationPower();
        }
        return power;
    }

    public boolean requestCalculations(Object[] aRequest){
        mRequest = aRequest;
        return true;
    }

    @Override
    public void onServerStart() {
        mDataHatch = null;
        super.onServerStart();
    }

    @Override
    public void startProcessing() {
        checkRecipe(null);
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

    public boolean saveRecipeData(NBTTagCompound aTag){
        for(GT_MetaTileEntity_Hatch_DataAccess aHatch: mDataAccessHatches){
           if(aHatch.getFreeSpace()>0){
              // aHatch.saveRecipeData(aTag);
               return true;
           }

        }
        return false;
    }

    public int getFreeSpace(){
        int tFreeSpace=0;
        for(GT_MetaTileEntity_Hatch_DataAccess aHatch: mDataAccessHatches){
            tFreeSpace+= aHatch.getFreeSpace();
        }
        return tFreeSpace;
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mMaxProgresstime = 100;
        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        return false;
    }


}