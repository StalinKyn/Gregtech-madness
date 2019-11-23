package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_Container_PrimitiveResearchStation;
import gregtech.common.gui.GT_GUIContainer_PrimitiveResearchStation;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeResearchStationBase;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.item.ItemCropSeed;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_PrimitiveResearchStation extends GT_MetaTileEntity_BasicMachine{

    GT_Recipe.GT_Recipe_ResearchStation currentRecipe = null;
    GT_Recipe.GT_Recipe_ResearchStation prevRecipe = null;

    public int mPassedIterations = 0;
    public int mMaxIterations = 0;

    public GT_MetaTileEntity_PrimitiveResearchStation(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[]{"Copies seeds with efficiency: "+Math.min((aTier+5)*10,100)+"%","Uses UUMatter for each seed","The better crop the more UUMatter it needs","Can replicate only scanned seeds"}, 1, 1, "PrimitiveResearchStation.png", "", new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("OVERLAY_BOTTOM")));
    }

    public GT_MetaTileEntity_PrimitiveResearchStation(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 13, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_PrimitiveResearchStation(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 13, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PrimitiveResearchStation(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }


    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_PrimitiveResearchStation(aPlayerInventory, getBaseMetaTileEntity());
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_PrimitiveResearchStation(aPlayerInventory, aBaseMetaTileEntity, GT_Values.RES_PATH_GUI+"basicmachines/PrimitiveResearchStation.png");
    }

    public int checkRecipe(){
        ItemStack[] tResearchInputs = getResearchInputs();
        if(tResearchInputs!=null)
            currentRecipe = GT_Recipe.GT_Recipe_ResearchStation.findRecipe(tResearchInputs,true);//todo check this
        if(currentRecipe!=null){
            ItemStack[] tItemInputs = getItemInputs();
            if(GT_Recipe.GT_Recipe_ResearchStation.checkInputs(false,false, new FluidStack[0], tItemInputs,currentRecipe)){
                if(currentRecipe!=prevRecipe){
                    mPassedIterations = 0;
                }

                if(!GT_Recipe.GT_Recipe_ResearchStation.checkInputs(true,false, new FluidStack[0], tItemInputs,currentRecipe)){
                    return 0;
                }
                if(mPassedIterations==0)
                    mMaxIterations = currentRecipe.mMinIterationsCount+getBaseMetaTileEntity().getRandomNumber(currentRecipe.mMaxIterationsCount-currentRecipe.mMinIterationsCount);
                mMaxProgresstime = currentRecipe.mSingleResearchTime;
                mEUt = currentRecipe.mEUt;
                return 2;
            }
        }
        return 0;
    }

    public ItemStack[] getResearchInputs(){
        ItemStack[] tInputs = new ItemStack[4];
        for(int i = 0; i < 4; i++){
            tInputs[i]=getInputAt(i);
        }
        return tInputs;
    }

    public ItemStack[] getItemInputs(){
        ItemStack[] tInputs = new ItemStack[9];
        for(int i = 4; i < 13; i++){
            tInputs[i-4]=getInputAt(i);
        }
        return tInputs;
    }

    public ItemStack getSpecialInput(){
        return getInputAt(13);
    }

    @Override
    protected ItemStack getInputAt(int aIndex) {
        return super.getInputAt(aIndex);
    }

    @Override
    public void endProcess() {
        mPassedIterations++;
        prevRecipe = currentRecipe;
        currentRecipe = null;
        saveData(prevRecipe.mTargetRecipe.mOutputs[0]);
        if(prevRecipe.mMinIterationsCount > mPassedIterations)
            return;
        if(prevRecipe.mMaxIterationsCount<=mPassedIterations){
            saveData(prevRecipe.mTargetRecipe.mOutputs[0]);
            mPassedIterations = 0;
            return;
        }
        if(mMaxIterations<=mPassedIterations){
            saveData(prevRecipe.mTargetRecipe.mOutputs[0]);
            mPassedIterations = 0;
        }
        super.endProcess();
    }

    @Override
    protected boolean hasEnoughEnergyToCheckRecipe() {
        return true;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack);
    }

    public int getCapacity() {
        return 100000;
    }

    public boolean saveData(ItemStack aStack){
        System.out.println("Successed at "+mPassedIterations);
        ItemStack tBook = getSpecialInput();
        if(GT_Utility.areStacksEqual(tBook,ItemList.EngineersBook.get(1L))){
            NBTTagCompound tTag = tBook.getTagCompound();
            if(tTag==null){
                tTag = new NBTTagCompound();
                tTag.setInteger("capacitySize",16);
                tTag.setInteger("usedCapacity",0);
            }
            int size = tTag.getInteger("capacitySize");
            int usedCapacity = tTag.getInteger("usedCapacity");
            if(size-usedCapacity<1)
                return false;
            tTag.setTag("researchItemTag"+(usedCapacity),aStack.writeToNBT(new NBTTagCompound()));
            tTag.setInteger("usedCapacity",(usedCapacity+1));
            tBook.setTagCompound(tTag);
        }
        return false;
    }

    @Override
    public int getInputSlot() {
        return super.getInputSlot();
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return true;
    }
}
