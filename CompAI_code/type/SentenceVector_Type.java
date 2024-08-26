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


public class SentenceVector_Type extends Annotation_Type {
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
      if (SentenceVector_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = SentenceVector_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new SentenceVector(addr, SentenceVector_Type.this);
          SentenceVector_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else {
        return new SentenceVector(addr, SentenceVector_Type.this);
      }
    }
  };
  /** @generated */
  public final static int typeIndexID = SentenceVector.typeIndexID;
  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst =
      JCasRegistry.getFeatOkTst("lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");

  /**** Feature: reasonNotTransformed ***/
  /** @generated */
  final Feature casFeat_reasonNotTransformed;
  /** @generated */
  final int casFeatCode_reasonNotTransformed;

  /** @generated */
  public String getReasonNotTransformed(int addr) {
    if (featOkTst && casFeat_reasonNotTransformed == null) {
      jcas.throwFeatMissing("reasonNotTransformed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_reasonNotTransformed);
  }

  /** @generated */
  public void setReasonNotTransformed(int addr, String v) {
    if (featOkTst && casFeat_reasonNotTransformed == null) {
      jcas.throwFeatMissing("reasonNotTransformed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_reasonNotTransformed, v);
  }

  /**** Feature: transformPreporcessed ***/
  /** @generated */
  final Feature casFeat_transformPreporcessed;
  /** @generated */
  final int casFeatCode_transformPreporcessed;

  /** @generated */
  public boolean transformPreporcessed(int addr) {
    if (featOkTst && casFeat_transformPreporcessed == null) {
      jcas.throwFeatMissing("transformPreporcessed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_transformPreporcessed);
  }

  /** @generated */
  public void setTransformPreporcessed(int addr, boolean v) {
    if (featOkTst && casFeat_transformPreporcessed == null) {
      jcas.throwFeatMissing("transformPreporcessed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    ll_cas.ll_setBooleanValue(addr, casFeatCode_transformPreporcessed, v);
  }

  /**** Feature: vector ***/
  /** @generated */
  final Feature casFeat_vector;
  /** @generated */
  final int casFeatCode_vector;

  /** @generated */
  public String getVector(int addr) {
    if (featOkTst && casFeat_vector == null) {
      jcas.throwFeatMissing("vector", "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_vector);
  }

  /** @generated */
  public void setVector(int addr, String v) {
    if (featOkTst && casFeat_vector == null) {
      jcas.throwFeatMissing("vector", "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_vector, v);
  }

  /**** Feature: vectorDimension ***/
  /** @generated */
  final Feature casFeat_vectorDimension;
  /** @generated */
  final int casFeatCode_vectorDimension;

  /** @generated */
  public int getVectorDimension(int addr) {
    if (featOkTst && casFeat_vectorDimension == null) {
      jcas.throwFeatMissing("vectorDimension",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return ll_cas.ll_getIntValue(addr, casFeatCode_vectorDimension);
  }

  /** @generated */
  public void setVectorDimension(int addr, int v) {
    if (featOkTst && casFeat_vectorDimension == null) {
      jcas.throwFeatMissing("vectorDimension",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    ll_cas.ll_setIntValue(addr, casFeatCode_vectorDimension, v);
  }

  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public SentenceVector_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_reasonNotTransformed =
        jcas.getRequiredFeatureDE(casType, "reasonNotTransformed", "uima.cas.String", featOkTst);
    casFeatCode_reasonNotTransformed =
        (null == casFeat_reasonNotTransformed) ? JCas.INVALID_FEATURE_CODE
            : ((FeatureImpl) casFeat_reasonNotTransformed).getCode();

    casFeat_transformPreporcessed =
        jcas.getRequiredFeatureDE(casType, "transformPreporcessed", "uima.cas.Boolean", featOkTst);
    casFeatCode_transformPreporcessed =
        (null == casFeat_transformPreporcessed) ? JCas.INVALID_FEATURE_CODE
            : ((FeatureImpl) casFeat_transformPreporcessed).getCode();

    casFeat_vector = jcas.getRequiredFeatureDE(casType, "vector", "uima.cas.String", featOkTst);
    casFeatCode_vector = (null == casFeat_vector) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_vector).getCode();

    casFeat_vectorDimension =
        jcas.getRequiredFeatureDE(casType, "vectorDimension", "uima.cas.Integer", featOkTst);
    casFeatCode_vectorDimension = (null == casFeat_vectorDimension) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_vectorDimension).getCode();

  }

}
