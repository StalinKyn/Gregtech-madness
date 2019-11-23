package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_EngineersWorktable extends GT_MetaTileEntity_BasicMachine{



    public GT_MetaTileEntity_EngineersWorktable(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[]{"Best Primitive Engineer's friend","Can assemble dreams"}, 1, 1, "EngineersWorkstation.png", "", new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("OVERLAY_BOTTOM")));
    }

    public GT_MetaTileEntity_EngineersWorktable(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 9, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_EngineersWorktable(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 9, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EngineersWorktable(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }
/*
    public int checkRecipe(){
       GT_Recipe tRecipe=  getRecipeList().findRecipe(getBaseMetaTileEntity(),false,false,0,new FluidStack[]{getFluid()},getAllInputs());
        if(tRecipe== null)
            return 0;
        if(checkRecipeInput(tRecipe,true)){
            mMaxProgresstime = tRecipe.mDuration;
            mOutputItems[0] = GT_Utility.copyAmount(tRecipe.mOutputs[0].stackSize,new Object[]{tRecipe.mOutputs[0]});
            return 2;
        }
        return 0;
    }*/

    protected boolean checkRecipeInput(GT_Recipe aRecipe,boolean consumeBySuccess){
        ItemStack[] mInputs = getAllInputs();
        for(byte i = 0; i < aRecipe.mInputs.length;i++) {
            if (!GT_Utility.areStacksEqual(aRecipe.mInputs[i],mInputs[i],true)||!(aRecipe.mInputs[i].stackSize<=mInputs[i].stackSize)){
                return false;
            }
        }
        if(consumeBySuccess) {
            for (byte i = 0; i < mInputs.length; i++) {
                if(mInputs[i]!=null)
                mInputs[i].stackSize -= aRecipe.mInputs[i].stackSize;
            }
        }
        return true;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        checkRecipe();
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sEngineersWorkstationRecipes;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack);
    }


    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
            super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    protected boolean hasEnoughEnergyToCheckRecipe() {
            return true;
    }

    public int getCapacity() {
        return 100000;
    }

}
