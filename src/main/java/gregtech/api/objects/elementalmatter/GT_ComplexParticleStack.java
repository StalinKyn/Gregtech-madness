package gregtech.api.objects.elementalmatter;

public class GT_ComplexParticleStack {

    public GT_ComplexParticle mParticle;
    public int mAmount;
    public GT_ComplexParticleTypes mType;
    public double mVelocity;

    public final static char HARDON ='h';
    public final static char ATOM ='a';

    public  GT_ComplexParticleStack(GT_ComplexParticle aParticle, int aAmount){
        mParticle = aParticle;
        mAmount = aAmount;
    }

    public static GT_ComplexParticleStack loadFromString(String aString){
        if(aString==null ||aString.length()==0)
            return null;
        char type = aString.charAt(0);
        switch (type){
            case HARDON:
                GT_ComplexParticleStack tHardon = new GT_ComplexParticleStack( new GT_ComplexParticle(),0);
                String[] aCodes = aString.split(",");
                for (int i = 1; i < aCodes[0].length();i++){
                    tHardon.mParticle.mElementaryParticles.add(new GT_ElementaryParticleStack(GT_ElementaryParticle.loadFromChar(aCodes[0].charAt(i)),1));
                }
                tHardon.mAmount = Integer.parseInt(aCodes[1]);
                tHardon.init();
                return tHardon;
            case ATOM:
                GT_ComplexParticleStack tAtom = new GT_ComplexParticleStack(new GT_ComplexParticle(),0);
                aCodes = aString.split(";");
                String[] aParticles = aCodes[0].split(":");
                for(int j = 1; j < aParticles.length;j++ )
                    switch (aParticles[j].charAt(0)){
                        case 'p':
                            short aCount = Short.parseShort(aParticles[j].split(",")[1]);
                            tAtom.mParticle.mElementaryParticles.add(new GT_ElementaryParticleStack(GT_ElementaryParticle.loadFromChar(aParticles[j].charAt(1)),aCount));
                            break;
                        default:
                            tAtom.mParticle.mComplexParticles.add(GT_ComplexParticleStack.loadFromString(aParticles[j]));
                            break;
                    }
                tAtom.mAmount = Integer.parseInt(aCodes[1]);
                tAtom.mVelocity = Double.parseDouble(aCodes[2]);
                tAtom.init();
                return tAtom;
        }
        return null;
    }

    public String saveToString(GT_ComplexParticleStack aStack){
        switch (aStack.mParticle.mType){
            case HARDON:
                String out = "";
                for(GT_ElementaryParticleStack aPart:aStack.mParticle.mElementaryParticles){
                    out+=aPart.mParticle.saveToChar();
                }
                return out;
            case ATOM:
                out = "";
                for(GT_ElementaryParticleStack aPart:aStack.mParticle.mElementaryParticles){
                    out+=aPart.mParticle.saveToChar();
                }
                for(GT_ComplexParticleStack aPart:aStack.mParticle.mComplexParticles){
                    out+=aPart.saveToString(aPart);
                }
                return out;
        }
        return null;
    }


    public int getElectricCharge(){
        int out = 0;
        for(GT_ElementaryParticleStack aPart:mParticle.mElementaryParticles){
            out+=aPart.mParticle.mElectricCharge*aPart.mAmount;
        }
        for(GT_ComplexParticleStack aPart:mParticle.mComplexParticles){
            out+=aPart.getElectricCharge()*aPart.mAmount;
        }
        return out;
    }

    public double getMass(){
        double out = 0;
        if(mType == GT_ComplexParticleTypes.nuclide) {
            for (GT_ElementaryParticleStack aPart : mParticle.mElementaryParticles) {
                out += aPart.mParticle.mMass * aPart.mAmount;
            }
            for (GT_ComplexParticleStack aPart : mParticle.mComplexParticles) {
                out += aPart.getMass() * aPart.mAmount;
            }
            out -= getBoundEnegry();
            return out;
        }else{
            if(mType == GT_ComplexParticleTypes.proton)
                return 938.272d;
            else if(mType == GT_ComplexParticleTypes.neutron)
                return 939.565d;
        }
        return out;
    }

    public short getNucleusElectricCharge(){
        short out = 0;
        if(mType == GT_ComplexParticleTypes.nuclide)
            for(GT_ComplexParticleStack aPart:mParticle.mComplexParticles){
                out+=aPart.getElectricCharge()*aPart.mAmount;
            }
        return out;
    }

    public short getNeutronCount(){
        short out = 0;
        if(mType == GT_ComplexParticleTypes.nuclide)
            for(GT_ComplexParticleStack aPart:mParticle.mComplexParticles){
                if(aPart.mType == GT_ComplexParticleTypes.neutron)
                    out+=aPart.mAmount;
            }
        return out;
    }

    public short getProtonCount(){
        short out = 0;
        if(mType == GT_ComplexParticleTypes.nuclide)
            for(GT_ComplexParticleStack aPart:mParticle.mComplexParticles){
                if(aPart.mType == GT_ComplexParticleTypes.proton)
                    out+=aPart.mAmount;
            }
        return out;
    }

    public short getElectronCount(){
        short out = 0;
        if(mType == GT_ComplexParticleTypes.nuclide)
            for(GT_ElementaryParticleStack aPart:mParticle.mElementaryParticles){
                if(aPart.mParticle.mCode =='e')
                    out+=aPart.mAmount;
            }
        return out;
    }

    public double getEnergy(){
        return getMass()/Math.pow((1-Math.pow(mVelocity/GT_EM_Utility.speedOfLight,2)),0.5f);
    }

    public double getBoundEnegry(){
        double boundCoefficient = 3d;
        if(mType==GT_ComplexParticleTypes.nuclide)
            return (getNeutronCount()+getProtonCount())*boundCoefficient;
        return 0;
    }

    public void init(){
        mType = mParticle.getType();
    }
}
/*
*
* */