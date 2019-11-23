package gregtech.nei;

import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EMMultiBlockBase;
import gregtech.api.util.GT_Recipe;

public class NEI_GT_Config
        implements IConfigureNEI {
    public static boolean sIsAdded = true;
    public static GT_NEI_AssLineHandler ALH;
    public static GT_NEI_ResearchStationHandler RSH;
    public static GT_NEI_ResearchStationHandler PRSH;

    public void loadConfig() {
        sIsAdded = false;
        for (GT_Recipe.GT_Recipe_Map tMap : GT_Recipe.GT_Recipe_Map.sMappings) {
            if (tMap.mNEIAllowed) {
                new GT_NEI_DefaultHandler(tMap);
            }
        }
        if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
            ALH=new GT_NEI_AssLineHandler(GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes);
            RSH=new GT_NEI_ResearchStationHandler(GT_Recipe.GT_Recipe_Map.sResearchStationVisualRecipes,true);
            PRSH=new GT_NEI_ResearchStationHandler(GT_Recipe.GT_Recipe_Map.sPrimitiveResearchStationVisualRecipes,false);
            for(GT_MetaTileEntity_EMMultiBlockBase aMultiblock:GT_MetaTileEntity_EMMultiBlockBase.mNEImaps){
                new GT_NEI_EMHandler(aMultiblock);
            }
        }
        sIsAdded = true;
    }

    public String getName() {
        return "GregTech NEI Plugin";
    }

    public String getVersion() {
        return "(5.03a)";
    }
}
