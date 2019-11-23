package gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Collection;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_PlasmaBlastFurnace extends GT_MetaTileEntity_MultiBlockBase{

	private int mFuelValue;
	private FluidStack mPlasmaStack;
	private int speedMultiplier =2;

	public GT_MetaTileEntity_PlasmaBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PlasmaBlastFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PlasmaBlastFurnace(this.mName);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
        		"Controller block for the Plasma Blast Furnace",
        		"Can process all IV+ materials",
        		"Plasma is used for Overclocking",
				"Does not lose efficiency when overclocked",
				"Size(WxHxD): 3x4x3 (Hollow), Controller (Front middle bottom)",
				"16x Superconductor Coils (Two middle Layers, hollow)",
                "Robust Osmiridium Casings for the rest",
				"1x Input Bus/Hatch (Bottom casing)",
				"1x Output Bus/Hatch (Bottom casing)",
				"1x Maintenance Hatch (Bottom casing)",
				"1x Energy Hatch (Botton casing)"};
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[1][54], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[1][54]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ElectricAirFilter.png");
    }

    @Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<ItemStack> tInputList = getStoredInputs();
		int tInputList_sS = tInputList.size();
		for (int i = 0; i < tInputList_sS - 1; i++) {
			for (int j = i + 1; j < tInputList_sS; j++) {
				if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
					if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
						tInputList.remove(j--);
						tInputList_sS = tInputList.size();
					} else {
						tInputList.remove(i--);
						tInputList_sS = tInputList.size();
						break;
					}
				}
			}
		}
		tInputList.add(mInventory[1]);
		ItemStack[] inputs = tInputList.toArray(new ItemStack[tInputList.size()]);

		ArrayList<FluidStack> tFluidList = getStoredFluids();
		int tFluidList_sS = tFluidList.size();
		for (int i = 0; i < tFluidList_sS - 1; i++) {
			for (int j = i + 1; j < tFluidList_sS; j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
						tFluidList_sS = tFluidList.size();
					} else {
						tFluidList.remove(i--);
						tFluidList_sS = tFluidList.size();
						break;
					}
				}
			}
		}
		FluidStack[] fluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);

		for(FluidStack fluid:fluids){
			if(getFuelValue(fluid ) != 0){
				mFuelValue = Math.round(getFuelValue(fluid));
				mPlasmaStack = fluid;
				break;
			}
		}

		if ((inputs.length > 0 || fluids.length > 1)&&mFuelValue!=0) {
			long voltage = getMaxInputVoltage();
			byte tier = (byte) Math.max(1, GT_Utility.getTier(voltage));
			GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sBlastRecipes.findRecipe(getBaseMetaTileEntity(), false,
					false, gregtech.api.enums.GT_Values.V[tier], fluids, inputs);
			if (recipe != null && recipe.isRecipeInputEqual(true, fluids, inputs)) {
				this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;

				int EUt = Math.round(recipe.mEUt/16);
				int maxProgresstime = recipe.mDuration;
				int plasmaToConsume = Math.max(Math.round(recipe.mDuration*recipe.mEUt/mFuelValue/speedMultiplier),1);
				if(plasmaToConsume>mPlasmaStack.amount){
					return false;
				}
				this.mEUt = -EUt;
				this.mMaxProgresstime = Math.max(Math.round(maxProgresstime*recipe.mEUt/mFuelValue*speedMultiplier),1);
				mOutputItems = new ItemStack[recipe.mOutputs.length];
 		        for (int i = 0; i < recipe.mOutputs.length; i++) {
 		            if (getBaseMetaTileEntity().getRandomNumber(10000) < recipe.getOutputChance(i)) {
 		                this.mOutputItems[i] = recipe.getOutput(i);
 		            }
 		        }
 		        mPlasmaStack.amount -= plasmaToConsume;
				String fn = FluidRegistry.getFluidName(mPlasmaStack.getFluid());
				String[] nameSegments = fn.split("\\.",2);
				if (nameSegments.length==2){
					String outputName=nameSegments[1];
					FluidStack output = FluidRegistry.getFluidStack(outputName, plasmaToConsume);
					if (output==null){
						output = FluidRegistry.getFluidStack("molten."+outputName, plasmaToConsume);
					}
					if (output!=null) {
						addOutput(output);
					}
				}
				if(recipe.mSpecialValue > mPlasmaStack.getFluid().getTemperature())
					return false;
				this.mOutputFluids = recipe.mFluidOutputs;
				this.updateSlots();
				return true;
			}
		}
		return false;
	}


	@Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int one = 1;
        int two = 2;
        
        for (int i = -one; i < two; i++) {
            for (int j = -one; j < two; j++) {
                if ((i != 0) || (j != 0)) {
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, two, zDir + j) != GregTech_API.sBlockCasings1) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, two, zDir + j) != 15) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, one, zDir + j) != GregTech_API.sBlockCasings1) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, one, zDir + j) != 15) {
                        return false;
                    }
                    /*if (aBaseMetaTileEntity.getBlockOffset(xDir, two, zDir) != GregTech_API.sBlockCasings5) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir, two, zDir) != 8) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getBlockOffset(xDir, one, zDir) != GregTech_API.sBlockCasings5) {
                        return false;
                    }
                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir, one, zDir) != 8) {
                        return false;
                    }*/
                    if (!addOutputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j), 182)) {
                    	if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j) != GregTech_API.sBlockCasings8) {
                    		return false;
                    	}
                    	if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j) != 6) {
                    		return false;
                    	}
                    }
                }
            }
        }
        for (int i = -one; i < two; i++) {
            for (int j = -one; j < two; j++) {
                if (xDir + i != 0 || zDir + j != 0) {//sneak exclusion of the controller block
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
                    if (!addMaintenanceToMachineList(tTileEntity, 182) && !addInputToMachineList(tTileEntity, 182) && !addOutputToMachineList(tTileEntity, 182) && !addEnergyInputToMachineList(tTileEntity, 182)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != GregTech_API.sBlockCasings8) {
                            return false;
                        }
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 6) {
                            return false;
                        }
                    }
                }
            }
        }
        //bottom casing done
        return true;
    }

	public int getFuelValue(FluidStack aLiquid) {
		if (aLiquid == null || GT_Recipe.GT_Recipe_Map.sTurbineFuels == null) return 0;
		FluidStack tLiquid;
		Collection<GT_Recipe> tRecipeList = GT_Recipe.GT_Recipe_Map.sPlasmaFuels.mRecipeList;
		if (tRecipeList != null) for (GT_Recipe tFuel : tRecipeList)
			if ((tLiquid = GT_Utility.getFluidForFilledItem(tFuel.getRepresentativeInput(0), true)) != null)
				if (aLiquid.isFluidEqual(tLiquid)) return tFuel.mSpecialValue;
		return 0;
	}

	@Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide() && aTick % 20L == 0L) {
            //refresh casing on state change
            int Xpos = aBaseMetaTileEntity.getXCoord() + ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
            int Ypos = aBaseMetaTileEntity.getYCoord()+3;
            int Zpos = aBaseMetaTileEntity.getZCoord() + ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
            try {
                aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(Xpos - 1, Ypos, Zpos - 1, Xpos + 1, Ypos, Zpos + 1);
            } catch (Exception ignored) {}
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }	
}