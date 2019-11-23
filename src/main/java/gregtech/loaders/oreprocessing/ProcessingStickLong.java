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

public class ProcessingStickLong implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static int[] vs = new int[]{4,16,64,256,1024,4096,16384,65536,262144};
    public ProcessingStickLong() {
        OrePrefixes.stickLong.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            GT_Values.RA.addCutterRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L), null, (int) Math.max(aMaterial.getMass(), 1L), vs[aMaterial.mWorkerTier-1]);
            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"sf", "G ", Character.valueOf('G'), OrePrefixes.gemFlawless.get(aMaterial)});
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"sf", "G ", Character.valueOf('G'), OrePrefixes.gemExquisite.get(aMaterial)});
            }
        }
        if (!aMaterial.contains(SubTag.NO_SMASHING)) {
            GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L), 200, vs[aMaterial.mWorkerTier-1]*4);
            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial))
                GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"ShS", Character.valueOf('S'), OrePrefixes.stick.get(aMaterial)});
        }
    }
}
