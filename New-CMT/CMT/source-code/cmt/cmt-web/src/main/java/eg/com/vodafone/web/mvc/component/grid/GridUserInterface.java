package eg.com.vodafone.web.mvc.component.grid;

import java.util.List;

public class GridUserInterface {
	
	private List <UIColumn> columns;
	
	private String controllerUrlMapping;
	private String width;
	private String id;
	private String var;
	private String scrollable;
	
	public GridUserInterface(){
		
	}
	
	public GridUserInterface(List <UIColumn> columns ){
		this.columns = columns;
	}
	
	public List<UIColumn> getColumns() {
		return columns;
	}
	public void setColumns(List <UIColumn> columns) {
		this.columns = columns;
	}
	

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getControllerUrlMapping() {
		return controllerUrlMapping;
	}

	public void setControllerUrlMapping(String controllerUrlMapping) {
		this.controllerUrlMapping = controllerUrlMapping;
	}

	public void setScrollable(String scrollable) {
		this.scrollable = scrollable;
	}

	public String getScrollable() {
		return scrollable;
	}

}
