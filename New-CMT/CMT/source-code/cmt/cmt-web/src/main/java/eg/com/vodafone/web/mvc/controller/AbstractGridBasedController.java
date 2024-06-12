package eg.com.vodafone.web.mvc.controller;

import java.util.List;

import eg.com.vodafone.web.mvc.formbean.grid.GridBean;
import eg.com.vodafone.web.mvc.formbean.grid.PaginationBean;
import eg.com.vodafone.web.mvc.model.searchresult.SearchResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



import eg.com.vodafone.web.mvc.util.GridConstants;
import eg.com.vodafone.web.mvc.util.GridUtils;
import eg.com.vodafone.web.mvc.util.PaginationUtils;

public abstract class AbstractGridBasedController extends AbstractController implements
		GridBasedController {

	
	protected static final String GRID_RESULTS_MODEL_NAME = "value";
	protected static final String GRID_MODEL_NAME = "gridBean";
	protected static final String GRID_VIEW_NAME = "datatable";
    protected static final String SEARCH_CRITERIA ="searchCriteria";
	

	protected final List<? extends SearchResult> paginate(int pageIndex,
			GridBean gridBean) {

		PaginationBean paginationBean = PaginationUtils.goToPage(gridBean
				.getPaginationBean(), pageIndex);
		gridBean.setPaginationBean(paginationBean);

		return this.getResultsList(gridBean
				.getSearchCriteria(), gridBean.getPaginationBean()
				.getStartRowIndex(), gridBean.getPaginationBean()
				.getEndRowIndex());
	}


	@RequestMapping(value = "/refresh.htm", method = RequestMethod.POST)
	public final ModelAndView refresh(@RequestParam("pageIndex") int pageIndex,@RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys,
			GridBean gridBean) {

		GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);
		
		List<? extends SearchResult> results = this.paginate(pageIndex,
				gridBean);
		ModelAndView modelAndView = new ModelAndView(GRID_VIEW_NAME);
		modelAndView.addObject(GRID_RESULTS_MODEL_NAME, results);
		modelAndView.addObject(GRID_MODEL_NAME, gridBean);
		return modelAndView;
	}

	@RequestMapping(value = "/first.htm", method = RequestMethod.POST)
	public final ModelAndView first(@RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys,GridBean gridBean) {
		
		GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);
		
		PaginationBean paginationBean = PaginationUtils.goFirst(gridBean
				.getPaginationBean());
		gridBean.setPaginationBean(paginationBean);

		List<? extends SearchResult> results = this.paginate(gridBean
				.getPaginationBean().getCurrentPageIndex(), gridBean);
		ModelAndView modelAndView = new ModelAndView(GRID_VIEW_NAME);
		modelAndView.addObject(GRID_RESULTS_MODEL_NAME, results);
		modelAndView.addObject(GRID_MODEL_NAME, gridBean);

		return modelAndView;

	}

	@RequestMapping(value = "/last.htm", method = RequestMethod.POST)
	public final ModelAndView last(@RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys,GridBean gridBean) {

		GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);
		
		PaginationBean paginationBean = PaginationUtils.goLast(gridBean
				.getPaginationBean());
		gridBean.setPaginationBean(paginationBean);

		List<? extends SearchResult> results = this.paginate(gridBean
				.getPaginationBean().getCurrentPageIndex(), gridBean);
		ModelAndView modelAndView = new ModelAndView(GRID_VIEW_NAME);
		modelAndView.addObject(GRID_RESULTS_MODEL_NAME, results);
		modelAndView.addObject(GRID_MODEL_NAME, gridBean);
		return modelAndView;
	}

	@RequestMapping(value = "/previous.htm", method = RequestMethod.POST)
	public final ModelAndView previous(@RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys,GridBean gridBean) {

		GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);
		
		PaginationBean paginationBean = PaginationUtils
				.goOneStepBackward(gridBean.getPaginationBean());
		gridBean.setPaginationBean(paginationBean);

		List<? extends SearchResult> results = this.paginate(gridBean
				.getPaginationBean().getCurrentPageIndex(), gridBean);
		ModelAndView modelAndView = new ModelAndView(GRID_VIEW_NAME);
		modelAndView.addObject(GRID_RESULTS_MODEL_NAME, results);
		modelAndView.addObject(GRID_MODEL_NAME, gridBean);
		return modelAndView;
	}

	@RequestMapping(value = "/next.htm", method = RequestMethod.POST)
	public final ModelAndView next(@RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys,GridBean gridBean) {

		GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);
		
		PaginationBean paginationBean = PaginationUtils
				.goOneStepForward(gridBean.getPaginationBean());
		gridBean.setPaginationBean(paginationBean);

		List<? extends SearchResult> results = this.paginate(gridBean
				.getPaginationBean().getCurrentPageIndex(), gridBean);
		ModelAndView modelAndView = new ModelAndView(GRID_VIEW_NAME);
		modelAndView.addObject(GRID_RESULTS_MODEL_NAME, results);
		modelAndView.addObject(GRID_MODEL_NAME, gridBean);
		return modelAndView;
	}

	@RequestMapping(value = "/sort.htm", method = RequestMethod.POST)
	public final ModelAndView sort(@RequestParam("name") String sortField,
			@RequestParam("order") boolean order,@RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys, GridBean gridBean) {
		
		GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);

		String sortOrder = order ? GridConstants.SORT_ORDER_ASC
				: GridConstants.SORT_ORDER_DESC;
		gridBean = GridUtils.sort(gridBean, sortField, sortOrder);

		List<? extends SearchResult> results = this.paginate(gridBean
				.getPaginationBean().getCurrentPageIndex(), gridBean);
		ModelAndView modelAndView = new ModelAndView(GRID_VIEW_NAME);
		modelAndView.addObject(GRID_RESULTS_MODEL_NAME, results);
		modelAndView.addObject(GRID_MODEL_NAME, gridBean);
		return modelAndView;
	}

	@RequestMapping(value = "/update.htm", method = RequestMethod.POST)
	public final ModelAndView updateRowsPerPage(
			@RequestParam("rowsPerPage") int rowsPerPage, @RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys,GridBean gridBean) {
		
		gridBean = GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);
		PaginationBean paginationBean = PaginationUtils.changeRowsPerPage(
				gridBean.getPaginationBean(), rowsPerPage);
		gridBean.setPaginationBean(paginationBean);

		List<? extends SearchResult> results = this.paginate(gridBean
				.getPaginationBean().getCurrentPageIndex(), gridBean);
		ModelAndView modelAndView = new ModelAndView(GRID_VIEW_NAME);
		modelAndView.addObject(GRID_RESULTS_MODEL_NAME, results);
		modelAndView.addObject(GRID_MODEL_NAME, gridBean);
		return modelAndView;
	}
	
	@RequestMapping(value="/updateSelection.htm", method=RequestMethod.POST)
	public @ResponseBody void updateSelection(@RequestParam("newSelections") String newSelectedKeys , @RequestParam("cancelledSelections") String unSelectedKeys , GridBean gridBean) {
		
		GridUtils.updateSelections(gridBean, newSelectedKeys, unSelectedKeys);
	}
	

}
