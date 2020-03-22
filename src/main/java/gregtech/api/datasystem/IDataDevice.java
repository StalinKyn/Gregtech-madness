package gregtech.api.datasystem;

public interface IDataDevice {

    void onProcessAborted();

    GT_DataNode getNode();

    void onDisconnected();

}
