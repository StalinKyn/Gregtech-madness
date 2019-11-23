package gregtech.common.gui;

import gregtech.api.enums.GT_Values;
import gregtech.common.items.GT_VolumetricFlask;
import gregtech.common.net.MessageSetFlaskCapacity;
import gregtech.common.net.MessageSetTeleporterCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class GT_GUIContainer_HandTeleporter
        extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("gregtech:textures/gui/HandTelepoter.png");

    private GuiIntegerBox xCoord;
    private GuiIntegerBox yCoord;
    private GuiIntegerBox zCoord;
    private GuiButton apply;
    private GuiButton plus1;
    private GuiButton plus10;
    private GuiButton plus100;
    private GuiButton plus1000;
    private GuiButton minus1;
    private GuiButton minus10;
    private GuiButton minus100;
    private GuiButton minus1000;
    private GT_Container_HandTeleporter container;

    private GuiIntegerBox[] boxes =new GuiIntegerBox[3];
    private byte focused = 0;

    public GT_GUIContainer_HandTeleporter(GT_Container_HandTeleporter container) {
        super(container);
        this.container = container;
    }

    public void initGui() {
        super.initGui();
        int leftOffset = 125;
        buttonList.add(plus1 = new GuiButton(0, guiLeft + leftOffset, guiTop + 26, 22, 20, "+1"));
        buttonList.add(plus10 = new GuiButton(0, guiLeft + leftOffset, guiTop + 52, 28, 20, "+10"));
        buttonList.add(plus100 = new GuiButton(0, guiLeft + leftOffset, guiTop + 78, 32, 20, "+100"));
        buttonList.add(plus1000 = new GuiButton(0, guiLeft + leftOffset, guiTop + 104, 38, 20, "+1000"));
        leftOffset = 10;
        buttonList.add(minus1 = new GuiButton(0, guiLeft + leftOffset, guiTop + 26, 22, 20, "-1"));
        buttonList.add(minus10 = new GuiButton(0, guiLeft + leftOffset, guiTop + 52, 28, 20, "-10"));
        buttonList.add(minus100 = new GuiButton(0, guiLeft + leftOffset, guiTop + 78, 32, 20, "-100"));
        buttonList.add(minus1000 = new GuiButton(0, guiLeft + leftOffset, guiTop + 104, 38, 20, "-1000"));

        buttonList.add(apply = new GuiButton(0, guiLeft + 64, guiTop + 138, 38, 20, "Teleport"));

        xCoord = new GuiIntegerBox(fontRendererObj, guiLeft + 64, guiTop + 42, 59, fontRendererObj.FONT_HEIGHT);
        xCoord.setEnableBackgroundDrawing(false);
        xCoord.setMaxStringLength(16);
        xCoord.setTextColor(16777215);
        xCoord.setVisible(true);
        xCoord.setFocused(true);
        xCoord.setText("0");
        boxes[0] = xCoord;

        yCoord = new GuiIntegerBox(fontRendererObj, guiLeft + 64, guiTop + 62, 59, fontRendererObj.FONT_HEIGHT);
        yCoord.setEnableBackgroundDrawing(false);
        yCoord.setMaxStringLength(16);
        yCoord.setTextColor(16777215);
        yCoord.setVisible(true);
        yCoord.setFocused(false);
        yCoord.setText("0");
        boxes[1] = yCoord;

        zCoord = new GuiIntegerBox(fontRendererObj, guiLeft + 64, guiTop + 82, 59, fontRendererObj.FONT_HEIGHT);
        zCoord.setEnableBackgroundDrawing(false);
        zCoord.setMaxStringLength(16);
        zCoord.setTextColor(16777215);
        zCoord.setVisible(true);
        zCoord.setFocused(false);
        zCoord.setText("0");
        boxes[2] = zCoord;
    }


    protected final void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        try {
            Long.parseLong(xCoord.getText());
            apply.enabled = (xCoord.getText().length() > 0);
        } catch (NumberFormatException e) {
            apply.enabled = false;
        }

        xCoord.drawTextBox();
        yCoord.drawTextBox();
        zCoord.drawTextBox();
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        byte b= focused;
        focused = -1;
        for(byte i = 0; i < boxes.length;i++){
            boxes[i].mouseClicked(p_73864_1_,p_73864_2_,p_73864_3_);
            if(boxes[i].isFocused())
                focused= i;
        }
        if(focused==-1)
            focused = b;
    }

    protected void keyTyped(char character, int key) {
        if (!checkHotbarKeys(key)) {
            if(key==15) {
                        boxes[focused].setFocused(false);
                        focused=(byte)((focused+1)==3?0:focused+1);
                        boxes[focused].setFocused(true);

            }
            if (key == 28)
                actionPerformed(apply);
            if (((key == 211) || (key == 205) || (key == 203) || (key == 14) || (character == '-') || (Character.isDigit(character))) && (boxes[focused].textboxKeyTyped(character, key))) {

                try {
                    String out = boxes[focused].getText();
                    boolean fixed = false;
                    while ((out.startsWith("0")) && (out.length() > 1)) {
                        out = out.substring(1);
                        fixed = true;
                    }
                    if (fixed) {
                        boxes[focused].setText(out);
                    }
                    if (out.isEmpty()) {
                        out = "0";
                    }
                    long result = Long.parseLong(out);
                    if (result < 0L) {
                        boxes[focused].setText("1");
                    }

                } catch (NumberFormatException localNumberFormatException) {
                }
            } else {
                super.keyTyped(character, key);
            }
        }
    }

    protected void actionPerformed(GuiButton btn) {
        try {
            if (btn == apply) {
                GT_Values.NW.sendToServer(new MessageSetTeleporterCoords(Integer.parseInt(xCoord.getText()),Integer.parseInt(yCoord.getText()),Integer.parseInt(zCoord.getText()), Minecraft.getMinecraft().thePlayer));
                mc.thePlayer.closeScreen();
            }

        } catch (NumberFormatException e) {
            xCoord.setText("1");
        }

        boolean isPlus = (btn == plus1) || (btn == plus10) || (btn == plus100) || (btn == plus1000);
        boolean isMinus = (btn == minus1) || (btn == minus10) || (btn == minus100) || (btn == minus1000);

        if ((isPlus) || (isMinus)) {
            addQty(getQty(btn));
        }
    }

    private void addQty(int i) {
        try {
            String out = boxes[focused].getText();

            boolean fixed = false;
            while ((out.startsWith("0")) && (out.length() > 1)) {
                out = out.substring(1);
                fixed = true;
            }

            if (fixed) {
                boxes[focused].setText(out);
            }
            if (out.isEmpty()) {
                out = "0";
            }
            long result = Integer.parseInt(out);

            if ((result == 1L) && (i > 1)) {
                result = 0L;
            }
            result += i;
            if (result < 1L) {
                result = 1L;
            }
            out = Long.toString(result);
            Integer.parseInt(out);
            boxes[focused].setText(out);
        } catch (NumberFormatException localNumberFormatException) {
        }
    }


    protected int getQty(GuiButton btn) {
        try {
            DecimalFormat df = new DecimalFormat("+#;-#");
            return df.parse(btn.displayString).intValue();
        } catch (ParseException e) {
        }

        return 0;
    }

    public class GuiIntegerBox extends GuiTextField {
        private final int maxValue;

        public GuiIntegerBox(FontRenderer fontRenderer, int x, int y, int width, int height) {
            this(fontRenderer, x, y, width, height, Integer.MAX_VALUE);
        }

        public GuiIntegerBox(FontRenderer fontRenderer, int x, int y, int width, int height, int maxValue) {
            super(fontRenderer, x, y, width, height);
            this.maxValue = maxValue;
        }


        public void writeText(String selectedText) {
            String original = getText();
            super.writeText(selectedText);

            try {
                int i = Integer.parseInt(getText());
                if (i > maxValue) {
                    setText(String.valueOf(maxValue));
                } else if (i < 0) {
                    setText("0");
                }
            } catch (NumberFormatException e) {
                setText(original);
            }
        }


        public void setText(String s) {
            try {
                int i = Integer.parseInt(s);
                if (i > maxValue) {
                    s = String.valueOf(maxValue);
                } else if (i < 0) {
                    s = "0";
                }
            } catch (NumberFormatException e) {
                s = String.valueOf(maxValue);
            }
            super.setText(s);
        }
    }
}
