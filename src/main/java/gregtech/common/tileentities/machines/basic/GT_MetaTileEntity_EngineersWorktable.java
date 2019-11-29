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
        super(aID, aName, aNameRegional, aTier, 1, new String[]{"Best Primitive Engineer's friend","Can assemble dreams"}, 1, 1, "EngineersWorkstation.png", "", new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/engineers_workstation/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/engineers_workstation/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/engineers_workstation/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/engineers_workstation/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/engineers_workstation/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/engineers_workstation/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/engineers_workstation/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("OVERLAY_BOTTOM")));
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

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sEngineersWorkstationRecipes;
    }

    @Override
    protected boolean hasEnoughEnergyToCheckRecipe() {
            return true;
    }


    @Override
    public int getInputSlot() {
        return super.getInputSlot();
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return true;
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 0;
    }

    @Override
    public long maxEUInput() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 0;
    }

    @Override
    public int dechargerSlotCount() {
        return 0;
    }

}
