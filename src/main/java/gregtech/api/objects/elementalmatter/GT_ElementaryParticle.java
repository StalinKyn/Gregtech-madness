package gregtech.api.objects.elementalmatter;

import net.minecraft.nbt.NBTTagCompound;
import scala.Char;


import java.util.HashMap;

public class  GT_ElementaryParticle implements  IParticle{

    //to save some RAM
    public static byte oneThird = 2;
    public static byte minusOneThird = -2;
    public static byte oneSecond = 3;
    public static byte minusOneSecond = -3;
    public static byte one = 6;
    public static byte minusOne = -6;
    public static byte twoThird = 4;
    public static byte minusTwoThird = -4;
    public static byte zero = 0;

    public static HashMap<Character,GT_ElementaryParticle> mElementaryParticles = new HashMap<>(20);

    public final long mLifeTime;
    public final double mMass;// MeV/c^2
    public final byte mElectricCharge, mSpin;
    public final String mName;
    public final char mCode;

    public GT_ElementaryParticle(double aMass, byte aElectricCharge, byte aSpin, long aLifeTime, String aName, char aCode){
        mMass = aMass;
        mElectricCharge = aElectricCharge;
        mSpin = aSpin;
        mLifeTime = aLifeTime;
        mName = aName;
        mCode = aCode;
        mElementaryParticles.put(aCode,this);
    }
    //quarks
    public static GT_ElementaryParticle upQuark = new GT_ElementaryParticle(2.2,twoThird,oneSecond,Long.MAX_VALUE,"Up Quark",'u');
    public static GT_ElementaryParticle downQuark = new GT_ElementaryParticle(4.7,minusOneThird,oneSecond,Long.MAX_VALUE,"Down Quark",'d');
    public static GT_ElementaryParticle charmQuark = new GT_ElementaryParticle(1280,twoThird,oneSecond,Long.MAX_VALUE,"Charm Quark",'c');
    public static GT_ElementaryParticle strangeQuark = new GT_ElementaryParticle(96,minusOneThird,oneSecond,Long.MAX_VALUE,"Strange Quark",'s');
    public static GT_ElementaryParticle topQuark = new GT_ElementaryParticle(173100,twoThird,oneSecond,Long.MAX_VALUE,"Top Quark",'t');
    public static GT_ElementaryParticle bottomQuark = new GT_ElementaryParticle(4180,minusOneThird,oneSecond,Long.MAX_VALUE,"Bottom Quark",'b');
   //antiquarks
    public static GT_ElementaryParticle antiUpQuark = new GT_ElementaryParticle(2.2,minusTwoThird,minusOneSecond,Long.MAX_VALUE,"Anti Up Quark",'U');
    public static GT_ElementaryParticle antiDownQuark = new GT_ElementaryParticle(4.7,oneThird, minusOneSecond,Long.MAX_VALUE,"Anti Down Quark",'D');
    public static GT_ElementaryParticle antiCharmQuark = new GT_ElementaryParticle(1280,minusTwoThird,minusOneSecond,Long.MAX_VALUE,"Anti Charm Quark",'C');
    public static GT_ElementaryParticle antiStrangeQuark = new GT_ElementaryParticle(96,oneThird, minusOneSecond,Long.MAX_VALUE,"Anti Strange Quark",'S');
    public static GT_ElementaryParticle antiTopQuark = new GT_ElementaryParticle(173100,minusTwoThird,minusOneSecond,Long.MAX_VALUE,"Anti Top Quark",'T');
    public static GT_ElementaryParticle antiBottomQuark = new GT_ElementaryParticle(4180,oneThird, minusOneSecond,Long.MAX_VALUE,"Anti Bottom Quark",'B');
    //short definitions
    public static GT_ElementaryParticle U = upQuark;
    public static GT_ElementaryParticle D = downQuark;
    public static GT_ElementaryParticle C = charmQuark;
    public static GT_ElementaryParticle S = strangeQuark;
    public static GT_ElementaryParticle T = topQuark;
    public static GT_ElementaryParticle B = bottomQuark;
    public static GT_ElementaryParticle aU = antiUpQuark;
    public static GT_ElementaryParticle aD = antiDownQuark;
    public static GT_ElementaryParticle aC = antiCharmQuark;
    public static GT_ElementaryParticle aS = antiStrangeQuark;
    public static GT_ElementaryParticle aT = antiTopQuark;
    public static GT_ElementaryParticle aB = antiBottomQuark;

    public static GT_ElementaryParticle electron = new GT_ElementaryParticle(0.511,minusOne,oneSecond, Long.MAX_VALUE,"Electron",'e');
    public static GT_ElementaryParticle positron = new GT_ElementaryParticle(0.511,one,minusOneSecond, Long.MAX_VALUE,"Positron",'E');
    public static GT_ElementaryParticle muon = new GT_ElementaryParticle(105.66,minusOne,oneSecond, Long.MAX_VALUE,"Muon",'m');
    public static GT_ElementaryParticle antiMuon = new GT_ElementaryParticle(105.66,one,minusOneSecond, Long.MAX_VALUE,"Anti Muon",'M');
    public static GT_ElementaryParticle tauLepton = new GT_ElementaryParticle(1776.99,minusOne,oneSecond, Long.MAX_VALUE,"Tau-Lepton",'l');
    public static GT_ElementaryParticle antiTauLepnton = new GT_ElementaryParticle(1776.99,one,minusOneSecond, Long.MAX_VALUE,"Anti Tau-Lepton",'L');

    public static GT_ElementaryParticle E = electron;
    public static GT_ElementaryParticle P = positron;
    public static GT_ElementaryParticle M = muon;
    public static GT_ElementaryParticle aM = antiMuon;

    public static GT_ElementaryParticle electronNeutrino = new GT_ElementaryParticle(0.0000022,zero,oneSecond, Long.MAX_VALUE,"Electron Neutrino",'n');
    public static GT_ElementaryParticle electronAntiNeutrino = new GT_ElementaryParticle(0.0000022,zero,minusOneSecond, Long.MAX_VALUE,"Electron Anti Neutrino",'N');
    public static GT_ElementaryParticle muonNeutrino = new GT_ElementaryParticle(0.0000022,zero,oneSecond, Long.MAX_VALUE,"Muon Neutrino",'j');
    public static GT_ElementaryParticle muonAntiNeutrino = new GT_ElementaryParticle(0.0000022,zero,minusOneSecond, Long.MAX_VALUE,"Muon Anti Neutrino",'J');
    public static GT_ElementaryParticle tauNeutrino = new GT_ElementaryParticle(0.0000022,zero,oneSecond, Long.MAX_VALUE,"Tau Neutrino",'y');
    public static GT_ElementaryParticle tauAntiNeutrino = new GT_ElementaryParticle(0.0000022,zero,minusOneSecond, Long.MAX_VALUE,"Tau Anti Neutrino",'Y');

    public static GT_ElementaryParticle loadFromChar(char aChar) {
        return mElementaryParticles.get(aChar);

    }

    public char saveToChar() {
        return mCode;
    }
}
