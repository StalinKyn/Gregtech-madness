package gregtech.common.items;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.items.GT_MetaGenerated_Item_X32;

public class GT_MetaGenerated_Item_06
        extends GT_MetaGenerated_Item_X32 {
    public static GT_MetaGenerated_Item_06 INSTANCE;

    public GT_MetaGenerated_Item_06() {
        super("metaitem.06", new OrePrefixes[]{OrePrefixes.crateGtDust, OrePrefixes.crateGtIngot, OrePrefixes.crateGtGem, OrePrefixes.crateGtPlate});
        INSTANCE = this;
    }

    public boolean doesShowInCreative(OrePrefixes aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return aDoShowAllItems;
    }


    @Override
    protected int getMaterilaOffset() {
        return 1000;
    }
}
