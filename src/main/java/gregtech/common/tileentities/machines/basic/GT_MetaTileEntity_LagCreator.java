package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_LagCreator
        extends GT_MetaTileEntity_BasicMachine {
    int mSleepTime = 0;

    public GT_MetaTileEntity_LagCreator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "Puts things into Boxes", 2, 1, "Packager.png", "", new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_BOXINATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_BOXINATOR), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_BOXINATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_BOXINATOR), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_BOXINATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_BOXINATOR), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_BOXINATOR_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_BOXINATOR)});
    }

    public GT_MetaTileEntity_LagCreator(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_LagCreator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LagCreator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        try {
            Thread.sleep(mSleepTime);
        }catch (InterruptedException e){

        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if(getBaseMetaTileEntity().isServerSide()){
            if(aPlayer.isSneaking()){
                GT_Utility.sendChatToPlayer(aPlayer,"sleeping time is: "+ --mSleepTime);
            }
            else{
                GT_Utility.sendChatToPlayer(aPlayer,"sleeping time is: "+ ++mSleepTime);

            }
            if(mSleepTime<0)
                mSleepTime = 0;
        }
    }
}
