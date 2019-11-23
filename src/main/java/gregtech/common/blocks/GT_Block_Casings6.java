package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class GT_Block_Casings6
        extends GT_Block_Casings_Abstract {
    public GT_Block_Casings6() {
        super(GT_Item_Casings6.class, "gt.blockcasings6", GT_Material_Casings.INSTANCE);
        for (int i = 0; i < 16; i++) {
            Textures.BlockIcons.casingTexturePages[1][i+64] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Casing 1");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Casing 2");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Casing 3");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Casing 4");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Casing 5");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Casing 6");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Casing 7");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Casing 8");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Casing 9");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Casing 10");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Casing 11");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Casing 12");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Casing 13");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Casing 14");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Casing 15");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Casing 16");



        ItemList.Casing1.set(new ItemStack(this, 1, 0));
        ItemList.Casing2.set(new ItemStack(this, 1, 1));
        ItemList.Casing3.set(new ItemStack(this, 1, 2));
        ItemList.Casing4.set(new ItemStack(this, 1, 3));
        ItemList.Casing5.set(new ItemStack(this, 1, 4));
        ItemList.Casing6.set(new ItemStack(this, 1, 5));
        ItemList.Casing7.set(new ItemStack(this, 1, 6));
        ItemList.Casing8.set(new ItemStack(this, 1, 7));
        ItemList.Casing9.set(new ItemStack(this, 1, 8));
        ItemList.Casing10.set(new ItemStack(this, 1, 9));
        ItemList.Casing11.set(new ItemStack(this, 1, 10));
        ItemList.Casing12.set(new ItemStack(this, 1, 11));
        ItemList.Casing13.set(new ItemStack(this, 1, 12));
        ItemList.Casing14.set(new ItemStack(this, 1, 13));
        ItemList.Casing15.set(new ItemStack(this, 1, 14));
        ItemList.Casing16.set(new ItemStack(this, 1, 15));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return Textures.BlockIcons.CASING_1.getIcon();
            case 1:
                return Textures.BlockIcons.CASING_2.getIcon();
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



}
