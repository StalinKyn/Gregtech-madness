package gregtech.common.tileentities.machines.multi;

import gregtech.api.datasystem.*;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_CircuitAccess;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;


public abstract class GT_MetaTileEntity_ComputerBase extends GT_MetaTileEntity_DataWorkerBase implements IDataProducer, IDataHandler<Integer>{

    public long mCalculationPower = 0;
    public int rWorkTime = 0, rCalculations = 0;

    public ArrayList<GT_MetaTileEntity_Hatch_CircuitAccess> mCircuitAccessHatches = new ArrayList<GT_MetaTileEntity_Hatch_CircuitAccess>();
    public ArrayList<GT_MetaTileEntity_Hatch_DataAccess> mDataAccessHatches = new ArrayList<>();

    private HashSet<Integer> cashedData;

    IDataDevice mWorker = null;

    public GT_MetaTileEntity_ComputerBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ComputerBase(String aName) {
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
            ((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity).mComputer = this;
            mDataAccessHatches.add((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        if(rWorkTime == 0 || rCalculations > mCalculationPower)
            return false;
        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = rWorkTime;
        mSystemController.mSystem.sendAutomatedBundle(getNode(),new GT_RequestBundle(rCalculations, rWorkTime, this).approve(),mWorker.getNode());
        rWorkTime = 0;
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        onDataContainersUpdated();
        return false;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (getBaseMetaTileEntity().isClientSide()) return false;
        if(mWorker==null||getNode()==null) {
            stopMachine();
            return false;
        }
        //long nano = System.nanoTime();
        mSystemController.mSystem.sendAutomatedBundle(getNode(),new GT_InformationBundle(rCalculations), mWorker.getNode());
       // System.out.println("time "+(System.nanoTime()-nano));
        return super.onRunningTick(aStack);
    }

    @Override
    public boolean canProduce(GT_InformationBundle aBundle) {
        if (aBundle instanceof GT_RequestBundle){
            GT_RequestBundle aRequest = (GT_RequestBundle) aBundle;
            if(aRequest.mComputation<=mCalculationPower && !getBaseMetaTileEntity().isActive() /*&& getBaseMetaTileEntity().isAllowedToWork()*/){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean produceDataBundle(GT_InformationBundle aBundle) {
        if (aBundle instanceof GT_RequestBundle) {
            GT_RequestBundle aRequest = (GT_RequestBundle) aBundle;
            if (aRequest.mComputation <= mCalculationPower && !getBaseMetaTileEntity().isActive()/* && getBaseMetaTileEntity().isAllowedToWork()*/) {
                mWorker = aRequest.mSender;
                rWorkTime = aRequest.mTime;
                rCalculations = aRequest.mComputation;
                startProcessing();
                return true;
            }
        }
        return false;
    }

    public int getCalculationPower(){
        int power = 0;
        for(GT_MetaTileEntity_Hatch_CircuitAccess aHatch: mCircuitAccessHatches){
            power+=aHatch.getCalculationPower();
        }
        return power;
    }

    public boolean requestCalculations(Object[] aRequest){
       // mRequest = aRequest;
        return true;
    }

    @Override
    public void onServerStart() {
        mDataHatch = null;
        super.onServerStart();
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

    public boolean saveRecipeData(Integer aTag){
        for(GT_MetaTileEntity_Hatch_DataAccess aHatch: mDataAccessHatches){
           if(aHatch.getFreeSpace()>0){
               aHatch.saveRecipeData(aTag);
               return true;
           }

        }
        return false;
    }

    @Override
    public void addAllDataToHashSet(HashSet<Integer> set) {
        for(GT_MetaTileEntity_Hatch_DataAccess aHatch: mDataAccessHatches){
            aHatch.addAllToHashSet(set);
        }
        cashedData = set;
    }

    public int getFreeSpace(){
        int tFreeSpace=0;
        for(GT_MetaTileEntity_Hatch_DataAccess aHatch: mDataAccessHatches){
            tFreeSpace+= aHatch.getFreeSpace();
        }
        return tFreeSpace;
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        if(mWorker!=null)
            mWorker.onProcessAborted();
    }

    @Override
    public void onProcessAborted() {
        super.stopMachine();
    }

    @Override
    public void onPacketStuck() {
        stopMachine();
    }

    @Override
    public HashSet<Integer> getStoredData(int selector) {
        if(selector != 1)
            return null;
        HashSet<Integer> tOut = new HashSet<>();
        for(GT_MetaTileEntity_Hatch_DataAccess aHatch: mDataAccessHatches){
            aHatch.addAllToHashSet(tOut);
        }
        return tOut;
    }

    @Override
    public boolean saveData(Integer data) {
        saveRecipeData(data);
        return true;
    }

    @Override
    public boolean canStore(Integer item) {
        return getFreeSpace()>0;
    }

    public void onDataContainersUpdated(){
        HashSet<Integer> tOut = new HashSet<>();
        for(GT_MetaTileEntity_Hatch_DataAccess aHatch: mDataAccessHatches){
            aHatch.addAllToHashSet(tOut);
        }
        if(!tOut.equals(cashedData)){
            mSystemController.onDataUnpdated();
        }
    }
}