package gregtech.loaders.oreprocessing;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.item.ItemStack;

public class ProcessingStick implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public static int[] vs = new int[]{24,96,384,1536,6144,24576,98304,393216,1572864};
    public static int[] vs1 = new int[]{4,16,64,256,1024,4096,16384,65536,262144};
    public ProcessingStick() {
        OrePrefixes.stick.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_WORKING)) {
            GT_Values.RA.addLatheRecipe(aMaterial.contains(SubTag.CRYSTAL) ? GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L) : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial.mMacerateInto, 2L), (int) Math.max(aMaterial.getMass() * 5L, 1L), vs[aMaterial.mWorkerTier-1]/3*2);
            GT_Values.RA.addCutterRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L), null, (int) Math.max(aMaterial.getMass() * 2L, 1L), vs1[aMaterial.mWorkerTier-1]);
            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"s", "X", Character.valueOf('X'), OrePrefixes.stickLong.get(aMaterial)});
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"f ", " X", Character.valueOf('X'), OrePrefixes.ingot.get(aMaterial)});
            }
        }
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
            GT_Values.RA.addForgeHammerRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L), (int) Math.max(aMaterial.getMass(), 1L), vs[aMaterial.mWorkerTier-1]/3*2);
        }
    }
}
