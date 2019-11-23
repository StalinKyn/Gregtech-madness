package gregtech.api.objects.elementalmatter;

import java.util.ArrayList;

public class GT_ComplexParticle implements IParticle{

    public ArrayList<GT_ComplexParticleStack> mComplexParticles;
    public ArrayList<GT_ElementaryParticleStack> mElementaryParticles;

    public char mType;

    public GT_ComplexParticle(ArrayList<GT_ComplexParticleStack> aComplexParticles, ArrayList<GT_ElementaryParticleStack> aElementaryParticles){
        mComplexParticles = aComplexParticles;
        mElementaryParticles = aElementaryParticles;
    }

    public GT_ComplexParticle(GT_ElementaryParticleStack aParticle){
        mElementaryParticles = new ArrayList<>();
        mElementaryParticles.add(aParticle);
    }

    public GT_ComplexParticle(){
        mComplexParticles  = new ArrayList<>();
        mElementaryParticles = new ArrayList<>();
    }

    public GT_ComplexParticleTypes getType(){
        byte uq = 0, dq = 0, e = 0;
        for(GT_ElementaryParticleStack aPart : mElementaryParticles){
            if(aPart.mParticle.mCode == 'u')
                uq++;
            else if(aPart.mParticle.mCode == 'd')
                dq++;
            else if(aPart.mParticle.mCode == 'e')
                e++;
        }
        if(uq == 2 && dq == 1)
            return GT_ComplexParticleTypes.proton;
        else if(uq == 1 && dq == 2)
            return GT_ComplexParticleTypes.neutron;
        byte i = 0;

        for(GT_ComplexParticleStack aPart: mComplexParticles){
            if(aPart.mParticle.getType() == GT_ComplexParticleTypes.neutron||aPart.mParticle.getType() == GT_ComplexParticleTypes.proton)
                i++;
            else
                i--;
        }
        if(i == 2 && e == 1)
            return GT_ComplexParticleTypes.nuclide;
        return GT_ComplexParticleTypes.unknown;
    }

}
