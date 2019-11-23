package gregtech.common.items;

import appeng.client.render.items.ItemEncodedPatternRenderer;
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.objects.elementalmatter.*;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_ClosedSpatialCellRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;
import java.util.List;

public class GT_ClosedSpatialCell_Item extends GT_Generic_Item {

    IIconContainer[] mAtomIcons = new IIconContainer[128];

    public GT_ClosedSpatialCell_Item(String unlocalized, String english) {
        super(unlocalized, english, null);
        setMaxStackSize(16);
        setNoRepair();
        if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
            MinecraftForgeClient.registerItemRenderer( this, new GT_ClosedSpatialCellRenderer());
        }
        mAtomIcons[0] = new Textures.ItemIcons.CustomIcon("atoms/HE");
        mAtomIcons[1] = new Textures.ItemIcons.CustomIcon("atoms/HE1");
    }

    @Override
    protected void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        final boolean isShiftHeld = Keyboard.isKeyDown( Keyboard.KEY_LSHIFT ) || Keyboard.isKeyDown( Keyboard.KEY_RSHIFT );
        final boolean isControlHeld = Keyboard.isKeyDown( Keyboard.KEY_LCONTROL ) || Keyboard.isKeyDown( Keyboard.KEY_RCONTROL );

        NBTTagCompound tNBT = aStack.getTagCompound();
        if(tNBT == null)
            tNBT = new NBTTagCompound();
        GT_ComplexParticleStack aParticle = null;
        long time = System.nanoTime();
        if(tNBT.getString("data")!=null)
             aParticle = GT_ComplexParticleStack.loadFromString(tNBT.getString("data"));
        if(aParticle==null)
            return;
        System.out.println(System.nanoTime()-time);
        aList.add("Closed Spatial Cell Currently Contains:");
        aList.add(EnumChatFormatting.BLUE+"144'000"+EnumChatFormatting.RESET+EnumChatFormatting.GRAY+" of Helium"+EnumChatFormatting.RESET);
        GT_AtomStats aAtom = null;
        if(aParticle.mType == GT_ComplexParticleTypes.nuclide)
            aAtom = GT_AtomStats.getStats((short)(aParticle.getNucleusElectricCharge()/6),aParticle.getNeutronCount());
        if(isShiftHeld) {
            if(aAtom == null)
                return;
            aList.add(EnumChatFormatting.YELLOW  + aAtom.mName + EnumChatFormatting.RESET);
            aList.add(EnumChatFormatting.BLUE + "Z: " + EnumChatFormatting.RESET + EnumChatFormatting.GREEN + aParticle.getProtonCount() + EnumChatFormatting.RESET);
            aList.add(EnumChatFormatting.BLUE + "N: " + EnumChatFormatting.RESET + EnumChatFormatting.GREEN + aParticle.getNeutronCount() + EnumChatFormatting.RESET);
            aList.add(EnumChatFormatting.BLUE + "A: " + EnumChatFormatting.RESET + EnumChatFormatting.GREEN + (aParticle.getProtonCount()+aParticle.getNeutronCount()) + EnumChatFormatting.RESET);
            aList.add("Average Atomic Weight: " + EnumChatFormatting.BLUE + aAtom.mMass + EnumChatFormatting.RESET);
            aList.add("Half-Life: " + EnumChatFormatting.BLUE + (aAtom.mHalfLife==Double.MAX_VALUE?"Stable":aAtom.mHalfLife)+ EnumChatFormatting.RESET);
            double mass = aParticle.getMass();
            double enegry = aParticle.getEnergy();
            aList.add("mass: " + EnumChatFormatting.BLUE + GT_EM_Utility.convertMEVtoAMU(mass) + EnumChatFormatting.RESET+" AMU");
            aList.add("velocity: "+EnumChatFormatting.BLUE + aParticle.mVelocity + EnumChatFormatting.RESET + " m/s");
            aList.add("energy: " + EnumChatFormatting.BLUE + enegry + EnumChatFormatting.RESET+" MEV");
            aList.add("kinetic energy: " + EnumChatFormatting.BLUE + (enegry-mass) + EnumChatFormatting.RESET+" MEV");
            if(aAtom.mDecayModes.length>0) {
                aList.add("Possible Decay Modes: ");
                for (byte i = 0; i < aAtom.mDecayModes.length; i++) {
                    aList.add(EnumChatFormatting.BLUE + aAtom.mDecayModes[i]+EnumChatFormatting.RESET+EnumChatFormatting.GRAY + " with "+EnumChatFormatting.RESET + EnumChatFormatting.GREEN + aAtom.mDecayChances[i]*100f +EnumChatFormatting.RESET +EnumChatFormatting.GRAY+ " percent Chance"+EnumChatFormatting.RESET);
                }
            }
        } else if(isControlHeld){
            aList.add("e: " + EnumChatFormatting.BLUE + "2" + EnumChatFormatting.RESET);
            aList.add("n: " + EnumChatFormatting.BLUE + "2" + EnumChatFormatting.RESET);
            aList.add("A: " + EnumChatFormatting.BLUE + "4" + EnumChatFormatting.RESET);
            aList.add("possible decay modes:");
            aList.add(EnumChatFormatting.BLUE + "stable" + EnumChatFormatting.RESET);
        }



    }

    public GT_ComplexParticleStack getParticle(ItemStack aStack){
        NBTTagCompound tNBT = aStack.getTagCompound();
        String data;
        if(tNBT == null||(data = tNBT.getString("data"))==null)
            return null;
        return GT_ComplexParticleStack.loadFromString(data);
    }

    public void setParticle(ItemStack aStack,GT_ComplexParticleStack aParticle){
        NBTTagCompound tNBT = aStack.getTagCompound();
        if(tNBT == null)
            tNBT = new NBTTagCompound();
        tNBT.setString("data",aParticle.saveToString(aParticle));
        aStack.setTagCompound(tNBT);
    }

    public boolean decreaseAmount(ItemStack aStack, int aAmount){
        NBTTagCompound tNBT = aStack.getTagCompound();
        String data;
        if(tNBT == null||(data = tNBT.getString("data"))==null)
            return false;
        GT_ComplexParticleStack tParticle = GT_ComplexParticleStack.loadFromString(data);
        if(tParticle.mAmount>=aAmount)
            tParticle.mAmount-= aAmount;
        else
            return false;
        tNBT.setString("data",tParticle.saveToString(tParticle));
        aStack.setTagCompound(tNBT);
        return true;
    }

    public boolean increaseAmount(ItemStack aStack, int aAmount){
        NBTTagCompound tNBT = aStack.getTagCompound();
        String data;
        if(tNBT == null||(data = tNBT.getString("data"))==null)
            return false;
        GT_ComplexParticleStack tParticle = GT_ComplexParticleStack.loadFromString(data);
        if(tParticle.mAmount>=aAmount)
            tParticle.mAmount+= aAmount;
        else
            return false;
        tNBT.setString("data",tParticle.saveToString(tParticle));
        aStack.setTagCompound(tNBT);
        return true;
    }

    public void removeParticles(ItemStack aStack){
        NBTTagCompound tNBT = aStack.getTagCompound();
        String data;
        if(tNBT == null||(data = tNBT.getString("data"))==null)
            return;
            tNBT.removeTag("data");
            aStack.setTagCompound(tNBT);
    }

    public IIcon getShiftIcon(){
       return mAtomIcons[0].getIcon();
    }

    public IIcon getControlIcon(){
        return mAtomIcons[1].getIcon();
    }
}
