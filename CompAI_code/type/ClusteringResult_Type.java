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


public class ClusteringResult_Type extends Annotation_Type {
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
      if (ClusteringResult_Type.this.useExistingInstance) {
        // Return eq fs instance if already created
        FeatureStructure fs = ClusteringResult_Type.this.jcas.getJfsFromCaddr(addr);
        if (null == fs) {
          fs = new ClusteringResult(addr, ClusteringResult_Type.this);
          ClusteringResult_Type.this.jcas.putJfsFromCaddr(addr, fs);
          return fs;
        }
        return fs;
      } else {
        return new ClusteringResult(addr, ClusteringResult_Type.this);
      }
    }
  };
  /** @generated */
  public final static int typeIndexID = ClusteringResult.typeIndexID;
  /**
   * @generated
   * @modifiable
   */
  public final static boolean featOkTst =
      JCasRegistry.getFeatOkTst("lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");

  /**** Feature: distancesToClusters ***/
  /** @generated */
  final Feature casFeat_distancesToClusters;
  /** @generated */
  final int     casFeatCode_distancesToClusters;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getDistancesToClusters(int addr) {
        if (featOkTst && casFeat_distancesToClusters == null)
      jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    return ll_cas.ll_getRefValue(addr, casFeatCode_distancesToClusters);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setDistancesToClusters(int addr, int v) {
        if (featOkTst && casFeat_distancesToClusters == null)
      jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    ll_cas.ll_setRefValue(addr, casFeatCode_distancesToClusters, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public int getDistancesToClusters(int addr, int i) {
        if (featOkTst && casFeat_distancesToClusters == null)
      jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_distancesToClusters), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_distancesToClusters), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_distancesToClusters), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setDistancesToClusters(int addr, int i, int v) {
        if (featOkTst && casFeat_distancesToClusters == null)
      jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_distancesToClusters), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_distancesToClusters), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_distancesToClusters), i, v);
  }


  /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
  public ClusteringResult_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    casFeat_distancesToClusters = jcas.getRequiredFeatureDE(casType, "distancesToClusters", "uima.cas.FSArray", featOkTst);
    casFeatCode_distancesToClusters  = (null == casFeat_distancesToClusters) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_distancesToClusters).getCode();
   
  }

}
