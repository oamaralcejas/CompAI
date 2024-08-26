package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class ClassProbabilityPair extends Annotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(ClassProbabilityPair.class);
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
  public ClassProbabilityPair() {}

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public ClassProbabilityPair(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public ClassProbabilityPair(JCas jCas) {
    super(jCas);
    readObject();
  }

  /** @generated */
  public ClassProbabilityPair(JCas jcas, int begin, int end) {
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

  // *--------------*
  // * Feature: classLabel

  /**
   * getter for classLabel
   * 
   * @generated
   */
  public String getClassLabel() {
    if (ClassProbabilityPair_Type.featOkTst && ((ClassProbabilityPair_Type) jcasType).casFeat_classLabel == null) {
      jcasType.jcas.throwFeatMissing("classLabel",
          "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((ClassProbabilityPair_Type) jcasType).casFeatCode_classLabel);
  }

  /**
   * setter for classLabel
   * 
   * @generated
   */
  public void setClassLabel(String v) {
    if (ClassProbabilityPair_Type.featOkTst && ((ClassProbabilityPair_Type) jcasType).casFeat_classLabel == null) {
      jcasType.jcas.throwFeatMissing("classLabel",
          "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((ClassProbabilityPair_Type) jcasType).casFeatCode_classLabel, v);
  }


  // *--------------*
  // * Feature: probability

  /**
   * getter for probability
   * 
   * @generated
   */
  public double getProbability() {
    if (ClassProbabilityPair_Type.featOkTst && ((ClassProbabilityPair_Type) jcasType).casFeat_probability == null) {
      jcasType.jcas.throwFeatMissing("probability",
          "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((ClassProbabilityPair_Type) jcasType).casFeatCode_probability);
  }

  /**
   * setter for probability
   * 
   * @generated
   */
  public void setProbability(double v) {
    if (ClassProbabilityPair_Type.featOkTst && ((ClassProbabilityPair_Type) jcasType).casFeat_probability == null) {
      jcasType.jcas.throwFeatMissing("probability",
          "lu.svv.saa.linklaters.privacypolicies.type.ClassProbabilityPair");
    }
    jcasType.ll_cas.ll_setDoubleValue(addr, ((ClassProbabilityPair_Type) jcasType).casFeatCode_probability, v);
  }

}
