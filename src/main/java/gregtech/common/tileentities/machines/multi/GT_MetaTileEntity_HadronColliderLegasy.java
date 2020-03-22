package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IGuideRenderer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IProjectable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_HadronColliderLegasy extends GT_MetaTileEntity_DataWorkerBaseLegasy implements IProjectable {

    public static String[][] mStruct = new String[][]{
            {
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNaaaaaaaaaNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNaaaaaaaaaaaaaaaNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaNNNNNaaaaaaaaaa",
                    "aaaaaaaaaNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNaaaaaaaaa",
                    "aaaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaaa",
                    "aaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaa",
                    "aaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaa",
                    "aaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaa",
                    "aaaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaaa",
                    "aaaaaaaaaNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNaaaaaaaaa",
                    "aaaaaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaNNNNNaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNaaaaaaaaaaaaaaaNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNaaaaaaaaaNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNNNNNaaaaaaaaaNNNNNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaNNNNNNNNaaaaaaaaaaaaaaaNNNNNNNNaaaaaaaaa",
                    "aaaaaaaaNNNNNNNaaaaaaaaaaaaaaaaaaaNNNNNNNaaaaaaaa",
                    "aaaaaaaNNNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNNNaaaaaaa",
                    "aaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaaa",
                    "aaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaa",
                    "aaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaaa",
                    "aaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaNNNNNNaaaaaaa",
                    "aaaaaaaaNNNNNNaaaaaaaaaaaaaaaaaaaaNNNNNNNaaaaaaaa",
                    "aaaaaaaaaNNNNNNNNaaaaaaaaaaaaaaaNNNNNNNNaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNNNNNaaaaaaaaaNNNNNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNNNNNaaaaaaaaaNNNNNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaNNNNNNNNaaaaaaaaaaaaaaaNNNNNNNNaaaaaaaaa",
                    "aaaaaaaaNNNNNNNaaaaaaaaaaaaaaaaaaaNNNNNNNaaaaaaaa",
                    "aaaaaaaNNNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNNNaaaaaaa",
                    "aaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaaa",
                    "aaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaa",
                    "aaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaaa",
                    "aaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaNNNNNNaaaaaaa",
                    "aaaaaaaaNNNNNNaaaaaaaaaaaaaaaaaaaaNNNNNNNaaaaaaaa",
                    "aaaaaaaaaNNNNNNNNaaaaaaaaaaaaaaaNNNNNNNNaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNNNNNaaaaaaaaaNNNNNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaNNNNcNNNNaaaaaaaaaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNNNNNaaaaaaaaaNNNNNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaNNNNNNNNaaaaaaaaaaaaaaaNNNNNNNNaaaaaaaaa",
                    "aaaaaaaaNNNNNNNaaaaaaaaaaaaaaaaaaaNNNNNNNaaaaaaaa",
                    "aaaaaaaNNNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNNNaaaaaaa",
                    "aaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaaa",
                    "aaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "NNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNN",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaa",
                    "aaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaa",
                    "aaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNNNaaaaaa",
                    "aaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaaaaaaNNNNNNaaaaaaa",
                    "aaaaaaaaNNNNNNaaaaaaaaaaaaaaaaaaaaNNNNNNNaaaaaaaa",
                    "aaaaaaaaaNNNNNNNNaaaaaaaaaaaaaaaNNNNNNNNaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNNNNNaaaaaaaaaNNNNNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNaaaaaaaaaNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNaaaaaaaaaaaaaaaNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaNNNNNaaaaaaaaaa",
                    "aaaaaaaaaNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNaaaaaaaaa",
                    "aaaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaaa",
                    "aaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaa",
                    "aaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaa",
                    "aaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaa",
                    "aaaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaaa",
                    "aaaaaaaaaNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNaaaaaaaaa",
                    "aaaaaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaNNNNNaaaaaaaaaa",
                    "aaaaaaaaaaaNNNNNNaaaaaaaaaaaaaaaNNNNNNaaaaaaaaaaa",
                    "aaaaaaaaaaaaaNNNNNNNaaaaaaaaaNNNNNNNaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}
    };

    public GT_MetaTileEntity_HadronColliderLegasy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_HadronColliderLegasy(String aName) {
        super(aName);
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "AssemblyLine.png");
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
    public boolean onWrenchRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        maintainMachine();
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return false;
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)}; //todo update textures
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16]};
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Флекс Line",
                "Size: 3x(5-16)x4, variable length",
                "Bottom: Steel Machine Casing(or Maintenance or Input Hatch),",
                "Input Bus (Last Output Bus), Steel Machine Casing",
                "Middle: Reinforced Glass, Assembly Liапрврпne, Reinforced Glass",
                "UpMiddle: Grate Machine Casing,",
                "    Assembler Machine Casing,",
                "    Grate Machine Casing (or Controller or Data Access Hatch)",
                "Top: Steel Casing(or Energy Hatch)",
                "Up to 16 repeating slices, last is Output Bus",
                "Optional 1x Data Access Hatch next to the Controller"};
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_HadronColliderLegasy(this.mName);
    }

    @Override
    public String[][] GetStructure() {
        return mStruct;
    }

    @Override
    public boolean RenderStructure(boolean aBuild, IGuideRenderer aRenderer, int[] aParams) {
        if(aParams[0]<1||aParams[0]>16)
            aParams[0] = 1;
        GT_Multiblock_Utility u = new GT_Multiblock_Utility(false,true,mStruct,new Object[]{'N', GregTech_API.sBlockCasings6, (byte)(aParams[0]-1), 192+aParams[0]-1 , new byte[]{1,2,3,4,5,6,7,8,9},10});//GT_Utility.sendChatToPlayer(aPlayer, ""+u.checkStructure(getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing(),aPlayer));
       u.RenderGuide(this.getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing(),aBuild, aRenderer);
        return true;
    }

    @Override
    public String[] GetParamNames() {
        return null;
    }


    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        GT_Multiblock_Utility u = new GT_Multiblock_Utility(false,true,mStruct,new Object[]{'N', GregTech_API.sBlockCasings4, (byte)12, 60 , new byte[]{1,2,3,4,5,6,7,8,9},10});
        u.RenderGuide(this.getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing(),true, null);
        return true;
    }
}
