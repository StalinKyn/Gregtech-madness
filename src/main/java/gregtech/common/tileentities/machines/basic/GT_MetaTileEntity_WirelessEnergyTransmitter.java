package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IComputerModule;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IEnergyConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_Cover_None;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.common.covers.GT_Cover_Vent;
import gregtech.common.covers.GT_Cover_WirelessAcceptor;
import gregtech.common.items.behaviors.Behaviour_None;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.HashSet;

import static gregtech.api.enums.GT_Values.V;

public class GT_MetaTileEntity_WirelessEnergyTransmitter extends GT_MetaTileEntity_BasicMachine {

    int mRadius = 0;
    public GT_MetaTileEntity_WirelessEnergyTransmitter mController = null;
    public ArrayList<GT_MetaTileEntity_WirelessEnergyTransmitter> mTransmitters = new ArrayList<>(7);
    public ArrayList<ICoverable> mAccessors = new ArrayList<>();
    public boolean canWork = false;

    public GT_MetaTileEntity_WirelessEnergyTransmitter(int aID, String aName, String aNameRegional, int aTier,int aRadius) {
        super(aID, aName, aNameRegional, aTier, 16, new String[]{"Digging ore instead of you",
                "Work are"}, 0, 0, "Miner.png", "", new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/miner/OVERLAY_BOTTOM")));
        mRadius = aRadius;
    }

    public GT_MetaTileEntity_WirelessEnergyTransmitter(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName,int aRadius) {
        super(aName, aTier, 16, aDescription, aTextures, 0, 0, aGUIName, aNEIName);
        mRadius = aRadius;
    }

    public GT_MetaTileEntity_WirelessEnergyTransmitter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName,int aRadius) {
        super(aName, aTier, 16, aDescription, aTextures, 0, 0, aGUIName, aNEIName);
        mRadius = aRadius;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_WirelessEnergyTransmitter(mName, mTier, mDescriptionArray, mTextures, mGUIName, mNEIName,mRadius);
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public long maxEUStore() {
        return mTier == 1 ? 4096 : V[mTier] * 512;
    }

    public boolean consumeEU(int aAmount){
        return getBaseMetaTileEntity().decreaseStoredEnergyUnits(aAmount,false);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
           GT_Utility.sendChatToPlayer(aPlayer,""+canWork);
           onPostTick(getBaseMetaTileEntity(),228);
        return super.onWireCutterRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        System.out.println("click");
        mController = null;
        mTransmitters.clear();
        for(int i = 0; i < 2; i ++)
            for(int j = 0; j < 2; j ++)
                for(int k = 0; k < 2; k ++){
                    TileEntity tTile = getBaseMetaTileEntity().getTileEntity(getBaseMetaTileEntity().getXCoord()+mRadius*i,getBaseMetaTileEntity().getYCoord()+mRadius*j,getBaseMetaTileEntity().getZCoord()+mRadius*k);
                    if(tTile instanceof IGregTechTileEntity&&((IGregTechTileEntity)tTile).getMetaTileEntity()instanceof GT_MetaTileEntity_WirelessEnergyTransmitter){
                        mTransmitters.add((GT_MetaTileEntity_WirelessEnergyTransmitter)((IGregTechTileEntity)tTile).getMetaTileEntity());
                        ((GT_MetaTileEntity_WirelessEnergyTransmitter)((IGregTechTileEntity)tTile).getMetaTileEntity()).mController = this;
                    }
                    else {
                        mController = null;
                        mTransmitters.clear();
                        for(int x = -1; x < 1;x++)
                            for(int y = -1; y < 1;y++)
                                for(int z = -1; z < 1;z++){
                                    TileEntity rTile = getBaseMetaTileEntity().getTileEntity(getBaseMetaTileEntity().getXCoord()+mRadius*x,getBaseMetaTileEntity().getYCoord()+mRadius*y,getBaseMetaTileEntity().getZCoord()+mRadius*z);
                                    if(rTile instanceof IGregTechTileEntity&&((IGregTechTileEntity)rTile).getMetaTileEntity()instanceof GT_MetaTileEntity_WirelessEnergyTransmitter){
                                        HashSet<GT_MetaTileEntity_WirelessEnergyTransmitter> aSet = new HashSet<GT_MetaTileEntity_WirelessEnergyTransmitter>();
                                        aSet.add(this);
                                        ((GT_MetaTileEntity_WirelessEnergyTransmitter)((IGregTechTileEntity)rTile).getMetaTileEntity()).onScrewdriverRightClick(aSide,aPlayer,aX,aY,aZ, aSet);
                                    }
                                }
                    }
                }
        for(int x = 1; x<mRadius;x++)
            for(int y = 1; y<mRadius;y++)
                for(int z = 1; z<mRadius;z++){
                    TileEntity tTile = getBaseMetaTileEntity().getTileEntityOffset(x,y,z);
                    if(tTile instanceof IGregTechTileEntity)
                        mAccessors.add((IGregTechTileEntity)tTile);
                }
        System.out.println("ended");

    }

    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ, HashSet<GT_MetaTileEntity_WirelessEnergyTransmitter> aPassedSet) {
        if(aPassedSet.contains(this))
            return;
        aPassedSet.add(this);
        onScrewdriverRightClick(aSide,aPlayer,aX,aY,aZ);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if(!consumeEU(mTier)) {
            canWork = false;
            return;
        }
        canWork = true;
        if(this == mController){
            for(GT_MetaTileEntity_WirelessEnergyTransmitter aTile : mTransmitters){
                if(!aTile.canWork)
                    return;
            }
            nextAcessor:for(ICoverable aTile:mAccessors){
                if(aTile!=null){
                    long tEURequest = 0;
                    int[] tSides = {0,0,0,0,0,0};
                    for(byte i = 0;i<6;i++){
                       GT_CoverBehavior aCover = aTile.getCoverBehaviorAtSide(i);
                        if(aCover instanceof  GT_Cover_WirelessAcceptor){
                            tEURequest += ((GT_Cover_WirelessAcceptor)aCover).mVoltage;
                            tSides[i] = ((GT_Cover_WirelessAcceptor)aCover).mVoltage;
                        }
                    }
                    if(tEURequest==0)
                       continue;
                    for(GT_MetaTileEntity_WirelessEnergyTransmitter tTrans:mTransmitters){
                        if(tTrans.canGiveEU(tEURequest)){
                            for(byte i = 0; i < 6; i ++){
                                if(tSides[i]!=0&&aTile.injectEnergyUnits(i,tSides[i],1)!=0){
                                    tTrans.consumeEU(tSides[i]);
                                }

                            }
                            continue nextAcessor;

                        }
                    }

                }
            }

        }



    }

    @Override
    public long maxAmperesIn() {
        return 16;
    }

    public boolean canGiveEU(long aEUAmount){
        return getBaseMetaTileEntity().getStoredEU()>aEUAmount;
    }

    public void abortProcess(){
        if(mController==this){

        }
    }

}
