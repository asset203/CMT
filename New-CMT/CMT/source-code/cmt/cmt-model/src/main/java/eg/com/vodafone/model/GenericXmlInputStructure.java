package eg.com.vodafone.model;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/14/13
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class GenericXmlInputStructure extends VInputStructure {

    private boolean simple;
    private int converterId;

    public GenericXmlInputStructure(){

    }
    public GenericXmlInputStructure(VInputStructure inputStructure) {
        super(inputStructure);
    }
    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public int getConverterId() {
        return converterId;
    }

    public void setConverterId(int converterId) {
        this.converterId = converterId;
    }
}
