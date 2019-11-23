package gregtech.common.net;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.net.GT_Packet;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import gregtech.common.items.GT_VolumetricFlask;
import gregtech.common.items.behaviors.Behaviour_HandTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;

public final class MessageSetTeleporterCoords extends GT_Packet {
    private int x,y,z, dimID, playerID;

    public MessageSetTeleporterCoords() {
        super(true);
    }

    public MessageSetTeleporterCoords(int x,int y,int z, int dimID, int playerID) {
        super(false);
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
        this.playerID = playerID;
    }

    public MessageSetTeleporterCoords(int x,int y,int z, EntityPlayer p) {
        super(false);
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = p.worldObj.provider.dimensionId;
        this.playerID = p.getEntityId();
    }

    @Override
    public byte getPacketID() {
        return 6;
    }

    @Override
    public byte[] encode() {
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput(16);
        tOut.writeInt(x);
        tOut.writeInt(y);
        tOut.writeInt(z);
        tOut.writeInt(dimID);
        tOut.writeInt(playerID);
        return tOut.toByteArray();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput aData) {
        return new MessageSetTeleporterCoords(aData.readInt(),aData.readInt(),aData.readInt(), aData.readInt(), aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        World w = DimensionManager.getWorld(dimID);
        if (w != null && w.getEntityByID(playerID) instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) w.getEntityByID(playerID)).getHeldItem();
            if ((stack != null) && (stack.stackSize > 0)) {
                Item item = stack.getItem();
                if(item instanceof GT_MetaBase_Item){
                    ArrayList<IItemBehaviour<GT_MetaBase_Item>> tList = ((GT_MetaBase_Item)item).getItemBehaviours(stack.getItemDamage());
                    if(tList!=null)
                    for(IItemBehaviour<GT_MetaBase_Item> tBeh:tList){
                        if(tBeh instanceof Behaviour_HandTeleporter){
                            ((Behaviour_HandTeleporter)tBeh).tpPlayer((EntityPlayer)w.getEntityByID(playerID),new int[]{x,y,z});
                        }
                    }
                }

            }
        }
    }
}