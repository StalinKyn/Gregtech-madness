package gregtech.api.metatileentity.implementations;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.V;

public class GT_MetaTileEntity_Hatch_Dynamo extends GT_MetaTileEntity_Hatch {

    public int mAmpers = 1;

    public GT_MetaTileEntity_Hatch_Dynamo(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, new String[]{"Generating electric Energy from Multiblocks", "Puts out up to 1 Amp"});
    }

    public GT_MetaTileEntity_Hatch_Dynamo(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Dynamo(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_Dynamo(int aID, String aName, String aNameRegional, int aTier, int aAmperage) {
        super(aID, aName, aNameRegional, aTier, 0, new String[]{"Generating electric Energy from Multiblocks", "Puts out up to "+aAmperage+ ((aAmperage==1)?"1 Amp":aAmperage+" Amps")});
        mAmpers = aAmperage;
    }

    public GT_MetaTileEntity_Hatch_Dynamo(String aName, int aTier, String aDescription, ITexture[][][] aTextures, int aAmperage) {
        super(aName, aTier, 0, aDescription, aTextures);
        mAmpers = aAmperage;
    }

    public GT_MetaTileEntity_Hatch_Dynamo(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, int aAmperage) {
        super(aName, aTier, 0, aDescription, aTextures);
        mAmpers = aAmperage;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier]};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier]};
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
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 512 * mAmpers;
    }

    @Override
    public long maxEUOutput() {//actually used like max out voltage
        return V[mTier];
    }

    public long getMaxOutput(){//true max output
        return V[mTier]*mAmpers;
    }//true max EU flow

    @Override
    public long maxAmperesOut() {
        return mAmpers;
    }

    @Override
    public long maxEUStore() {
        return 512 + V[mTier + 1] * 2 * mAmpers;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Dynamo(mName, mTier, mDescriptionArray, mTextures, mAmpers);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setEUVar(maxEUOutput()*4);
    }
}
