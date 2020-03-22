package gregtech.api.datasystem;


public class GT_InformationBundle {
    public int mDataFlow;

    public boolean isAutomated = false;
    public GT_DataNode to = null;

    public GT_InformationBundle(int aDataFlow){
        mDataFlow = aDataFlow;
    }

    public  void  automate(GT_DataNode to){
        this.to = to;
        isAutomated = true;
    }
}
