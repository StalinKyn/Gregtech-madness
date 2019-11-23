package gregtech.common.items.behaviors;

import com.enderio.core.common.util.Util;
import com.enderio.core.common.vecmath.Vector3d;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class Behaviour_WirelessSoftMallet
        extends Behaviour_None {

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        if(aWorld.isRemote)
            return false;
        return super.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        Vector3d headVec = Util.getEyePositionEio(aPlayer);
        Vec3 start = headVec.getVec3();
        Vec3 lookVec = aPlayer.getLook(1.0F);
        double reach = 500;
        headVec.add(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        MovingObjectPosition tRayCast = aWorld.rayTraceBlocks(start, headVec.getVec3());
        if(tRayCast.typeOfHit== MovingObjectPosition.MovingObjectType.BLOCK&&aWorld.getTileEntity(tRayCast.blockX,(tRayCast.blockY),tRayCast.blockZ)instanceof IGregTechTileEntity){
            IGregTechTileEntity tTile = (IGregTechTileEntity)aWorld.getTileEntity(tRayCast.blockX,(tRayCast.blockY),tRayCast.blockZ);
            if(tTile.isAllowedToWork()){
                tTile.disableWorking();
                GT_Utility.sendChatToPlayer(aPlayer,"Processing Disabled");
            }
            else{
                tTile.enableWorking();
                GT_Utility.sendChatToPlayer(aPlayer,"Processing Enabled");
            }
        }
        return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
    }

    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {

        return aList;
    }
}
