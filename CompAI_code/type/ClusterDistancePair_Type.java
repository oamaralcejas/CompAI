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


public class ClusterDistancePair_Type extends Annotation_Type {
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
      if (ClusterDistancePair_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = ClusterDistancePair_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new ClusterDistancePair(addr, ClusterDistancePair_Type.this);
          ClusterDistancePair_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else {
        return new ClusterDistancePair(addr, ClusterDistancePair_Type.this);
      }
    }
  };
  /** @generated */
  public final static int typeIndexID = ClusterDistancePair.typeIndexID;
  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst =
      JCasRegistry.getFeatOkTst("lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");

  /**** Feature: clusterLabel ***/
  /** @generated */
  final Feature casFeat_clusterLabel;
  /** @generated */
  final int casFeatCode_clusterLabel;

  /** @generated */
  public String getClusterLabel(int addr) {
    if (featOkTst && casFeat_clusterLabel == null) {
      jcas.throwFeatMissing("clusterLabel", "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    return ll_cas.ll_getStringValue(addr, casFeatCode_clusterLabel);
  }

  /** @generated */
  public void setClusterLabel(int addr, String v) {
    if (featOkTst && casFeat_clusterLabel == null) {
      jcas.throwFeatMissing("clusterLabel", "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    ll_cas.ll_setStringValue(addr, casFeatCode_clusterLabel, v);
  }

  /**** Feature: distance ***/
  /** @generated */
  final Feature casFeat_distance;
  /** @generated */
  final int casFeatCode_distance;

  /** @generated */
  public double getDistance(int addr) {
    if (featOkTst && casFeat_distance == null) {
      jcas.throwFeatMissing("distance", "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_distance);
  }

  /** @generated */
  public void setDistance(int addr, double v) {
    if (featOkTst && casFeat_distance == null) {
      jcas.throwFeatMissing("distance", "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    ll_cas.ll_setDoubleValue(addr, casFeatCode_distance, v);
  }

  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public ClusterDistancePair_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_clusterLabel =
        jcas.getRequiredFeatureDE(casType, "clusterLabel", "uima.cas.String", featOkTst);
    casFeatCode_clusterLabel = (null == casFeat_clusterLabel) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_clusterLabel).getCode();

    casFeat_distance =
        jcas.getRequiredFeatureDE(casType, "distance", "uima.cas.Double", featOkTst);
    casFeatCode_distance = (null == casFeat_distance) ? JCas.INVALID_FEATURE_CODE
        : ((FeatureImpl) casFeat_distance).getCode();
  }

}
