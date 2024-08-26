package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class SentenceVector extends Annotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(SentenceVector.class);
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
  public SentenceVector() {}

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public SentenceVector(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public SentenceVector(JCas jCas) {
    super(jCas);
    readObject();
  }

  /** @generated */
  public SentenceVector(JCas jcas, int begin, int end) {
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
  // * Feature: reasonNotTransformed

  /**
   * getter for reasonNotTransformed
   * 
   * @generated
   */
  public String getReasonNotTransformed() {
    if (SentenceVector_Type.featOkTst
        && ((SentenceVector_Type) jcasType).casFeat_reasonNotTransformed == null) {
      jcasType.jcas.throwFeatMissing("reasonNotTransformed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return jcasType.ll_cas.ll_getStringValue(addr,
        ((SentenceVector_Type) jcasType).casFeatCode_reasonNotTransformed);
  }

  /**
   * setter for reasonNotTransformed
   * 
   * @generated
   */
  public void setReasonNotTransformed(String v) {
    if (SentenceVector_Type.featOkTst
        && ((SentenceVector_Type) jcasType).casFeat_reasonNotTransformed == null) {
      jcasType.jcas.throwFeatMissing("reasonNotTransformed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    jcasType.ll_cas.ll_setStringValue(addr,
        ((SentenceVector_Type) jcasType).casFeatCode_reasonNotTransformed, v);
  }

  // *--------------*
  // * Feature: transformPreporcessed

  /**
   * getter for transformPreporcessed
   * 
   * @generated
   */
  public boolean transformPreporcessed() {
    if (SentenceVector_Type.featOkTst
        && ((SentenceVector_Type) jcasType).casFeat_transformPreporcessed == null) {
      jcasType.jcas.throwFeatMissing("transformPreporcessed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return jcasType.ll_cas.ll_getBooleanValue(addr,
        ((SentenceVector_Type) jcasType).casFeatCode_transformPreporcessed);
  }

  /**
   * setter for transformPreporcessed
   * 
   * @generated
   */
  public void setTransformPreporcessed(boolean v) {
    if (SentenceVector_Type.featOkTst
        && ((SentenceVector_Type) jcasType).casFeat_transformPreporcessed == null) {
      jcasType.jcas.throwFeatMissing("transformPreporcessed",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    jcasType.ll_cas.ll_setBooleanValue(addr,
        ((SentenceVector_Type) jcasType).casFeatCode_transformPreporcessed, v);
  }

  // *--------------*
  // * Feature: vector

  /**
   * getter for vector
   * 
   * @generated
   */
  public String getVector() {
    if (SentenceVector_Type.featOkTst && ((SentenceVector_Type) jcasType).casFeat_vector == null) {
      jcasType.jcas.throwFeatMissing("vector",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return jcasType.ll_cas.ll_getStringValue(addr,
        ((SentenceVector_Type) jcasType).casFeatCode_vector);
  }

  /**
   * setter for vector
   * 
   * @generated
   */
  public void setVector(String v) {
    if (SentenceVector_Type.featOkTst && ((SentenceVector_Type) jcasType).casFeat_vector == null) {
      jcasType.jcas.throwFeatMissing("vector",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((SentenceVector_Type) jcasType).casFeatCode_vector, v);
  }


  // *--------------*
  // * Feature: vectorDimension

  /**
   * getter for vectorDimension
   * 
   * @generated
   */
  public int getVectorDimension() {
    if (SentenceVector_Type.featOkTst
        && ((SentenceVector_Type) jcasType).casFeat_vectorDimension == null) {
      jcasType.jcas.throwFeatMissing("vectorDimension",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    return jcasType.ll_cas.ll_getIntValue(addr,
        ((SentenceVector_Type) jcasType).casFeatCode_vectorDimension);
  }

  /**
   * setter for vectorDimension
   * 
   * @generated
   */
  public void setVectorDimension(int v) {
    if (SentenceVector_Type.featOkTst
        && ((SentenceVector_Type) jcasType).casFeat_vectorDimension == null) {
      jcasType.jcas.throwFeatMissing("vectorDimension",
          "lu.svv.saa.linklaters.privacypolicies.type.SentenceVector");
    }
    jcasType.ll_cas.ll_setIntValue(addr,
        ((SentenceVector_Type) jcasType).casFeatCode_vectorDimension, v);
  }

}
