package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class ClassificationResult extends Annotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(ClassificationResult.class);
  /**
   * @generated
   * @ordered
   */
  public final static int type = typeIndexID;

  /** @generated */
  @Override
  public int getTypeIndexID() {
    return typeIndexID;
  }

  /**
   * Never called. Disable default constructor
   * 
   * @generated
   */
  public ClassificationResult() {}

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public ClassificationResult(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public ClassificationResult(JCas jCas) {
    super(jCas);
    readObject();
  }

  /** @generated */
  public ClassificationResult(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }

  /**
   * <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
   * 
   * @generated modifiable
   */
  private void readObject() {}

//*--------------*
  //* Feature: probabilitiesOfClasses

  /** getter for probabilitiesOfClasses - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getProbabilitiesOfClasses() {
    if (ClassificationResult_Type.featOkTst && ((ClassificationResult_Type)jcasType).casFeat_probabilitiesOfClasses == null)
      jcasType.jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((ClassificationResult_Type)jcasType).casFeatCode_probabilitiesOfClasses)));}
    
  /** setter for probabilitiesOfClasses - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setProbabilitiesOfClasses(FSArray v) {
    if (ClassificationResult_Type.featOkTst && ((ClassificationResult_Type)jcasType).casFeat_probabilitiesOfClasses == null)
      jcasType.jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    jcasType.ll_cas.ll_setRefValue(addr, ((ClassificationResult_Type)jcasType).casFeatCode_probabilitiesOfClasses, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for probabilitiesOfClasses - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public ClassProbabilityPair getProbabilitiesOfClasses(int i) {
    if (ClassificationResult_Type.featOkTst && ((ClassificationResult_Type)jcasType).casFeat_probabilitiesOfClasses == null)
      jcasType.jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((ClassificationResult_Type)jcasType).casFeatCode_probabilitiesOfClasses), i);
    return (ClassProbabilityPair)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((ClassificationResult_Type)jcasType).casFeatCode_probabilitiesOfClasses), i)));}

  /** indexed setter for probabilitiesOfClasses - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setProbabilitiesOfClasses(int i, ClassProbabilityPair v) { 
    if (ClassificationResult_Type.featOkTst && ((ClassificationResult_Type)jcasType).casFeat_probabilitiesOfClasses == null)
      jcasType.jcas.throwFeatMissing("probabilitiesOfClasses", "lu.svv.saa.linklaters.privacypolicies.type.ClassificationResult");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((ClassificationResult_Type)jcasType).casFeatCode_probabilitiesOfClasses), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((ClassificationResult_Type)jcasType).casFeatCode_probabilitiesOfClasses), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
}
