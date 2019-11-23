package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IGuideRenderer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IProjectable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Utility;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;

public class GT_MetaTileEntity_TeslaTower extends GT_MetaTileEntity_MultiBlockBase implements IProjectable {

    public static HashMap<World,ArrayList<GT_MetaTileEntity_TeslaTower>> mTowers= new HashMap<>();
    public static HashMap<World, Long[][]> mWorldElectricity = new HashMap<>();
    public ArrayList<GT_MetaTileEntity_TeslaTower> mTowerList;
    public Long[][] mThisWorldElectricity;
    public World mWorld;
    public int[] mPosition;
    public double tEfficiency = 0F;
    public boolean mSender;
    public long mEnergyConsumed = 0;
    public long tWorldEU = 0;
    public int[] aArray;

    byte lastUsedStruct = -1;
    short counter = 0;


    //region Structure
    public String[][][] mStructures = new String[][][]{
            {
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaWcWaaaaa",
                    "aaQaaWWWaaQaa",
                    "aaaaaWWWaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaWWWaaaaa",
                    "aaQaaWWWaaQaa",
                    "aaaaaWWWaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaWWWaaaaa",
                    "aaQaaWWWaaQaa",
                    "aaaaaWWWaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaQaaaEaaaQaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaQaaaEaaaQaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQQQQQaaaa",
                    "aaaQQaaaQQaaa",
                    "aaaQaaaaaQaaa",
                    "aaaQaaEaaQaaa",
                    "aaaQaaaaaQaaa",
                    "aaaQQaaaQQaaa",
                    "aaaaQQQQQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaQaEaQaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaQaEaQaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaRRRaaaaa",
                    "aaaRRRaRRRaaa",
                    "aaRRaaaaaRRaa",
                    "aaRaaaaaaaRaa",
                    "aRRaaQaQaaRRa",
                    "aRaaaaEaaaaRa",
                    "aRRaaQaQaaRRa",
                    "aaRaaaaaaaRaa",
                    "aaRRaaaaaRRaa",
                    "aaaRRRaRRRaaa",
                    "aaaaaRRRaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaRRRaaaaa",
                    "aaaRRRRRRRaaa",
                    "aaRRRRRRRRRaa",
                    "aRRRRRERRRRRa",
                    "aRRRaaEaaRRRa",
                    "RRRRaQEQaRRRR",
                    "RRREEEEEEERRR",
                    "RRRRaQEQaRRRR",
                    "aRRRaaEaaRRRa",
                    "aRRRRRERRRRRa",
                    "aaRRRRRRRRRaa",
                    "aaaRRRRRRRaaa",
                    "aaaaaRRRaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaRRRaaaaa",
                    "aaaRRRaRRRaaa",
                    "aaRRaaaaaRRaa",
                    "aaRaaaaaaaRaa",
                    "aRRaaaaaaaRRa",
                    "aRaaaaaaaaaRa",
                    "aRRaaaaaaaRRa",
                    "aaRaaaaaaaRaa",
                    "aaRRaaaaaRRaa",
                    "aaaRRRaRRRaaa",
                    "aaaaaRRRaaaaa",
                    "aaaaaaaaaaaaa"}


    },
            {
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaWcWaaaaa",
                            "aaQaaWWWaaQaa",
                            "aaaaaWWWaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaWWWaaaaa",
                            "aaQaaWWWaaQaa",
                            "aaaaaWWWaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaWWWaaaaa",
                            "aaQaaWWWaaQaa",
                            "aaaaaWWWaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaQaaaEaaaQaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaQaaaEaaaQaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaaaaQaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaQQQQQaaaa",
                            "aaaQQaaaQQaaa",
                            "aaaQaaaaaQaaa",
                            "aaaQaaEaaQaaa",
                            "aaaQaaaaaQaaa",
                            "aaaQQaaaQQaaa",
                            "aaaaQQQQQaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaEaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaEaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaEaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaEaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaQaaEaaQaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaQaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQQQaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaQaEaQaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaQQQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQQQaaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaQaEaQaaaa",
                            "aaaaQaaaQaaaa",
                            "aaaaaQQQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaEaaaaaa",
                            "aaaaaQaQaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaRRRaaaaa",
                            "aaaRRRaRRRaaa",
                            "aaRRaaaaaRRaa",
                            "aaRaaaaaaaRaa",
                            "aRRaaQaQaaRRa",
                            "aRaaaaEaaaaRa",
                            "aRRaaQaQaaRRa",
                            "aaRaaaaaaaRaa",
                            "aaRRaaaaaRRaa",
                            "aaaRRRaRRRaaa",
                            "aaaaaRRRaaaaa",
                            "aaaaaaaaaaaaa"},
                    {
                            "aaaaaRRRaaaaa",
                            "aaaRRRRRRRaaa",
                            "aaRRRRRRRRRaa",
                            "aRRRRRERRRRRa",
                            "aRRRaaEaaRRRa",
                            "RRRRaQEQaRRRR",
                            "RRREEEEEEERRR",
                            "RRRRaQEQaRRRR",
                            "aRRRaaEaaRRRa",
                            "aRRRRRERRRRRa",
                            "aaRRRRRRRRRaa",
                            "aaaRRRRRRRaaa",
                            "aaaaaRRRaaaaa"},
                    {
                            "aaaaaaaaaaaaa",
                            "aaaaaRRRaaaaa",
                            "aaaRRRaRRRaaa",
                            "aaRRaaaaaRRaa",
                            "aaRaaaaaaaRaa",
                            "aRRaaaaaaaRRa",
                            "aRaaaaaaaaaRa",
                            "aRRaaaaaaaRRa",
                            "aaRaaaaaaaRaa",
                            "aaRRaaaaaRRaa",
                            "aaaRRRaRRRaaa",
                            "aaaaaRRRaaaaa",
                            "aaaaaaaaaaaaa"}


            },{
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaWcWaaaaa",
                    "aaQaaWWWaaQaa",
                    "aaaaaWWWaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaWWWaaaaa",
                    "aaQaaWWWaaQaa",
                    "aaaaaWWWaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaWWWaaaaa",
                    "aaQaaWWWaaQaa",
                    "aaaaaWWWaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaQaaaEaaaQaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaQaaaEaaaQaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaaaaQaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQQQQQaaaa",
                    "aaaQQaaaQQaaa",
                    "aaaQaaaaaQaaa",
                    "aaaQaaEaaQaaa",
                    "aaaQaaaaaQaaa",
                    "aaaQQaaaQQaaa",
                    "aaaaQQQQQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaQaaEaaQaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaQaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaQaEaQaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaQaEaQaaaa",
                    "aaaaQaaaQaaaa",
                    "aaaaaQQQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaEaaaaaa",
                    "aaaaaQaQaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaRRRaaaaa",
                    "aaaRRRaRRRaaa",
                    "aaRRaaaaaRRaa",
                    "aaRaaaaaaaRaa",
                    "aRRaaQaQaaRRa",
                    "aRaaaaEaaaaRa",
                    "aRRaaQaQaaRRa",
                    "aaRaaaaaaaRaa",
                    "aaRRaaaaaRRaa",
                    "aaaRRRaRRRaaa",
                    "aaaaaRRRaaaaa",
                    "aaaaaaaaaaaaa"},
            {
                    "aaaaaRRRaaaaa",
                    "aaaRRRRRRRaaa",
                    "aaRRRRRRRRRaa",
                    "aRRRRRERRRRRa",
                    "aRRRaaEaaRRRa",
                    "RRRRaQEQaRRRR",
                    "RRREEEEEEERRR",
                    "RRRRaQEQaRRRR",
                    "aRRRaaEaaRRRa",
                    "aRRRRRERRRRRa",
                    "aaRRRRRRRRRaa",
                    "aaaRRRRRRRaaa",
                    "aaaaaRRRaaaaa"},
            {
                    "aaaaaaaaaaaaa",
                    "aaaaaRRRaaaaa",
                    "aaaRRRaRRRaaa",
                    "aaRRaaaaaRRaa",
                    "aaRaaaaaaaRaa",
                    "aRRaaaaaaaRRa",
                    "aRaaaaaaaaaRa",
                    "aRRaaaaaaaRRa",
                    "aaRaaaaaaaRaa",
                    "aaRRaaaaaRRaa",
                    "aaaRRRaRRRaaa",
                    "aaaaaRRRaaaaa",
                    "aaaaaaaaaaaaa"}


    }
    };
    //endregion

    public GT_MetaTileEntity_TeslaTower(int aID, String aName, String aNameRegional, boolean aSender) {
        super(aID, aName, aNameRegional);
        mSender = aSender;
    }

    public GT_MetaTileEntity_TeslaTower(String aName, boolean aSender) {
        super(aName);
        mSender = aSender;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TeslaTower(this.mName,this.mSender);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Processing Array"
        };
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[48], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[48]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ProcessingArray.png");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        byte aSide = getBaseMetaTileEntity().getBackFacing();
        IGregTechTileEntity tTileEntity = getBaseMetaTileEntity();
        mWorld = tTileEntity.getWorld();
        mPosition = new int[]{tTileEntity.getXCoord()+ForgeDirection.getOrientation(aSide).offsetX,tTileEntity.getYCoord(),tTileEntity.getZCoord()+ForgeDirection.getOrientation(aSide).offsetZ};
        if(mTowers.get(mWorld)==null)
            mTowers.put(mWorld,mTowerList = new ArrayList<>());
        else
            mTowerList = mTowers.get(mWorld);
        if(mWorldElectricity.get(mWorld)==null)
            mWorldElectricity.put(mWorld,mThisWorldElectricity = new Long[][]{{-1L,-1L,0L,0L},{-1L,-1L,0L,0L},{-1L,-1L,0L,0L}});// 3 sizes of towers, {node xPos, node zPos, net Electricity} 0 <= nodePos <= height*4
        else
            mThisWorldElectricity = mWorldElectricity.get(mWorld);

    }

    public void checkNearbySpace(){
        new GT_Runnable_CheckAir(this).run();
    }

    public int[] getMearbyBlocks(){
        int xCenter,yCenter,zCenter,mHeight;
        IGregTechTileEntity baseTile;
        Block exeption = GregTech_API.sBlockCasings8;
        byte meta = 7;
        int xp,xm,zp,zm,yt,innerRadius = 7;
        xCenter = mPosition[0];yCenter = mPosition[1]+getHeight();zCenter = mPosition[2];
        mHeight = getHeight()/2+innerRadius;
        baseTile = getBaseMetaTileEntity();
        xp = xCenter+innerRadius;
        xm = xCenter-innerRadius;
        zp = zCenter+innerRadius;
        zm = zCenter-innerRadius;
        yt = yCenter+1;
        System.out.println("Running "+xCenter+" "+yCenter+" "+zCenter+" "+xp+" "+xm+" "+zp+" "+zm+" "+yt);
        for(int i = xCenter-mHeight;i<xCenter+mHeight;i++){
            for(int j = zCenter-mHeight;j<zCenter+mHeight;j++) {
                for(int h = yCenter-mHeight;h<yCenter+mHeight;h++) {
                    System.out.println("loop "+i+" "+h+" "+j);
                    if(i>xm&&i<xp&&j>zm&&j<zp&&h<yt) {
                        System.out.println("inner "+i+" "+h+" "+j);
                        continue;
                    }
                    Block aBlock = baseTile.getBlock(i,h,j);
                    if(aBlock == Blocks.air) {
                        System.out.println("air");
                        continue;
                    }else if(aBlock==exeption&&baseTile.getMetaID(i,h,j) == meta){
                        System.out.println("exeption");
                        continue;
                    }
                    else {
                        System.out.println("wrong block!!! "+aBlock.getLocalizedName()+" "+aBlock.getUnlocalizedName());

                        aArray = new int[]{i,h,j};
                        return new int[]{i,h,j};
                    }


                }
            }
        }
        return null;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
            return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    public boolean checkRecipe(ItemStack aStack) {
        counter++;
        if(counter>120){
            checkNearbySpace();
            counter = -480;
        }
        if(aArray!=null){

            mWorld.addWeatherEffect(new EntityLightningBolt(mWorld, mPosition[0], mPosition[1]+getHeight()+1 , mPosition[2]));
            mWorld.addWeatherEffect(new EntityLightningBolt(mWorld, aArray[0], aArray[1], aArray[2]));
            if(GregTech_API.sMachineExplosions)
                mWorld.createExplosion(null, aArray[0] + 0.5, aArray[1] + 0.5, aArray[2] + 0.5, 20F, true);

            aArray = null;
            mThisWorldElectricity[lastUsedStruct][2] = 0L;
            checkNearbySpace();
        }
        float tConfigAdjust = 1;
        if (mInventory[1] != null && mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit")) {
            float circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                tConfigAdjust = circuit_config/25;
            }

        }
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        if(mSender){
            mEUt = -1;
            this.mMaxProgresstime = 20;
            return true;


        }else {
            long tVoltage = mDynamoHatches.get(0).maxEUOutput();
            if(mThisWorldElectricity[lastUsedStruct][2]>tVoltage*20){
                mMaxProgresstime = 20;
                mEUt = (int) tVoltage;
                System.out.println("volatge "+tVoltage);
                return true;
            }
        }
        return false;
    }

    public int checkCables(){
        int yOffset = -1;
        int aReturnValue = 0;
        while(getBaseMetaTileEntity().getBlock(mPosition[0],mPosition[1]+yOffset,mPosition[2]) == GregTech_API.sBlockCasings8&&getBaseMetaTileEntity().getMetaID(mPosition[0],mPosition[1]+yOffset,mPosition[2])==8){
            aReturnValue++;
            yOffset--;
        }
        if(getBaseMetaTileEntity().getBlock(mPosition[0],mPosition[1]+yOffset,mPosition[2]) == Blocks.bedrock)
            return aReturnValue;
        return 0;

    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if(lastUsedStruct>=0){
            GT_Multiblock_Utility u = new GT_Multiblock_Utility(false,true,mStructures[lastUsedStruct],new Object[]{'Q',GregTech_API.sBlockCasings8, (byte)7, 0 , new byte[]{},10,'W', GregTech_API.sBlockCasings4, (byte)14, 48, new byte[]{5,6,7}, 10, 'E', GregTech_API.sBlockCasings8, (byte)8, 0 , new byte[]{},10,'R', GregTech_API.sBlockCasings5, (byte)0, 0 , new byte[]{},10},(byte)-1);//GT_Utility.sendChatToPlayer(aPlayer, ""+u.checkStructure(getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing(),aPlayer));
            if(u.checkStructure(getBaseMetaTileEntity(),this, getBaseMetaTileEntity().getFrontFacing())) {
                if(checkCables()<getHeight()*2)
                    return false;
                onStructureCheckSuccess();
                return true;
            } else {
                lastUsedStruct = -1;
                mTowerList.remove(this);
                return false;
            }
        }
        for(byte c = 0; c < mStructures.length; c++){
            GT_Multiblock_Utility u = new GT_Multiblock_Utility(false,true,mStructures[c],new Object[]{'Q',GregTech_API.sBlockCasings8, (byte)7, 0 , new byte[]{},10,'W', GregTech_API.sBlockCasings4, (byte)14, 48, new byte[]{5,6,7}, 10, 'E', GregTech_API.sBlockCasings8, (byte)8, 0 , new byte[]{},10,'R', GregTech_API.sBlockCasings5, (byte)0, 0 , new byte[]{},10},(byte)-1);//GT_Utility.sendChatToPlayer(aPlayer, ""+u.checkStructure(getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing(),aPlayer));
            if(u.checkStructure(getBaseMetaTileEntity(),this, getBaseMetaTileEntity().getFrontFacing())){
                lastUsedStruct = c;
                onStructureCheckSuccess();
                return true;
            }
        }
        mTowerList.remove(this);
        return false;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(mSender&& getPowerLimit((byte) Math.max(1, GT_Utility.getTier(getMaxInputVoltage())))>mThisWorldElectricity[lastUsedStruct][2]) {
            for(GT_MetaTileEntity_Hatch_Energy aHatch:mEnergyHatches){
                if(aHatch.getBaseMetaTileEntity().getUniversalEnergyStored()>aHatch.getMinimumStoredEU()+aHatch.maxEUInput()){
                    long consume;
                    if(aHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits( consume = aHatch.getBaseMetaTileEntity().getStoredEU() - (aHatch.getMinimumStoredEU()+aHatch.maxEUInput()),false))
                        mEnergyConsumed+= consume;
                }
            }
            mThisWorldElectricity[lastUsedStruct][2]+=Math.round(mEnergyConsumed*tEfficiency);
            mEnergyConsumed = 0;
        }
        else if(!mSender)
            mThisWorldElectricity[lastUsedStruct][2]-=mEUt;
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if(mSender&&!aBaseMetaTileEntity.isActive()&&lastUsedStruct>=0&&mThisWorldElectricity[lastUsedStruct][2]>getMaxInputVoltage()*getMaxInputAmperage()*5){
            mThisWorldElectricity[lastUsedStruct][2] -= getMaxInputVoltage()*getMaxInputAmperage()*5;
        }
    }

    public long getPowerLimit(byte aTier){
        return 4000L*(long)Math.pow(10,aTier-3);
    }

    public void onStructureCheckSuccess(){
       // System.out.println("struct "+lastUsedStruct +" "+mSender);
        if(tWorldEU != 0){
            mThisWorldElectricity[lastUsedStruct][2] = tWorldEU;
            tWorldEU = 0;
        }

        if(mTowerList.contains(this)) {
            recalculateEfficiency();
            return;
        }
        mTowerList.add(this);
        int xMod = mPosition[0]%(getHeight()*4);
        int zMod = mPosition[2]%(getHeight()*4);
        if(mSender) {
            if (mThisWorldElectricity[lastUsedStruct][3] == 0) {
                mThisWorldElectricity[lastUsedStruct][0] = (long) xMod;
                mThisWorldElectricity[lastUsedStruct][1] = (long) zMod;
                mThisWorldElectricity[lastUsedStruct][3] = 1L;
            } else {
                mThisWorldElectricity[lastUsedStruct][3]++;
                long xOffset = Math.round(xMod - mThisWorldElectricity[lastUsedStruct][0]) / mThisWorldElectricity[lastUsedStruct][3];
                long zOffset = Math.round(zMod - mThisWorldElectricity[lastUsedStruct][1]) / mThisWorldElectricity[lastUsedStruct][3];
                mThisWorldElectricity[lastUsedStruct][0] += xOffset;
                mThisWorldElectricity[lastUsedStruct][1] += zOffset;
                recalculateEfficiency();
            }
        }
        else{
            updateEfficiency();
        }
    }

    public void recalculateEfficiency(){
        for(GT_MetaTileEntity_TeslaTower aTower : mTowerList){
            if(this.getHeight()==aTower.getHeight()){
                aTower.updateEfficiency();
            }
        }
    }

    @Override
    public void onRemoval() {

        mTowerList.remove(this);
        if(lastUsedStruct>=0){
            mThisWorldElectricity[lastUsedStruct][3]--;
        }
        super.onRemoval();
    }

    public void updateEfficiency(){
        int xMod = mPosition[0]%(getHeight()*4);
        int zMod = mPosition[2]%(getHeight()*4);
        long xOffset = (xMod - mThisWorldElectricity[lastUsedStruct][0]);
        long zOffset = (zMod - mThisWorldElectricity[lastUsedStruct][1]);
        tEfficiency = (Math.cos(xOffset*Math.PI/getHeight()/2)+ Math.cos(zOffset*Math.PI/getHeight()/2))/2;
        System.out.println("xPos "+ mPosition[0]+" zPos"+mPosition[2]+" xMod "+xMod+" zMOd "+zMod+" xOffset "+xOffset+" zOffset "+zOffset);
        System.out.println(Arrays.asList(mThisWorldElectricity[lastUsedStruct]));
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public String[][] GetStructure() {
        return mStructures[0];
    }

    public int getHeight(){
        switch (lastUsedStruct){
            case 0:
                return 28;
            case 1:
                return 24;
            case 2:
                return 20;
            default:
                return 0;
        }
    }

    @Override
    public boolean RenderStructure(boolean aBuild, IGuideRenderer aRenderer, int[] aParams) {
        System.out.println("rendring");
        if(aParams[0]>2)
            return false;
        GT_Multiblock_Utility u = new GT_Multiblock_Utility(false,true,mStructures[aParams[0]],new Object[]{'Q',GregTech_API.sBlockCasings8, (byte)7, (byte)0 , new byte[]{},10,'W', GregTech_API.sBlockCasings4, (byte)14, (byte)0, null, 10, 'E', GregTech_API.sBlockCasings8, (byte)8, (byte)0 , new byte[]{},10,'R', GregTech_API.sBlockCasings5, (byte)0, (byte)0 , new byte[]{},10},(byte)-1);//GT_Utility.sendChatToPlayer(aPlayer, ""+u.checkStructure(getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getBackFacing(),aPlayer));
        u.RenderGuide(this.getBaseMetaTileEntity(),this, this.getBaseMetaTileEntity().getFrontFacing(),aBuild, aRenderer);
        return true;
    }

    @Override
    public String[] GetParamNames() {
        return new String[]{"Struct Type: "," "," "};
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        System.out.println(" eff "+tEfficiency);
        counter = 110;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        System.out.println("getting energy");
        if(mSender) {
            tWorldEU = aNBT.getLong("worldEnergy");
            lastUsedStruct = aNBT.getByte("structType");
            tEfficiency = aNBT.getDouble("tEfficiency");
        }
        System.out.println("got "+tWorldEU);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        System.out.println("saving energy");
        if(lastUsedStruct>=0&&mSender) {
            aNBT.setLong("worldEnergy",mThisWorldElectricity[lastUsedStruct][2]);
            aNBT.setByte("structType",lastUsedStruct);
            aNBT.setDouble("tEfficiency",tEfficiency);
        }
        System.out.println("saving "+lastUsedStruct+" "+mThisWorldElectricity[lastUsedStruct][2]);
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{"Coil capacity: "+getPowerLimit((byte) Math.max(1, GT_Utility.getTier(getMaxInputVoltage()))),lastUsedStruct>=0?"Energy Stored"+mThisWorldElectricity[lastUsedStruct][2]:" "};
    }
}

class GT_Runnable_CheckAir implements Runnable {

    GT_MetaTileEntity_TeslaTower mTower;

    public  GT_Runnable_CheckAir(GT_MetaTileEntity_TeslaTower aTower){
        mTower = aTower;
        System.out.println("created thread");
    }

    @Override
    public void run() {
        mTower.getMearbyBlocks();
    }
}
