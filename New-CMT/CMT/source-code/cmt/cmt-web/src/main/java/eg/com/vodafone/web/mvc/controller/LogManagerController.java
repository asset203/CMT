package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.DCLogEntry;
import eg.com.vodafone.model.enums.CalendarMonthEnum;
import eg.com.vodafone.model.enums.LogType;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.DCLogManagerService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.component.grid.GridUserInterface;
import eg.com.vodafone.web.mvc.formbean.DCLogSearchForm;
import eg.com.vodafone.web.mvc.formbean.grid.GridBean;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import eg.com.vodafone.web.mvc.model.searchcriteria.SortField;
import eg.com.vodafone.web.mvc.model.searchresult.DCLogSearchResult;
import eg.com.vodafone.web.mvc.model.searchresult.SearchResult;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import eg.com.vodafone.web.mvc.util.GridConstants;
import eg.com.vodafone.web.mvc.util.GridUtils;
import eg.com.vodafone.web.mvc.validator.DCLogSearchValidator;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 3/11/13
 * Time: 3:44 PM
 */
@Controller
@RequestMapping("/logManager/*")
public class LogManagerController extends AbstractGridBasedController {

    private final static Logger logger = LoggerFactory.getLogger(LogManagerController.class);
    private final static String SYSTEM_LIST_KEY = "systemList";
    private final static String LOG_TYPES_MAP_KEY = "logTypes";
    private final static String SEARCH_BEAN_KEY = "dcLogSearchForm";
    private final static String VIEW_PAGE = "/logmanager/viewlogs";
    //Grid XML
    private static final String GRID_XML_CONFIG = "eg/com/vodafone/grids/dcLogEntryList-grid.xml";
    private final static SimpleDateFormat SIMPLE_DATE_FORMAT =
            new SimpleDateFormat(CMTConstants.DATE_DB_PATTERN, Locale.US);
    @Autowired
    private DCLogManagerService dcLogManagerService;
    @Autowired
    private DataCollectionSystemServiceInterface dataCollectionService;
    @Autowired
    private DCLogSearchValidator dcLogSearchValidator;

    /**
     * Entry point
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "viewLogs", method = RequestMethod.GET)
    public ModelAndView viewLogs() throws ParseException {

        logger.info("entered LogManager");
        ModelAndView modelAndView = new ModelAndView(VIEW_PAGE);
        List<String> systemList = getAllSystems();
        DCLogSearchForm dcLogSearchForm = new DCLogSearchForm();
        dcLogSearchForm.setLogType(LogType.ALL);
        modelAndView.addObject(SEARCH_BEAN_KEY, dcLogSearchForm);
        modelAndView.addObject(SYSTEM_LIST_KEY, systemList);
        modelAndView.addObject(LOG_TYPES_MAP_KEY, getHashedLogTypes());

        return modelAndView;
    }

    @RequestMapping(value = "searchLogs", method = RequestMethod.POST)
    public ModelAndView viewLogsByFilter(
            @Valid @ModelAttribute(value = "dcLogSearchForm") DCLogSearchForm dcLogSearchForm,
            BindingResult result) {
        logger.info("entered search submission validation");

        ModelAndView modelAndView = new ModelAndView(VIEW_PAGE);
        modelAndView.addObject(SYSTEM_LIST_KEY, dcLogManagerService.getAllSystems());
        modelAndView.addObject(LOG_TYPES_MAP_KEY, getHashedLogTypes());
        dcLogSearchValidator.validate(dcLogSearchForm, result);
        if (result.hasErrors()) {
            logger.debug("validation errors exist");
            modelAndView.addObject(SEARCH_BEAN_KEY, dcLogSearchForm);
            return modelAndView;
        }
        //prepare Grid
        prepareGridData(modelAndView, dcLogSearchForm);
        modelAndView.addObject(SEARCH_BEAN_KEY, dcLogSearchForm);

        return modelAndView;
    }

    /**
     * Get Hashed Log Types
     *
     * @return hashed log types
     */
    private TemplateHashModel getHashedLogTypes() {
        TemplateHashModel enumModels = BeansWrapper.getDefaultInstance().getEnumModels();
        TemplateHashModel myEnumModel = null;
        try {
            myEnumModel = (TemplateHashModel) enumModels.get("eg.com.vodafone.model.enums.LogType");
        } catch (TemplateModelException e) {
            logger.error("TemplateModelException thrown when trying to hash log types enum", e);
        }
        return myEnumModel;
    }

    /**
     * This method returns yesterday's date as String
     * with the pattern dd-MMM-yyyy
     *
     * @return yesterday's date String
     */
    private String getYesterdayDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String yesterdayDate;
        synchronized (SIMPLE_DATE_FORMAT) {
            yesterdayDate = SIMPLE_DATE_FORMAT.format(calendar.getTime());
        }
        logger.debug("Yesterday's date: {} ", yesterdayDate);

        return yesterdayDate;
    }

    private Date parseToDate(String date) {
        try {
            synchronized (SIMPLE_DATE_FORMAT) {
                return SIMPLE_DATE_FORMAT.parse(date);
            }
        } catch (ParseException ex) {
            logger.error("Parse exception", ex.getMessage());
        }
        return null;
    }

    /**
     * Get All system names
     *
     * @return all available system names
     */
    private List<String> getAllSystems() {
        List<String> systemNames
                = dataCollectionService.listAllSystems();
        if (systemNames == null || systemNames.isEmpty()) {
            throw new GenericException("System list size is Zero");
        }
        return systemNames;
    }

    @SuppressWarnings("unchecked")
    private void prepareGridData(ModelAndView modelAndView, DCLogSearchForm dcLogSearchForm) {
        int logCounts = this.getCount(dcLogSearchForm);
        if (logCounts > 0) {
            GridBean gridBean = GridUtils.createGridBean(logCounts);
            gridBean.setSearchCriteria(dcLogSearchForm);
            GridUserInterface viewTaskGrid = GridUtils.createGridUserInterface(GRID_XML_CONFIG);
            gridBean.setGridUserInterface(viewTaskGrid);
            GridUtils.populateSortColumns(gridBean);

            List<DCLogSearchResult> logSearchResultListList =
                    (List<DCLogSearchResult>) this.getResultsList(dcLogSearchForm, 1,
                            GridConstants.GRID_DEFAULT_NO_ROWS_PER_PAGE);
            modelAndView.addObject(GRID_RESULTS_MODEL_NAME, logSearchResultListList);
            modelAndView.addObject(GRID_MODEL_NAME, gridBean);

        }
    }

    @Override
    public int getCount(Object... args) {
        DCLogSearchForm searchCriteria = (DCLogSearchForm) args[0];
        return dcLogManagerService.getFilteredDCLogsCount(
                searchCriteria.getSystemName(), searchCriteria.getLogType().getValue(),
                parseToDate(searchCriteria.getFromDate()),
                parseToDate(searchCriteria.getToDate()));
    }

    @Override
    public List<? extends SearchResult> getResultsList(
            SearchCriteria searchCriteria, int startRowIndex, int endRowIndex) {

        SortField sortField = null;
        if ((searchCriteria).getSortFields() != null
                && !(searchCriteria).getSortFields().isEmpty()) {
            sortField = (searchCriteria).getSortFields().get(0);
        }
        List<DCLogEntry> dcLogEntries
                = dcLogManagerService.getFilteredDCLogsByPageIndex(
                ((DCLogSearchForm) searchCriteria).getSystemName(),
                ((DCLogSearchForm) searchCriteria).getLogType().getValue(),
                parseToDate(((DCLogSearchForm) searchCriteria).getFromDate()),
                parseToDate(((DCLogSearchForm) searchCriteria).getToDate()),
                ((sortField != null) ? sortField.getExpression() : ""),
                ((sortField != null) ? sortField.getOrder() : "ASC"),
                startRowIndex, endRowIndex);

        return mapSearchResultToGrid(dcLogEntries);
    }

    private List<DCLogSearchResult> mapSearchResultToGrid(List<DCLogEntry> searchResult) {
        if (searchResult != null && !searchResult.isEmpty()) {
            List<DCLogSearchResult> dcLogSearchResults = new ArrayList<DCLogSearchResult>();
            DCLogSearchResult dcLogSearchResult = new DCLogSearchResult();

            for (DCLogEntry entry : searchResult) {
                dcLogSearchResult.setLogDate(entry.getLogDate());
                dcLogSearchResult.setSystemName(entry.getSystemName());
                dcLogSearchResult.setNodeName(entry.getNodeName());
                dcLogSearchResult.setLogType(entry.getLogType());
                dcLogSearchResult.setRetrialCount(entry.getRetrialCount());
                dcLogSearchResult.setErrorCode(entry.getErrorCode());
                dcLogSearchResult.setLogEntryDescription(entry.getLogEntryDescription());
                dcLogSearchResults.add(dcLogSearchResult);
                dcLogSearchResult = new DCLogSearchResult();
            }
            return dcLogSearchResults;

        }
        return null;
    }
}
