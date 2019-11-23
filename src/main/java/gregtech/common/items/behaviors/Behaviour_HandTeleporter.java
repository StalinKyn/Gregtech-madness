package gregtech.common.items.behaviors;

import com.enderio.core.common.util.Util;
import com.enderio.core.common.vecmath.Vector3d;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.events.TeleporterUsingEvent;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.security.Key;
import java.util.List;

public class Behaviour_HandTeleporter
        extends Behaviour_None {

    public byte currentMode = 0;
    public boolean isUsed = false;

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        isUsed = true;
        if(currentMode==1){

        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if(isUsed&&currentMode!=2){
            isUsed = false;
            return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            changeMode(aPlayer);
        }
        else{
            switch (currentMode){
                case 0:
                    simpleClickTP(aPlayer,aWorld);
                    break;
                case 1:
                    savedcoordsTP(aStack,aPlayer,aWorld);
                    break;
                case 2:
                    teleporterLikeGUITP(aPlayer,aWorld);
                    break;
            }
        }


        return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
    }

    public void changeMode(EntityPlayer aPlayer){
        currentMode++;
        if(currentMode>2)
            currentMode=0;
        GT_Utility.sendChatToPlayer(aPlayer,"Set Mode: "+currentMode);
    }

    public void simpleClickTP(EntityPlayer aPlayer,World aWorld){
        Vector3d headVec = Util.getEyePositionEio(aPlayer);
        Vec3 start = headVec.getVec3();
        Vec3 lookVec = aPlayer.getLook(1.0F);
        double reach = 500;
        headVec.add(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach);
        MovingObjectPosition tRayCast = aWorld.rayTraceBlocks(start, headVec.getVec3());
        if(tRayCast.typeOfHit== MovingObjectPosition.MovingObjectType.BLOCK&&aWorld.getBlock(tRayCast.blockX,(tRayCast.blockY+1),tRayCast.blockZ).isAir(aWorld,tRayCast.blockX,(tRayCast.blockY+1),tRayCast.blockZ))
            teleport(aPlayer,aWorld,aPlayer.getHeldItem(),tRayCast.blockX,(tRayCast.blockY+1),tRayCast.blockZ);
    }

    public void savedcoordsTP(ItemStack aStack, EntityPlayer aPlayer, World aWorld){

    }

    public void teleporterLikeGUITP(EntityPlayer aPlayer,World aWorld){
        aPlayer.openGui(GT_Values.GT,1011,aWorld,0,0,0);
    }

    public void tpPlayer(EntityPlayer aPlayer,int[] coords){
        teleport(aPlayer,aPlayer.worldObj,aPlayer.getHeldItem(),coords[0],coords[1],coords[2]);
    }

    @Override
    public void onUpdate(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) {
        super.onUpdate(aItem, aStack, aWorld, aPlayer, aTimer, aIsInHand);
    }

    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {

        return aList;
    }

    public void teleport(EntityPlayer aPlayer, World aWorld, ItemStack aStack, int aX, int aY,int aZ){

            if (true) {//can reduce energy
                int tDistance = distanceCalculation(aPlayer,aX,aY,aZ);
                Object tObject = aPlayer;
                    if (((tObject instanceof Entity)) && (!((Entity) tObject).isDead)) {
                        Entity tEntity = (Entity) tObject;
                        TeleporterUsingEvent tEvent = new TeleporterUsingEvent(tEntity, aX, aY,aZ, aPlayer.dimension, false);
                        MinecraftForge.EVENT_BUS.post(tEvent);
                        if (!tEvent.isCanceled() ) {//consume eu
                            if (tEntity.ridingEntity != null) {
                                tEntity.mountEntity(null);
                            }
                            if (tEntity.riddenByEntity != null) {
                                tEntity.riddenByEntity.mountEntity(null);
                            }
                            ((EntityLivingBase) tEntity).setPositionAndUpdate(aX, aY, aZ);


                        }
                    }

            }

    }

    private int distanceCalculation(EntityPlayer aPlayer, int aX, int aY,int aZ) {
        return Math.abs(((int) Math.sqrt(Math.pow(aPlayer.posX - aX, 2.0D) + Math.pow(aPlayer.posY - aY, 2.0D) + Math.pow(aPlayer.posZ - aZ, 2.0D))));
    }
}
