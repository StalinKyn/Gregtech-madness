package gregtech.api.interfaces.metatileentity;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_Data_Packet;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Commutator;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;

public interface IMetaTileEntityDataCable extends IMetaTileEntity {
    public void transferRef(byte aSide, GT_MetaTileEntity_Commutator aHatch, HashSet<TileEntity> aAlreadyPassedSet);

}
