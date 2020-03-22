package gregtech.api.datasystem;

import gregtech.api.util.GT_Recipe;

public interface IResearcher extends IDataDevice {

    boolean canResearch(GT_Recipe.GT_Recipe_ResearchStation aRecipe);

    Object getProcessing();

    GT_InformationBundle[] requestDataBundle(GT_Recipe.GT_Recipe_ResearchStation aRecipe);

    boolean isProcessing();

    int getProgress();

}
