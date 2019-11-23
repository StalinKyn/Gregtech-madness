package gregtech.api.util;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IGuideRenderer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_GuideRendererParticle;
import gregtech.common.items.behaviors.Behaviour_HolographicProjector;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class GT_Multiblock_Utility {
    public char[][][] pattern;
    public byte[] contreollerCoords = new byte[3];
    private Object[] machineCasingMap;
    boolean XYSymmetrical;
    boolean ZYSymmetrical;
    int[] casingCouter = null;
    String[] mArguments = null;
    int[][] mValues = null;
    short xMarg = 0;
    short yMarg = 0;
    short zMarg = 0;
    byte mFlipped = 1;
    boolean mDebugMode = true;

    public GT_Multiblock_Utility(boolean XYSymmetrical,boolean ZYSymmetrical, String[][] pattern, Object[] machineCasingMap){
        this.pattern = new char[pattern.length][pattern[0].length][pattern[0][0].length()];
        for(byte i = 0; i < pattern.length;i++){
            for(byte j = 0; j < pattern[0].length; j++){
                this.pattern[i][j] = pattern[i][j].toCharArray();
            }
        }
        this.machineCasingMap = machineCasingMap;
        this.XYSymmetrical =XYSymmetrical;
        this.ZYSymmetrical = ZYSymmetrical;
        GT_Log.out.println("GT_Mod: GTMultiblockUtility is set up");
    }

    public GT_Multiblock_Utility(boolean XYSymmetrical,boolean ZYSymmetrical, String[][] pattern, Object[] machineCasingMap, byte aFlipped){
        this(XYSymmetrical,ZYSymmetrical,pattern,machineCasingMap);
        mFlipped = aFlipped;
    }

    // recipe example : 'A'- char, Block - casing block, Int - casing meta, Int - casing index, Byte Array - allowed hatches Int - minimum casing count
    // 'c' - controller 'a' - air 'n' - null 's' - skip block
    // argument example: "h.3" , values[0] = {aMinCount,aMaxCount} helpful for stuctures with variable x y or z, like AL

    public boolean checkStructure(IGregTechTileEntity aBaseMetaTileEntity, GT_MetaTileEntity_MultiBlockBase aController, byte controllerBackFacing){
        boolean aResult = false;
        aResult = checkStructure(aBaseMetaTileEntity,aController,controllerBackFacing,(byte)1,(byte)1);
        if(XYSymmetrical&&!aResult)
            aResult = checkStructure(aBaseMetaTileEntity,aController,controllerBackFacing,(byte)-1,(byte)1);
        if (ZYSymmetrical&&!aResult)
            aResult = checkStructure(aBaseMetaTileEntity,aController,controllerBackFacing,(byte)1,(byte)-1);
        if (XYSymmetrical&&ZYSymmetrical&&aResult){
            aResult = checkStructure(aBaseMetaTileEntity,aController,controllerBackFacing,(byte)-1,(byte)-1);
        }
        return aResult;
    }

    private boolean checkStructure(IGregTechTileEntity aBaseMetaTileEntity, GT_MetaTileEntity_MultiBlockBase aController, byte controllerBackFacing, byte xCoef, byte zCoef){
        byte tFacing = controllerBackFacing;
        if(!patternProcessing())
            return false;
        char lastChar = 'n';
        Block currentBlock = GregTech_API.sBlockCasings1;
        byte currentCasingMeta = -1;
        int currentCasingIndex = -1;
        byte[] allowedHatches = null;
        int currentCasingNumber = 0;
        casingCouter = new int[machineCasingMap.length/6];
        for(int q = 0; q < casingCouter.length;q++){
            casingCouter[q] = 0;
        }
        aController.mInputHatches.clear();
        aController.mOutputHatches.clear();
        aController.mInputBusses.clear();
        aController.mOutputBusses.clear();
        aController.mEnergyHatches.clear();
        aController.mDynamoHatches.clear();
        aController.mMaintenanceHatches.clear();
        aController.mMufflerHatches.clear();
       for( byte h = (byte)-contreollerCoords[0]; h < (pattern.length - contreollerCoords[0]);h++){//y
            for( byte i = (byte)-contreollerCoords[1]; i < (pattern[0].length - contreollerCoords[1]);i++){//x
                for( byte j = (byte)-contreollerCoords[2]; j < (pattern[0][0].length - contreollerCoords[2]);j++){//z
                    if(h==0&&i==0&&j==0){
                        if(mDebugMode)
                            System.out.println("controller "+pattern[h+contreollerCoords[0]][i+contreollerCoords[1]][j+contreollerCoords[2]]);
                        continue;
                    }


                    if(lastChar != pattern[h+contreollerCoords[0]][i+contreollerCoords[1]][j+contreollerCoords[2]]){
                        for(short s = 0; s <machineCasingMap.length; s+=6){
                            if(pattern[h+contreollerCoords[0]][i+contreollerCoords[1]][j+contreollerCoords[2]]==(char)machineCasingMap[s]){
                                lastChar = (char)machineCasingMap[s];
                               // System.out.println("checking "+lastChar);
                                currentBlock = (Block)machineCasingMap[s+1];
                                currentCasingMeta = (byte)machineCasingMap[s+2];
                                currentCasingIndex = (int)machineCasingMap[s+3];
                                allowedHatches = (byte[])machineCasingMap[s+4];
                                currentCasingNumber = s/6;
                                if(mDebugMode)
                                    System.out.println("updatung chars "+s);
                                break;
                            }
                        }
                    }

                    //region patternWorks
                    if(Character.isLowerCase(pattern[h+contreollerCoords[0]][i+contreollerCoords[1]][j+contreollerCoords[2]])) {
                        char a = pattern[h+contreollerCoords[0]][i+contreollerCoords[1]][j+contreollerCoords[2]];
                        switch (a) {
                            case 'a':
                                 if(aBaseMetaTileEntity.getAirOffset(tFacing == 5 ? -i : tFacing == 4 ? i : tFacing == 3 ? -j : j, -h*mFlipped, tFacing == 5 ? -j : tFacing == 4 ? j : tFacing == 3 ? -i : i))
                                     continue;
                                 else
                                    if(mDebugMode)
                                        System.out.println("not an air "+ i+ " "+h+" "+j);
                                     return false;
                            case 's':
                                continue;
                            case'd':
                                if(aController.addDynamoToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(tFacing == 5 ? -i : tFacing == 4 ? i : tFacing == 3 ? -j : j, -h*mFlipped, tFacing == 5 ? -j : tFacing == 4 ? j : tFacing == 3 ? -i : i),currentCasingIndex)){
                                    continue;
                                }
                                else
                                    if(mDebugMode)
                                        System.out.println("not a dynamo");
                                    return false;

                            default:
                                if(mDebugMode)
                                    System.out.println("unknown char: "+a +" "+ i+ " "+h+" "+j);
                                return false;
                        }
                    }
                    //endregion

                    if(aBaseMetaTileEntity.getBlockOffset(tFacing == 5 ? -i*xCoef : tFacing == 4 ? i*xCoef : tFacing == 3 ? -j*zCoef : j*zCoef, -h*mFlipped, tFacing == 5 ? -j*zCoef: tFacing == 4? j*zCoef : tFacing == 3 ? -i*xCoef : i*xCoef)==currentBlock&&aBaseMetaTileEntity.getMetaIDOffset(tFacing == 5 ? -i*xCoef : tFacing == 4 ? i*xCoef : tFacing == 3 ? -j*zCoef : j*zCoef, -h*mFlipped, tFacing == 5 ? -j*zCoef: tFacing == 4? j*zCoef : tFacing == 3 ? -i*xCoef : i*xCoef)==currentCasingMeta){
                        if(mDebugMode)
                            System.out.println("got "+lastChar+" bumping "+ currentCasingNumber);
                        casingCouter[currentCasingNumber] += 1;
                    } else if (allowedHatches!=null&&allowedHatches.length>0) {
                        byte n = aController.addHatchToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(tFacing == 5 ? -i*xCoef : tFacing == 4 ? i*xCoef : tFacing == 3 ? -j*zCoef : j*zCoef, -h*mFlipped, tFacing == 5 ? -j*zCoef: tFacing == 4? j*zCoef : tFacing == 3 ? -i*xCoef : i*xCoef), currentCasingIndex);
                        boolean addedHatch = false;
                            for(byte m = 0; m < allowedHatches.length; m++){
                                if(n == allowedHatches[m]){
                                    addedHatch = true;
                                }
                            }
                        if(!addedHatch){
                            if(mDebugMode)
                                System.out.println("not a hatch");
                            return false;
                        }
                    }else {
                        if(mDebugMode)
                            System.out.println("not a hatch 2");
                        return false;
                    }
                     /* if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? i : tSide == 4 ? -i : j), tY + h, tZ + (tSide == 3 ? i : tSide == 2 ? -i : j)) == getCasingBlock() && (getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? i : tSide == 4 ? -i : j), tY + h, tZ + (tSide == 3 ? i : tSide == 2 ? -i : j)) == getCasingMeta())) {
                        } else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? i : tSide == 4 ? -i : j), tY + h, tZ + (tSide == 3 ? i : tSide == 2 ? -i : j)))) {
                            return false;
                        }*/
                }
            }
        }
        for(int z = 0; z < casingCouter.length;z++){
            if(mDebugMode)
                System.out.println("top "+casingCouter[z]);
        }
        for(short s = 0; s <machineCasingMap.length; s+=6){
            if(mDebugMode)
                System.out.println("tp "+casingCouter[s/6]+" "+(int)machineCasingMap[s+5]);
            if(casingCouter[s/6]<(int)machineCasingMap[s+5])
                return false;
        }
        return true;
    }




    private boolean patternProcessing(){
        for(byte i = 0; i < pattern.length; i++){
            for(byte j = 0; j < pattern[0].length;j++){
                for(byte k = 0; k < pattern[0][0].length; k++){
                    if(pattern[i][j][k] == 'c'){
                        contreollerCoords[0] = (byte)(i);
                        contreollerCoords[1] = (byte)(j);
                        contreollerCoords[2] = (byte)(k);
                        return  true;
                    }
                }
            }
        }
        return  false;
    }

    public boolean RenderGuide(IGregTechTileEntity aBaseMetaTileEntity, GT_MetaTileEntity_MultiBlockBase aController, byte controllerBackFacing, boolean build, IGuideRenderer aRenderer,byte aFlipped) {
        mFlipped = aFlipped;
        return RenderGuide(aBaseMetaTileEntity,aController,controllerBackFacing,build,aRenderer);
    }

    public boolean RenderGuide(IGregTechTileEntity aBaseMetaTileEntity, GT_MetaTileEntity_MultiBlockBase aController, byte controllerBackFacing, boolean build, IGuideRenderer aRenderer){
        //Minecraft.getMinecraft().effectRenderer.addEffect(new GT_GuideRendererParticle(aWorld, aX, aY, aZ, aBlock, aMeta));//new BlockHint(world,x,y,z,block,meta
        byte tFacing = controllerBackFacing;
        if(!patternProcessing())
            return false;
        char lastChar = 'n';
        Block currentBlock = GregTech_API.sBlockCasings1;
        byte currentCasingMeta = -1;
        for( byte h = (byte)-contreollerCoords[0]; h < (pattern.length - contreollerCoords[0]);h++){//y
            for( byte i = (byte)-contreollerCoords[1]; i < (pattern[0].length - contreollerCoords[1]);i++){//x
                for( byte j = (byte)-contreollerCoords[2]; j < (pattern[0][0].length - contreollerCoords[2]);j++) {//z
                    if (h == 0 && i == 0 && j == 0)
                        continue;
                    if (lastChar != pattern[h + contreollerCoords[0]][i + contreollerCoords[1]][j + contreollerCoords[2]]) {
                        for (short s = 0; s < machineCasingMap.length; s += 6) {
                            if (pattern[h + contreollerCoords[0]][i + contreollerCoords[1]][j + contreollerCoords[2]] == (char) machineCasingMap[s]) {
                                lastChar = (char) machineCasingMap[s];
                                currentBlock = (Block) machineCasingMap[s + 1];
                                currentCasingMeta = (byte) machineCasingMap[s + 2];
                            }
                        }
                    }

                    //region patternWorks
                    if (Character.isLowerCase(pattern[h + contreollerCoords[0]][i + contreollerCoords[1]][j + contreollerCoords[2]])) {
                        char a = pattern[h + contreollerCoords[0]][i + contreollerCoords[1]][j + contreollerCoords[2]];
                        switch (a) {
                            case 'a':
                                continue;

                            case 's':
                                continue;
                            default:

                        }
                    }
                    //endregion
                    if (build) {//x, y, z, TT_Container_Casings.sBlockCasingsTT, 14, 2
                        aBaseMetaTileEntity.getWorld().setBlock((tFacing == 5 ? -i : tFacing == 4 ? i : tFacing == 3 ? -j : j) + aBaseMetaTileEntity.getXCoord(),
                                aBaseMetaTileEntity.getYCoord() - h*mFlipped,
                                (tFacing == 5 ? -j : tFacing == 4 ? j : tFacing == 3 ? -i : i) + aBaseMetaTileEntity.getZCoord(),
                                currentBlock, currentCasingMeta, 2);
                    } else {
                    Minecraft.getMinecraft().effectRenderer.addEffect(new GT_GuideRendererParticle(aBaseMetaTileEntity.getWorld(), (tFacing == 5 ? -i : tFacing == 4 ? i : tFacing == 3 ? -j : j) + aBaseMetaTileEntity.getXCoord(),
                            aBaseMetaTileEntity.getYCoord() - h*mFlipped,
                            (tFacing == 5 ? -j : tFacing == 4 ? j : tFacing == 3 ? -i : i) + aBaseMetaTileEntity.getZCoord(), currentBlock, currentCasingMeta,aRenderer));//new BlockHint(world,x,y,z,block,meta
                    }
                }
            }
        }
        return true;
    }

    //region trash
   /* public static String[][] test = new String[][]{
            {
            "aaaaaaaaaaaaaaaaaaaMMMMMMMMMaaaaaaaaaaaaaaaaaaa",
            "aaaaaaaaaaaaaaaaMMMaaaaaaaaaMMMaaaaaaaaaaaaaaaa",
            "aaaaaaaaaaaaaaMMaaaaaaaaaaaaaaaMMaaaaaaaaaaaaaa",
            "aaaaaaaaaaaaMMaaaaaaaaaaaaaaaaaaaMMaaaaaaaaaaaa",
            "aaaaaaaaaaMMaaaaaaaaaaaaaaaaaaaaaaaMMaaaaaaaaaa",
            "aaaaaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaaaaa",
            "aaaaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaaaa",
            "aaaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaaa",
            "aaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaa",
            "aaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaa",
            "aaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaa",
            "aaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaa",
            "aaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaa",
            "aaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaa",
            "aaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaa",
            "aaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaa",
            "aMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMa",
            "aMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMa",
            "aMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMa",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "MaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaM",
            "aMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMa",
            "aMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMa",
            "aMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMa",
            "aaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaa",
            "aaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaa",
            "aaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaa",
            "aaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaa",
            "aaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaa",
            "aaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaa",
            "aaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaa",
            "aaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaa",
            "aaaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaaa",
            "aaaaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaaaa",
            "aaaaaaaaaMaaaaaaaaaaaaaaaaaaaaaaaaaaaMaaaaaaaaa",
            "aaaaaaaaaaMMaaaaaaaaaaaaaaaaaaaaaaaMMaaaaaaaaaa",
            "aaaaaaaaaaaaMMaaaaaaaaaaaaaaaaaaaMMaaaaaaaaaaaa",
            "aaaaaaaaaaaaaaMMaaaaaaaaaaaaaaaMMaaaaaaaaaaaaaa",
            "aaaaaaaaaaaaaaaaMMMaaaaaaaaaMMMaaaaaaaaaaaaaaaa",
            "aaaaaaaaaaaaaaaaaaaMMMMcMMMMaaaaaaaaaaaaaaaaaaa",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}
    };*/

    /*public static String[][] test = new String[][]{
            {
                    "aaaaaaaaaaaaaaaaaaaMMMMMMMMMaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaMMMMMMMMMMMMMMMaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaMMMMMMMMMMMMMMMMMMMaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaMMMMMMMaaaaaaaaaMMMMMMMaaaaaaaaaaaa",
                    "aaaaaaaaaaMMMMMMaaaaaaaaaaaaaaaMMMMMMaaaaaaaaaa",
                    "aaaaaaaaaMMMMMaaaaaaaaaaaaaaaaaaaMMMMMaaaaaaaaa",
                    "aaaaaaaaMMMMaaaaaaaaaaaaaaaaaaaaaaaMMMMaaaaaaaa",
                    "aaaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaaa",
                    "aaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaa",
                    "aaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaa",
                    "aaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaa",
                    "aaaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaaa",
                    "aaaaaaaaMMMMaaaaaaaaaaaaaaaaaaaaaaaMMMMaaaaaaaa",
                    "aaaaaaaaaMMMMMaaaaaaaaaaaaaaaaaaaMMMMMaaaaaaaaa",
                    "aaaaaaaaaaMMMMMMaaaaaaaaaaaaaaaMMMMMMaaaaaaaaaa",
                    "aaaaaaaaaaaaMMMMMMMaaaaaaaaaMMMMMMMaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaMMMMMMMMMMMMMMMMMMMaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaMMMMMMMMMMMMMMMaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaMMMMMMMMMaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaaaaaaaaNNNNNNNNNaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaNNNNNNNaaaaaaaaaNNNNNNNaaaaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNaaaaaaaaaaaaaaaNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaNNNNNaaaaaaaaa",
                    "aaaaaaaaNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNaaaaaaaa",
                    "aaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaa",
                    "aaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "NNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNN",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaa",
                    "aaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaa",
                    "aaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaa",
                    "aaaaaaaNNNaaaaaaaaaaaaaaaaaaaaaaaaaaaNNNaaaaaaa",
                    "aaaaaaaaNNNNaaaaaaaaaaaaaaaaaaaaaaaNNNNaaaaaaaa",
                    "aaaaaaaaaNNNNNaaaaaaaaaaaaaaaaaaaNNNNNaaaaaaaaa",
                    "aaaaaaaaaaNNNNNNaaaaaaaaaaaaaaaNNNNNNaaaaaaaaaa",
                    "aaaaaaaaaaaaNNNNNNNaaaaaaaaaNNNNNNNaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaNNNNNNNNNNNNNNNNNNNaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaNNNNNNNNNNNNNNNaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaNNNNcNNNNaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"},
            {
                    "aaaaaaaaaaaaaaaaaaaMMMMMMMMMaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaMMMMMMMMMMMMMMMaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaMMMMMMMMMMMMMMMMMMMaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaMMMMMMMaaaaaaaaaMMMMMMMaaaaaaaaaaaa",
                    "aaaaaaaaaaMMMMMMaaaaaaaaaaaaaaaMMMMMMaaaaaaaaaa",
                    "aaaaaaaaaMMMMMaaaaaaaaaaaaaaaaaaaMMMMMaaaaaaaaa",
                    "aaaaaaaaMMMMaaaaaaaaaaaaaaaaaaaaaaaMMMMaaaaaaaa",
                    "aaaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaaa",
                    "aaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaa",
                    "aaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "MMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMM",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaa",
                    "aaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaa",
                    "aaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaa",
                    "aaaaaaaMMMaaaaaaaaaaaaaaaaaaaaaaaaaaaMMMaaaaaaa",
                    "aaaaaaaaMMMMaaaaaaaaaaaaaaaaaaaaaaaMMMMaaaaaaaa",
                    "aaaaaaaaaMMMMMaaaaaaaaaaaaaaaaaaaMMMMMaaaaaaaaa",
                    "aaaaaaaaaaMMMMMMaaaaaaaaaaaaaaaMMMMMMaaaaaaaaaa",
                    "aaaaaaaaaaaaMMMMMMMaaaaaaaaaMMMMMMMaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaMMMMMMMMMMMMMMMMMMMaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaMMMMMMMMMMMMMMMaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaMMMMMMMMMaaaaaaaaaaaaaaaaaaa",
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}
    };*/
    //endregion

}
