package eg.com.vodafone.web.mvc.formbean.grid;


import eg.com.vodafone.web.mvc.util.GridConstants;

public class PaginationBean {
	
	
	// rows -- start and end rows indexes will be used to be sent for iBatis(pagination)
	private int currentPageIndex; 
	private int startRowIndex;
	private int endRowIndex;
	private int totalNoRows;
	
	
	// pages
	private int startPageIndex;
	private int endPageIndex;
	private int totalNoPages; // total number of pages
	
	// defualt numbers
	private int rowsPerPage ;
	
	public PaginationBean (){
		
	}
	
	public PaginationBean(int totalNoRows){
		this.currentPageIndex = 1;
		this.startRowIndex = 1;
		this.startPageIndex = 1;
		this.rowsPerPage =  GridConstants.GRID_DEFAULT_NO_ROWS_PER_PAGE;
		
		this.totalNoRows = totalNoRows;
		this.endRowIndex = (totalNoRows > this.rowsPerPage ? this.rowsPerPage :this.totalNoRows);
		
		this.totalNoPages = (totalNoRows / this.rowsPerPage) + (totalNoRows % this.rowsPerPage >0 ? 1:0);
		
		this.endPageIndex = (this.totalNoPages >GridConstants.GRID_MAX_NO_LINKS_GROUP ?GridConstants.GRID_MAX_NO_LINKS_GROUP : this.totalNoPages);
		
		
	}
	
	
	

	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public int getTotalNoRows() {
		return totalNoRows;
	}

	public void setTotalNoRows(int totalNoRows) {
		this.totalNoRows = totalNoRows;
	}

	public int getTotalNoPages() {
		return totalNoPages;
	}

	public void setTotalNoPages(int totalNoPages) {
		this.totalNoPages = totalNoPages;
	}

	public int getStartRowIndex() {
		return startRowIndex;
	}

	public void setStartRowIndex(int startRowIndex) {
		this.startRowIndex = startRowIndex;
	}

	public int getEndRowIndex() {
		return endRowIndex;
	}

	public void setEndRowIndex(int endRowIndex) {
		this.endRowIndex = endRowIndex;
	}
	
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public int getStartPageIndex() {
		return startPageIndex;
	}

	public void setStartPageIndex(int startPageIndex) {
		this.startPageIndex = startPageIndex;
	}

	public int getEndPageIndex() {
		return endPageIndex;
	}

	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}
	
	
	
}
