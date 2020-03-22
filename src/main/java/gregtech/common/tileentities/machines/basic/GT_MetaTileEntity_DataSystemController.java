package gregtech.common.tileentities.machines.basic;

import gregtech.api.datasystem.*;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IDataConnected;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_DataCable;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe.GT_Recipe_ResearchStation;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_ComputerBase;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeResearchStationBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class GT_MetaTileEntity_DataSystemController extends GT_MetaTileEntity_BasicMachine implements INodeContainer, IDataConnected {

    public GT_DataSystem mSystem;
    public ArrayList<IResearcher> mWorkers = new ArrayList<>();
    public ArrayList<IDataProducer> mProducers = new ArrayList<>();
    public ArrayList<IDataHandler<Integer>> mHandlers = new ArrayList<>();
    public ArrayList<IDataListener<Integer>> mListeners = new ArrayList<>();
    public HashMap<IDataDevice,Integer> mDevicesMap = new HashMap<>();
    public ArrayList<ResearchOrder> mProcessingOrders = new ArrayList<>();

    protected ArrayList<GT_InformationBundle> mOrders = new ArrayList<>();

    GT_DataNode mNode;

    private int mUpdate, unprocessedBundles;

    public GT_MetaTileEntity_DataSystemController(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "Heats tools for hardening", 1, 1, "E_Oven.png", "", new ITexture[]{new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/ELECTRIC_OVEN/OVERLAY_BOTTOM"))});
    }

    public GT_MetaTileEntity_DataSystemController(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_DataSystemController(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DataSystemController(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if(getBaseMetaTileEntity().isClientSide())
            return;

        if(mUpdate>-10)
            mUpdate--;
        if(mUpdate == 0){
            try {
                if (mSystem == null)
                    initDataSystem();
                else
                    reInitDataSystem();
            }catch (Exception e){
                int a = 0;
            }
        }
        if(mSystem == null)
            return;
        mSystem.onUpdate();




       Iterator<ResearchOrder> iterator = mProcessingOrders.iterator();
       while (iterator.hasNext()){
           ResearchOrder order = iterator.next();
           if(order.mFinished){
               if(order.mSucceed) {
                   order.onRemoved();
                   iterator.remove();
               }
               else
                   order.restart();
           }
       }

    }

    @Override
    public boolean allowToCheckRecipe() {
        return false;
    }

    @Override
    public void acceptBundle(GT_InformationBundle aBundle) {
        if(aBundle.isAutomated){
            mSystem.sendInformation(getNode(),mSystem.getPathFromController(aBundle.to),aBundle);
        }
        if(aBundle instanceof GT_ResearchDoneBundle){
            for(IDataHandler<Integer> tHandler: mHandlers){
                if(tHandler.canStore(((GT_ResearchDoneBundle) aBundle).mResearch.mID)){
                    tHandler.saveData(((GT_ResearchDoneBundle) aBundle).mResearch.mID);
                    for(IDataListener<Integer> tListener: mListeners){
                        tListener.onDataUpdated();
                    }
                }
            }
        }
        if(aBundle instanceof  GT_MessageBundle){
            if(((GT_MessageBundle) aBundle).mID==1){
                for(IResearcher aDataWorker : mWorkers){
                    if(!aDataWorker.isProcessing()&&aDataWorker instanceof GT_MetaTileEntity_LargeResearchStationBase){
                        //todo: can process ? if so prepare computers to work
                        GT_Recipe_ResearchStation aResearch = GT_Recipe_ResearchStation.mIDtoRecipeMap.get(((GT_MessageBundle)aBundle).mInformation);
                        if(!aDataWorker.canResearch(aResearch))
                            continue;
                        GT_InformationBundle[] bundles = aDataWorker.requestDataBundle(aResearch);

                        a:for(GT_InformationBundle tBundle: bundles){
                            for(IDataProducer tProducer : mProducers){
                                if(tProducer.canProduce(tBundle)){
                                    tProducer.produceDataBundle(tBundle);
                                    continue a;
                                }
                            }
                            return;
                        }
                        ResearchOrder order = new ResearchOrder(bundles,aDataWorker,((GT_MessageBundle)aBundle).mInformation, this);
                        ((GT_MetaTileEntity_LargeResearchStationBase)aDataWorker).setOrder(order);
                        mProcessingOrders.add(order);
                            //((GT_MetaTileEntity_LargeResearchStationBase)aDataWorker).nextResearchID = ((GT_MessageBundle) aBundle).mInformation;
                        return;
                    }
                }
            }
        }
       // System.out.println("accepted data: "+aBundle.mDataFlow);
        //System.out.println("end " +System.currentTimeMillis());
        //System.out.println("nanos elapsed: "+( System.nanoTime() - aBundle.time));
    }

    protected void reInitDataSystem(){
        IDataDevice[] aWorkers = mWorkers.toArray(new IDataDevice[mWorkers.size()]);
        IDataDevice[] aProducers = mProducers.toArray(new IDataDevice[mProducers.size()]);
        mSystem.resetMaps();
        mSystem = null;
        mWorkers.clear();
        mProducers.clear();
        initDataSystem();
        for(IDataDevice aDevice : aWorkers){
            if(!mWorkers.contains(aDevice))
                aDevice.onDisconnected();
        }
        for(IDataDevice aDevice : aProducers){
            if(!mProducers.contains(aDevice))
                aDevice.onDisconnected();
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        mUpdate = 50;
    }

    protected void initDataSystem(){
        mSystem = new GT_DataSystem(this);
        mNode = new GT_DataNode(this);
        for(byte i = 0; i < 6; i++){
            TileEntity tile = getBaseMetaTileEntity().getTileEntityAtSide(i);
            if(tile instanceof  IGregTechTileEntity && ((IGregTechTileEntity) tile).getMetaTileEntity() instanceof GT_MetaPipeEntity_DataCable){
                GT_MetaPipeEntity_DataCable cable = (GT_MetaPipeEntity_DataCable)((IGregTechTileEntity) tile).getMetaTileEntity();
                if(cable.isConnectedAtSide(GT_Utility.getOppositeSide(i))){
                    cable.initConnections(GT_Utility.getOppositeSide(i),this, new HashSet<TileEntity>(),new ArrayList<GT_MetaPipeEntity_DataCable>(),mNode);
                }
            }
        }
    }

    @Override
    public GT_DataNode getNode() {
        return mNode;
    }

    public void onSystemChanged(){
        mUpdate = 50;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mUpdate = 50;
    }

    @Override
    public boolean transfersDataAt(byte aSide) {
        return getBaseMetaTileEntity().getBackFacing()!=aSide;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        reInitDataSystem();
        try {
            mSystem.sendInformation(getNode(), mSystem.getPathFromController(mWorkers.get(0).getNode()), new GT_InformationBundle(10));
        }catch (Exception e){
            int a = 0;
        }
    }

    public boolean addWorker(IResearcher aWorker){
        addDevice(aWorker);
        return mWorkers.add(aWorker);
    }

    public boolean addProducer(IDataProducer aProducer){
        addDevice(aProducer);
        return mProducers.add(aProducer);
    }

    public boolean addHandler(IDataHandler<Integer> aHandler){
        if(aHandler instanceof IDataDevice)
            addDevice((IDataDevice)aHandler);
        return mHandlers.add(aHandler);
    }

    public boolean addListener(IDataListener<Integer> aListener){
        if(aListener instanceof IDataDevice)
            addDevice((IDataDevice)aListener);
        return mListeners.add(aListener);
    }

    @Override
    public void onPacketStuck() {

    }

    public void addDevice(IDataDevice aDevice){
        if(mDevicesMap.get(aDevice)!=null)
            mDevicesMap.put(aDevice,mDevicesMap.size());
    }

    public HashSet<Integer> getCompletedResearches(){
        HashSet<Integer> tOut = new HashSet<>(100);
        for(IDataHandler<Integer> tHandler : mHandlers){
            tHandler.addAllDataToHashSet(tOut);
        }
        return tOut;
    }

    public HashSet<Integer> getProcessingResearches(){
        HashSet<Integer> tOut = new HashSet<>(10);
        for(IResearcher aWorker : mWorkers){
            Object o = aWorker.getProcessing();
            if(o instanceof GT_Recipe_ResearchStation)
                tOut.add(((GT_Recipe_ResearchStation) o).mID);
        }
        return tOut;
    }

    @Override
    public void initConnections(GT_MetaTileEntity_DataSystemController aController, ArrayList<GT_MetaPipeEntity_DataCable> aCables, GT_DataNode aLastNode) {

    }

    public void onDataUnpdated(){
        for(IDataListener<Integer> tListener: mListeners){
            tListener.onDataUpdated();
        }
    }

    public class ResearchOrder{
        public GT_InformationBundle[] mBundles;
        public boolean mSucceed = false;
        public boolean mFinished = false;
        public IResearcher mResearcher;
        public int mResearchID;
        public GT_MetaTileEntity_DataSystemController mController;

        public ResearchOrder(GT_InformationBundle[] aBundles, IResearcher aResearcher, int aResearchID, GT_MetaTileEntity_DataSystemController aController){
            mBundles = aBundles;
            mResearcher = aResearcher;
            mResearchID = aResearchID;
            mController = aController;
            ArrayList<Integer> ar = new ArrayList<>();
            ArrayList<Integer> arr = new ArrayList<>();
            for(IResearcher tResearcher:mController.mWorkers){
                ar.add((Integer)tResearcher.getProcessing());
                arr.add(tResearcher.getProgress());
            }
            for(IDataListener<Integer> listener:mController.mListeners){
                listener.onTempDataUpdated(ar,arr);
            }
        }

        public void onProcessFinished(boolean aSucceed){
            mSucceed = aSucceed;
            mFinished = true;
        }

        public void restart(){
            mFinished = false;
            IResearcher aDataWorker = mResearcher;
            if(!aDataWorker.isProcessing()&&aDataWorker instanceof GT_MetaTileEntity_LargeResearchStationBase){
                //todo: can process ? if so prepare computers to work
                GT_Recipe_ResearchStation aResearch = GT_Recipe_ResearchStation.mIDtoRecipeMap.get(mResearchID);
                if(!aDataWorker.canResearch(aResearch))
                   return;
                GT_InformationBundle[] bundles = aDataWorker.requestDataBundle(aResearch);

                a:for(GT_InformationBundle tBundle: bundles){
                    for(IDataProducer tProducer : mProducers){
                        if(tProducer.canProduce(tBundle)){
                            tProducer.produceDataBundle(tBundle);
                            continue a;
                        }
                    }
                    return;
                }

                //((GT_MetaTileEntity_LargeResearchStationBase)aDataWorker).nextResearchID = ((GT_MessageBundle) aBundle).mInformation;
                return;
            }
        }

        public void onRemoved() {
            ArrayList<Integer> ar = new ArrayList<>();
            ArrayList<Integer> arr = new ArrayList<>();
            for(IResearcher tResearcher:mController.mWorkers){
                ar.add((Integer)tResearcher.getProcessing());
                arr.add(tResearcher.getProgress());
            }
            for (IDataListener<Integer> tListener : mController.mListeners){
                tListener.onTempDataUpdated(ar,arr);
            }

        }



    }
}
