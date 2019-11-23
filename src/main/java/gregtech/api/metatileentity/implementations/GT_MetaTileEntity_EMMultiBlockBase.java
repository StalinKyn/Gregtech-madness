package gregtech.api.metatileentity.implementations;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.nei.GT_NEI_EMHandler;
import net.minecraft.item.ItemStack;

import javax.xml.transform.sax.TemplatesHandler;
import java.util.ArrayList;

public abstract class GT_MetaTileEntity_EMMultiBlockBase extends GT_MetaTileEntity_MultiBlockBase{

    public static boolean disableEMMaintenance = true;
    public static ArrayList<GT_MetaTileEntity_EMMultiBlockBase> mNEImaps = new ArrayList<>();

    public boolean hasEMBeenAccepted = false;

    public ArrayList<GT_MetaTileEntity_Hatch_EM_Input> mEMInputHatches = new ArrayList<>();

    public GT_MetaTileEntity_EMMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID,aName,aNameRegional);
    }

    public GT_MetaTileEntity_EMMultiBlockBase(String aName) {
        super(aName);
    }

    public GT_MetaTileEntity_EMMultiBlockBase(int aID, String aName, String aNameRegional, int aSlotCount) {
        super(aID, aName, aNameRegional, aSlotCount);
    }

    public GT_MetaTileEntity_EMMultiBlockBase(String aName,int aSlotCount) {
        super(aName, aSlotCount);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (--mUpdate == 0 || --mStartUpCheck == 0) {
                mInputHatches.clear();
                mInputBusses.clear();
                mOutputHatches.clear();
                mOutputBusses.clear();
                mDynamoHatches.clear();
                mEnergyHatches.clear();
                mMufflerHatches.clear();
                mMaintenanceHatches.clear();
                mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);
            }
            if (mStartUpCheck < 0) {
                if (mMachine) {
                    for (GT_MetaTileEntity_Hatch_Maintenance tHatch : mMaintenanceHatches) {
                        if (isValidMetaTileEntity(tHatch)) {
                            if (!GT_MetaTileEntity_EMMultiBlockBase.disableEMMaintenance) {
                                if (tHatch.mAuto && (!mWrench || !mScrewdriver || !mSoftHammer || !mHardHammer || !mSolderingTool || !mCrowbar))
                                    tHatch.autoMaintainance();
                                if (tHatch.mWrench) mWrench = true;
                                if (tHatch.mScrewdriver) mScrewdriver = true;
                                if (tHatch.mSoftHammer) mSoftHammer = true;
                                if (tHatch.mHardHammer) mHardHammer = true;
                                if (tHatch.mSolderingTool) mSolderingTool = true;
                                if (tHatch.mCrowbar) mCrowbar = true;
                            } else {
                                mWrench = true;
                                mScrewdriver = true;
                                mSoftHammer = true;
                                mHardHammer = true;
                                mSolderingTool = true;
                                mCrowbar = true;
                            }

                            tHatch.mWrench = false;
                            tHatch.mScrewdriver = false;
                            tHatch.mSoftHammer = false;
                            tHatch.mHardHammer = false;
                            tHatch.mSolderingTool = false;
                            tHatch.mCrowbar = false;
                        }
                    }
                    if (getRepairStatus() > 0) {
                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {
                            if (onRunningTick(mInventory[1])) {
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) {
                                    stopMachine();
                                }
                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
                                    if (mOutputItems != null) for (ItemStack tStack : mOutputItems)
                                        if (tStack != null) {
                                            try {
                                                GT_Mod.achievements.issueAchivementHatch(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), tStack);
                                            } catch (Exception ignored) {
                                            }
                                            addOutput(tStack);
                                        }
                                    if (mOutputFluids != null) {
                                        addFluidOutputs(mOutputFluids);
                                    }
                                    mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                    mOutputItems = null;
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;
                                    if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe(mInventory[1]);
                                    if (mOutputFluids != null && mOutputFluids.length > 0) {
                                        if (mOutputFluids.length > 1) {
                                            try {
                                                GT_Mod.achievements.issueAchievement(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), "oilplant");
                                            } catch (Exception ignored) {
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()||hasEMBeenAccepted) {
                                if (aBaseMetaTileEntity.isAllowedToWork()) {
                                    hasEMBeenAccepted = false;
                                    checkRecipe(mInventory[1]);
                                }
                                if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                            }
                        }
                    } else {
                        stopMachine();
                    }
                } else {
                    stopMachine();
                }
            }
            aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8) | (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        }
    }

    @Override
    public void onMachineBlockUpdate() {
        mUpdate = 300;
    }

    public boolean addEMInputToMachineList(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EM_Input) {
            if (notifyOnEMAccepted()) ((GT_MetaTileEntity_Hatch_EM_Input) aMetaTileEntity).notifyOnEMAccepted = true;
            ((GT_MetaTileEntity_Hatch_EM_Input) aMetaTileEntity).multiBlock = this;
            return mEMInputHatches.add((GT_MetaTileEntity_Hatch_EM_Input) aMetaTileEntity);
        }
        return false;
    }

    public boolean notifyOnEMAccepted(){return true;}

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public void loadAllCraftingRecipes(ArrayList<TemplateRecipeHandler.CachedRecipe> arecipes, String outputId, Object... results){

    }

    public void loadCraftingRecipes(ArrayList<TemplateRecipeHandler.CachedRecipe> arecipes, ItemStack aResult){

    }

    public void loadUsageRecipes(ArrayList<TemplateRecipeHandler.CachedRecipe> arecipes, ItemStack aInput){

    }

    public String getNEIPath(){
        return "";
    }

    public int[][] getNEIInputCoords(){
        return new int[0][0];
    }

    public int[][] getNEIOutputCoords(){
        return new int[0][0];
    }

    public int[][] getNEIFluidInputCoords(){
        return new int[0][0];
    }

    public int[][] getNEIFluidOutputCoords(){
        return new int[0][0];
    }
}
