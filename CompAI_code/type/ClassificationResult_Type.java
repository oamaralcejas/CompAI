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


public class ClassificationResult_Type extends Annotation_Type {
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
      if (ClassificationResult_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = ClassificationResult_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new ClassificationResult(addr, ClassificationResult_Type.this);
          ClassificationResult_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else {
        return new ClassificationResult(addr, ClassificationResult_Type.this);
      }
    }
  };
  /** @generated */
  public final static int typeIndexID = ClassificationResult.typeIndexID;
  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst =
      JCasRegistry.getFeatOkTst("lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");

  /**** Feature: probabilitiesOfClasses ***/
  /** @generated */
  final Feature casFeat_probabilitiesOfClasses;
  /** @generated */
  final int     casFeatCode_probabilitiesOfClasses;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getProbabilitiesOfClasses(int addr) {
        if (featOkTst && casFeat_probabilitiesOfClasses == null)
      jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    return ll_cas.ll_getRefValue(addr, casFeatCode_probabilitiesOfClasses);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setProbabilitiesOfClasses(int addr, int v) {
        if (featOkTst && casFeat_probabilitiesOfClasses == null)
      jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    ll_cas.ll_setRefValue(addr, casFeatCode_probabilitiesOfClasses, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getProbabilitiesOfClasses(int addr, int i) {
        if (featOkTst && casFeat_probabilitiesOfClasses == null)
      jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_probabilitiesOfClasses), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_probabilitiesOfClasses), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_probabilitiesOfClasses), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setProbabilitiesOfClasses(int addr, int i, int v) {
        if (featOkTst && casFeat_probabilitiesOfClasses == null)
      jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_probabilitiesOfClasses), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_probabilitiesOfClasses), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_probabilitiesOfClasses), i, v);
  }


  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public ClassificationResult_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_probabilitiesOfClasses = jcas.getRequiredFeatureDE(casType, "probabilitiesOfClasses", "uima.cas.FSArray", featOkTst);
    casFeatCode_probabilitiesOfClasses  = (null == casFeat_probabilitiesOfClasses) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_probabilitiesOfClasses).getCode();
   
  }

}
