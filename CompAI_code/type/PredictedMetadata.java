package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class PredictedMetadata extends Annotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(PredictedMetadata.class);
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
  public PredictedMetadata() {}

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public PredictedMetadata(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public PredictedMetadata(JCas jCas) {
    super(jCas);
    readObject();
  }

  /** @generated */
  public PredictedMetadata(JCas jcas, int begin, int end) {
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
  // * Feature: metadata1

  /**
   * getter for metadata1
   * 
   * @generated
   */
  public String getMetadata1() {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata1 == null) {
      jcasType.jcas.throwFeatMissing("metadata1",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata1);
  }

  /**
   * setter for metadata1
   * 
   * @generated
   */
  public void setMetadata1(String v) {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata1 == null) {
      jcasType.jcas.throwFeatMissing("metadata1",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata1, v);
  }

  // *--------------*
  // * Feature: metadata2

  /**
   * getter for metadata2
   * 
   * @generated
   */
  public String getMetadata2() {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata2 == null) {
      jcasType.jcas.throwFeatMissing("metadata2",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata2);
  }

  /**
   * setter for metadata2
   * 
   * @generated
   */
  public void setMetadata2(String v) {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata2 == null) {
      jcasType.jcas.throwFeatMissing("metadata2",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata2, v);
  }

  // *--------------*
  // * Feature: metadata3

  /**
   * getter for metadata3
   * 
   * @generated
   */
  public String getMetadata3() {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata3 == null) {
      jcasType.jcas.throwFeatMissing("metadata3",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata3);
  }

  /**
   * setter for metadata3
   * 
   * @generated
   */
  public void setMetadata3(String v) {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata3 == null) {
      jcasType.jcas.throwFeatMissing("metadata3",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata3, v);
  }

  // *--------------*
  // * Feature: metadata4

  /**
   * getter for metadata4
   * 
   * @generated
   */
  public String getMetadata4() {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata4 == null) {
      jcasType.jcas.throwFeatMissing("metadata4",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata4);
  }

  /**
   * setter for metadata4
   * 
   * @generated
   */
  public void setMetadata4(String v) {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_metadata4 == null) {
      jcasType.jcas.throwFeatMissing("metadata4",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_metadata4, v);
  }


  // *--------------*
  // * Feature: keywords

  /**
   * getter for keywords
   * 
   * @generated
   */
  public String getKeywords() {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_keywords == null) {
      jcasType.jcas.throwFeatMissing("keywords",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_keywords);
  }

  /**
   * setter for keywords
   * 
   * @generated
   */
  public void setKeywords(String v) {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_keywords == null) {
      jcasType.jcas.throwFeatMissing("keywords",
          "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_keywords, v);
  }

  // *--------------*
  // * Feature: value

  /**
   * getter for value
   * 
   * @generated
   */
  public String getValue() {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_value == null) {
      jcasType.jcas.throwFeatMissing("value", "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_value);
  }

  /**
   * setter for value
   * 
   * @generated
   */
  public void setValue(String v) {
    if (PredictedMetadata_Type.featOkTst && ((PredictedMetadata_Type) jcasType).casFeat_value == null) {
      jcasType.jcas.throwFeatMissing("value", "lu.svv.saa.linklaters.privacypolicies.type.PredictedMetadata");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((PredictedMetadata_Type) jcasType).casFeatCode_value, v);
  }
}
