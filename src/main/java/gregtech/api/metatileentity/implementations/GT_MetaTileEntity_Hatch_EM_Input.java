package gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.gui.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IEMConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.elementalmatter.GT_ComplexParticleStack;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_ClosedSpatialCell_Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GT_MetaTileEntity_Hatch_EM_Input extends GT_MetaTileEntity_Hatch implements IEMConnected {

    public boolean notifyOnEMAccepted = false;
    public GT_MetaTileEntity_EMMultiBlockBase multiBlock = null;

    public GT_MetaTileEntity_Hatch_EM_Input(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier), new String[]{
                "Elemental Matter Input for Multiblocks",
                "Capacity: " + getSlots(aTier) + " stack" + (getSlots(aTier) >= 2 ? "s" : "")});
    }

    public GT_MetaTileEntity_Hatch_EM_Input(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_EM_Input(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_IN)};
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_EM_Input(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        switch (mTier) {
            case 0:
                return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
            case 1:
                return new GT_Container_2by2(aPlayerInventory, aBaseMetaTileEntity);
            case 2:
                return new GT_Container_3by3(aPlayerInventory, aBaseMetaTileEntity);
            default:
                return new GT_Container_4by4(aPlayerInventory, aBaseMetaTileEntity);
        }
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        switch (mInventory.length) {
            case 1:
                return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, "Elemental Matter Input Hatch");
            case 4:
                return new GT_GUIContainer_2by2(aPlayerInventory, aBaseMetaTileEntity, "Elemental Matter Input Hatch");
            case 9:
                return new GT_GUIContainer_3by3(aPlayerInventory, aBaseMetaTileEntity, "Elemental Matter Input Hatch");
            case 16:
                return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, "Elemental Matter Input Hatch");
            default:
                return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, "Elemental Matter Input Hatch");
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            fillStacksIntoFirstSlots();
        }
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        for (int i = 0; i < mInventory.length; i++)
            for (int j = i + 1; j < mInventory.length; j++)
                if (mInventory[j] != null && (mInventory[i] == null || GT_Utility.areStacksEqual(mInventory[i], mInventory[j])))
                    GT_Utility.moveStackFromSlotAToSlotB(getBaseMetaTileEntity(), getBaseMetaTileEntity(), j, i, (byte) 64, (byte) 1, (byte) 64, (byte) 1);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == getBaseMetaTileEntity().getFrontFacing() && (aStack.getItem() instanceof GT_ClosedSpatialCell_Item);
    }

    @Override
    public boolean transfersEMAt(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean inputEMFrom(GT_ComplexParticleStack aParticle, byte aSide) {
        //inputEM
        multiBlock.hasEMBeenAccepted = true;
        return true;
    }
}
