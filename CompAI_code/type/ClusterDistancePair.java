package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class ClusterDistancePair extends Annotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(ClusterDistancePair.class);
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
  public ClusterDistancePair() {}

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public ClusterDistancePair(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public ClusterDistancePair(JCas jCas) {
    super(jCas);
    readObject();
  }

  /** @generated */
  public ClusterDistancePair(JCas jcas, int begin, int end) {
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
  // * Feature: clusterLabel

  /**
   * getter for clusterLabel
   * 
   * @generated
   */
  public String getClusterLabel() {
    if (ClusterDistancePair_Type.featOkTst && ((ClusterDistancePair_Type) jcasType).casFeat_clusterLabel == null) {
      jcasType.jcas.throwFeatMissing("clusterLabel",
          "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    return jcasType.ll_cas.ll_getStringValue(addr, ((ClusterDistancePair_Type) jcasType).casFeatCode_clusterLabel);
  }

  /**
   * setter for clusterLabel
   * 
   * @generated
   */
  public void setClusterLabel(String v) {
    if (ClusterDistancePair_Type.featOkTst && ((ClusterDistancePair_Type) jcasType).casFeat_clusterLabel == null) {
      jcasType.jcas.throwFeatMissing("clusterLabel",
          "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    jcasType.ll_cas.ll_setStringValue(addr, ((ClusterDistancePair_Type) jcasType).casFeatCode_clusterLabel, v);
  }


  // *--------------*
  // * Feature: distance

  /**
   * getter for distance
   * 
   * @generated
   */
  public double getDistance() {
    if (ClusterDistancePair_Type.featOkTst && ((ClusterDistancePair_Type) jcasType).casFeat_distance == null) {
      jcasType.jcas.throwFeatMissing("distance",
          "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((ClusterDistancePair_Type) jcasType).casFeatCode_distance);
  }

  /**
   * setter for distance
   * 
   * @generated
   */
  public void setDistance(double v) {
    if (ClusterDistancePair_Type.featOkTst && ((ClusterDistancePair_Type) jcasType).casFeat_distance == null) {
      jcasType.jcas.throwFeatMissing("distance",
          "lu.svv.saa.linklaters.privacypolicies.type.ClusterDistancePair");
    }
    jcasType.ll_cas.ll_setDoubleValue(addr, ((ClusterDistancePair_Type) jcasType).casFeatCode_distance, v);
  }

}
