package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GT_Block_Casings8
        extends GT_Block_Casings_Abstract {
    public GT_Block_Casings8() {
        super(GT_Item_Casings8.class, "gt.blockcasings8", GT_Material_Casings.INSTANCE);
        for (int i = 0; i < 9; i = (i + 1)) {
            Textures.BlockIcons.casingTexturePages[1][i+48] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Chemically Inert Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "PTFE Pipe Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Robust HSS-G Machine Casing");//50
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Firebox HSS-G Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Pipe HSS-G Machine Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Durable HSS-G Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Super Heat-Proof Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Rubber Framebox");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Wire case");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Casing Collider Center");



        ItemList.Casing_Chemically_Inert.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Pipe_Polytetrafluoroethylene.set(new ItemStack(this, 1, 1));
        ItemList.Casing_RobustHSSG.set(new ItemStack(this,1,2));
        ItemList.Casing_Firebox_HSSG.set(new ItemStack(this,1,3));
        ItemList.Casing_Pipe_HSSG.set(new ItemStack(this,1,4));
        ItemList.Casing_Durable_HSSG.set(new ItemStack(this,1,5));
        ItemList.Casing_SuperHeatProof.set(new ItemStack(this,1,6));
        ItemList.Casing_Rubber.set(new ItemStack(this,1,7));
        ItemList.Casing_Wire.set(new ItemStack(this,1,8));
        ItemList.GlassCasing1.set(new ItemStack(this,1,9));

        setLightOpacity(0);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        if(aMeta == 41)
            aMeta = aMeta;
        switch (aMeta&31) {
            case 0:
                return Textures.BlockIcons.MACHINE_CASING_CHEMICALLY_INERT.getIcon();
            case 1:
                return Textures.BlockIcons.MACHINE_CASING_PIPE_POLYTETRAFLUOROETHYLENE.getIcon();
            case 2:
                return Textures.BlockIcons.MACHINE_CASING_ROBUST_HSSG.getIcon();
            case 3:
                return Textures.BlockIcons.MACHINE_CASING_FIREBOX_HSSG.getIcon();
            case 4:
                return Textures.BlockIcons.MACHINE_CASING_PIPE_HSSG.getIcon();
            case 5:
                return Textures.BlockIcons.MACHINE_CASING_DURABLE_HSSG.getIcon();
            case 6:
                return Textures.BlockIcons.MACHINE_CASING_SUPERHEATPROOF.getIcon();
            case 7:
                return Textures.BlockIcons.MACHINE_CASING_RUBBER.getIcon();
            case 8:
                return Textures.BlockIcons.CASING_WIRE.getIcon();
            case 9:
                if(((aSide == 2 || aSide == 3)&& (aMeta&32)==0)||((aSide == 4 || aSide == 5)&& (aMeta&32)==1))
                    return Textures.BlockIcons.CASING_COLLIDER_CENTER.getIcon();
                else
                    return Textures.BlockIcons.CASING_COLLIDER_CENTER_BORDER_TOP.getIcon();

            case 10:
                return Textures.BlockIcons.GLASSCASING_2.getIcon();
            case 11:
                return Textures.BlockIcons.GLASSCASING_3.getIcon();
            case 12:
                return Textures.BlockIcons.GLASSCASING_4.getIcon();
            case 13:
                return Textures.BlockIcons.GLASSCASING_5.getIcon();
            case 14:
                return Textures.BlockIcons.GLASSCASING_6.getIcon();
            case 15:
                return Textures.BlockIcons.GLASSCASING_7.getIcon();

        }

        return Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
    }


    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        Block block = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_);

        if (this == Blocks.glass || this instanceof GT_Block_Casings8)
        {
            if (p_149646_1_.getBlockMetadata(p_149646_2_, p_149646_3_, p_149646_4_) != p_149646_1_.getBlockMetadata(p_149646_2_ - Facing.offsetsXForSide[p_149646_5_], p_149646_3_ - Facing.offsetsYForSide[p_149646_5_], p_149646_4_ - Facing.offsetsZForSide[p_149646_5_]))
            {
                return true;
            }

            if (block == this)
            {
                return false;
            }
        }

        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
        super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);

        if(aWorld.getBlockMetadata(aX,aY,aZ)==9){
            int tMeta;
           if(((aWorld.getBlock(aX, aY, aZ-1)) == this) && ((tMeta = aWorld.getBlockMetadata(aX, aY, aZ-1)) == 9)) {
               tMeta = tMeta&31;
               updateMeta(aWorld,aX,aY,aZ-1,tMeta);
               updateMeta(aWorld,aX,aY,aZ,tMeta);
           }else if ((aWorld.getBlock(aX, aY, aZ+1) == this) && ((tMeta = aWorld.getBlockMetadata(aX, aY, aX+1)) == 9)){
               tMeta = tMeta&31;
               updateMeta(aWorld,aX,aY,aZ+1,tMeta);
               updateMeta(aWorld,aX,aY,aZ,tMeta);
           }else if(((aWorld.getBlock(aX-1, aY, aZ)) == this) && ((tMeta = aWorld.getBlockMetadata(aX-1, aY, aZ)) == 9)) {
                tMeta = 10;
                updateMeta(aWorld,aX-1,aY,aZ,tMeta);
                updateMeta(aWorld,aX,aY,aZ,tMeta);
           }else if ((aWorld.getBlock(aX+1, aY, aZ) == this) && ((tMeta = aWorld.getBlockMetadata(aX+1, aY, aZ)) == 9)){
                tMeta = 10;
                updateMeta(aWorld,aX+1,aY,aZ+1,tMeta);
                updateMeta(aWorld,aX,aY,aZ,tMeta);
           }
        }
        //if colider casing update meta
        //meta == 0 => colider casing faces 2 3
        //meta == 1 => colider casing faces 4 5
        //
        //
    }

    public void updateMeta(World aWorld, int aX, int aY, int aZ, int aMeta){
        aWorld.setBlockMetadataWithNotify(aX,aY,aZ,aMeta,0);
        //called when block placed and another block finds this and updates self meta
    }
}
