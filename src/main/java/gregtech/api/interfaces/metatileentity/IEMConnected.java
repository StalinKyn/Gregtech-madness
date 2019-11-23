package gregtech.api.interfaces.metatileentity;

import gregtech.api.objects.elementalmatter.GT_ComplexParticleStack;

public interface IEMConnected {
    public boolean transfersEMAt(byte aSide);

    public boolean inputEMFrom(GT_ComplexParticleStack aParticle, byte aSide);
}
