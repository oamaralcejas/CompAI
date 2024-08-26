package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;


public class UoA_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  protected FSGenerator getFSGenerator() {
    return fsGenerator;
  }

  /** @generated */
  @SuppressWarnings("rawtypes")
  private final FSGenerator fsGenerator = new FSGenerator() {
    public FeatureStructure createFS(int addr, CASImpl cas) {
      if (UoA_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = UoA_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new UoA(addr, UoA_Type.this);
          UoA_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else {
        return new UoA(addr, UoA_Type.this);
      }
    }
  };
  /** @generated */
  public final static int typeIndexID = UoA.typeIndexID;
  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst =
      JCasRegistry.getFeatOkTst("lu.svv.saa.linklaters.privacypolicies.type.UoA");

  /**** Feature: generalized ***/
  /** @generated */
  final Feature casFeat_generalized;
  /** @generated */
  final int casFeatCode_generalized;

  /** @generated */
  public String getGeneralized(int addr) {
    if (featOkTst && casFeat_generalized == null) {
      jcas.throwFeatMissing("generalized", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_generalized);
  }

  /** @generated */
  public void setGeneralized(int addr, String v) {
    if (featOkTst && casFeat_generalized == null) {
      jcas.throwFeatMissing("generalized", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_generalized, v);
  }

  /**** Feature: preprocessed ***/
  /** @generated */
  final Feature casFeat_preprocessed;
  /** @generated */
  final int casFeatCode_preprocessed;

  /** @generated */
  public String getPreprocessed(int addr) {
    if (featOkTst && casFeat_preprocessed == null) {
      jcas.throwFeatMissing("preprocessed", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_preprocessed);
  }

  /** @generated */
  public void setPreprocessed(int addr, String v) {
    if (featOkTst && casFeat_preprocessed == null) {
      jcas.throwFeatMissing("preprocessed", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_preprocessed, v);
  }

  /**** Feature: hasNE ***/
  /** @generated */
  final Feature casFeat_hasNE;
  /** @generated */
  final int casFeatCode_hasNE;

  /** @generated */
  public boolean hasNE(int addr) {
    if (featOkTst && casFeat_hasNE == null) {
      jcas.throwFeatMissing("hasNE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_hasNE);
  }

  /** @generated */
  public void setHasNE(int addr, boolean v) {
    if (featOkTst && casFeat_hasNE == null) {
      jcas.throwFeatMissing("hasNE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setBooleanValue(addr, casFeatCode_hasNE, v);
  }

  /**** Feature: hasPHONE ***/
  /** @generated */
  final Feature casFeat_hasPHONE;
  /** @generated */
  final int casFeatCode_hasPHONE;

  /** @generated */
  public boolean hasPHONE(int addr) {
    if (featOkTst && casFeat_hasPHONE == null) {
      jcas.throwFeatMissing("hasPHONE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_hasPHONE);
  }

  /** @generated */
  public void setHasPHONE(int addr, boolean v) {
    if (featOkTst && casFeat_hasPHONE == null) {
      jcas.throwFeatMissing("hasPHONE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setBooleanValue(addr, casFeatCode_hasPHONE, v);
  }

  /**** Feature: hasEMAIL ***/
  /** @generated */
  final Feature casFeat_hasEMAIL;
  /** @generated */
  final int casFeatCode_hasEMAIL;

  /** @generated */
  public boolean hasEMAIL(int addr) {
    if (featOkTst && casFeat_hasEMAIL == null) {
      jcas.throwFeatMissing("hasEMAIL", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_hasEMAIL);
  }

  /** @generated */
  public void setHasEMAIL(int addr, boolean v) {
    if (featOkTst && casFeat_hasEMAIL == null) {
      jcas.throwFeatMissing("hasEMAIL", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setBooleanValue(addr, casFeatCode_hasEMAIL, v);
  }

  /**** Feature: hasWEBSITE ***/
  /** @generated */
  final Feature casFeat_hasWEBSITE;
  /** @generated */
  final int casFeatCode_hasWEBSITE;

  /** @generated */
  public boolean hasWEBSITE(int addr) {
    if (featOkTst && casFeat_hasWEBSITE == null) {
      jcas.throwFeatMissing("hasWEBSITE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_hasWEBSITE);
  }

  /** @generated */
  public void setHasWEBSITE(int addr, boolean v) {
    if (featOkTst && casFeat_hasWEBSITE == null) {
      jcas.throwFeatMissing("hasWEBSITE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setBooleanValue(addr, casFeatCode_hasWEBSITE, v);
  }

  /**** Feature: hasADDRESS ***/
  /** @generated */
  final Feature casFeat_hasADDRESS;
  /** @generated */
  final int casFeatCode_hasADDRESS;

  /** @generated */
  public boolean hasADDRESS(int addr) {
    if (featOkTst && casFeat_hasADDRESS == null) {
      jcas.throwFeatMissing("hasADDRESS", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_hasADDRESS);
  }

  /** @generated */
  public void setHasADDRESS(int addr, boolean v) {
    if (featOkTst && casFeat_hasADDRESS == null) {
      jcas.throwFeatMissing("hasADDRESS", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setBooleanValue(addr, casFeatCode_hasADDRESS, v);
  }

  /**** Feature: containedKWs ***/
  /** @generated */
  final Feature casFeat_containedKWs;
  /** @generated */
  final int casFeatCode_containedKWs;

  /** @generated */
  public String getContainedKWs(int addr) {
    if (featOkTst && casFeat_containedKWs == null) {
      jcas.throwFeatMissing("containedKWs", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_containedKWs);
  }

  /** @generated */
  public void setContainedKWs(int addr, String v) {
    if (featOkTst && casFeat_containedKWs == null) {
      jcas.throwFeatMissing("containedKWs", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_containedKWs, v);
  }

  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public UoA_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_generalized =
        jcas.getRequiredFeatureDE(casType, "generalized", "uima.cas.String", featOkTst);
    casFeatCode_generalized = (null == casFeat_generalized) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_generalized).getCode();

    casFeat_preprocessed =
        jcas.getRequiredFeatureDE(casType, "preprocessed", "uima.cas.String", featOkTst);
    casFeatCode_preprocessed = (null == casFeat_preprocessed) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_preprocessed).getCode();

    casFeat_hasNE = jcas.getRequiredFeatureDE(casType, "hasNE", "uima.cas.Boolean", featOkTst);
    casFeatCode_hasNE = (null == casFeat_hasNE) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_hasNE).getCode();

    casFeat_hasPHONE =
        jcas.getRequiredFeatureDE(casType, "hasPHONE", "uima.cas.Boolean", featOkTst);
    casFeatCode_hasPHONE = (null == casFeat_hasPHONE) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_hasPHONE).getCode();

    casFeat_hasEMAIL =
        jcas.getRequiredFeatureDE(casType, "hasEMAIL", "uima.cas.Boolean", featOkTst);
    casFeatCode_hasEMAIL = (null == casFeat_hasEMAIL) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_hasEMAIL).getCode();

    casFeat_hasWEBSITE =
        jcas.getRequiredFeatureDE(casType, "hasWEBSITE", "uima.cas.Boolean", featOkTst);
    casFeatCode_hasWEBSITE = (null == casFeat_hasWEBSITE) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_hasWEBSITE).getCode();

    casFeat_hasADDRESS =
        jcas.getRequiredFeatureDE(casType, "hasADDRESS", "uima.cas.Boolean", featOkTst);
    casFeatCode_hasADDRESS = (null == casFeat_hasADDRESS) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_hasADDRESS).getCode();
    
    casFeat_containedKWs =
        jcas.getRequiredFeatureDE(casType, "containedKWs", "uima.cas.String", featOkTst);
    casFeatCode_containedKWs = (null == casFeat_containedKWs) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_containedKWs).getCode();

  }

}
