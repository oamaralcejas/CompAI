package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class UoA extends Annotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(UoA.class);
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
  public UoA() {}

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public UoA(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public UoA(JCas jCas) {
    super(jCas);
    readObject();
  }

  /** @generated */
  public UoA(JCas jcas, int begin, int end) {
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
  // * Feature: generalized

  /**
   * getter for generalized
   * 
   * @generated
   */
  public String getGeneralized() {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_generalized == null) {
      jcasType.jcas.throwFeatMissing("generalized",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((UoA_Type) jcasType).casFeatCode_generalized);
  }

  /**
   * setter for generalized
   * 
   * @generated
   */
  public void setGeneralized(String v) {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_generalized == null) {
      jcasType.jcas.throwFeatMissing("generalized",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((UoA_Type) jcasType).casFeatCode_generalized, v);
  }

  // *--------------*
  // * Feature: preprocessed

  /**
   * getter for preprocessed
   * 
   * @generated
   */
  public String getPreprocessed() {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_preprocessed == null) {
      jcasType.jcas.throwFeatMissing("preprocessed",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((UoA_Type) jcasType).casFeatCode_preprocessed);
  }

  /**
   * setter for preprocessed
   * 
   * @generated
   */
  public void setPreprocessed(String v) {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_preprocessed == null) {
      jcasType.jcas.throwFeatMissing("preprocessed",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((UoA_Type) jcasType).casFeatCode_preprocessed, v);
  }

  // *--------------*
  // * Feature: hasNE

  /**
   * getter for hasNE
   * 
   * @generated
   */
  public boolean hasNE() {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasNE == null) {
      jcasType.jcas.throwFeatMissing("hasNE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasNE);
  }

  /**
   * setter for hasNE
   * 
   * @generated
   */
  public void setHasNE(boolean v) {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasNE == null) {
      jcasType.jcas.throwFeatMissing("hasNE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    jcasType.ll_cas.ll_setBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasNE, v);
  }

  // *--------------*
  // * Feature: hasPHONE

  /**
   * getter for hasPHONE
   * 
   * @generated
   */
  public boolean hasPHONE() {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasPHONE == null) {
      jcasType.jcas.throwFeatMissing("hasPHONE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasPHONE);
  }

  /**
   * setter for hasPHONE
   * 
   * @generated
   */
  public void setHasPHONE(boolean v) {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasPHONE == null) {
      jcasType.jcas.throwFeatMissing("hasPHONE", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    jcasType.ll_cas.ll_setBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasPHONE, v);
  }

  // *--------------*
  // * Feature: hasEMAIL

  /**
   * getter for hasEMAIL
   * 
   * @generated
   */
  public boolean hasEMAIL() {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasEMAIL == null) {
      jcasType.jcas.throwFeatMissing("hasEMAIL", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasEMAIL);
  }

  /**
   * setter for hasEMAIL
   * 
   * @generated
   */
  public void setHasEMAIL(boolean v) {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasEMAIL == null) {
      jcasType.jcas.throwFeatMissing("hasEMAIL", "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    jcasType.ll_cas.ll_setBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasEMAIL, v);
  }

  // *--------------*
  // * Feature: hasWEBSITE

  /**
   * getter for hasWEBSITE
   * 
   * @generated
   */
  public boolean hasWEBSITE() {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasWEBSITE == null) {
      jcasType.jcas.throwFeatMissing("hasWEBSITE",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasWEBSITE);
  }

  /**
   * setter for hasWEBSITE
   * 
   * @generated
   */
  public void setHasWEBSITE(boolean v) {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasWEBSITE == null) {
      jcasType.jcas.throwFeatMissing("hasWEBSITE",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    jcasType.ll_cas.ll_setBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasWEBSITE, v);
  }

  // *--------------*
  // * Feature: hasADDRESS

  /**
   * getter for hasADDRESS
   * 
   * @generated
   */
  public boolean hasADDRESS() {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasADDRESS == null) {
      jcasType.jcas.throwFeatMissing("hasADDRESS",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasADDRESS);
  }

  /**
   * setter for hasADDRESS
   * 
   * @generated
   */
  public void setHasADDRESS(boolean v) {
    if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_hasADDRESS == null) {
      jcasType.jcas.throwFeatMissing("hasADDRESS",
          "lu.svv.saa.linklaters.privacypolicies.type.UoA");
    }
    jcasType.ll_cas.ll_setBooleanValue(addr, ((UoA_Type) jcasType).casFeatCode_hasADDRESS, v);
  }
  
//*--------------*
 // * Feature: containedKWs

 /**
  * getter for containedKWs
  * 
  * @generated
  */
 public String getContainedKWs() {
   if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_containedKWs == null) {
     jcasType.jcas.throwFeatMissing("containedKWs",
         "lu.svv.saa.linklaters.privacypolicies.type.UoA");
   }
   return jcasType.ll_cas.ll_getStringValue(addr, ((UoA_Type) jcasType).casFeatCode_containedKWs);
 }

 /**
  * setter for containedKWs
  * 
  * @generated
  */
 public void setContainedKWs(String v) {
   if (UoA_Type.featOkTst && ((UoA_Type) jcasType).casFeat_containedKWs == null) {
     jcasType.jcas.throwFeatMissing("containedKWs",
         "lu.svv.saa.linklaters.privacypolicies.type.UoA");
   }
   jcasType.ll_cas.ll_setStringValue(addr, ((UoA_Type) jcasType).casFeatCode_containedKWs, v);
 }


}
