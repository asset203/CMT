package eg.com.vodafone.model.enums;

/**
 * @author marwa.goda
 * @since 5/1/13
 */
public enum KpiNodeGrain {
  Hourly("H"), Daily("D");
  String displayName;
  
  private KpiNodeGrain(String displayName){
     this.displayName = displayName;

  }

  @Override
  public String toString(){
     return this.displayName;
  }
}
