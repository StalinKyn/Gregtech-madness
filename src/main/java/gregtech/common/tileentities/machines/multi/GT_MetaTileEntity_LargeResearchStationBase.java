package gregtech.common.tileentities.machines.multi;

import gregtech.api.datasystem.*;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_DataSystemController;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public abstract class GT_MetaTileEntity_LargeResearchStationBase extends GT_MetaTileEntity_DataWorkerBase implements IResearcher {

    int mPassedIterations = 0;
    int mTargetIterationsCount = 0;

    int computation = 0;

    public int nextResearchID  = -1;

    GT_MetaTileEntity_DataSystemController.ResearchOrder mOrder = null;


    public GT_MetaTileEntity_Hatch_InputBus mScanningHatch = null;

    GT_Recipe.GT_Recipe_ResearchStation currentRecipe = null;
    GT_Recipe.GT_Recipe_ResearchStation prevRecipe = null;

    protected IDataDevice mWorker = null;

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
        if(currentRecipe==null)
            return false;
        if(currentRecipe!=prevRecipe){
            mPassedIterations = 0;
            mTargetIterationsCount = currentRecipe.mMinIterationsCount+getBaseMetaTileEntity().getRandomNumber(currentRecipe.mMaxIterationsCount-currentRecipe.mMinIterationsCount+1);
        }
        if(!GT_Recipe.GT_Recipe_ResearchStation.checkInputs(true,false, getStoredFluids(), getStoredInputs(),currentRecipe)){
            return false;
        }
        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = currentRecipe.mSingleResearchTime;
        mEUt = currentRecipe.mEUt;
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

    @Override
    protected void endProcess() {
        if(currentRecipe==null)
            return;
        mPassedIterations++;
        prevRecipe = currentRecipe;
        currentRecipe = null;
        if(prevRecipe.mMinIterationsCount > mPassedIterations) {
            mOrder.mFinished = true;
            return;
        }
        if(mPassedIterations>=mTargetIterationsCount){
            saveData(prevRecipe);
            mPassedIterations = 0;
            mTargetIterationsCount = currentRecipe.mMinIterationsCount+getBaseMetaTileEntity().getRandomNumber(currentRecipe.mMaxIterationsCount-currentRecipe.mMinIterationsCount);
            mOrder.mFinished = true;
            mOrder.mSucceed = true;
            return;
        }
        else{
            mOrder.mFinished = true;
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

    public boolean saveData(GT_Recipe.GT_Recipe_ResearchStation aResearch){
        System.out.println("Successed at "+mPassedIterations);
        nextResearchID = -1;
        mSystemController.mSystem.sendInformation(getNode(),mSystemController.mSystem.getPathToController(getNode()),new GT_ResearchDoneBundle(aResearch));
        return true;
    }

    @Override
    public boolean canResearch(GT_Recipe.GT_Recipe_ResearchStation aRecipe) {
        if(isProcessing())
            return false;
        if(true)//check if can process
            return true;
        return false;
    }

    @Override
    public GT_InformationBundle[] requestDataBundle(GT_Recipe.GT_Recipe_ResearchStation aRecipe) {
        /*if((mScanningHatch != null)&&mScanningHatch.mInventory[0]!=null)
            currentRecipe = GT_Recipe.GT_Recipe_ResearchStation.findRecipe(mScanningHatch.mInventory);*/
        currentRecipe = aRecipe;
        if(currentRecipe!=null){
            if(GT_Recipe.GT_Recipe_ResearchStation.checkInputs(false,false, getStoredFluids(), getStoredInputs(),currentRecipe)){
               return new GT_InformationBundle[]{new GT_RequestBundle(currentRecipe.mComputation, currentRecipe.mSingleResearchTime, this)};
            }
        }
        return null;
    }

    @Override
    public void onBundleAccepted(GT_InformationBundle aBundle) {
        if(aBundle instanceof GT_MessageBundle){
            if(((GT_MessageBundle) aBundle).mID == 1){
                nextResearchID = ((GT_MessageBundle) aBundle).mInformation;
            }
        }
        if(aBundle instanceof GT_RequestBundle && ((GT_RequestBundle)aBundle).isApproved){
            startProcessing();
            mWorker = ((GT_RequestBundle)aBundle).mSender;
            computation = 5;
            return;
        }
        if (currentRecipe!= null && aBundle.mDataFlow >= currentRecipe.mComputation)
            computation++;

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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(getBaseMetaTileEntity().isClientSide())
            return;
        if(computation<0 && getBaseMetaTileEntity().isActive())
            stopMachine();
        if(computation>= 0 && getBaseMetaTileEntity().isActive())
            computation--;
        super.onPostTick(aBaseMetaTileEntity, aTick);

    }

    @Override
    public Object getProcessing() {
        return currentRecipe == null ? -1:currentRecipe.mID;
    }

    @Override
    public boolean isProcessing() {
        return getBaseMetaTileEntity().isActive();
    }

    public void setOrder(GT_MetaTileEntity_DataSystemController.ResearchOrder aOrder){
        mOrder = aOrder;
    }

    @Override
    public int getProgress() {
        return (int)((double)mPassedIterations/(double)mTargetIterationsCount*100f);
    }
}
