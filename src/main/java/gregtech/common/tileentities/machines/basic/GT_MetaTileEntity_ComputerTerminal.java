package gregtech.common.tileentities.machines.basic;

import gregtech.api.datasystem.*;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IDataConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_DataCable;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.net.GT_Packet_CompletedResearches;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.common.gui.*;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import static gregtech.api.enums.GT_Values.NW;

import java.util.*;

public class GT_MetaTileEntity_ComputerTerminal extends GT_MetaTileEntity_BasicMachine implements IDataListener<Integer>, IDataConnected, INodeContainer, IDataDevice {

    public int mRecipeID = -1;
    public boolean mSetNewID = false;
    public GT_DataNode mNode;
    public GT_MetaTileEntity_DataSystemController mController;
    public HashSet<Integer> mDoneResearches =new HashSet<>();
    public HashSet<Integer> mProcessingResearches = new HashSet<>();
    public Collection<Integer> mStations = new HashSet<>();
    public IGregTechTileEntity mDataHatch = null;
    public HashMap<Integer,Integer> mResearchProgressMap = new HashMap<>();

    public GT_MetaTileEntity_ComputerTerminal(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, new String[]{"Copies seeds with efficiency: "+Math.min((aTier+5)*10,100)+"%","Uses UUMatter for each seed","The better crop the more UUMatter it needs","Can replicate only scanned seeds"}, 1, 1, "OrganicReplicator.png", "", new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/organic_replicator/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("OVERLAY_BOTTOM")));
    }

    public GT_MetaTileEntity_ComputerTerminal(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_ComputerTerminal(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ComputerTerminal(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if(!(mDataHatch == null||mDataHatch.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_DataAccess))
            mDataHatch = null;
        return new GT_GUIContainer_ComputerTerminal(aPlayerInventory.player,aBaseMetaTileEntity,mDoneResearches == null?new HashSet<Integer>():mDoneResearches,mProcessingResearches==null?new HashSet<Integer>():mProcessingResearches,mResearchProgressMap, mStations == null ? new ArrayList<Integer>():mStations, mDataHatch);
        //return new GT_GUIContainer_Research(null, new GT_Container_Research(aPlayerInventory,aBaseMetaTileEntity) );
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if(!(mDataHatch == null||mDataHatch.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_DataAccess))
            mDataHatch = null;
        return new GT_Container_ComputerTerminal(aPlayerInventory,aBaseMetaTileEntity);
       // return super.getServerGUI(aID,aPlayerInventory,aBaseMetaTileEntity);
    }

    public int checkRecipe(){
        return 0;
    }
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (ItemList.IC2_Crop_Seeds.isStackEqual(aStack));
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        /*onAdjacentBlockChange(aBaseMetaTileEntity.getXCoord()+1,aBaseMetaTileEntity.getYCoord(),aBaseMetaTileEntity.getZCoord());
        onAdjacentBlockChange(aBaseMetaTileEntity.getXCoord()-1,aBaseMetaTileEntity.getYCoord(),aBaseMetaTileEntity.getZCoord());
        onAdjacentBlockChange(aBaseMetaTileEntity.getXCoord(),aBaseMetaTileEntity.getYCoord(),aBaseMetaTileEntity.getZCoord()+1);
        onAdjacentBlockChange(aBaseMetaTileEntity.getXCoord(),aBaseMetaTileEntity.getYCoord(),aBaseMetaTileEntity.getZCoord()-1);*/
    }

    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return aFluid.isFluidEqual(Materials.UUMatter.getFluid(1L));
    }

    public int getCapacity() {
        return 100000;
    }

    @Override
    public void onDataAdded(Integer data) {

    }

    @Override
    public void onDataUpdated() {
        mDoneResearches = mController.getCompletedResearches();
        mStations = mController.mDevicesMap.values();
        NW.sendPacketToAllPlayersInRange(getBaseMetaTileEntity().getWorld(),new GT_Packet_CompletedResearches(true,getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(),getBaseMetaTileEntity().getZCoord(),mDoneResearches, mStations),getBaseMetaTileEntity().getXCoord(),getBaseMetaTileEntity().getZCoord());
    }

    @Override
    public boolean transfersDataAt(byte aSide) {
        return getBaseMetaTileEntity().getFrontFacing()!=aSide;
    }

    @Override
    public void acceptBundle(GT_InformationBundle aBundle) {

    }

    @Override
    public GT_DataNode getNode() {
        return mNode;
    }

    @Override
    public void onPacketStuck() {

    }

    @Override
    public void initConnections(GT_MetaTileEntity_DataSystemController aController, ArrayList<GT_MetaPipeEntity_DataCable> aCables, GT_DataNode aLastNode){
        mNode = new GT_DataNode(this);
        mController = aController;
        aController.mSystem.addConnection(new GT_NodeConnection(aCables, mNode,aLastNode));
        aController.addListener(this);
    }

    public void acceptServerInformationComplited(HashSet<Integer> aCollection, Collection<Integer> aStations){
        mDoneResearches.clear();
        mDoneResearches.addAll(aCollection);
        mStations = aStations;
    }

    public void acceptServerInformationProcessing(ArrayList<Integer> aRecipes, ArrayList<Integer> aProgress, Collection<Integer> aStations){
        mProcessingResearches.clear();
        mProcessingResearches.addAll(aRecipes);
        mResearchProgressMap.clear();
        Iterator<Integer> iterator = aRecipes.iterator();
        int n = 0;
        while (iterator.hasNext()){
            int i = iterator.next();
            mResearchProgressMap.put(i,aProgress.get(n));
            n++;
        }


    }

    @Override
    public void onProcessAborted() {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onTempDataUpdated(Collection<Integer> collection, Collection<Integer> process) {
        mStations = mController.mDevicesMap.values();
        NW.sendPacketToAllPlayersInRange(getBaseMetaTileEntity().getWorld(),new GT_Packet_CompletedResearches(false,getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getYCoord(),getBaseMetaTileEntity().getZCoord(),collection,process, mStations),getBaseMetaTileEntity().getXCoord(),getBaseMetaTileEntity().getZCoord());
    }

    public void startResearch(int aID){
        if(mController == null)
            return;
        mController.mSystem.sendInformation(getNode(),mController.mSystem.getPathToController(getNode()),new GT_MessageBundle(1,aID));
    }

    public void sendResearchToStation(int aResearchID, int aStationID){

    }

    public void saveRecipe(int mRecipeID, int mSlotID){
        mSlotID-=36;
        int freeSpace = 0;
        if(mDataHatch == null||mDataHatch.isDead())
            return;
        ItemStack aStack = mDataHatch.getStackInSlot(mSlotID);
        if(aStack==null|| !(ItemList.Tool_DataStick.isStackEqual(aStack,false,true)||ItemList.Tool_DataOrb.isStackEqual(aStack,false,true)||ItemList.Tool_DataCluster.isStackEqual(aStack,false,true)))
            return;
        NBTTagCompound tTag = aStack.getTagCompound();
        if(tTag == null)
            return;
        if(tTag.getBoolean("isLocked"))
            return;
        int size = tTag.getInteger("capacitySize");
        int usedCapacity = tTag.getInteger("usedCapacity");
        freeSpace+=(size-usedCapacity);
        if(freeSpace<=0)
            return;
        GT_Recipe.GT_Recipe_ResearchStation aRecipe = GT_Recipe.GT_Recipe_ResearchStation.mIDtoRecipeMap.get(mRecipeID);
        if(aRecipe == null)
            return;
        NBTTagCompound aStackTag = aRecipe.writeRecipeToNBT(new NBTTagCompound());
        tTag.setTag("researchItemTag"+(usedCapacity),aStackTag);
        tTag.setInteger("rID"+(usedCapacity),aRecipe.mID);
        tTag.setInteger("usedCapacity",(usedCapacity+1));
        aStack.setTagCompound(tTag);
        System.out.println("saved recipe");
        return;
    }

    @Override
    public void onAdjacentBlockChange(int aX, int aY, int aZ) {
       TileEntity tile =getBaseMetaTileEntity().getTileEntity(aX,aY,aZ);
       if(tile instanceof IGregTechTileEntity){
           mDataHatch = (IGregTechTileEntity)tile;
       }
    }
}
