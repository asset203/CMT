package eg.com.vodafone.model.enums;

/**
 * @author marwa.goda
 * @since 5/2/13
 */
public enum NodeInUse {
  YES("Y"), NO("N");
   String value;
  NodeInUse(String value){
     this.value = value;
  }
  
  public String toString(){
    return this.value;
  }
}
