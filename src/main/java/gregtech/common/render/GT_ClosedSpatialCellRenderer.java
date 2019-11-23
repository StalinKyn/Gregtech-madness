package gregtech.common.render;

import appeng.items.misc.ItemEncodedPattern;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.items.GT_ClosedSpatialCell_Item;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class GT_ClosedSpatialCellRenderer implements IItemRenderer {


    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type )
    {
        final boolean isShiftHeld = Keyboard.isKeyDown( Keyboard.KEY_LSHIFT ) || Keyboard.isKeyDown( Keyboard.KEY_RSHIFT );
        final boolean isControlHeld = Keyboard.isKeyDown( Keyboard.KEY_LCONTROL ) || Keyboard.isKeyDown( Keyboard.KEY_RCONTROL );
        if(type == IItemRenderer.ItemRenderType.INVENTORY && (isShiftHeld||isControlHeld ))
        {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldUseRenderHelper( final ItemRenderType type, final ItemStack item, final ItemRendererHelper helper )
    {
        return false;
    }

    @Override
    public void renderItem( final ItemRenderType type, final ItemStack item, final Object... data )
    {
        final boolean isShiftHeld = Keyboard.isKeyDown( Keyboard.KEY_LSHIFT ) || Keyboard.isKeyDown( Keyboard.KEY_RSHIFT );

        GT_ClosedSpatialCell_Item cell = (GT_ClosedSpatialCell_Item)item.getItem();
        GL11.glPushAttrib( GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT );
        GT_RenderUtil.renderItemIcon(isShiftHeld?cell.getShiftIcon():cell.getControlIcon(), 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        GL11.glPopAttrib();

    }
}
