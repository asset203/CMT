package eg.com.vodafone.web.mvc.model.searchcriteria;

/**
 * @author rania.helal
 * @since Jun 29, 2011
 */
public class SortField {

    private String expression;
    private String order;

    public SortField() {
    }

    public SortField(String expression, String order) {
        this.expression = expression;
        this.order = order;
    }

    public String getExpression() {
        return expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
}