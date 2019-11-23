package gregtech.common.items.behaviors;

import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class Behaviour_DataOrb
        extends Behaviour_None {
    public static void copyInventory(ItemStack[] aInventory, ItemStack[] aNewContent, int aIndexlength) {
        for (int i = 0; i < aIndexlength; i++) {
            if (aNewContent[i] == null) {
                aInventory[i] = null;
            } else {
                aInventory[i] = GT_Utility.copy(new Object[]{aNewContent[i]});
            }
        }
    }

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

    public static String getDataName(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            return "";
        }
        return tNBT.getString("mDataName");
    }

    public static String getDataTitle(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            return "";
        }
        return tNBT.getString("mDataTitle");
    }

    public static NBTTagCompound setDataName(ItemStack aStack, String aDataName) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        }
        tNBT.setString("mDataName", aDataName);
        aStack.setTagCompound(tNBT);
        return tNBT;
    }

    public static NBTTagCompound setDataTitle(ItemStack aStack, String aDataTitle) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        }
        tNBT.setString("mDataTitle", aDataTitle);
        aStack.setTagCompound(tNBT);
        return tNBT;
    }

    public static ItemStack[] getNBTInventory(ItemStack aStack) {
        ItemStack[] tInventory = new ItemStack[256];
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            return tInventory;
        }
        NBTTagList tNBT_ItemList = tNBT.getTagList("Inventory", 10);
        for (int i = 0; i < tNBT_ItemList.tagCount(); i++) {
            NBTTagCompound tag = tNBT_ItemList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if ((slot >= 0) && (slot < tInventory.length)) {
                tInventory[slot] = GT_Utility.loadItem(tag);
            }
        }
        return tInventory;
    }

    public static NBTTagCompound setNBTInventory(ItemStack aStack, ItemStack[] aInventory) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        }
        NBTTagList tNBT_ItemList = new NBTTagList();
        for (int i = 0; i < aInventory.length; i++) {
            ItemStack stack = aInventory[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                tNBT_ItemList.appendTag(tag);
            }
        }
        tNBT.setTag("Inventory", tNBT_ItemList);
        aStack.setTagCompound(tNBT);
        return tNBT;
    }

    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        if (!(getDataTitle(aStack).length() == 0)) {
            aList.add(getDataTitle(aStack));
            aList.add(getDataName(aStack));
        }
        if(aStack.getTagCompound()!=null&&aStack.getTagCompound().getBoolean("isComputer")){
            NBTTagCompound aTag = aStack.getTagCompound();
            aList.add("total capacity is: "+aTag.getInteger("capacitySize"));
            aList.add("used capacity is: "+aTag.getInteger("usedCapacity"));
            aList.add("data is:");
            for(int i = 0; i < aTag.getInteger("usedCapacity");i++) {
                aList.add(""+(1+i)+" " + GT_LanguageManager.getTranslation(aTag.getString("unlocalized"+i)));
            }
        }
        if(aStack.getTagCompound()!=null&&aStack.getTagCompound().getBoolean("isLocked"))
            aList.add(EnumChatFormatting.RED+"Is locked"+EnumChatFormatting.RESET);
        return aList;
    }
}
