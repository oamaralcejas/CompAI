package lu.svv.saa.linklaters.privacypolicies.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

public class ClusteringResult extends Annotation {
  /**
   * @generated
   * @ordered
   */
  public final static int typeIndexID = JCasRegistry.register(ClusteringResult.class);
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
  public ClusteringResult() {}

  /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
  public ClusteringResult(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }

  /** @generated */
  public ClusteringResult(JCas jCas) {
    super(jCas);
    readObject();
  }

  /** @generated */
  public ClusteringResult(JCas jcas, int begin, int end) {
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
  //* Feature: distancesToClusters

  /** getter for distancesToClusters - gets 
   * @generated
   * @return value of the feature 
   */
  public FSArray getDistancesToClusters() {
    if (ClusteringResult_Type.featOkTst && ((ClusteringResult_Type)jcasType).casFeat_distancesToClusters == null)
      jcasType.jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((ClusteringResult_Type)jcasType).casFeatCode_distancesToClusters)));}
    
  /** setter for distancesToClusters - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDistancesToClusters(FSArray v) {
    if (ClusteringResult_Type.featOkTst && ((ClusteringResult_Type)jcasType).casFeat_distancesToClusters == null)
      jcasType.jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    jcasType.ll_cas.ll_setRefValue(addr, ((ClusteringResult_Type)jcasType).casFeatCode_distancesToClusters, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for distancesToClusters - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public ClusterDistancePair getDistancesToClusters(int i) {
    if (ClusteringResult_Type.featOkTst && ((ClusteringResult_Type)jcasType).casFeat_distancesToClusters == null)
      jcasType.jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((ClusteringResult_Type)jcasType).casFeatCode_distancesToClusters), i);
    return (ClusterDistancePair)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((ClusteringResult_Type)jcasType).casFeatCode_distancesToClusters), i)));}

  /** indexed setter for distancesToClusters - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setDistancesToClusters(int i, ClusterDistancePair v) { 
    if (ClusteringResult_Type.featOkTst && ((ClusteringResult_Type)jcasType).casFeat_distancesToClusters == null)
      jcasType.jcas.throwFeatMissing("distancesToClusters", "lu.svv.saa.linklaters.privacypolicies.type.ClusteringResult");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((ClusteringResult_Type)jcasType).casFeatCode_distancesToClusters), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((ClusteringResult_Type)jcasType).casFeatCode_distancesToClusters), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
}
