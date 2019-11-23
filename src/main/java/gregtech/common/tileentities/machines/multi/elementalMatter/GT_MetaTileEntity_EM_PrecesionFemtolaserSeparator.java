package gregtech.common.tileentities.machines.multi.elementalMatter;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EMMultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class GT_MetaTileEntity_EM_PrecesionFemtolaserSeparator extends GT_MetaTileEntity_EMMultiBlockBase {

    public GT_MetaTileEntity_EM_PrecesionFemtolaserSeparator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_PrecesionFemtolaserSeparator(String aName) {
        super(aName);
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16]};
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_PrecesionFemtolaserSeparator(mName);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void loadCraftingRecipes(ArrayList<TemplateRecipeHandler.CachedRecipe> arecipes, ItemStack aResult) {
        if(!GT_Utility.areStacksEqual(ItemList.ClosedSpatialCell.get(1L),aResult,true)||aResult.getTagCompound()==null||aResult.getTagCompound().getString("data")==null)
            return;

    }

    @Override
    public void loadAllCraftingRecipes(ArrayList<TemplateRecipeHandler.CachedRecipe> arecipes, String outputId, Object... results) {
        super.loadAllCraftingRecipes(arecipes, outputId, results);
    }

    @Override
    public void loadUsageRecipes(ArrayList<TemplateRecipeHandler.CachedRecipe> arecipes, ItemStack aInput) {
        if(!GT_Utility.areStacksEqual(ItemList.ClosedSpatialCell.get(1L),aInput,true)||aInput.getTagCompound()==null||aInput.getTagCompound().getString("data")==null)
            return;

    }

    @Override
    public int[][] getNEIInputCoords() {
        return super.getNEIInputCoords();
    }

    @Override
    public int[][] getNEIOutputCoords() {
        return super.getNEIOutputCoords();
    }

    @Override
    public int[][] getNEIFluidInputCoords() {
        return super.getNEIFluidInputCoords();
    }

    @Override
    public int[][] getNEIFluidOutputCoords() {
        return super.getNEIFluidOutputCoords();
    }
}
