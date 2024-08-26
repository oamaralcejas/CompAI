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


public class ClassProbabilityPair_Type extends Annotation_Type {
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
      if (ClassProbabilityPair_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = ClassProbabilityPair_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new ClassProbabilityPair(addr, ClassProbabilityPair_Type.this);
          ClassProbabilityPair_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else {
        return new ClassProbabilityPair(addr, ClassProbabilityPair_Type.this);
      }
    }
  };
  /** @generated */
  public final static int typeIndexID = ClassProbabilityPair.typeIndexID;
  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst =
      JCasRegistry.getFeatOkTst("lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");

  /**** Feature: classLabel ***/
  /** @generated */
  final Feature casFeat_classLabel;
  /** @generated */
  final int casFeatCode_classLabel;

  /** @generated */
  public String getClassLabel(int addr) {
    if (featOkTst && casFeat_classLabel == null) {
      jcas.throwFeatMissing("classLabel", "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_classLabel);
  }

  /** @generated */
  public void setClassLabel(int addr, String v) {
    if (featOkTst && casFeat_classLabel == null) {
      jcas.throwFeatMissing("classLabel", "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_classLabel, v);
  }

  /**** Feature: probability ***/
  /** @generated */
  final Feature casFeat_probability;
  /** @generated */
  final int casFeatCode_probability;

  /** @generated */
  public double getProbability(int addr) {
    if (featOkTst && casFeat_probability == null) {
      jcas.throwFeatMissing("probability", "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_probability);
  }

  /** @generated */
  public void setProbability(int addr, double v) {
    if (featOkTst && casFeat_probability == null) {
      jcas.throwFeatMissing("probability", "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    ll_cas.ll_setDoubleValue(addr, casFeatCode_probability, v);
  }

  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public ClassProbabilityPair_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_classLabel =
        jcas.getRequiredFeatureDE(casType, "classLabel", "uima.cas.String", featOkTst);
    casFeatCode_classLabel = (null == casFeat_classLabel) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_classLabel).getCode();

    casFeat_probability =
        jcas.getRequiredFeatureDE(casType, "probability", "uima.cas.Double", featOkTst);
    casFeatCode_probability = (null == casFeat_probability) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_probability).getCode();
  }

}
