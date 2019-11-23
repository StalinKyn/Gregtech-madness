package gregtech.common.items.behaviors;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class Behaviour_DataStick
        extends Behaviour_None {

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        if(aPlayer.isSneaking()){

            NBTTagCompound aTag = aStack.getTagCompound();
            if(aTag == null)
                aTag = new NBTTagCompound();
            aTag.setBoolean("isLocked" ,!aTag.getBoolean("isLocked"));
            aStack.setTagCompound(aTag);
        }
        return super.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aSide, hitX, hitY, hitZ);
    }

    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        String tString = GT_Utility.ItemNBT.getBookTitle(aStack);
        if (GT_Utility.isStringValid(tString)) {
            aList.add(tString);
        }
        tString = GT_Utility.ItemNBT.getBookAuthor(aStack);
        if (GT_Utility.isStringValid(tString)) {
            aList.add("by " + tString);
        }
        short tMapID = GT_Utility.ItemNBT.getMapID(aStack);
        if (tMapID >= 0) {
            aList.add("Map ID: " + tMapID);
        }
        tString = GT_Utility.ItemNBT.getPunchCardData(aStack);
        if (GT_Utility.isStringValid(tString)) {
            aList.add("Punch Card Data");
            int i = 0;int j = tString.length();
            for (; i < j; i += 64) {
                aList.add(tString.substring(i, Math.min(i + 64, j)));
            }
        }
        if(aStack.getTagCompound()!=null&&aStack.getTagCompound().getBoolean("isComputer")){
            NBTTagCompound aTag = aStack.getTagCompound();
            aList.add("total capacity is: "+aTag.getInteger("capacitySize"));
            aList.add("used capacity is: "+aTag.getInteger("usedCapacity"));
            aList.add("data is: "+ GT_LanguageManager.getTranslation(aTag.getString("unlocalized0")));
        }
        if(aStack.getTagCompound()!=null&&aStack.getTagCompound().getBoolean("isLocked"))
            aList.add(EnumChatFormatting.RED+"Is locked"+EnumChatFormatting.RESET);
        return aList;
    }
}
