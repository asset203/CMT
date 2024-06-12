package eg.com.vodafone.web.mvc.component.grid;

import java.util.List;

/**
 * @author amany.reda
 */
public class UIColumn {


    // UI
    private String id;
    private String label;
    private String width;
    private String linkStyle;
    // sort properties
    private boolean sortable;
    private String sortOrder;
    private String dbColumn;
    // label column
    private String modelProperty;
    private String expression;
    // link column
    private String linkLabel;
    private String dynamicLinkLabel;
    private String href;
    private String dynamicHref;
    private String onClick;
    private String dynamicOnClick;
    private String columnType;
    private boolean defaultSorted;
    private String checkBoxRowId;
    private String style;
    private String dataAvailable;
    private List<UILink> linkList;
    //Break line after number of characters
    private String splitAfter;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getModelProperty() {
        return modelProperty;
    }

    public void setModelProperty(String modelProperty) {
        this.modelProperty = modelProperty;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public void setDbColumn(String dbColumn) {
        this.dbColumn = dbColumn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getLinkLabel() {
        return linkLabel;
    }

    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getDynamicLinkLabel() {
        return dynamicLinkLabel;
    }

    public void setDynamicLinkLabel(String dynamicLinkLabel) {
        this.dynamicLinkLabel = dynamicLinkLabel;
    }

    public String getDynamicHref() {
        return dynamicHref;
    }

    public void setDynamicHref(String dynamicHref) {
        this.dynamicHref = dynamicHref;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public boolean isDefaultSorted() {
        return defaultSorted;
    }

    public void setDefaultSorted(boolean defaultSorted) {
        this.defaultSorted = defaultSorted;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getCheckBoxRowId() {
        return checkBoxRowId;
    }

    public void setCheckBoxRowId(String checkBoxRowId) {
        this.checkBoxRowId = checkBoxRowId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getLinkStyle() {
        return linkStyle;
    }

    public void setLinkStyle(String style) {
        this.linkStyle = style;
    }

    public String getDynamicOnClick() {
        return dynamicOnClick;
    }

    public void setDynamicOnClick(String dynamicOnClick) {
        this.dynamicOnClick = dynamicOnClick;
    }

    public String getDataAvailable() {
        return dataAvailable;
    }

    public void setDataAvailable(String dataAvailable) {
        this.dataAvailable = dataAvailable;
    }

    public List<UILink> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<UILink> linkList) {
        this.linkList = linkList;
    }

    public String getSplitAfter() {
        return splitAfter;
    }

    public void setSplitAfter(String splitAfter) {
        this.splitAfter = splitAfter;
    }
}
