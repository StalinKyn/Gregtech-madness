package gregtech.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EMMultiBlockBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GT_NEI_EMHandler
        extends TemplateRecipeHandler {
    public static final int sOffsetX = 5;
    public static final int sOffsetY = 11;

    static {
        GuiContainerManager.addInputHandler(new GT_NEI_ResearchStationHandler.GT_RectHandler());
        GuiContainerManager.addTooltipHandler(new GT_NEI_ResearchStationHandler.GT_RectHandler());
    }

    protected final GT_MetaTileEntity_EMMultiBlockBase mMultiBlock;

    public GT_NEI_EMHandler(GT_MetaTileEntity_EMMultiBlockBase aMultiBlock) {//this is called when recipes should be shown
        this.mMultiBlock = aMultiBlock;
        this.transferRects.add(new RecipeTransferRect(new Rectangle(138, 18, 18, 18), getOverlayIdentifier(), new Object[0]));
        if (!NEI_GT_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(GT_Values.GT, "NEIPlugins", "register-crafting-handler", "gregtech@" + getRecipeName() + "@" + getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    public static void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
    }

    public TemplateRecipeHandler newInstance() {
       return new GT_NEI_EMHandler(mMultiBlock);
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            mMultiBlock.loadAllCraftingRecipes(arecipes,outputId,results);
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadCraftingRecipes(ItemStack aResult) {
       mMultiBlock.loadCraftingRecipes(arecipes,aResult);
    }

    public void loadUsageRecipes(ItemStack aInput) {
        mMultiBlock.loadUsageRecipes(arecipes,aInput);
    }

    public String getOverlayIdentifier() {
        return this.mMultiBlock.mNEI;
    }

    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 75);
    }

    public int recipiesPerPage() {
        return 1;
    }

    public String getRecipeName() {
        return GT_LanguageManager.getTranslation(this.mMultiBlock.getMetaName()+"recipes");
    }

    public String getGuiTexture() {
        return this.mMultiBlock.getNEIPath();
    }

    public java.util.List<String> handleItemTooltip(GuiRecipe gui, ItemStack aStack, java.util.List<String> currenttip, int aRecipeIndex) {
        CachedRecipe tObject = (CachedRecipe) this.arecipes.get(aRecipeIndex);
        if ((tObject instanceof GT_NEI_ResearchStationHandler.CachedDefaultRecipe)) {
            GT_NEI_ResearchStationHandler.CachedDefaultRecipe tRecipe = (GT_NEI_ResearchStationHandler.CachedDefaultRecipe) tObject;
            for (PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if ((!(tStack instanceof GT_NEI_ResearchStationHandler.FixedPositionedStack)) || (((GT_NEI_ResearchStationHandler.FixedPositionedStack) tStack).mChance <= 0) || (((GT_NEI_ResearchStationHandler.FixedPositionedStack) tStack).mChance == 10000)) {
                        break;
                    }
                    currenttip.add("Chance: " + ((GT_NEI_ResearchStationHandler.FixedPositionedStack) tStack).mChance / 100 + "." + (((GT_NEI_ResearchStationHandler.FixedPositionedStack) tStack).mChance % 100 < 10 ? "0" + ((GT_NEI_ResearchStationHandler.FixedPositionedStack) tStack).mChance % 100 : Integer.valueOf(((GT_NEI_ResearchStationHandler.FixedPositionedStack) tStack).mChance % 100)) + "%");
                    break;
                }
            }
            for (PositionedStack tStack : tRecipe.mInputs) {
                if (aStack == tStack.item) {
                    if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) ||
                            (tStack.item.stackSize != 0)) {
                        break;
                    }
                    currenttip.add("Does not get consumed in the process");
                    break;
                }
            }
        }
        return currenttip;
    }

    public void drawExtras(int aRecipeIndex) {
        int tEUt = ((GT_NEI_ResearchStationHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
        int tDuration = ((GT_NEI_ResearchStationHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        int tSpecial = ((GT_NEI_ResearchStationHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
        drawText(80,80,"Total: "+ tEUt*tDuration + " EU",-16000016);
        drawText(80,90,"Usage: "+ tEUt + " EU/t",-16000016);
        drawText(80,100,"Computation: "+ tSpecial,-16000016);
        drawText(80,110,"Time: "+ tDuration/20 + " secs",-16000016);
    }

    public static class GT_RectHandler
            implements IContainerInputHandler, IContainerTooltipHandler {
        public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
            //if (canHandle(gui)) {
            //    if (button == 0) {
            //        return transferRect(gui, false);
            //    }
            //    if (button == 1) {
            //        return transferRect(gui, true);
            //    }
            //}
            return false;
        }

        public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyCode) {
            return false;
        }

        public boolean canHandle(GuiContainer gui) {
            return false;
            //return (((gui instanceof GT_GUIContainer_BasicMachine)) && (GT_Utility.isStringValid(((GT_GUIContainer_BasicMachine) gui).mNEI)));
        }

        public java.util.List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, java.util.List<String> currenttip) {
            //if ((canHandle(gui)) && (currenttip.isEmpty())) {
            //    if (new Rectangle(138, 18, 18, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) {
            //        currenttip.add("Recipes");
            //    }
            //}
            return currenttip;
        }

        private boolean transferRect(GuiContainer gui, boolean usage) {
            return (canHandle(gui)) && (new Rectangle(138, 18, 18, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) && (usage ? GuiUsageRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]) : GuiCraftingRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]));

        }

        public java.util.List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, java.util.List<String> currenttip) {
            return currenttip;
        }

        public java.util.List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, java.util.List<String> currenttip) {
            return currenttip;
        }

        public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
            return false;
        }

        public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        }

        public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        }

        public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {
        }

        public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
            return false;
        }

        public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        }

        public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {
        }
    }

    public class FixedPositionedStack
            extends PositionedStack {
        public final int mChance;
        public boolean permutated = false;

        public FixedPositionedStack(Object object, int x, int y) {
            this(object, x, y, 0);
        }

        public FixedPositionedStack(Object object, int x, int y, int aChance) {
            super(object, x, y, true);
            this.mChance = aChance;
        }

        public void generatePermutations() {
            if (this.permutated) {
                return;
            }
            ArrayList<ItemStack> tDisplayStacks = new ArrayList();
            for (ItemStack tStack : this.items) {
                if (GT_Utility.isStackValid(tStack)) {
                    if (tStack.getItemDamage() == 32767) {
                        java.util.List<ItemStack> permutations = codechicken.nei.ItemList.itemMap.get(tStack.getItem());
                        if (!permutations.isEmpty()) {
                            ItemStack stack;
                            for (Iterator i$ = permutations.iterator(); i$.hasNext(); tDisplayStacks.add(GT_Utility.copyAmount(tStack.stackSize, new Object[]{stack}))) {
                                stack = (ItemStack) i$.next();
                            }
                        } else {
                            ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
                            base.stackTagCompound = tStack.stackTagCompound;
                            tDisplayStacks.add(base);
                        }
                    } else {
                        tDisplayStacks.add(GT_Utility.copy(new Object[]{tStack}));
                    }
                }
            }
            this.items = ((ItemStack[]) tDisplayStacks.toArray(new ItemStack[0]));
            if (this.items.length == 0) {
                this.items = new ItemStack[]{new ItemStack(Blocks.fire)};
            }
            this.permutated = true;
            setPermutationToRender(0);
        }
    }
    //0-13 iputs = inputs |14-17 = scanInputs| 0 out = data orb| 1 out = ResearchItem
//0-9 iputs = inputs |10-13 = scanInputs| 0 out =  paper with project| 1 out = ResearchItem
    public class CachedDefaultRecipe
            extends CachedRecipe {
        public final GT_Recipe mRecipe;
        public final java.util.List<PositionedStack> mOutputs = new ArrayList();
        public final java.util.List<PositionedStack> mInputs = new ArrayList();

        public CachedDefaultRecipe(GT_Recipe aRecipe,GT_MetaTileEntity_EMMultiBlockBase aMultiBlock) {
            super();
            this.mRecipe = aRecipe;
            int[][] tCoords = aMultiBlock.getNEIInputCoords();
            for (int i = 0; i < tCoords.length; i++) {
                Object obj = aRecipe instanceof GT_Recipe.GT_Recipe_WithAlt ? ((GT_Recipe.GT_Recipe_WithAlt) aRecipe).getAltRepresentativeInput(i) : aRecipe.getRepresentativeInput(i);
                if (obj == null)
                    continue;
                this.mInputs.add(new FixedPositionedStack(obj, tCoords[i][0], tCoords[i][1]));
            }
            tCoords = aMultiBlock.getNEIOutputCoords();
            for(int i = 0; i < tCoords.length; i++){
                ItemStack is = aRecipe.getOutput(i);
                if(is==null)
                    continue;
                this.mOutputs.add(new FixedPositionedStack(is,tCoords[i][0],tCoords[i][1],aRecipe.getOutputChance(i)));
            }
            tCoords = aMultiBlock.getNEIFluidInputCoords();
            for(int i = 0; i < tCoords.length; i++){
                if((aRecipe.mFluidInputs.length>i) && (aRecipe.mFluidInputs[i]!=null) && (aRecipe.mFluidInputs[i].getFluid() != null)){
                    this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[i],true), tCoords[i][0], tCoords[i][1]));
                }
            }
            tCoords = aMultiBlock.getNEIFluidOutputCoords();
            for(int i = 0; i < tCoords.length; i++){
                if((aRecipe.mFluidOutputs.length>i) && (aRecipe.mFluidOutputs[i]!=null) && (aRecipe.mFluidOutputs[i].getFluid() != null)){
                    this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[i],true), tCoords[i][0], tCoords[i][1]));
                }
            }
        }

        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients((int)(System.currentTimeMillis() / 10), this.mInputs);
        }

        public PositionedStack getResult() {
            return null;
        }

        public List<PositionedStack> getOtherStacks() {
            return this.mOutputs;
        }


    }
}
