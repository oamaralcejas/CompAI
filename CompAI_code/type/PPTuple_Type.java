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


public class PPTuple_Type extends Annotation_Type {
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
      if (PPTuple_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = PPTuple_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new PPTuple(addr, PPTuple_Type.this);
          PPTuple_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else {
        return new PPTuple(addr, PPTuple_Type.this);
      }
    }
  };
  /** @generated */
  public final static int typeIndexID = PPTuple.typeIndexID;
  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst =
      JCasRegistry.getFeatOkTst("lu.svv.saa.linklaters.privacypolicies.type.PPTuple");

  /**** Feature: text ***/
  /** @generated */
  final Feature casFeat_text;
  /** @generated */
  final int casFeatCode_text;

  /** @generated */
  public String getText(int addr) {
    if (featOkTst && casFeat_text == null) {
      jcas.throwFeatMissing("text", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_text);
  }

  /** @generated */
  public void setText(int addr, String v) {
    if (featOkTst && casFeat_text == null) {
      jcas.throwFeatMissing("text", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_text, v);
  }

  /**** Feature: metadata1 ***/
  /** @generated */
  final Feature casFeat_metadata1;
  /** @generated */
  final int casFeatCode_metadata1;

  /** @generated */
  public String getMetadata1(int addr) {
    if (featOkTst && casFeat_metadata1 == null) {
      jcas.throwFeatMissing("metadata1", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_metadata1);
  }

  /** @generated */
  public void setMetadata1(int addr, String v) {
    if (featOkTst && casFeat_metadata1 == null) {
      jcas.throwFeatMissing("metadata1", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_metadata1, v);
  }

  /**** Feature: metadata2 ***/
  /** @generated */
  final Feature casFeat_metadata2;
  /** @generated */
  final int casFeatCode_metadata2;

  /** @generated */
  public String getMetadata2(int addr) {
    if (featOkTst && casFeat_metadata2 == null) {
      jcas.throwFeatMissing("metadata2", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_metadata2);
  }

  /** @generated */
  public void setMetadata2(int addr, String v) {
    if (featOkTst && casFeat_metadata2 == null) {
      jcas.throwFeatMissing("metadata2", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_metadata2, v);
  }

  /**** Feature: metadata3 ***/
  /** @generated */
  final Feature casFeat_metadata3;
  /** @generated */
  final int casFeatCode_metadata3;

  /** @generated */
  public String getMetadata3(int addr) {
    if (featOkTst && casFeat_metadata3 == null) {
      jcas.throwFeatMissing("metadata3", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_metadata3);
  }

  /** @generated */
  public void setMetadata3(int addr, String v) {
    if (featOkTst && casFeat_metadata3 == null) {
      jcas.throwFeatMissing("metadata3", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_metadata3, v);
  }

  /**** Feature: metadata4 ***/
  /** @generated */
  final Feature casFeat_metadata4;
  /** @generated */
  final int casFeatCode_metadata4;

  /** @generated */
  public String getMetadata4(int addr) {
    if (featOkTst && casFeat_metadata4 == null) {
      jcas.throwFeatMissing("metadata4", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_metadata4);
  }

  /** @generated */
  public void setMetadata4(int addr, String v) {
    if (featOkTst && casFeat_metadata4 == null) {
      jcas.throwFeatMissing("metadata4", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_metadata4, v);
  }

  /**** Feature: keywords ***/
  /** @generated */
  final Feature casFeat_keywords;
  /** @generated */
  final int casFeatCode_keywords;

  /** @generated */
  public String getKeywords(int addr) {
    if (featOkTst && casFeat_keywords == null) {
      jcas.throwFeatMissing("keywords", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_keywords);
  }

  /** @generated */
  public void setKeywords(int addr, String v) {
    if (featOkTst && casFeat_keywords == null) {
      jcas.throwFeatMissing("keywords", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_keywords, v);
  }

  /**** Feature: value ***/
  /** @generated */
  final Feature casFeat_value;
  /** @generated */
  final int casFeatCode_value;

  /** @generated */
  public String getValue(int addr) {
    if (featOkTst && casFeat_value == null) {
      jcas.throwFeatMissing("value", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_value);
  }

  /** @generated */
  public void setValue(int addr, String v) {
    if (featOkTst && casFeat_value == null) {
      jcas.throwFeatMissing("value", "lu.svv.saa.linklaters.privacypolicies.type.PPTuple");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_value, v);
  }

  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public PPTuple_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_text = jcas.getRequiredFeatureDE(casType, "text", "uima.cas.String", featOkTst);
    casFeatCode_text =
        (null == casFeat_text) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_text).getCode();

    casFeat_metadata1 =
        jcas.getRequiredFeatureDE(casType, "metadata1", "uima.cas.String", featOkTst);
    casFeatCode_metadata1 = (null == casFeat_metadata1) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_metadata1).getCode();

    casFeat_metadata2 =
        jcas.getRequiredFeatureDE(casType, "metadata2", "uima.cas.String", featOkTst);
    casFeatCode_metadata2 = (null == casFeat_metadata2) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_metadata2).getCode();

    casFeat_metadata3 =
        jcas.getRequiredFeatureDE(casType, "metadata3", "uima.cas.String", featOkTst);
    casFeatCode_metadata3 = (null == casFeat_metadata3) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_metadata3).getCode();

    casFeat_metadata4 =
        jcas.getRequiredFeatureDE(casType, "metadata4", "uima.cas.String", featOkTst);
    casFeatCode_metadata4 = (null == casFeat_metadata4) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_metadata4).getCode();


    casFeat_keywords = jcas.getRequiredFeatureDE(casType, "keywords", "uima.cas.String", featOkTst);
    casFeatCode_keywords = (null == casFeat_keywords) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_keywords).getCode();


    casFeat_value = jcas.getRequiredFeatureDE(casType, "value", "uima.cas.String", featOkTst);
    casFeatCode_value = (null == casFeat_value) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_value).getCode();

  }

}
