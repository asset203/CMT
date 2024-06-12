package eg.com.vodafone.web.mvc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import eg.com.vodafone.web.mvc.component.grid.GridUserInterface;
import eg.com.vodafone.web.mvc.component.grid.UIColumn;
import eg.com.vodafone.web.mvc.controller.AbstractGridBasedController;
import eg.com.vodafone.web.mvc.formbean.grid.GridBean;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import eg.com.vodafone.web.mvc.model.searchcriteria.SortField;
import org.apache.log4j.Logger;
import org.milyn.Smooks;
import org.milyn.payload.JavaResult;
import org.xml.sax.SAXException;


public class GridUtils {

	private static final String SMOOKS_CONFIG = "eg/com/vodafone/grids/grid-user-interface-smooks.xml";
	private static final Logger logger = Logger
			.getLogger(GridUtils.class);

	public static List<UIColumn> populateUiColumnsAfterSort(
			List<UIColumn> columns, String dbColumnName, String type) {
		List<UIColumn> newColumns = new ArrayList<UIColumn>();
		for (UIColumn column : columns) {
			if (dbColumnName.equals(column.getDbColumn())) {
				column
						.setSortOrder(GridConstants.SORT_ORDER_ASC.equals(type) ? GridConstants.SORT_ORDER_ASC
								: GridConstants.SORT_ORDER_DESC);
			} else {
				column.setSortOrder(GridConstants.SORT_ORDER_NONE);
			}
			newColumns.add(column);
		}
		return newColumns;
	}

	public static GridBean sort(GridBean gridBean, String name, String sortOrder) {

		// add new sort field to the search criteria
		SortField sortField = new SortField();
		sortField.setExpression(name);
		sortField.setOrder(sortOrder);

		if (gridBean.getSearchCriteria() == null) {
			gridBean.setSearchCriteria(new SearchCriteria());

		} else {
			 gridBean.getSearchCriteria().getSortFields()
					.clear();
		}
		 gridBean.getSearchCriteria().getSortFields().add(
				sortField);

		List<UIColumn> oldUiColumns = gridBean.getGridUserInterface()
				.getColumns();
		List<UIColumn> newColumns = GridUtils.populateUiColumnsAfterSort(
				oldUiColumns, name, sortOrder);
		gridBean.getGridUserInterface().setColumns(newColumns);

		// reset pagination to page 1
		gridBean.getPaginationBean().setCurrentPageIndex(1);
		gridBean.getPaginationBean().setStartPageIndex(1);
		int gridTotalNoPages = gridBean.getPaginationBean().getTotalNoPages();
        gridBean.getPaginationBean().setEndPageIndex(gridTotalNoPages<GridConstants.GRID_MAX_NO_LINKS_GROUP?gridTotalNoPages:GridConstants.GRID_MAX_NO_LINKS_GROUP);

		return gridBean;
	}

	public static List<SortField> getDefaultSortFields(List<UIColumn> columns) {
		List<SortField> sortFields = new ArrayList<SortField>();

		for (UIColumn column : columns) {
			if (column.isDefaultSorted()) {
				SortField sortField = new SortField();
				sortField.setExpression(column.getModelProperty());
				sortField.setOrder(column.getSortOrder());
				sortFields.add(sortField);
			}
		}
		return sortFields;
	}

	public static GridBean createGridBean(int totalNoRows) {
		return new GridBean(totalNoRows);
	}

	public static GridUserInterface createGridUserInterface(String gridXmlConfig) {

		GridUserInterface gridUserInterface = null;
		InputStream smooksConfig = GridUtils.class
				.getClassLoader().getResourceAsStream(SMOOKS_CONFIG);
		InputStream gridConfig = AbstractGridBasedController.class
				.getClassLoader().getResourceAsStream(gridXmlConfig);
		Smooks smooks = null;
		try {
			smooks = new Smooks(smooksConfig);
			JavaResult javaResult = new JavaResult();
			smooks.filterSource(new StreamSource(gridConfig), javaResult);
			gridUserInterface = (GridUserInterface) javaResult
					.getBean("gridUserInterface");
		} catch (IOException e) {
			logger.error(e);
		} catch (SAXException e) {
			logger.error(e);
		} finally {
			if (smooks != null)
				smooks.close();
		}

		return gridUserInterface;
	}
	
	public static GridBean updateSelections (GridBean gridBean , String newSelectedKeys ,String unSelectedKeys){
		if (newSelectedKeys !=null && !"".equals(newSelectedKeys)){
			String [] keys = newSelectedKeys.split(",");
			
			for (String key : keys){
				gridBean.getSelectedKeys().add(key);
			}	
		}
		
		if (unSelectedKeys !=null && !"".equals(unSelectedKeys)){
			String [] keys = unSelectedKeys.split(",");
			
			for (String key : keys){
				gridBean.getSelectedKeys().remove(key);
			}
		}
		return gridBean;
	}

	public static void populateSortColumns(GridBean gridBean) {
		for (SortField sortField : gridBean.getSearchCriteria().getSortFields()) {
			for (UIColumn column : gridBean.getGridUserInterface().getColumns()) {
				if (sortField.getExpression().equals(column.getDbColumn())) {
					column
							.setSortOrder(GridConstants.SORT_ORDER_ASC.equals(sortField.getOrder()) ? GridConstants.SORT_ORDER_ASC
									: GridConstants.SORT_ORDER_DESC);
				} else {
					column.setSortOrder(GridConstants.SORT_ORDER_NONE);
				}
			}
		}
	}

}
