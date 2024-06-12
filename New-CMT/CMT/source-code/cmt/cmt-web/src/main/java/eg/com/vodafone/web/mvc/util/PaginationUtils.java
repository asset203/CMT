package eg.com.vodafone.web.mvc.util;

import eg.com.vodafone.web.mvc.formbean.grid.PaginationBean;

public class PaginationUtils {

	public static PaginationBean goOneStepForward(PaginationBean paginationBean) {

		// calculate the pageIndex
		int currentPageIndex;
		if (paginationBean.getCurrentPageIndex() != paginationBean
				.getTotalNoPages()) {
			currentPageIndex = paginationBean.getCurrentPageIndex() + 1;
		} else {
			currentPageIndex = paginationBean.getTotalNoPages();
		}

		// calculate the start and end page index
		int startPageIndex;
		int endPageIndex;
		if (currentPageIndex > paginationBean.getEndPageIndex()) {
			startPageIndex = paginationBean.getStartPageIndex()
					+ GridConstants.GRID_MAX_NO_LINKS_GROUP;

			int expectedEndPageIndex = paginationBean.getEndPageIndex()
					+ GridConstants.GRID_MAX_NO_LINKS_GROUP;

			endPageIndex = (expectedEndPageIndex > paginationBean
					.getTotalNoPages()) ? paginationBean.getTotalNoPages()
					: expectedEndPageIndex;

		} else {
			startPageIndex = paginationBean.getStartPageIndex();
			endPageIndex = paginationBean.getEndPageIndex();
		}

		paginationBean.setCurrentPageIndex(currentPageIndex);
		paginationBean.setStartPageIndex(startPageIndex);
		paginationBean.setEndPageIndex(endPageIndex);

		return paginationBean;
	}

	public static PaginationBean goOneStepBackward(PaginationBean paginationBean) {
		// calculate the pageIndex
		int currentPageIndex;
		if (paginationBean.getCurrentPageIndex() > 1) {
			currentPageIndex = paginationBean.getCurrentPageIndex() - 1;
		} else {
			currentPageIndex = 1;
		}

		// calculate the start and end page index
		int startPageIndex;
		int endPageIndex;

		if (currentPageIndex < paginationBean.getStartPageIndex()) {
			startPageIndex = paginationBean.getStartPageIndex()
					- GridConstants.GRID_MAX_NO_LINKS_GROUP;

			int decreasePortion = paginationBean.getEndPageIndex()
					% GridConstants.GRID_MAX_NO_LINKS_GROUP;
			endPageIndex = paginationBean.getEndPageIndex()
					- ((decreasePortion > 0 ? decreasePortion : GridConstants.GRID_MAX_NO_LINKS_GROUP));
		} else {
			startPageIndex = paginationBean.getStartPageIndex();
			endPageIndex = paginationBean.getEndPageIndex();
		}

		paginationBean.setCurrentPageIndex(currentPageIndex);
		paginationBean.setStartPageIndex(startPageIndex);
		paginationBean.setEndPageIndex(endPageIndex);

		return paginationBean;
	}

	public static PaginationBean goFirst(PaginationBean paginationBean) {
		int currentPageIndex = 1;

		// calculate the start and end page index
		int startPageIndex = 1;
		int endPageIndex;

		if (paginationBean.getTotalNoPages() > GridConstants.GRID_MAX_NO_LINKS_GROUP) {
			endPageIndex = GridConstants.GRID_MAX_NO_LINKS_GROUP;
		} else {
			endPageIndex = paginationBean.getTotalNoPages();
		}

		paginationBean.setCurrentPageIndex(currentPageIndex);
		paginationBean.setStartPageIndex(startPageIndex);
		paginationBean.setEndPageIndex(endPageIndex);

		return paginationBean;
	}

	public static PaginationBean goLast(PaginationBean paginationBean) {
		int currentPageIndex = paginationBean.getTotalNoPages();

		// calculate the start and end page index
		int startPageIndex;
		if (paginationBean.getTotalNoPages() > GridConstants.GRID_MAX_NO_LINKS_GROUP) {
			int remOfPages = paginationBean.getTotalNoPages()
					% GridConstants.GRID_MAX_NO_LINKS_GROUP;

			startPageIndex = paginationBean.getTotalNoPages()
					- (remOfPages > 0 ? (remOfPages - 1) : GridConstants.GRID_MAX_NO_LINKS_GROUP);
		} else {
			startPageIndex = 1;
		}

		int endPageIndex = paginationBean.getTotalNoPages();

		paginationBean.setStartPageIndex(startPageIndex);
		paginationBean.setEndPageIndex(endPageIndex);
		paginationBean.setCurrentPageIndex(currentPageIndex);

		return paginationBean;
	}

	public static PaginationBean changeRowsPerPage(
			PaginationBean paginationBean, int rowsPerPage) {

		// reset Pagination
		int currentPageIndex = 1;

		// calculate the start and end page index
		int totalNoPages;
		if (paginationBean.getTotalNoRows() > rowsPerPage) {
			totalNoPages = (paginationBean.getTotalNoRows() / rowsPerPage)
					+ (paginationBean.getTotalNoRows() % rowsPerPage > 0 ? 1
							: 0);
		} else {
			totalNoPages = 1;
		}

		int startPageIndex = 1;
		int endPageIndex;
		if (totalNoPages > GridConstants.GRID_MAX_NO_LINKS_GROUP) {
			endPageIndex = GridConstants.GRID_MAX_NO_LINKS_GROUP;
		} else {
			endPageIndex = totalNoPages;
		}

		paginationBean.setRowsPerPage(rowsPerPage);
		paginationBean.setTotalNoPages(totalNoPages);
		paginationBean.setStartPageIndex(startPageIndex);
		paginationBean.setEndPageIndex(endPageIndex);
		paginationBean.setCurrentPageIndex(currentPageIndex);

		return paginationBean;
	}

	public static PaginationBean goToPage(PaginationBean paginationBean,
			int pageIndex) {
		// calculate the start and end row index
		int rowsPerPage = paginationBean.getRowsPerPage();
		int startRowIndex = (pageIndex * rowsPerPage) - (rowsPerPage - 1);
		int endRowIndex = (pageIndex * rowsPerPage) > paginationBean
				.getTotalNoRows() ? paginationBean.getTotalNoRows()
				: (pageIndex * rowsPerPage);

		paginationBean.setStartRowIndex(startRowIndex);
		paginationBean.setEndRowIndex(endRowIndex);
		paginationBean.setCurrentPageIndex(pageIndex);

		return paginationBean;
	}

}
