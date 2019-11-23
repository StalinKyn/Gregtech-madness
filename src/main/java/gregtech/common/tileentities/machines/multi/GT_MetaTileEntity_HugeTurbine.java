package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_Container_3by3;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IGuideRenderer;
import gregtech.api.interfaces.metatileentity.IProjectable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Multiblock_Utility;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_HugeTurbine;
import gregtech.common.gui.GT_GUIContainer_HugeTurbine;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public abstract class GT_MetaTileEntity_HugeTurbine extends GT_MetaTileEntity_MultiBlockBase implements IProjectable {

    protected int baseEff = 0;
    protected int optFlow = 0;
    protected double realOptFlow = 0;
    protected int storedFluid = 0;
    protected int counter = 0;


    public static String[][] mStructure = new String[][]{
            {
                    "sCCCs",
                    "sCCCs",
                    "CCCCC",
                    "CCCCC",
                    "CCCCC",
                    "sCCCs",
                    "sCCCs"},
            {
                    "CCCCC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CNNNC"},
            {
                    "CCdCC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CNcNC"},
            {
                    "CCCCC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CaaaC",
                    "CNNNC"},
            {
                    "sCCCs",
                    "sCCCs",
                    "CCCCC",
                    "CCCCC",
                    "CCCCC",
                    "sCCCs",
                    "sCCCs"}
    };


    public GT_MetaTileEntity_HugeTurbine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional,6);
    }

    public GT_MetaTileEntity_HugeTurbine(String aName) {
        super(aName, 6);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_HugeTurbine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return checkMachine(getBaseMetaTileEntity(),getRealInventory()[1]);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        GT_Multiblock_Utility u = new GT_Multiblock_Utility(false,true,mStructure,new Object[]{'C',getCasingBlock(), getCasingMeta(), (int)getCasingTextureIndex() , new byte[]{1,3,6,7},100,'N',getCasingBlock(), getCasingMeta(), (int)getCasingTextureIndex() , new byte[]{},8});
        return u.checkStructure(getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing());
    }

    public abstract Block getCasingBlock();

    public abstract byte getCasingMeta();

    public abstract byte getCasingTextureIndex();

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_HugeTurbine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        getServerGUI(1,aPlayer.inventory,getBaseMetaTileEntity());
        return false;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        System.out.println("checking rotors");
        for(int j = 1;j<6;j++){
            ItemStack tStack = mInventory[j];
            if(tStack==null || !(tStack.getItem() instanceof GT_MetaGenerated_Tool)  || tStack.getItemDamage() < 170 || tStack.getItemDamage() >178)return false;
            if(!GT_Utility.areStacksEqual(aStack, tStack,true)) return false;
        }
        ArrayList<FluidStack> tFluids = getStoredFluids();
        System.out.println("getting fluids "+tFluids.size());
        if (tFluids.size() > 0) {
            if (baseEff == 0 || optFlow == 0 || counter >= 1000 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()
                    || this.getBaseMetaTileEntity().hasInventoryBeenModified()) {
                counter = 0;
                baseEff = (int) ((50.0F
                        + (10.0F * ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack))) * 100);
                optFlow = 5* (int) Math.max(Float.MIN_NORMAL,
                        ((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack).getSpeedMultiplier()
                                * ((GT_MetaGenerated_Tool) aStack.getItem()).getPrimaryMaterial(aStack).mToolSpeed
                                * 50);
            } else {
                counter++;
            }
        }

        int newPower = fluidIntoPower(tFluids, optFlow, baseEff);  // How much the turbine should be producing with this flow
        System.out.println("opt flow "+optFlow+" baseEff "+baseEff+" new power "+newPower);
        int difference = newPower - this.mEUt; // difference between current output and new output

        // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the difference in power level (per tick)
        // This is how much the turbine can actually change during this tick
        int maxChangeAllowed = Math.max(10, (int) Math.ceil(Math.abs(difference) * 0.01));

        if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
            int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
            this.mEUt += change; // Apply the change
        } else
            this.mEUt = newPower;
        System.out.println("EU/t "+mEUt);
        if (mEUt <= 0) {

//	            this.mEfficiencyIncrease = (-10);
            this.mEfficiency = 0;
            //stopMachine();
            return false;
        } else {
            this.mMaxProgresstime = 1;
            this.mEfficiencyIncrease = (10);
            if(this.mDynamoHatches.size()>0){
                if(this.mDynamoHatches.get(0).getMaxOutput() < (int)((long)mEUt * (long)mEfficiency / 10000L)){
                    explodeMultiblock();}
            }
            return true;
        }
    }

    @Override
    public boolean doRandomMaintenanceDamage() {

        return super.doRandomMaintenanceDamage();
    }

    abstract int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff);

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return 0;
        }
        if (aStack.getItem() instanceof GT_MetaGenerated_Tool_01) {
            return 10000;
        }
        return 0;
    }
    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getInfoData() {
        String tRunning = mMaxProgresstime>0 ? "Turbine running":"Turbine stopped";
        String tMaintainance = getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance" ;
        int tDura = 0;

        if (mInventory[1] != null && mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
            tDura = (int) ((100.0f / GT_MetaGenerated_Tool.getToolMaxDamage(mInventory[1]) * (GT_MetaGenerated_Tool.getToolDamage(mInventory[1]))+1));
        }

        return new String[]{
                "Huge Turbine",
                tRunning,
                "Current Output: "+mEUt+" EU/t",
                "Optimal Flow: "+(int)realOptFlow+" L/t",
                "Fuel Remaining: "+storedFluid+"L",
                "Current Speed: "+(mEfficiency/100)+"%",
                "Turbine Damage: "+tDura+"%",
                tMaintainance};
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[][] GetStructure() {
        return mStructure;
    }

    @Override
    public boolean RenderStructure(boolean aBuild, IGuideRenderer aRenderer, int[] aParams) {
        GT_Multiblock_Utility u = new GT_Multiblock_Utility(false,true,mStructure,new Object[]{'C',getCasingBlock(), getCasingMeta(), (int)getCasingTextureIndex() , new byte[]{1,3,6,7},120,'N',getCasingBlock(), getCasingMeta(), (int)getCasingTextureIndex() , new byte[]{},8});
        u.RenderGuide(this.getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing(),aBuild, aRenderer);
        return true;
    }

    @Override
    public String[] GetParamNames() {
        return null;
    }

}
