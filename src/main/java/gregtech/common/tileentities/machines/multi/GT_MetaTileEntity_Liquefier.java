package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class GT_MetaTileEntity_Liquefier
        extends GT_MetaTileEntity_MultiBlockBase {

    public int mWorkingTemp = 73;

    public GT_MetaTileEntity_Liquefier(int aID, String aName, String aNameRegional, int aWorkingTemp) {
        super(aID, aName, aNameRegional);
        mWorkingTemp = aWorkingTemp;
    }

    public GT_MetaTileEntity_Liquefier(String aName, int aWorkingTemp) {
        super(aName);
        mWorkingTemp = aWorkingTemp;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Liquefier(this.mName,mWorkingTemp);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Liquefier"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "VacuumFreezer.png");
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sLiquefierRecipes;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<FluidStack> tInputList = getStoredFluids();
        FluidStack[] tFluidInputs = tInputList.toArray(new FluidStack[tInputList.size()]);
        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sLiquefierRecipes.findRecipe(
                getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluidInputs ,new ItemStack[]{mInventory[1]});
        if(tRecipe!=null&&mWorkingTemp>tRecipe.mSpecialValue)
            return false;
        if (tRecipe != null && tRecipe.isRecipeInputEqual(true, tFluidInputs, new ItemStack[]{mInventory[1]})) {
            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;
            this.mEUt = tRecipe.mEUt;
            this.mMaxProgresstime = tRecipe.mDuration;
            while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                this.mEUt *= 4;
                this.mMaxProgresstime /= 2;
            }
            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }
            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
            this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
            return true;
        }
        return false;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return addMaintenanceToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(0,1,0),16)&&addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(0,2,0),16)
                &&addOutputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(0,3,0),16)&&addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(0,4,0),16);
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