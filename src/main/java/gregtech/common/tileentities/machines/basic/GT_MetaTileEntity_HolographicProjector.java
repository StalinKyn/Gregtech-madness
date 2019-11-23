package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IGuideRenderer;
import gregtech.api.interfaces.metatileentity.IProjectable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_GuideRendererParticle;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.threads.GT_Runnable_MachineBlockUpdate;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.gui.GT_Container_HolographicProjector;
import gregtech.common.gui.GT_Container_Teleporter;
import gregtech.common.gui.GT_GUIContainer_HolographicProjector;
import gregtech.common.gui.GT_GUIContainer_Teleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class GT_MetaTileEntity_HolographicProjector
        extends GT_MetaTileEntity_BasicMachine implements IGuideRenderer {

    public ArrayList<GT_GuideRendererParticle> mParticles = new ArrayList<>();

    public int param1 = 0;
    public int param2 = 0;
    public int param3 = 0;

    public String[] mParamNames = {" "," "," "};

    private long mTickSturctBuilded = 0;


    public GT_MetaTileEntity_HolographicProjector(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "Creates Holographic projection of any GregTech Multiblocks", 1, 1, "Scanner.png", "", new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_SCANNER), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_SCANNER), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_SCANNER), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_ACTIVE), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER)});
    }

    public GT_MetaTileEntity_HolographicProjector(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_HolographicProjector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_HolographicProjector(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    public int checkRecipe() {
        mParticles.clear();
         TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getFrontFacing());
        if((tTileEntity instanceof IGregTechTileEntity)&&(((IGregTechTileEntity) tTileEntity).getMetaTileEntity()instanceof IProjectable) ){
            IProjectable tMachine = (IProjectable)((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            String[][] tProject = tMachine.GetStructure();
            int energyConsume =tProject.length*tProject[0].length*tProject[0][0].length();
            mEUt = energyConsume;
            mMaxProgresstime = 40;
            tMachine.RenderStructure(false,this, new int[]{param1,param2,param3});
            return 2;
        }
      return 0;
    }

    public String[] getParamNames() {
        TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getFrontFacing());
        if((tTileEntity instanceof IGregTechTileEntity)&&(((IGregTechTileEntity) tTileEntity).getMetaTileEntity()instanceof IProjectable) ){
            IProjectable tMachine = (IProjectable)((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            return (tMachine.GetParamNames()==null?mParamNames:tMachine.GetParamNames());
        }
       return new String[]{" "," "," "};
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_HolographicProjector(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_HolographicProjector(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void startProcess() {
        super.startProcess();
    }

    @Override
    public void endProcess() {
      /*  super.endProcess();
        for(GT_GuideRendererParticle mParticle:mParticles){
            mParticle.setDead();
        }*/
    }

    public int getCapacity() {
        return 1000;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("param1",param1);
        aNBT.setInteger("param2",param2);
        aNBT.setInteger("param3",param3);
        super.saveNBTData(aNBT);

    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        param1 = aNBT.getInteger("param1");
        param2 = aNBT.getInteger("param2");
        param3 = aNBT.getInteger("param3");
        super.loadNBTData(aNBT);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
        if(aPlayer.capabilities.isCreativeMode&&aPlayer.isSneaking()){
            TileEntity tTileEntity = getBaseMetaTileEntity().getTileEntityAtSide(getBaseMetaTileEntity().getFrontFacing());
            if((tTileEntity instanceof IGregTechTileEntity)&&(((IGregTechTileEntity) tTileEntity).getMetaTileEntity()instanceof IProjectable) ){
                mTickSturctBuilded = getBaseMetaTileEntity().getTimer();
                GT_Runnable_MachineBlockUpdate.isProcessingAllowed = false;
                IProjectable tMachine = (IProjectable)((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                tMachine.RenderStructure(true,this,new int[]{param1,param2,param3});
            }
        }

    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if(mTickSturctBuilded!=0&&aTick>mTickSturctBuilded+500){
            mTickSturctBuilded = 0;
            GT_Runnable_MachineBlockUpdate.isProcessingAllowed = true;
        }
    }

    @Override
    public ArrayList<GT_GuideRendererParticle> getParticleArray() {
        return mParticles;
    }
}