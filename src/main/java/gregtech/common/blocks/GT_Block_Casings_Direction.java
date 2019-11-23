package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GT_Block_Casings_Direction
        extends GT_Block_Casings_Abstract {
    public GT_Block_Casings_Direction() {
        super(GT_Item_CasingsDirection.class, "gt.blockcasingsdirection", GT_Material_Casings.INSTANCE);
        for (int i = 0; i < 16; i++) {
            Textures.BlockIcons.casingTexturePages[1][i+80] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Casing Collider Center");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Casing Collider Side");




        ItemList.Casing_Collider_Center.set(new ItemStack(this, 1, 0));

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        if(aMeta == 16)
            aMeta = aMeta;
        switch (aMeta&7) {
            case 0:
                if(((aSide == 2 || aSide == 3)&& (aMeta&8)==0)||((aSide == 4 || aSide == 5)&& (aMeta&8)==8))
                    return Textures.BlockIcons.CASING_COLLIDER_CENTER.getIcon();
                else
                    if((aMeta&8)==0)
                        return aSide==0||aSide==1?Textures.BlockIcons.CASING_COLLIDER_CENTER_BORDER_TOP.getIcon():Textures.BlockIcons.CASING_COLLIDER_CENTER_BORDER_SIDE.getIcon();
                     else
                         return aSide==0||aSide==1?Textures.BlockIcons.CASING_COLLIDER_CENTER_BORDER_SIDE.getIcon():Textures.BlockIcons.CASING_COLLIDER_CENTER_BORDER_SIDE.getIcon();
            case 1:
                return Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            case 2:
                return Textures.BlockIcons.CASING_3.getIcon();
            case 3:
                return Textures.BlockIcons.CASING_4.getIcon();
            case 4:
                return Textures.BlockIcons.CASING_5.getIcon();
            case 5:
                return Textures.BlockIcons.CASING_6.getIcon();
            case 6:
                return Textures.BlockIcons.CASING_7.getIcon();
            case 7:
                return Textures.BlockIcons.CASING_8.getIcon();
            case 8:
                return Textures.BlockIcons.CASING_9.getIcon();
            case 9:
                return Textures.BlockIcons.CASING_10.getIcon();
            case 10:
                return Textures.BlockIcons.CASING_11.getIcon();
            case 11:
                return Textures.BlockIcons.CASING_12.getIcon();
            case 12:
                return Textures.BlockIcons.CASING_13.getIcon();
            case 13:
                return Textures.BlockIcons.CASING_14.getIcon();
            case 14:
                return Textures.BlockIcons.CASING_15.getIcon();
            case 15:
                return Textures.BlockIcons.CASING_16.getIcon();

        }

        return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
    }

    @Override
    public IIcon getIcon(IBlockAccess aWorld, int aX, int aY, int aZ, int aSide) {
        int aMeta = aWorld.getBlockMetadata(aX,aY,aZ);
        if((aMeta&7)!=1)
            return super.getIcon(aWorld,aX,aY,aZ,aSide);

        if(aWorld.getBlock(aX,aY,aZ+2)==this&&aWorld.getBlockMetadata(aX,aY,aZ+2)==8&&
                aWorld.getBlock(aX-1,aY,aZ+1)==this&&aWorld.getBlockMetadata(aX-1,aY,aZ+1)==8&&
                (aSide==2||aSide==5))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        if(aWorld.getBlock(aX,aY,aZ-2)==this&&aWorld.getBlockMetadata(aX,aY,aZ-2)==8&&
                aWorld.getBlock(aX+1,aY,aZ-1)==this&&aWorld.getBlockMetadata(aX+1,aY,aZ-1)==8&&
                (aSide==3||aSide==4))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        if(aWorld.getBlock(aX-2,aY,aZ)==this&&aWorld.getBlockMetadata(aX-2,aY,aZ)==0&&
                aWorld.getBlock(aX-1,aY,aZ-1)==this&&aWorld.getBlockMetadata(aX-1,aY,aZ-1)==0&&
                (aSide==3||aSide==5))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        if(aWorld.getBlock(aX+2,aY,aZ)==this&&aWorld.getBlockMetadata(aX+2,aY,aZ)==0&&
                aWorld.getBlock(aX+1,aY,aZ+1)==this&&aWorld.getBlockMetadata(aX+1,aY,aZ+1)==0&&
                (aSide==2||aSide==4))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        if(aWorld.getBlock(aX,aY,aZ-2)==this&&aWorld.getBlockMetadata(aX,aY,aZ-2)==8&&
                aWorld.getBlock(aX-1,aY,aZ-1)==this&&aWorld.getBlockMetadata(aX-1,aY,aZ-1)==8&&
                (aSide==3||aSide==5))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        if(aWorld.getBlock(aX,aY,aZ+2)==this&&aWorld.getBlockMetadata(aX,aY,aZ+2)==8&&
                aWorld.getBlock(aX+1,aY,aZ+1)==this&&aWorld.getBlockMetadata(aX+1,aY,aZ+1)==8&&
                (aSide==2||aSide==4))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        if(aWorld.getBlock(aX+2,aY,aZ)==this&&aWorld.getBlockMetadata(aX+2,aY,aZ)==0&&
                aWorld.getBlock(aX+1,aY,aZ-1)==this&&aWorld.getBlockMetadata(aX+1,aY,aZ-1)==0&&
                (aSide==4||aSide==3))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        if(aWorld.getBlock(aX-2,aY,aZ)==this&&aWorld.getBlockMetadata(aX-2,aY,aZ)==0&&
                aWorld.getBlock(aX-1,aY,aZ+1)==this&&aWorld.getBlockMetadata(aX-1,aY,aZ+1)==0&&
                (aSide==2||aSide==5))
            return(aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        byte[] coords = new byte[]{};
        int meta = -1;
        a:for(byte x = -1; x <2; x++){
            for(byte y = -1;y<2; y++){
                for(byte z = -1; z<2; z++){
                    if(x!=0&&y!=0&&z!=0)
                        continue;
                    if(aWorld.getBlock(aX+x,aY+y,aZ+z)==this&&((((meta = aWorld.getBlockMetadata(aX+x, aY+y, aZ+z))&7)) == 0)){
                       if((((meta&8)==0)&&z!=0)||(((meta&8)==8)&&x!=0))
                           continue;
                        coords = new byte[]{x,y,z};
                        break a;
                    }
                }
            }
        }
        if(coords.length == 0)
            return super.getIcon(aWorld,aX,aY,aZ,aSide);
        byte x = (byte)-coords[0];
        byte y = (byte)-coords[1];
        byte z = (byte)-coords[2];
        meta = meta&8;
        if(y==0){
            if(x>0&&aSide==5)
                return (aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
            else if(x<0&&aSide==4)
                return (aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
            if(z>0&&aSide==3)
                return (aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
            else if(z<0&&aSide==2)
                return (aMeta&8)==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
        }
        if(((aSide==3||aSide==2)&&meta==8)||((aSide==4||aSide==5)&&meta==0)||((aSide==0||aSide==1)&&!(y<0&&aSide==0||y>0&&aSide==1)))
            return Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
        if(y == 1){
            if(x>0)
                return aSide==2?Textures.BlockIcons.COLLIDER_MIDDLE_1.getIcon():aSide==3?Textures.BlockIcons.COLLIDER_MIDDLE_3.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            else if (z>0)
                return aSide==4?Textures.BlockIcons.COLLIDER_MIDDLE_3.getIcon():aSide==5?Textures.BlockIcons.COLLIDER_MIDDLE_1.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            if(x<0)
                return aSide==2?Textures.BlockIcons.COLLIDER_MIDDLE_3.getIcon():aSide==3?Textures.BlockIcons.COLLIDER_MIDDLE_1.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            else if (z<0)
                return aSide==4?Textures.BlockIcons.COLLIDER_MIDDLE_1.getIcon():aSide==5?Textures.BlockIcons.COLLIDER_MIDDLE_3.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            else if(aSide==1)
                return (aMeta&8)==0?meta==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_TOP.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():meta==0?Textures.BlockIcons.COLLIDER_MIDDLE_ON_TOP.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();
            return ((aSide ==2||aSide==3)||(aSide ==4||aSide==5))?Textures.BlockIcons.COLLIDER_MIDDLE_2.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();

        }else if(y == 0){
            if(x>0&&(aSide==2)||z>0&&(aSide==5)||x<0&&(aSide==3)||z<0&&(aSide==4))
                return Textures.BlockIcons.COLLIDER_MIDDLE_4.getIcon();
            else if(z>0&&(aSide==4)||z<0&&(aSide==5)||x<0&&(aSide==2)||x>0&&(aSide==3))
                return Textures.BlockIcons.COLLIDER_MIDDLE_6.getIcon();
            else if(x>0&&aSide==4)
                return Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon();
            else if(x<0&&aSide==5)
                return Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon();
            return Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
        }else if(y==-1){
            if(x>0)
                return aSide==2?Textures.BlockIcons.COLLIDER_MIDDLE_7.getIcon():aSide==3?Textures.BlockIcons.COLLIDER_MIDDLE_9.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            else if (z>0)
                return aSide==4?Textures.BlockIcons.COLLIDER_MIDDLE_9.getIcon():aSide==5?Textures.BlockIcons.COLLIDER_MIDDLE_7.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            if(x<0)
                return aSide==2?Textures.BlockIcons.COLLIDER_MIDDLE_9.getIcon():aSide==3?Textures.BlockIcons.COLLIDER_MIDDLE_7.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            else if (z<0)
                return aSide==4?Textures.BlockIcons.COLLIDER_MIDDLE_7.getIcon():aSide==5?Textures.BlockIcons.COLLIDER_MIDDLE_9.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_DEFAULT.getIcon();
            else if(aSide==0)
                return (aMeta&8)==0?meta==0?Textures.BlockIcons.COLLIDER_MIDDLE_OFF_TOP.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_OFF_SIDE.getIcon():meta==0?Textures.BlockIcons.COLLIDER_MIDDLE_ON_TOP.getIcon():Textures.BlockIcons.COLLIDER_MIDDLE_ON_SIDE.getIcon();

            return Textures.BlockIcons.COLLIDER_MIDDLE_8.getIcon();

        }
        return Textures.BlockIcons.COLLIDER_MIDDLE_1.getIcon();

    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        int tMeta = aWorld.getBlockMetadata(aX,aY,aZ)&7;
        processUpdate(aWorld,aX,aY,aZ,tMeta);
        //if colider casing update meta
        //meta == 0 => colider casing faces 2 3
        //meta == 1 => colider casing faces 4 5
        //
        //
        for(byte x =-3;x<=3;x++){
            for(byte z =-3;z<=3;z++){
                if((x==z||Math.abs(x)+1==Math.abs(z)||Math.abs(x)==Math.abs(z)+1)&&x!=0&&z!=0){
                    processUpdate(aWorld,aX+x,aY,aZ+z);
                }
            }
        }



        //update z+ x+
        //update z++ x++
    }

    public void updateMeta(World aWorld, int aX, int aY, int aZ, int aMeta){
        aWorld.setBlockMetadataWithNotify(aX,aY,aZ,aMeta,2);
        //called when block placed and another block finds this and updates self meta
    }

    @Override
    public boolean onBlockEventReceived(World aWorld, int aX, int aY, int aZ, int aData1, int aData2) {
        processUpdate(aWorld,aX,aY,aZ,aWorld.getBlockMetadata(aX,aY,aZ));
        return super.onBlockEventReceived(aWorld,aX,aY,aZ,aData1,aData2);
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        processUpdate(aWorld,aX+1,aY,aZ,aWorld.getBlockMetadata(aX+1,aY,aZ));
        processUpdate(aWorld,aX-1,aY,aZ,aWorld.getBlockMetadata(aX-1,aY,aZ));
        processUpdate(aWorld,aX,aY,aZ+1,aWorld.getBlockMetadata(aX,aY,aZ+1));
        processUpdate(aWorld,aX,aY,aZ-1,aWorld.getBlockMetadata(aX,aY,aZ-1));
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetaData);
    }

    public void processUpdate(World aWorld, int aX, int aY, int aZ){
        processUpdate(aWorld,aX,aY,aZ,aWorld.getBlockMetadata(aX,aY,aZ));
    }

    public void processUpdate(World aWorld, int aX, int aY, int aZ, int tMeta){
        if((tMeta&7)==0&&aWorld.getBlock(aX,aY,aZ) == this){

            if(((aWorld.getBlock(aX, aY, aZ-1)) == this) && (((tMeta = aWorld.getBlockMetadata(aX, aY, aZ-1))&7) == 0)) {
                tMeta = tMeta&7;
                updateMeta(aWorld,aX,aY,aZ-1,tMeta);
                updateMeta(aWorld,aX,aY,aZ,tMeta);
            //    updateMeta(aWorld,aX,aY,aZ+1,tMeta);
            }else if ((aWorld.getBlock(aX, aY, aZ+1) == this) && (((tMeta = aWorld.getBlockMetadata(aX, aY, aZ+1))&7) == 0)){
                tMeta = tMeta&7;
                updateMeta(aWorld,aX,aY,aZ+1,tMeta);
                updateMeta(aWorld,aX,aY,aZ,tMeta);
            //    updateMeta(aWorld,aX,aY,aZ-1,tMeta);
            }else if(((aWorld.getBlock(aX-1, aY, aZ)) == this) && (((tMeta = aWorld.getBlockMetadata(aX-1, aY, aZ))&7) == 0)) {
                tMeta =  tMeta|8;
                updateMeta(aWorld,aX-1,aY,aZ,tMeta);
                updateMeta(aWorld,aX,aY,aZ,tMeta);
           //     updateMeta(aWorld,aX+1,aY,aZ,tMeta);
            }else if ((aWorld.getBlock(aX+1, aY, aZ) == this) && (((tMeta = aWorld.getBlockMetadata(aX+1, aY, aZ))&7) == 0)){
                tMeta = tMeta|8;
                updateMeta(aWorld,aX+1,aY,aZ,tMeta);
                updateMeta(aWorld,aX,aY,aZ,tMeta);
         //       updateMeta(aWorld,aX-1,aY,aZ,tMeta);
            }
            else{
                for(int x = -1; x <2;x +=2){
                    for(int z = -1; z <2;z +=2){
                        boolean a =(((tMeta = aWorld.getBlockMetadata(aX+2*x, aY, aZ+2*z))&7) == 0);
                        if (((aWorld.getBlock(aX+x, aY, aZ+z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+x, aY, aZ+z))&7) == 0)&&(aWorld.getBlock(aX+2*x, aY, aZ+z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+2*x, aY, aZ+z))&7) == 0))||
                            ((aWorld.getBlock(aX+x*3, aY, aZ+2*z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+3*x, aY, aZ+2*x))&7) == 0)&&(aWorld.getBlock(aX+2*x, aY, aZ+2*z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+2*x, aY, aZ+2*z))&7) == 0))){
                            updateMeta(aWorld,aX,aY,aZ,tMeta);
                        }else  if (((aWorld.getBlock(aX+x, aY, aZ+z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+x, aY, aZ+z))&7) == 0)&&(aWorld.getBlock(aX+x, aY, aZ+2*z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+x, aY, aZ+2*z))&7) == 0))||
                                ((aWorld.getBlock(aX+x*2, aY, aZ+3*z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+2*x, aY, aZ+3*x))&7) == 0)&&(aWorld.getBlock(aX+2*x, aY, aZ+2*z) == this) && (((tMeta = aWorld.getBlockMetadata(aX+2*x, aY, aZ+2*z))&7) == 0))){
                            updateMeta(aWorld,aX,aY,aZ,tMeta);
                        }

                    }
                }
            }


        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
        if(world instanceof World)
        processUpdate((World)world,x,y,z,world.getBlockMetadata(x,y,z));
    }
}
