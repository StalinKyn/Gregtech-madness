package gregtech.api.datasystem;

public interface IDataProducer extends IDataDevice {

    boolean canProduce(GT_InformationBundle aBundle);

    boolean produceDataBundle(GT_InformationBundle aBundle);

}
