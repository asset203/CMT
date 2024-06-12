package eg.com.vodafone.web.mvc.formbean.grid;

import eg.com.vodafone.web.mvc.component.grid.GridUserInterface;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;

import java.util.HashSet;
import java.util.Set;


public class GridBean {

	private PaginationBean paginationBean;
	private SearchCriteria searchCriteria;
	private GridUserInterface gridUserInterface;

	// manage checkbox selections
	private Set<String> selectedKeys = new HashSet<String>();
	
	

	public GridBean( int totalNoRows) {
		this.paginationBean = new PaginationBean(totalNoRows);
	}

	public PaginationBean getPaginationBean() {
		return paginationBean;
	}

	public void setPaginationBean(PaginationBean paginationBean) {
		this.paginationBean = paginationBean;
	}

	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public GridUserInterface getGridUserInterface() {
		return gridUserInterface;
	}

	public void setGridUserInterface(GridUserInterface gridUserInterface) {
		this.gridUserInterface = gridUserInterface;
	}

	public Set<String> getSelectedKeys() {
		return selectedKeys;
	}

	public void setSelectedKeys(Set<String> selectedKeys) {
		this.selectedKeys = selectedKeys;
	}
	
}
