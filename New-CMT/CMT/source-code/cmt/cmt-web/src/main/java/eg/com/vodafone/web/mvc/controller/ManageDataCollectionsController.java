package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.DataColumn;
import eg.com.vodafone.model.KPIType;
import eg.com.vodafone.model.VInputStructure;
import eg.com.vodafone.model.XmlConverter;
import eg.com.vodafone.model.XmlVendor;
import eg.com.vodafone.model.enums.DataColumnType;
import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.service.DataCollectionServiceInterface;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.component.grid.GridUserInterface;
import eg.com.vodafone.web.mvc.formbean.dataCollection.*;
import eg.com.vodafone.web.mvc.formbean.grid.GridBean;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import eg.com.vodafone.web.mvc.util.CacheService;
import eg.com.vodafone.web.mvc.util.GridConstants;
import eg.com.vodafone.web.mvc.util.GridUtils;
import eg.com.vodafone.web.mvc.validator.*;
import eg.com.vodafone.web.security.DefaultUserContextService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static eg.com.vodafone.web.mvc.formbean.dataCollection.DataCollectionType.DB;
import static eg.com.vodafone.web.mvc.formbean.dataCollection.DataCollectionType.None;
import static eg.com.vodafone.web.mvc.formbean.dataCollection.DataCollectionType.TEXT;
import static eg.com.vodafone.web.mvc.formbean.dataCollection.DataCollectionWizardStep.*;
import static eg.com.vodafone.web.mvc.formbean.dataCollection.OUTPUT_TABLE_OPTION.CREATE_NEW;
import static eg.com.vodafone.web.mvc.formbean.dataCollection.OUTPUT_TABLE_OPTION.USER_EXISTING;
import static eg.com.vodafone.web.mvc.util.CMTConstants.*;
import static eg.com.vodafone.web.mvc.util.CacheName.TEXT_FILE_CACHE;
import static eg.com.vodafone.web.mvc.util.SerializationUtil.*;

/**
 * @author Radwa Osama
 * @since 4/4/13
 */
@Controller
@RequestMapping("/dataCollection/*")
public class ManageDataCollectionsController extends AbstractGridBasedController {

    @Autowired
    DataCollectionServiceInterface dataCollectionService;
    @Autowired
    DefaultUserContextService defaultUserContextService;

    @Autowired
    DataCollectionTypeFormBeanValidator dataCollectionTypeFormBeanValidator;

    @Autowired
    ExtractSourceDataFormBeanValidator extractSourceDataFormBeanValidator;

    @Autowired
    ExtractSourceColumnFormBeanValidator extractSourceColumnFormBeanValidator;

    @Autowired
    DefineSQLColumnsFormBeanValidator defineSQLColumnsFormBeanValidator;

    @Autowired
    DefineOutputTableFormBeanValidator defineOutputTableFormBeanValidator;

    @Autowired
    DefineDataCollectionMappingFormBeanValidator defineDataCollectionMappingFormBeanValidator;

    @Autowired
    DefineDBExtractionSQLValidator defineDBExtractionSQLValidator;

    @Autowired
    ExtractXMLSourceColumnsValidator extractXMLSourceColumnsValidator;

    @Autowired
    CacheService cacheService;

    private static final String DATA_COLLECTION_GRID_XML_CONFIG = "eg/com/vodafone/grids/data-collection-grid.xml";

    @RequestMapping(value = "manage", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView manageDataCollections(DataCollectionSearchCriteria searchCriteria,
            @RequestParam(value = "create", defaultValue = "false") String create,
            @RequestParam(value = "delete", defaultValue = "false") String delete) {

        ModelAndView modelAndView = new ModelAndView("/dataCollection/manage-data-collection");

        modelAndView.addObject(SEARCH_CRITERIA, searchCriteria);

        if (StringUtils.isNotEmpty(create) && TRUE.equalsIgnoreCase(create)) {
            modelAndView.addObject(CREATE_SUCSS_MSG_FLAG, true);
        }

        if (StringUtils.isNotEmpty(delete) && TRUE.equalsIgnoreCase(delete)) {
            modelAndView.addObject(DELETE_SUCSS_MSG_FLAG, true);
        }

        int total = getCount(searchCriteria);

        if (total > 0) {

            int DEFAULT_START_INDEX = 1;
            modelAndView.addObject(GRID_RESULTS_MODEL_NAME,
                    getResultsList(searchCriteria, DEFAULT_START_INDEX, GridConstants.GRID_DEFAULT_NO_ROWS_PER_PAGE));

            modelAndView.addObject(GRID_MODEL_NAME,
                    createDataCollectionGridBean(searchCriteria, total));
        }

        return modelAndView;
    }

    @RequestMapping(value = "delete/{dataCollectionName}/{dropOutputTable}", method = RequestMethod.GET)
    public ModelAndView deleteDataCollection(GridBean gridBean,
            @PathVariable(value = "dataCollectionName") String dataCollectionName,
            @PathVariable(value = "dropOutputTable") boolean dropOutputTable) {

        try {

            dataCollectionService.deleteDataCollection(dataCollectionName, dropOutputTable);

        } catch (BusinessException exception) {

            ModelAndView modelAndView = new ModelAndView("/dataCollection/manage-data-collection");
            modelAndView.addObject(GRID_RESULTS_MODEL_NAME,
                    getResultsList(gridBean.getSearchCriteria(),
                            gridBean.getPaginationBean().getStartRowIndex(),
                            gridBean.getPaginationBean().getEndRowIndex()));

            modelAndView.addObject(GRID_MODEL_NAME, gridBean);
            modelAndView.addObject(SEARCH_CRITERIA, gridBean.getSearchCriteria());
            modelAndView.addObject("delete_error", "This data collection \'" + dataCollectionName
                    + "\' can't be deleted because it is used by a data collection system");

            return modelAndView;

        } catch (Exception exception) {
            throw new GenericException("Error deleting data collection:" + exception.getMessage(), exception);
        }

        return new ModelAndView(new RedirectView("/dataCollection/manage?delete=true", true));
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ModelAndView editDataCollection(@ModelAttribute(value = "dataCollectionName") String dataCollectionName,
            HttpSession session) {

        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil(dataCollectionService);
        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = dataCollectionUtil.createDataCollectionWizardFormBean(dataCollectionName);

        dataCollectionWizardFormBean.setEditMode(true);

        prepareExtractSourceDataFormBean(dataCollectionWizardFormBean.getExtractSourceDataFormBean());
        prepareExtractSourceColumnFormBean(dataCollectionWizardFormBean.getExtractSourceColumnFormBean());
        prepareDefineSQLColumnsFormBean(dataCollectionWizardFormBean.getDefineSQLColumnsFormBean());
        prepareDefineOutputTableFormBean(dataCollectionWizardFormBean.getDefineOutputTableFormBean());
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_PLAIN_COLUMNS);

        String nextStep;
        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        DataCollectionType type = dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType();

        switch (type) {
            case TEXT:
                nextStep = "/dataCollection/define/extractSourceColumns?uuid=" + wizardUUID;
                break;
            case DB:
                nextStep = "/dataCollection/define/defineDBExtractionSQL?uuid=" + wizardUUID;
                break;
            case XML:

                XMLComplexity xmlComplexity
                        = dataCollectionWizardFormBean.getExtractXMLSourceColumns().getXmlComplexity();

                if (XMLComplexity.SIMPLE.equals(xmlComplexity)) {
                    nextStep = "/dataCollection/define/extractSourceColumns?uuid=" + wizardUUID;
                } else {
                    nextStep = "/dataCollection/define/outputTable?uuid=" + wizardUUID;
                }

                break;
            default:
                nextStep = "/dataCollection/manage";
        }

        return new ModelAndView(new RedirectView(nextStep, true));
    }

    @RequestMapping(value = "define/type")
    public ModelAndView defineDataCollection() {

        // Prepare form bean
        DataCollectionWizardFormBean dataCollectionWizardFormBean = prepareDataCollectionWizardFormBean();
        dataCollectionWizardFormBean.setDataCollectionTypeFormBean(prepareDataCollectionTypeFormBean());

        return new ModelAndView("/dataCollection/new/data-collection-type").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/type/steps", method = RequestMethod.POST)
    public ModelAndView loadWizardSteps(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        // Prepare form bean
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DATA_COLLECTION_TYPE);
        dataCollectionWizardFormBean.setDataCollectionTypeFormBean(
                prepareDataCollectionTypeFormBean(dataCollectionWizardFormBean.getDataCollectionTypeFormBean()));

        return new ModelAndView("/dataCollection/new/data-collection-type").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/type", method = RequestMethod.POST)
    public ModelAndView defineDataCollectionNextStep(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "back", defaultValue = "false") String back,
            BindingResult result, HttpSession session) {

        if (StringUtils.isNotEmpty(back) && "true".equalsIgnoreCase(back)) {
            return getDefineDataCollection(dataCollectionWizardFormBean);
        }

        // Validate bean
        dataCollectionTypeFormBeanValidator.validate(
                dataCollectionWizardFormBean.getDataCollectionTypeFormBean(), result);

        // If no errors move to the next step
        if (result.hasErrors()) {
            return getDefineDataCollection(dataCollectionWizardFormBean);
        }

        String nextStep;
        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        DataCollectionType type = dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType();

        switch (type) {
            case TEXT:
                nextStep = "/dataCollection/define/extractDataSource?uuid=" + wizardUUID;
                break;
            case DB:
                nextStep = "/dataCollection/define/defineDBExtractionSQL?uuid=" + wizardUUID;
                break;
            case XML:
                nextStep = "/dataCollection/define/extractXMLSourceColumns?uuid=" + wizardUUID;
                break;
            default:
                nextStep = "/dataCollection/define/manage";
        }

        return new ModelAndView(new RedirectView(nextStep, true));
    }

    @RequestMapping(value = "define/extractDataSource")
    public ModelAndView defineExtractSourceData(@RequestParam(value = "uuid", defaultValue = "") String wizardUUID,
            HttpSession session) {

        if (!isValidWizard(wizardUUID, session)) {
            return resetWizard();
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);
        session.removeAttribute(wizardUUID);

        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = (DataCollectionWizardFormBean) deSerializeBean(decode64Based(serializedBean), DataCollectionWizardFormBean.class);

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, EXTRACT_SOURCE_DATA);

        dataCollectionWizardFormBean.setExtractSourceDataFormBean(
                prepareExtractSourceDataFormBean(dataCollectionWizardFormBean.getExtractSourceDataFormBean()));

        return new ModelAndView("/dataCollection/new/extract-source-data").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/extractDataSource/uploadSource", method = RequestMethod.POST)
    public ModelAndView uploadSourceData(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            BindingResult result) {

        ExtractSourceDataFormBean extractSourceDataFormBean = dataCollectionWizardFormBean.getExtractSourceDataFormBean();

        extractSourceDataFormBeanValidator.validateUploadedFile(extractSourceDataFormBean, result);

        if (!result.hasErrors()) {

            SourceDataParser parser = new SourceDataParser(extractSourceDataFormBean.getUploadFile());

            try {

                String sampleLines = parser.parseResult(100);
                extractSourceDataFormBean.setSampleLines(sampleLines);

                // Write file to cache
                cacheService.add(TEXT_FILE_CACHE, dataCollectionWizardFormBean.getUuid(), parser.getLines());

            } catch (IOException exception) {
                throw new GenericException("Error in parsing uploaded file:" + exception.getMessage(), exception);
            }

        }

        extractSourceDataFormBean.setFileName("");
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, EXTRACT_SOURCE_DATA);
        dataCollectionWizardFormBean.setExtractSourceDataFormBean(prepareExtractSourceDataFormBean(extractSourceDataFormBean));

        return new ModelAndView("/dataCollection/new/extract-source-data").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/extractDataSource", method = RequestMethod.POST)
    public ModelAndView defineExtractSourceDataNextStep(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "back", defaultValue = "false") String back,
            BindingResult result, HttpSession session) {

        if (StringUtils.isNotEmpty(back) && "true".equalsIgnoreCase(back) && !dataCollectionWizardFormBean.isEditMode()) {

            return getDefineExtractSourceData(dataCollectionWizardFormBean);
        }

        ExtractSourceDataFormBean extractSourceDataFormBean = dataCollectionWizardFormBean.getExtractSourceDataFormBean();

        // validate user inputs
        extractSourceDataFormBeanValidator.validate(
                extractSourceDataFormBean, result, dataCollectionWizardFormBean.getUuid());

        if (result.hasErrors()) {

            return getDefineExtractSourceData(dataCollectionWizardFormBean);
        }

        extractSourceDataFormBean.setUploadFile(null);
        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        return new ModelAndView(new RedirectView("/dataCollection/define/extractSourceColumns?uuid=" + wizardUUID, true));
    }

    @RequestMapping(value = "define/defineDBExtractionSQL")
    public ModelAndView defineDBExtractionSQL(@RequestParam(value = "uuid", defaultValue = "") String wizardUUID,
            HttpSession session) {

        if (!isValidWizard(wizardUUID, session)) {
            return resetWizard();
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);
        session.removeAttribute(wizardUUID);

        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = (DataCollectionWizardFormBean) deSerializeBean(decode64Based(serializedBean), DataCollectionWizardFormBean.class);

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_EXTRACTION_SQL_STATEMENT);

        dataCollectionWizardFormBean.setDefineDBExtractionSQL(
                prepareDefineDBExtractionSQL(dataCollectionWizardFormBean.getDefineDBExtractionSQL()));

        return new ModelAndView("/dataCollection/new/define-db-extraction-sql").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/defineDBExtractionSQL", method = RequestMethod.POST)
    public ModelAndView defineDBExtractionSQLNextStep(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "back", defaultValue = "false") String back,
            BindingResult result, HttpSession session) throws IOException {

        if (StringUtils.isNotEmpty(back) && "true".equalsIgnoreCase(back)) {
            return getDefineDBExtractionSQL(dataCollectionWizardFormBean);
        }
        defineDBExtractionSQLValidator.validate(dataCollectionWizardFormBean.getDefineDBExtractionSQL(), result);
        if (result.hasErrors()) {

            return getDefineDBExtractionSQL(dataCollectionWizardFormBean);
        }

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
        DefineDBExtractionSQL defineDBExtractionSQL = dataCollectionWizardFormBean.getDefineDBExtractionSQL();
        ExtractSourceColumnFormBean extractSourceColumnFormBean = new ExtractSourceColumnFormBean();
        extractSourceColumnFormBean.setColumns(
                dataCollectionUtil.getSourceColumns(defineDBExtractionSQL,
                        dataCollectionWizardFormBean.getExtractSourceColumnFormBean()));
        dataCollectionWizardFormBean.setExtractSourceColumnFormBean(extractSourceColumnFormBean);

        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        return new ModelAndView(new RedirectView("/dataCollection/define/extractSourceColumns?uuid=" + wizardUUID, true));
    }

    @RequestMapping(value = "define/extractXMLSourceColumns")
    public ModelAndView defineExtractXMLSourceColumns(@RequestParam(value = "uuid", defaultValue = "") String wizardUUID,
            HttpSession session) {

        if (!isValidWizard(wizardUUID, session)) {
            return resetWizard();
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);
        session.removeAttribute(wizardUUID);

        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = (DataCollectionWizardFormBean) deSerializeBean(decode64Based(serializedBean), DataCollectionWizardFormBean.class);

        return getExtractXMLSourceColumns(dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/extractXMLSourceColumns/uploadSource", method = RequestMethod.POST)
    public ModelAndView uploadXmlSourceFile(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            BindingResult result) {

        ExtractXMLSourceColumns extractXMLSourceColumns = dataCollectionWizardFormBean.getExtractXMLSourceColumns();

        extractXMLSourceColumnsValidator.validateFileUpload(extractXMLSourceColumns, result);

        if (result.hasErrors()) {

            return getExtractXMLSourceColumns(dataCollectionWizardFormBean);
        }

        try {

            // Parse file
            XmlSourceDataParser xmlSourceDataParser
                    = new XmlSourceDataParser(extractXMLSourceColumns.getUploadFile().getInputStream());

            ExtractSourceColumnFormBean extractSourceColumnFormBean = new ExtractSourceColumnFormBean();
            extractSourceColumnFormBean.setColumns(xmlSourceDataParser.extractSourceData());

            dataCollectionWizardFormBean.setExtractSourceColumnFormBean(extractSourceColumnFormBean);

            extractXMLSourceColumns.setFileName(extractXMLSourceColumns.getUploadFile().getOriginalFilename());

        } catch (IOException e) {

            throw new BusinessException("Error Parsing uploaded XML file");
        }

        return getExtractXMLSourceColumns(dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/extractXMLSourceColumns/reloadConverters", method = RequestMethod.POST)
    public ModelAndView reloadConverters(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        ExtractXMLSourceColumns extractXMLSourceColumns
                = dataCollectionWizardFormBean.getExtractXMLSourceColumns();

        String selection = extractXMLSourceColumns.getXmlVendor();

        if (DEFAULT_SELECT.equals(selection)) {
            extractXMLSourceColumns.setXmlConverterTypes(null);
        } else {

            extractXMLSourceColumns.setXmlConverterTypes(listConvertersByVendorId(selection));
        }

        return getExtractXMLSourceColumns(dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/extractXMLSourceColumns", method = RequestMethod.POST)
    public ModelAndView defineExtractXMLSourceColumnsNextStep(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "back", defaultValue = "false") String back,
            BindingResult result, HttpSession session) {

        ExtractXMLSourceColumns extractXMLSourceColumns = dataCollectionWizardFormBean.getExtractXMLSourceColumns();

        if (StringUtils.isNotEmpty(back) && "true".equalsIgnoreCase(back)) {
            return getExtractXMLSourceColumns(dataCollectionWizardFormBean);
        }

        if (XMLComplexity.SIMPLE.equals(extractXMLSourceColumns.getXmlComplexity())) {

            extractXMLSourceColumnsValidator.validateSourceColumnsWhereExtracted(
                    dataCollectionWizardFormBean.getExtractSourceColumnFormBean(), result);

        } else if (XMLComplexity.VENDOR_SPECIFIC.equals(extractXMLSourceColumns.getXmlComplexity())) {
            extractXMLSourceColumnsValidator.validate(extractXMLSourceColumns, result);
        }

        if (result.hasErrors()) {
            return getExtractXMLSourceColumns(dataCollectionWizardFormBean);
        }

        if (XMLComplexity.VENDOR_SPECIFIC.equals(extractXMLSourceColumns.getXmlComplexity())) {

            DataCollectionUtil dataCollectionUtil = new DataCollectionUtil(dataCollectionService);

            ExtractSourceColumnFormBean extractSourceColumnFormBean = new ExtractSourceColumnFormBean();
            extractSourceColumnFormBean.setColumns(
                    dataCollectionUtil.createSourceColumnsForExtractXMLSourceColumns(extractXMLSourceColumns));
            dataCollectionWizardFormBean.setExtractSourceColumnFormBean(extractSourceColumnFormBean);
        }

        extractXMLSourceColumns.setUploadFile(null);

        String nextStep;
        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        if (XMLComplexity.SIMPLE.equals(extractXMLSourceColumns.getXmlComplexity())) {
            nextStep = "/dataCollection/define/extractSourceColumns?uuid=" + wizardUUID;
        } else {
            nextStep = "/dataCollection/define/outputTable?uuid=" + wizardUUID;
        }

        return new ModelAndView(new RedirectView(nextStep, true));

    }

    @RequestMapping(value = "define/extractSourceColumns")
    public ModelAndView defineSourceColumns(@RequestParam(value = "uuid", defaultValue = "") String wizardUUID,
            HttpSession session) throws IOException {

        if (!isValidWizard(wizardUUID, session)) {
            return resetWizard();
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);
        session.removeAttribute(wizardUUID);

        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = (DataCollectionWizardFormBean) deSerializeBean(decode64Based(serializedBean), DataCollectionWizardFormBean.class);

        if (!dataCollectionWizardFormBean.isEditMode()
                && TEXT.equals(dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType())) {

            ExtractSourceDataFormBean extractSourceDataFormBean = dataCollectionWizardFormBean.getExtractSourceDataFormBean();

            dataCollectionWizardFormBean.setExtractSourceColumnFormBean(
                    prepareExtractSourceColumnFormBean(extractSourceDataFormBean, dataCollectionWizardFormBean.getUuid()));

        } else {
            updateDBExtractionSQL(dataCollectionWizardFormBean);
            prepareExtractSourceColumnFormBean(dataCollectionWizardFormBean.getExtractSourceColumnFormBean());
        }
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_PLAIN_COLUMNS);

        return new ModelAndView("/dataCollection/new/extract-source-columns").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/cancel", method = RequestMethod.POST)
    public ModelAndView defineSourceColumnsCancel(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        return new ModelAndView(new RedirectView("/dataCollection/manage", true));
    }

    @RequestMapping(value = "define/extractSourceColumns", method = RequestMethod.POST)
    public ModelAndView defineSourceColumnsNextStep(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "back", defaultValue = "false") String back,
            BindingResult result, HttpSession session) throws IOException {

        ExtractSourceColumnFormBean extractSourceColumnFormBean
                = dataCollectionWizardFormBean.getExtractSourceColumnFormBean();

        if (StringUtils.isNotEmpty(back) && "true".equalsIgnoreCase(back)) {

            if (!dataCollectionWizardFormBean.isEditMode()) {

                extractSourceColumnFormBean.setTableCreated(false);

            }

            return getExtractSourceColumnFormBean(dataCollectionWizardFormBean);
        }

        // Validate data types
        extractSourceColumnFormBeanValidator.validate(extractSourceColumnFormBean, result);
        if (TEXT.equals(dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType())) {
            extractSourceColumnFormBeanValidator.validateUseDateOption(extractSourceColumnFormBean, result);
        }
        if (result.hasErrors()) {
            return getExtractSourceColumnFormBean(dataCollectionWizardFormBean);
        }

        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        if (DB.equals(dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType())) {
            updateDBExtractionSQL(dataCollectionWizardFormBean);
            return new ModelAndView(new RedirectView("/dataCollection/define/outputTable?uuid=" + wizardUUID, true));
        } else {
            return new ModelAndView(new RedirectView("/dataCollection/define/sqlColumns?uuid=" + wizardUUID, true));
        }
    }

    @RequestMapping(value = "define/sqlColumns")
    public ModelAndView defineSQLColumns(@RequestParam(value = "uuid", defaultValue = "") String wizardUUID,
            HttpSession session) {

        if (!isValidWizard(wizardUUID, session)) {
            return resetWizard();
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);
        session.removeAttribute(wizardUUID);

        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = (DataCollectionWizardFormBean) deSerializeBean(decode64Based(serializedBean), DataCollectionWizardFormBean.class);

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_SQL_COLUMNS);

        dataCollectionWizardFormBean.setDefineSQLColumnsFormBean(
                prepareDefineSQLColumnsFormBean(dataCollectionWizardFormBean.getDefineSQLColumnsFormBean()));

        return new ModelAndView("/dataCollection/new/define-sql-columns").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/sqlColumns/addColumn", method = RequestMethod.POST)
    public ModelAndView addSQLColumn(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            BindingResult result) throws IOException {

        // Validate new column
        DefineSQLColumnsFormBean defineSQLColumnsFormBean
                = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean();

        defineSQLColumnsFormBeanValidator.validate(defineSQLColumnsFormBean, result);

        if (!result.hasErrors()) {

            List<SourceColumn> sourceColumnList;
            if (defineSQLColumnsFormBean.getSqlExpressionColumns() == null) {
                sourceColumnList = new ArrayList<SourceColumn>();
            } else {
                sourceColumnList = defineSQLColumnsFormBean.getSqlExpressionColumns();
            }
            //***added by basma
            List<SourceColumn> extractedColumnsAndSqlColumns = new ArrayList<SourceColumn>(sourceColumnList);
            ExtractSourceColumnFormBean extractSourceColumnFormBean
                    = dataCollectionWizardFormBean.getExtractSourceColumnFormBean();
            if (extractSourceColumnFormBean.getColumns() != null && !extractSourceColumnFormBean.getColumns().isEmpty()) {
                extractedColumnsAndSqlColumns.addAll(extractSourceColumnFormBean.getColumns());
            }
            defineSQLColumnsFormBeanValidator.validateUniqueColumnName(extractedColumnsAndSqlColumns, defineSQLColumnsFormBean.getSourceColumn(), result);
            //***end added by basma

            if (!result.hasErrors()) {
                SourceColumn sourceColumn = defineSQLColumnsFormBean.getSourceColumn();
                sourceColumn.setSelected(true);
                sourceColumn.setIndex(extractedColumnsAndSqlColumns.size());
                sourceColumnList.add(sourceColumn);
                defineSQLColumnsFormBean.setSqlExpressionColumns(sourceColumnList);
                defineSQLColumnsFormBean.setSourceColumn(null);
            }
        }

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_SQL_COLUMNS);
        dataCollectionWizardFormBean.setDefineSQLColumnsFormBean(prepareDefineSQLColumnsFormBean(defineSQLColumnsFormBean));

        return new ModelAndView("/dataCollection/new/define-sql-columns").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/sqlColumns/deleteColumn", method = RequestMethod.POST)
    public ModelAndView deleteSQLColumn(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "index") String index) throws IOException {

        DefineSQLColumnsFormBean defineSQLColumnsFormBean
                = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean();

        List<SourceColumn> sourceColumnList = defineSQLColumnsFormBean.getSqlExpressionColumns();
        if (sourceColumnList != null) {
            SourceColumn columnToDelete = sourceColumnList.get(Integer.parseInt(index));

            for (SourceColumn column : sourceColumnList) {
                if (columnToDelete.getIndex() < column.getIndex()) {
                    column.setIndex(column.getIndex() - 1);
                }
            }
            sourceColumnList.remove(Integer.parseInt(index));
        }

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_SQL_COLUMNS);
        dataCollectionWizardFormBean.setDefineSQLColumnsFormBean(prepareDefineSQLColumnsFormBean(defineSQLColumnsFormBean));

        return new ModelAndView("/dataCollection/new/define-sql-columns").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/sqlColumns", method = RequestMethod.POST)
    public ModelAndView defineSQLColumnsNextStep(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "back", defaultValue = "false") String back,
            BindingResult result, HttpSession session) throws IOException {

        if (StringUtils.isNotEmpty(back) && "true".equalsIgnoreCase(back)) {
            return getDefineSqlColumnsFormBean(dataCollectionWizardFormBean);
        }

        DefineSQLColumnsFormBean defineSQLColumnsFormBean
                = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean();

        // Validate Query
        defineSQLColumnsFormBeanValidator.validate(defineSQLColumnsFormBean, result);

        try {

            // Pass list of selected columns and Sql Expression Columns
            DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();

            String name = dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionName();
            //*** added by basma
            String dateColumnName = null;
            String dateColumnFormat = null;
            if (dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getUseDateColumn()) {
                dateColumnName = dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getDateColumnName();
                dateColumnFormat = dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getDateColumnPrecession().getFormat();
            }
            //*** end added by basma
            List<SourceColumn> allColumns = new ArrayList<SourceColumn>(
                    dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns());

            List<SourceColumn> selectedPlainColumns
                    = dataCollectionUtil.getSelectedSourceColumn(
                            dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns());

            List<SourceColumn> expressionColumns
                    = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();

            dataCollectionUtil.setTypeParametersToDefault(expressionColumns);

            if (expressionColumns != null) {
                allColumns.addAll(expressionColumns);
            }
            if ((selectedPlainColumns == null || selectedPlainColumns.isEmpty())
                    && (expressionColumns == null || expressionColumns.isEmpty())) {
                defineSQLColumnsFormBean.setQueryError("No columns were added !");
                return getDefineSqlColumnsFormBean(dataCollectionWizardFormBean);
            } else {
                String[] sampleData = null;
                sampleData = new String[dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns().size()];
                for (SourceColumn column : dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns()) {
                    sampleData[column.getIndex()] = column.getSampleValue();
                }

                String extractionSql
                        = dataCollectionService.buildAndExecuteExtractionSqlOnTempTable(name,
                                dataCollectionUtil.createDataColumn(allColumns),
                                defineSQLColumnsFormBean.getWhereClause(),
                                defineSQLColumnsFormBean.getGroupByClause(),
                                defineSQLColumnsFormBean.getHavingClause(),
                                dateColumnName, dateColumnFormat, sampleData);
                defineSQLColumnsFormBean.setExtractionSql(extractionSql);
            }

        } catch (Exception e) {

            if (e.getMessage() == null) {
                defineSQLColumnsFormBean.setQueryError("Invalid SQL expression");
            } else {
                defineSQLColumnsFormBean.setQueryError("Invalid SQL expression, details are following: " + e.getMessage());
            }
            return getDefineSqlColumnsFormBean(dataCollectionWizardFormBean);
        }

        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        return new ModelAndView(new RedirectView("/dataCollection/define/outputTable?uuid=" + wizardUUID, true));
    }

    @RequestMapping(value = "define/outputTable")
    public ModelAndView defineOutputTable(@RequestParam(value = "uuid", defaultValue = "") String wizardUUID,
            HttpSession session) {

        if (!isValidWizard(wizardUUID, session)) {
            return resetWizard();
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);
        session.removeAttribute(wizardUUID);

        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = (DataCollectionWizardFormBean) deSerializeBean(decode64Based(serializedBean), DataCollectionWizardFormBean.class);

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_OUTPUT_TABLE);

        dataCollectionWizardFormBean.setDefineOutputTableFormBean(
                prepareDefineOutputTableFormBean(dataCollectionWizardFormBean.getDefineOutputTableFormBean()));

        return new ModelAndView("/dataCollection/new/define-output-table").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/outputTable/existingTableColumns", method = RequestMethod.POST)
    public ModelAndView loadExistingTableColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        String selectedTableName = defineOutputTableFormBean.getExistingTable();

        if (DEFAULT_SELECT.equals(selectedTableName)) {
            defineOutputTableFormBean.setExistingTableColumns(null);
            defineOutputTableFormBean.setNewOutputTableColumns(null);
            defineOutputTableFormBean.setTableSelected(false);

        } else {

            try {
                defineOutputTableFormBean.setExistingTableColumns(
                        dataCollectionService.getOutputTableColumns(selectedTableName));

                defineOutputTableFormBean.setTableSelected(true);
            } catch (BusinessException e) {
                if (e.getCode() == BusinessException.TABLE_DOES_NOT_EXIST) {
                    defineOutputTableFormBean.setErrorMessage("Table was not found, please refer to admin");
                } else {
                    defineOutputTableFormBean.setErrorMessage("Error  while getting table columns, please refer to logs");
                }
                defineOutputTableFormBean.setExistingTableColumns(null);
            }
        }

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_OUTPUT_TABLE);

        dataCollectionWizardFormBean.setDefineOutputTableFormBean(
                prepareDefineOutputTableFormBean(defineOutputTableFormBean));

        return new ModelAndView("/dataCollection/new/define-output-table").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/outputTable/addColumn", method = RequestMethod.POST)
    public ModelAndView addNewOutputColumn(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            BindingResult result) throws IOException {

        // Validate new column
        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        defineOutputTableFormBeanValidator.validateAddNewColumn(defineOutputTableFormBean, result);

        if (!result.hasErrors()) {

            List<SourceColumn> sourceColumnList;
            if (defineOutputTableFormBean.getNewOutputTableColumns() == null) {
                sourceColumnList = new ArrayList<SourceColumn>();
            } else {
                sourceColumnList = defineOutputTableFormBean.getNewOutputTableColumns();
            }

            sourceColumnList.add(defineOutputTableFormBean.getSourceColumn());
            defineOutputTableFormBean.setNewOutputTableColumns(sourceColumnList);
            defineOutputTableFormBean.setSourceColumn(null);
        }

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_OUTPUT_TABLE);

        dataCollectionWizardFormBean.setDefineOutputTableFormBean(
                prepareDefineOutputTableFormBean(defineOutputTableFormBean));

        return new ModelAndView("/dataCollection/new/define-output-table").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/outputTable/deleteColumn", method = RequestMethod.POST)
    public ModelAndView deleteOutputColumn(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "index") String index) throws IOException {

        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        List<SourceColumn> sourceColumnList = defineOutputTableFormBean.getNewOutputTableColumns();
        if (sourceColumnList != null) {
            sourceColumnList.remove(Integer.parseInt(index));
        }

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_OUTPUT_TABLE);

        dataCollectionWizardFormBean.setDefineOutputTableFormBean(
                prepareDefineOutputTableFormBean(defineOutputTableFormBean));

        return new ModelAndView("/dataCollection/new/define-output-table").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/outputTable", method = RequestMethod.POST)
    public ModelAndView outputTableNextStep(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            @RequestParam(value = "back", defaultValue = "false") String back,
            BindingResult result, HttpSession session) throws IOException {

        if (StringUtils.isNotEmpty(back) && "true".equalsIgnoreCase(back)) {
            return getDefineOutputTableFormBean(dataCollectionWizardFormBean);
        }

        // Validate if use existing table was selected, then there exist for
        // each source column at least one matching type column in the output table
        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        OUTPUT_TABLE_OPTION outputTableOption = defineOutputTableFormBean.getOutputTableOption();

        if (USER_EXISTING.equals(outputTableOption)) {
            defineOutputTableFormBeanValidator.validateUseExistingTable(dataCollectionWizardFormBean, result);
        } else {
            defineOutputTableFormBeanValidator.validateCreateNewTable(dataCollectionWizardFormBean, result);
        }

        if (result.hasErrors()) {

            prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_OUTPUT_TABLE);

            dataCollectionWizardFormBean.setDefineOutputTableFormBean(
                    prepareDefineOutputTableFormBean(defineOutputTableFormBean));

            return new ModelAndView("/dataCollection/new/define-output-table").
                    addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);

        }

        String wizardUUID = UUID.randomUUID().toString();
        session.setAttribute(wizardUUID, encode64Based(serializeBean(dataCollectionWizardFormBean)));

        return new ModelAndView(new RedirectView("/dataCollection/define/dataCollectionMapping?uuid=" + wizardUUID, true));
    }

    @RequestMapping(value = "define/dataCollectionMapping")
    public ModelAndView defineDataCollectionMapping(@RequestParam(value = "uuid", defaultValue = "") String wizardUUID,
            HttpSession session) {

        if (!isValidWizard(wizardUUID, session)) {
            return resetWizard();
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);
        session.removeAttribute(wizardUUID);

        DataCollectionWizardFormBean dataCollectionWizardFormBean
                = (DataCollectionWizardFormBean) deSerializeBean(decode64Based(serializedBean), DataCollectionWizardFormBean.class);

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_DATA_COLLECTION_MAPPING);

        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();

        DefineDataCollectionMappingFormBean defineDataCollectionMappingFormBean
                = new DefineDataCollectionMappingFormBean();

        defineDataCollectionMappingFormBean.
                setOutputColumns(dataCollectionUtil.getOutputColumns(dataCollectionWizardFormBean));

        // Added by Awad
        // CMT DashBoard Configuration
        List<SourceColumn> columns = dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns();

        List<SourceColumn> sqlColumns = dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns();

        prepareDataCollectionMappingFormBean(defineDataCollectionMappingFormBean, columns, sqlColumns,
                dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionName());

        // End Awad
        if (NODE_NAME.MAPPED.equals(dataCollectionWizardFormBean.getDefineOutputTableFormBean().getNodeName())) {

            defineDataCollectionMappingFormBean.setNodeNameColumn(dataCollectionUtil.createNodeNameColumn());
            defineDataCollectionMappingFormBean.setMappedNodeName(true);
        } else {
            defineDataCollectionMappingFormBean.setMappedNodeName(false);
            defineDataCollectionMappingFormBean.setNodeNameColumn(null);
        }
        dataCollectionWizardFormBean.setDefineDataCollectionMappingFormBean(defineDataCollectionMappingFormBean);

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        return new ModelAndView("/dataCollection/new/data-collection-mapping").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    @RequestMapping(value = "define/dataCollectionMapping", method = RequestMethod.POST)
    public ModelAndView saveDataCollection(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            BindingResult result) {

        DefineOutputTableFormBean defineOutputTableFormBean
                = dataCollectionWizardFormBean.getDefineOutputTableFormBean();

        OUTPUT_TABLE_OPTION outputTableOption = defineOutputTableFormBean.getOutputTableOption();

        DataCollectionUtil dataCollectionUtil = new DataCollectionUtil();
        List<DataColumn> outputColumns
                = dataCollectionUtil.createDataColumn(dataCollectionUtil.getNewOutputColumns(dataCollectionWizardFormBean));

        defineDataCollectionMappingFormBeanValidator.validate(dataCollectionUtil.createDataColumn(dataCollectionUtil.getSelectedPlainAndSqlColumns(dataCollectionWizardFormBean)), result);

        if (result.hasErrors()) {

            dataCollectionWizardFormBean.getDefineDataCollectionMappingFormBean().
                    setOutputColumns(dataCollectionUtil.getOutputColumns(dataCollectionWizardFormBean));

            // Added by Awad 
            // CMT DashBoard Configuration 
            prepareDataCollectionMappingFormBean(dataCollectionWizardFormBean.getDefineDataCollectionMappingFormBean(),
                    dataCollectionWizardFormBean.getExtractSourceColumnFormBean().getColumns(),
                    dataCollectionWizardFormBean.getDefineSQLColumnsFormBean().getSqlExpressionColumns(),
                    dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionName());

            // End Awad
            prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_DATA_COLLECTION_MAPPING);

            return new ModelAndView("/dataCollection/new/data-collection-mapping").
                    addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
        }

        if (DB.equals(dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType())) {
            dataCollectionWizardFormBean.getDefineDBExtractionSQL().setQuery(
                    dataCollectionWizardFormBean.getDefineDBExtractionSQL().getQuery().replaceAll("&quot;", "\"")
            );
        }

        VInputStructure vInputStructure = dataCollectionUtil.createVInputStructure(dataCollectionWizardFormBean);

        // Added by Awad
        DataCollectionTypeFormBean dataCollectionTypeFormBean = dataCollectionWizardFormBean.getDataCollectionTypeFormBean();

        // End
        if (CREATE_NEW.equals(outputTableOption)) {

            //create_new option is available with define new dc only
            /* if (dataCollectionWizardFormBean.isEditMode()) {

        dataCollectionService.editDataCollectionAndCreateOutputTable(
          vInputStructure,
          outputColumns, defaultUserContextService.getCurrentUser());

      } else {  */
            dataCollectionService.addDataCollectionAndCreateOutputTable(
                    vInputStructure,
                    outputColumns, defaultUserContextService.getCurrentUser(),
                    dataCollectionTypeFormBean.getDataCollectionName(),
                    dataCollectionWizardFormBean.getDefineDataCollectionMappingFormBean().getKpiType());
            // }

        } else if (USER_EXISTING.equals(outputTableOption)) {

            for (DataColumn dataColumn : outputColumns) {
                dataColumn.setName(dataColumn.getSrcColumn());
            }

            if (dataCollectionWizardFormBean.isEditMode()) {

                dataCollectionService.editDataCollectionAndUpdateOutputTable(
                        vInputStructure,
                        outputColumns, defaultUserContextService.getCurrentUser(), dataCollectionTypeFormBean.getDataCollectionName(),
                        dataCollectionWizardFormBean.getDefineDataCollectionMappingFormBean().getKpiType());

            } else {

                dataCollectionService.addDataCollectionUsingExsitingOutputTable(
                        vInputStructure,
                        outputColumns, defaultUserContextService.getCurrentUser(), dataCollectionTypeFormBean.getDataCollectionName(),
                        dataCollectionWizardFormBean.getDefineDataCollectionMappingFormBean().getKpiType());
            }

        }

        return new ModelAndView(new RedirectView("/dataCollection/manage?create=true", true));
    }

    @Override
    public int getCount(Object... args) {

        DataCollectionSearchCriteria searchCriteria = (DataCollectionSearchCriteria) args[0];
        if (searchCriteria != null && StringUtils.isNotEmpty(searchCriteria.getKeyword())) {
            return dataCollectionService.listAllDataCollectionsWithEditFlag(searchCriteria.getKeyword().trim()).size();
        } else {
            return dataCollectionService.listAllDataCollectionsWithEditFlag().size();
        }
    }

    @Override
    public List<DataCollectionGridEntry> getResultsList(SearchCriteria searchCriteria,
            int startRowIndex, int endRowIndex) {
        Map result = null;
        DataCollectionSearchCriteria dataCollectionSearchCriteria = (DataCollectionSearchCriteria) searchCriteria;

        if (searchCriteria != null && StringUtils.isNotEmpty(dataCollectionSearchCriteria.getKeyword())
                && !StringUtils.isWhitespace(dataCollectionSearchCriteria.getKeyword())) {
            result = dataCollectionService.listAllDataCollectionsWithEditFlag(dataCollectionSearchCriteria.getKeyword().trim(), startRowIndex, endRowIndex);
        } else {
            result = dataCollectionService.listAllDataCollectionsWithEditFlag(startRowIndex, endRowIndex);
        }

        if (result != null && !result.isEmpty()) {
            List<Map.Entry<String, Boolean>> pageResult = new ArrayList<Map.Entry<String, Boolean>>(result.entrySet());
            return createDataCollectionGridEntry(pageResult);
        }

        return new ArrayList<DataCollectionGridEntry>();
    }

    /**
     * @param searchCriteria define filters
     * @param total total number of elements in the grid
     * @return Grid Bean instance
     */
    private GridBean createDataCollectionGridBean(SearchCriteria searchCriteria, int total) {
        GridBean gridBean = GridUtils.createGridBean(total);
        gridBean.setSearchCriteria(searchCriteria);

        GridUserInterface gridUserInterface = GridUtils.createGridUserInterface(DATA_COLLECTION_GRID_XML_CONFIG);
        gridBean.setGridUserInterface(gridUserInterface);
        GridUtils.populateSortColumns(gridBean);

        return gridBean;
    }

    /**
     * @param entryList List of entries containing data collection name and
     * boolean identifying if it is editable or not
     * @return map each entry to instance of DataCollectionGridEntry
     */
    private List<DataCollectionGridEntry> createDataCollectionGridEntry(List<Map.Entry<String, Boolean>> entryList) {

        List<DataCollectionGridEntry> dataCollections = new ArrayList<DataCollectionGridEntry>(entryList.size());

        for (Map.Entry<String, Boolean> entry : entryList) {
            dataCollections.add(createDataCollectionGridEntry(entry));
        }

        return dataCollections;
    }

    /**
     * @param entry containing data collection name and boolean identifying if
     * it is editable or not
     * @return map each entry to instance of DataCollectionGridEntry
     */
    private DataCollectionGridEntry createDataCollectionGridEntry(Map.Entry<String, Boolean> entry) {
        return new DataCollectionGridEntry(entry.getKey(), entry.getValue());
    }

    /**
     * @return data collection wizard
     */
    private DataCollectionWizardFormBean prepareDataCollectionWizardFormBean() {

        // Prepare form bean
        DataCollectionWizardFormBean dataCollectionWizardFormBean = new DataCollectionWizardFormBean();
        dataCollectionWizardFormBean.setUuid(UUID.randomUUID().toString());
        return prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DATA_COLLECTION_TYPE);
    }

    /**
     * @return data collection wizard
     */
    private DataCollectionWizardFormBean prepareDataCollectionWizardFormBean(DataCollectionWizardFormBean dataCollectionWizardFormBean,
            DataCollectionWizardStep currentStep) {

        // Prepare form bean
        DataCollectionType type;

        if (dataCollectionWizardFormBean.getDataCollectionTypeFormBean() != null) {
            type
                    = dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType();
        } else {
            type = DataCollectionType.TEXT;
        }

        switch (type) {
            case TEXT:
            case None:
                dataCollectionWizardFormBean.setSteps(Arrays.asList(
                        DATA_COLLECTION_TYPE, EXTRACT_SOURCE_DATA, DEFINE_PLAIN_COLUMNS,
                        DEFINE_SQL_COLUMNS, DEFINE_OUTPUT_TABLE, DEFINE_DATA_COLLECTION_MAPPING));
                break;

            case XML:
                //*** modified by Basma
                if (dataCollectionWizardFormBean.getExtractXMLSourceColumns() != null
                        && dataCollectionWizardFormBean.getExtractXMLSourceColumns().getXmlComplexity() == XMLComplexity.VENDOR_SPECIFIC) {

                    dataCollectionWizardFormBean.setSteps(Arrays.asList(
                            DATA_COLLECTION_TYPE, EXTRACT_XML_SOURCE_COLUMN,
                            DEFINE_OUTPUT_TABLE, DEFINE_DATA_COLLECTION_MAPPING));

                } else {
                    dataCollectionWizardFormBean.setSteps(Arrays.asList(
                            DATA_COLLECTION_TYPE, EXTRACT_XML_SOURCE_COLUMN, DEFINE_PLAIN_COLUMNS,
                            DEFINE_SQL_COLUMNS, DEFINE_OUTPUT_TABLE, DEFINE_DATA_COLLECTION_MAPPING));
                }
                //*** end modified by Basma
                break;

            case DB:
                dataCollectionWizardFormBean.setSteps(Arrays.asList(
                        DATA_COLLECTION_TYPE, DEFINE_EXTRACTION_SQL_STATEMENT, DEFINE_PLAIN_COLUMNS,
                        DEFINE_OUTPUT_TABLE, DEFINE_DATA_COLLECTION_MAPPING));
                break;

            default:
                dataCollectionWizardFormBean.setSteps(Arrays.asList(DataCollectionWizardStep.values()));
                break;
        }

        dataCollectionWizardFormBean.setCurrentStep(currentStep);

        return dataCollectionWizardFormBean;
    }

    /**
     * @return Prepare form bean for 'Select data collection type' step
     */
    private DataCollectionTypeFormBean prepareDataCollectionTypeFormBean() {

        DataCollectionTypeFormBean dataCollectionTypeFormBean = new DataCollectionTypeFormBean();

        dataCollectionTypeFormBean.setDataCollectionType(None);

        return prepareDataCollectionTypeFormBean(dataCollectionTypeFormBean);
    }

    /**
     * @return Prepare form bean for 'Select data collection type' step
     */
    private DataCollectionTypeFormBean prepareDataCollectionTypeFormBean(DataCollectionTypeFormBean dataCollectionTypeFormBean) {

        Map<String, String> dataCollectionTypes = new LinkedHashMap<String, String>();

        for (DataCollectionType type : DataCollectionType.values()) {
            dataCollectionTypes.put(type.toString(), type.getDescription());
        }

        dataCollectionTypeFormBean.setDataCollectionTypes(dataCollectionTypes);

        return dataCollectionTypeFormBean;
    }

    /**
     * @return Prepare form bean for 'Extract source data' step
     */
    private ExtractSourceDataFormBean prepareExtractSourceDataFormBean() {

        ExtractSourceDataFormBean extractSourceDataFormBean = new ExtractSourceDataFormBean();

        extractSourceDataFormBean.setHeader(Header.DONT_USE);

        return prepareExtractSourceDataFormBean(extractSourceDataFormBean);
    }

    /**
     * @return Prepare form bean for 'Extract source data' step
     */
    private ExtractSourceDataFormBean prepareExtractSourceDataFormBean(ExtractSourceDataFormBean extractSourceDataFormBean) {

        if (extractSourceDataFormBean == null) {
            return prepareExtractSourceDataFormBean();
        }

        Map<String, String> delimiterTypes = new LinkedHashMap<String, String>();

        for (Delimiter type : Delimiter.values()) {
            delimiterTypes.put(type.toString(), type.getDescription());
        }

        extractSourceDataFormBean.setDelimiterTypes(delimiterTypes);

        Map<String, Header> headerTypes = new LinkedHashMap<String, Header>();

        for (Header header : Header.values()) {
            headerTypes.put(header.toString(), header);
        }

        extractSourceDataFormBean.setHeaderTypes(headerTypes);

        if (extractSourceDataFormBean.getHeader() == null) {
            extractSourceDataFormBean.setHeader(Header.DONT_USE);
        }

        return extractSourceDataFormBean;
    }

    private ExtractXMLSourceColumns prepareExtractXMLSourceColumns() {

        ExtractXMLSourceColumns extractXMLSourceColumns = new ExtractXMLSourceColumns();

        extractXMLSourceColumns.setXmlComplexity(XMLComplexity.SIMPLE);

        return prepareExtractXMLSourceColumns(extractXMLSourceColumns);
    }

    private ExtractXMLSourceColumns prepareExtractXMLSourceColumns(ExtractXMLSourceColumns extractXMLSourceColumns) {

        if (extractXMLSourceColumns == null) {
            return prepareExtractXMLSourceColumns();
        }

        Map<String, XMLComplexity> xmlComplexityTypes = new LinkedHashMap<String, XMLComplexity>();

        for (XMLComplexity xmlComplexity : XMLComplexity.values()) {
            xmlComplexityTypes.put(xmlComplexity.toString(), xmlComplexity);
        }

        extractXMLSourceColumns.setXmlComplexityTypes(xmlComplexityTypes);

        List<XmlVendor> xmlVendorList = dataCollectionService.listXmlVendor();

        Map<String, String> xmlVendors = new LinkedHashMap<String, String>();

        xmlVendors.put(DEFAULT_SELECT, DEFAULT_SELECT);

        if (xmlVendorList != null && !xmlVendorList.isEmpty()) {

            for (XmlVendor xmlVendor : xmlVendorList) {
                xmlVendors.put(xmlVendor.getId() + "", xmlVendor.getName());
            }
        }

        extractXMLSourceColumns.setXmlVendors(xmlVendors);

        if (!DEFAULT_SELECT.equals(extractXMLSourceColumns.getXmlVendor())) {

            extractXMLSourceColumns.setXmlConverterTypes(listConvertersByVendorId(extractXMLSourceColumns.getXmlVendor()));

        }

        return extractXMLSourceColumns;
    }

    private Map<String, String> listConvertersByVendorId(String vendorId) {

        Map<String, String> xmlConverters = new HashMap<String, String>();

        if (!StringUtils.isNumeric(vendorId)) {
            return xmlConverters;
        }

        List<XmlConverter> xmlConverterList
                = dataCollectionService.listXmlConverterByVendorId(Long.parseLong(vendorId));

        if (xmlConverterList != null && !xmlConverterList.isEmpty()) {

            for (XmlConverter xmlConverter : xmlConverterList) {
                xmlConverters.put(xmlConverter.getId() + "", xmlConverter.getName());
            }
        }

        return xmlConverters;
    }

    private ModelAndView getDefineDataCollection(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DATA_COLLECTION_TYPE);
        prepareDataCollectionTypeFormBean(dataCollectionWizardFormBean.getDataCollectionTypeFormBean());

        return new ModelAndView("/dataCollection/new/data-collection-type").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    private ModelAndView getDefineExtractSourceData(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, EXTRACT_SOURCE_DATA);
        dataCollectionWizardFormBean.setExtractSourceDataFormBean(
                prepareExtractSourceDataFormBean(dataCollectionWizardFormBean.getExtractSourceDataFormBean()));

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        return new ModelAndView("/dataCollection/new/extract-source-data").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    private ModelAndView getExtractSourceColumnFormBean(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_PLAIN_COLUMNS);

        dataCollectionWizardFormBean.setExtractSourceColumnFormBean(
                prepareExtractSourceColumnFormBean(dataCollectionWizardFormBean.getExtractSourceColumnFormBean()));

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        return new ModelAndView("/dataCollection/new/extract-source-columns").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    private ModelAndView getDefineSqlColumnsFormBean(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_SQL_COLUMNS);
        dataCollectionWizardFormBean.setDefineSQLColumnsFormBean(
                prepareDefineSQLColumnsFormBean(dataCollectionWizardFormBean.getDefineSQLColumnsFormBean()));

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        return new ModelAndView("/dataCollection/new/define-sql-columns").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    private ModelAndView getDefineOutputTableFormBean(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_OUTPUT_TABLE);

        dataCollectionWizardFormBean.setDefineOutputTableFormBean(
                prepareDefineOutputTableFormBean(dataCollectionWizardFormBean.getDefineOutputTableFormBean()));

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        return new ModelAndView("/dataCollection/new/define-output-table").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    private ModelAndView getDefineDBExtractionSQL(DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, DEFINE_EXTRACTION_SQL_STATEMENT);

        dataCollectionWizardFormBean.setDefineDBExtractionSQL(
                prepareDefineDBExtractionSQL(dataCollectionWizardFormBean.getDefineDBExtractionSQL()));

        updateDBExtractionSQL(dataCollectionWizardFormBean);

        return new ModelAndView("/dataCollection/new/define-db-extraction-sql").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);
    }

    private ModelAndView getExtractXMLSourceColumns(DataCollectionWizardFormBean dataCollectionWizardFormBean) {

        prepareDataCollectionWizardFormBean(dataCollectionWizardFormBean, EXTRACT_XML_SOURCE_COLUMN);

        dataCollectionWizardFormBean.setExtractXMLSourceColumns(
                prepareExtractXMLSourceColumns(dataCollectionWizardFormBean.getExtractXMLSourceColumns()));

        return new ModelAndView("/dataCollection/new/extract-xml-source-columns").
                addObject("dataCollectionWizardFormBean", dataCollectionWizardFormBean);

    }

    private DefineSQLColumnsFormBean prepareDefineSQLColumnsFormBean() {

        DefineSQLColumnsFormBean defineSQLColumnsFormBean = new DefineSQLColumnsFormBean();

        return prepareDefineSQLColumnsFormBean(defineSQLColumnsFormBean);
    }

    private DefineSQLColumnsFormBean prepareDefineSQLColumnsFormBean(DefineSQLColumnsFormBean defineSQLColumnsFormBean) {

        if (defineSQLColumnsFormBean == null) {
            return prepareDefineSQLColumnsFormBean();
        }

        Map<String, Type> dataTypes = new LinkedHashMap<String, Type>();

        for (Type type : Type.values()) {
            dataTypes.put(type.toString(), type);
        }

        defineSQLColumnsFormBean.setDataTypes(dataTypes);

        return defineSQLColumnsFormBean;
    }

    private DefineOutputTableFormBean prepareDefineOutputTableFormBean() {

        DefineOutputTableFormBean defineOutputTableFormBean = new DefineOutputTableFormBean();

        defineOutputTableFormBean.setNodeName(NODE_NAME.CONFIGURABLE);
        defineOutputTableFormBean.setOutputTableOption(CREATE_NEW);

        return prepareDefineOutputTableFormBean(defineOutputTableFormBean);
    }

    private DefineOutputTableFormBean prepareDefineOutputTableFormBean(DefineOutputTableFormBean defineOutputTableFormBean) {

        if (defineOutputTableFormBean == null) {
            return prepareDefineOutputTableFormBean();
        }

        Map<String, NODE_NAME> nodeNameTypes = new LinkedHashMap<String, NODE_NAME>();

        for (NODE_NAME nodeName : NODE_NAME.values()) {
            nodeNameTypes.put(nodeName.toString(), nodeName);
        }

        defineOutputTableFormBean.setNodeNameTypes(nodeNameTypes);

        List<String> existingTableNames = new ArrayList<String>();
        existingTableNames.add(DEFAULT_SELECT);

        List<String> availableOutputTables = dataCollectionService.listAvailableOutputTables();
        if (availableOutputTables != null && !availableOutputTables.isEmpty()) {
            existingTableNames.addAll(availableOutputTables);
        }

        defineOutputTableFormBean.setExistingTablesNames(existingTableNames);

        Map<String, String> databaseDataTypes = new HashMap<String, String>();

        for (DataColumnType type : DataColumnType.values()) {
            databaseDataTypes.put(type.getTypeCode() + "", type.getName());
        }

        defineOutputTableFormBean.setDatabaseDataTypes(databaseDataTypes);

        Map<String, Type> dataTypes = new LinkedHashMap<String, Type>();

        for (Type type : Type.values()) {
            dataTypes.put(type.toString(), type);
        }
        defineOutputTableFormBean.setDataTypes(dataTypes);

        return defineOutputTableFormBean;
    }

    private DefineDBExtractionSQL prepareDefineDBExtractionSQL() {

        DefineDBExtractionSQL defineDBExtractionSQL = new DefineDBExtractionSQL();

        defineDBExtractionSQL.setSelectedDBType(DBType.ORACLE);

        return prepareDefineDBExtractionSQL(defineDBExtractionSQL);
    }

    private DefineDBExtractionSQL prepareDefineDBExtractionSQL(DefineDBExtractionSQL defineDBExtractionSQL) {

        if (defineDBExtractionSQL == null) {
            return prepareDefineDBExtractionSQL();
        }

        Map<String, DBType> dataBaseTypes = new LinkedHashMap<String, DBType>();

        for (DBType type : DBType.values()) {
            dataBaseTypes.put(type.toString(), type);
        }

        defineDBExtractionSQL.setDbTypes(dataBaseTypes);

        return defineDBExtractionSQL;
    }

    /**
     * @param sourceDataFormBean Contains user input from extract data step
     * @return Populated Extract Source Data Bean
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private ExtractSourceColumnFormBean prepareExtractSourceColumnFormBean(ExtractSourceDataFormBean sourceDataFormBean,
            String uploadedFileId) throws IOException {

        ExtractSourceColumnFormBean sourceColumnFormBean = new ExtractSourceColumnFormBean();

        // Build
        boolean useHeaders = sourceDataFormBean.getHeader() != Header.DONT_USE;

        int ignoreLines
                = StringUtils.isEmpty(sourceDataFormBean.getIgnoreLines()) ? 0 : Integer.parseInt(sourceDataFormBean.getIgnoreLines());

        List<String> lines = (List<String>) cacheService.get(TEXT_FILE_CACHE, uploadedFileId);

        String header = null;
        String sampleValueString;

        String delimiter;

        if (Delimiter.OTHER.equals(sourceDataFormBean.getDelimiter())) {
            delimiter = sourceDataFormBean.getOtherDelimiter();
        } else {
            delimiter = sourceDataFormBean.getDelimiter().getValue();
        }

        if (useHeaders && !lines.isEmpty() && lines.size() > ignoreLines) {
            header = lines.get(ignoreLines);
            sampleValueString = lines.get(ignoreLines + 1);
        } else {
            sampleValueString = lines.get(ignoreLines);
        }
        if (!lines.isEmpty()) {
            List<String> sampleValues;
            List<String> sampleHeaders;

            if (StringUtils.isEmpty(sampleValueString) || StringUtils.isWhitespace(sampleValueString)) {
                sampleValues = Arrays.asList();
            } else {
                sampleValues = Arrays.asList(sampleValueString.split(delimiter));
            }

            if (useHeaders && header != null && StringUtils.isNotEmpty(header) && !StringUtils.isWhitespace(sampleValueString)) {
                sampleHeaders = Arrays.asList(header.split(delimiter));
            } else {
                sampleHeaders = Arrays.asList();
            }

            List<SourceColumn> sourceColumns = new ArrayList<SourceColumn>(sampleValues.size());

            int index = 0;
            String name;
            for (String value : sampleValues) {

                if (sampleHeaders.isEmpty() || sampleHeaders.size() <= index) {
                    name = "Column" + (index + 1);
                } else {
                    name = sampleHeaders.get(index);
                }

                int COLUMN_NAME_MAX_LENGTH = 30;
                int VAR_CHAR_MAX_LENGTH = 4000;

                sourceColumns.add(new SourceColumn(
                        limitString(value, VAR_CHAR_MAX_LENGTH),
                        limitString(removeSpecialCharacters(name), COLUMN_NAME_MAX_LENGTH),
                        value,
                        Type.STRING,
                        VAR_CHAR_MAX_LENGTH + "", index, 0));

                index++;
            }

            sourceColumnFormBean.setColumns(sourceColumns);
        }

        return prepareExtractSourceColumnFormBean(sourceColumnFormBean);
    }

    private ExtractSourceColumnFormBean prepareExtractSourceColumnFormBean(ExtractSourceColumnFormBean sourceColumnFormBean) {

        if (sourceColumnFormBean == null) {
            sourceColumnFormBean = new ExtractSourceColumnFormBean();
        }

        Map<String, Type> dataTypes = new LinkedHashMap<String, Type>();

        for (Type type : Type.values()) {
            dataTypes.put(type.toString(), type);
        }
        sourceColumnFormBean.setDataTypes(dataTypes);

        Map<String, String> datePrecessionTypes = new LinkedHashMap<String, String>();
        for (DateColumnPrecession type : DateColumnPrecession.values()) {
            datePrecessionTypes.put(type.toString(), type.getDescription());
        }
        sourceColumnFormBean.setDateColumnPrecessionTypes(datePrecessionTypes);
        return sourceColumnFormBean;
    }

    private String removeSpecialCharacters(String in) {
        if (StringUtils.isEmpty(in)) {
            return in;
        }

        return in.trim().replaceAll("[-+.^:,$\\/]", "");
    }

    private String limitString(String in, int maxLength) {
        if (StringUtils.isEmpty(in)) {
            return in;
        }

        if (in.length() < maxLength) {
            return in;
        }

        return in.substring(0, maxLength);
    }

    private ModelAndView resetWizard() {

        return new ModelAndView(new RedirectView("/dataCollection/define/type", true));
    }

    private boolean isValidWizard(String wizardUUID, HttpSession session) {
        if (StringUtils.isEmpty(wizardUUID)) {
            return false;
        }

        String serializedBean = (String) session.getAttribute(wizardUUID);

        return !StringUtils.isEmpty(serializedBean);

    }

    /**
     * A workaround method added on 20.10.2013 to replace the double quotes to
     * avoid its corruption in the free marker pages.
     *
     * @author Alia.Adel
     * @param dataCollectionWizardFormBean
     * @return
     */
    private DataCollectionWizardFormBean updateDBExtractionSQL(
            DataCollectionWizardFormBean dataCollectionWizardFormBean) {
        if (DB.equals(dataCollectionWizardFormBean.getDataCollectionTypeFormBean().getDataCollectionType())) {
            dataCollectionWizardFormBean.getDefineDBExtractionSQL().setQuery(
                    dataCollectionWizardFormBean.getDefineDBExtractionSQL().getQuery().replaceAll("\"", "&quot;")
            );
        }
        return dataCollectionWizardFormBean;
    }

    //added by Awad 
    // CMT DashBorad Configuration
    private void prepareDataCollectionMappingFormBean(DefineDataCollectionMappingFormBean defineDataCollectionMappingFormBean, List<SourceColumn> columns, List<SourceColumn> sqlColumns, String dataCollectionName) {

        List<SourceColumn> outputColumns = defineDataCollectionMappingFormBean.getOutputColumns();

        Map<String, String> dataCollectionKpiTypes = new LinkedHashMap<String, String>();

        if (columns != null && !columns.isEmpty()) {
            for (int i = 0; i < columns.size(); i++) {
                for (int j = 0; j < outputColumns.size(); j++) {
                    if (columns.get(i).getName().equalsIgnoreCase(outputColumns.get(j).getName())) {
                        columns.get(i).setKpiValue(outputColumns.get(j).getKpiValue());
                    }
                }
            }
        }

        if (sqlColumns != null && !sqlColumns.isEmpty()) {
            for (int i = 0; i < sqlColumns.size(); i++) {
                for (int j = 0; j < outputColumns.size(); j++) {
                    if (sqlColumns.get(i).getName().equalsIgnoreCase(outputColumns.get(j).getName())) {
                        sqlColumns.get(i).setKpiValue(outputColumns.get(j).getKpiValue());
                    }
                }
            }
        }

        List<DataCollectionColumnKpiType> columnsKpiTypes = new ArrayList<DataCollectionColumnKpiType>();

        columnsKpiTypes.addAll(Arrays.asList(DataCollectionColumnKpiType.values()));

        defineDataCollectionMappingFormBean.setKpiTypesColumns(columnsKpiTypes);

        List<KPIType> kpiTypes = dataCollectionService.listAllKpi();

        List<KPIType> kPIList = dataCollectionService.getKpiTypeByDataCollectionName(dataCollectionName);

        for (KPIType type : kpiTypes) {
            dataCollectionKpiTypes.put(String.valueOf(type.getId()), type.getName());
        }

        defineDataCollectionMappingFormBean.setDataCollectionKpiTypes(dataCollectionKpiTypes);

        if (kPIList != null && !kPIList.isEmpty()) {
            defineDataCollectionMappingFormBean.setKpiType(String.valueOf(kPIList.get(0).getId()));
        }
    }
    // End Awad

}
