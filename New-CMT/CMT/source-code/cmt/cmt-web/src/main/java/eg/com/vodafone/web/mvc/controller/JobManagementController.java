package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.Job;
import eg.com.vodafone.model.SSHResult;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.ZoneUtilization;
import eg.com.vodafone.model.enums.JobExecutionPeriod;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.DCJobService;
import eg.com.vodafone.service.impl.JobExecutionService;
import eg.com.vodafone.service.utils.SSHUtilityService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.component.grid.GridUserInterface;
import eg.com.vodafone.web.mvc.formbean.ForceJobFormBean;
import eg.com.vodafone.web.mvc.formbean.JobModel;
import eg.com.vodafone.web.mvc.formbean.grid.GridBean;
import eg.com.vodafone.web.mvc.model.JobAction;
import eg.com.vodafone.web.mvc.model.JobLevel;
import eg.com.vodafone.web.mvc.model.searchcriteria.JobSearchCriteria;
import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import eg.com.vodafone.web.mvc.model.searchcriteria.SortField;
import eg.com.vodafone.web.mvc.model.searchresult.JobSearchResult;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import eg.com.vodafone.web.mvc.util.GridConstants;
import eg.com.vodafone.web.mvc.util.GridUtils;
import eg.com.vodafone.web.mvc.validator.ForceJobValidator;
import eg.com.vodafone.web.mvc.validator.JobValidator;
import eg.com.vodafone.web.mvc.validator.JobValidatorForUtilization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

/**
 * @author marwa.goda
 * @since 4/21/13
 */
@Controller
@RequestMapping("/jobManagement/*")
public class JobManagementController extends AbstractGridBasedController {

    private static final String ADD_JOB_SUCCESS_MESSAGE = "<div class=\"SuccessMsg\">The new job has been defined successfully!</div>";
    private static final String ADD_JOB_ERROR_MESSAGE = "<div class=\"ErrorMsg\">Job wasn't defined. %s</div>";
    private static final String UPDATE_JOB_SUCCESS_MSG = "<div class=\"SuccessMsg\">The job has been updated successfully!</div>";
    private static final String UPDATE_JOB_ERROR_MSG = "<div class=\"ErrorMsg\">Job wasn't updated. %s</div>";
    private static final String FORCE_JOB_SUCCESS_MSG = "<div class=\"SuccessMsg\">Job execution has completed.</div>";
    private static final String FORCE_JOB_ERROR_MSG = "<div class=\"ErrorMsg\">Job couldn't be forced now. %s</div>";
    private static final String FORCE_JOB_TIMEOUT_MSG
            = "<div class=\"SuccessMsg\">Job execution has started & currently working in the background.</div>";
    private static final String DELETE_SUCCESS_MSG = "<div class=\"SuccessMsg\">The job has been deleted successfully!</div>";
    private static final String DELETE_ERROR_MSG = "<div class=\"ErrorMsg\">Job hasn't been deleted. %s</div>";
    private static final String DELETE_UNFOUND_MSG = "<div class=\"ErrorMsg\">The job that you want to delete doesn't exist</div>";
    private static final String JOB_DETAILS_VIEW = "jobDetails";
    private static final String MANAGE_JOBS_VIEW = "manageJobs";
    private static final String NODE_LIST_VIEW = "nodeList";
    private static final String SYSTEM_NODES_MODEL_ATTRIBUTE = "nodeList";
    private static final String JOB_MODEL_ATTRIBUTE = "job";
    private static final String SEARCH_CRITERIA_MODEL_ATTRIBUTE = "searchCriteria";
    private static final String NODE_LIST_MODEL_ATTRIBUTE = "nodeList";
    private static final String JOB_NOT_FOUND_MSG = "<div class=\"ErrorMsg\">Job not found!</div>";
    private static final String PAGE_TITLE_MODEL_ATTRIBUTE = "pageTitle";
    private static final String PAGE_TITLE_UPDATE = "Update Job";
    private static final String PAGE_TITLE_ADD = "Define New Job";
    private static final String FORCE_JOB_MODEL_ATTRIBUTE = "formBean";
    private static final String FORCE_JOB_PAGE = "forceJobExecution";
    private final static Logger LOGGER = LoggerFactory.getLogger(LogManagerController.class);
    private static final String GRID_XML_CONFIG = "eg/com/vodafone/grids/job-grid.xml";

    //Added by Awad
    private static final String JOB_MODEL_ZONE_UTILIZATION = "zonesUtilizationList";

    @Autowired
    private DCJobService dcJobService;
    @Autowired
    private DataCollectionSystemServiceInterface dataCollectionService;
    @Autowired
    private JobExecutionService jobExecutionService;
    @Autowired
    private JobValidator jobValidator;
    @Autowired
    private ForceJobValidator forceJobValidator;

    @Autowired
    JobValidatorForUtilization jobValidatorForUtilization;

    @RequestMapping(value = "jobDetails", method = RequestMethod.GET)
    public ModelAndView initAddNewJob() {
        ModelAndView modelAndView = new ModelAndView();
        JobModel jobModel = new JobModel();
        jobModel.setJob(new Job());
        jobModel.setJobAction(JobAction.SAVE_JOB.toString());

        List<ZoneUtilization> zonesUtilization = new ArrayList<ZoneUtilization>();
        modelAndView = populateJobModelAndView(JOB_DETAILS_VIEW, jobModel, zonesUtilization);

        modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, new ArrayList<String>());
        modelAndView.addObject(PAGE_TITLE_MODEL_ATTRIBUTE, PAGE_TITLE_ADD);
        return modelAndView;
    }

    private ModelAndView populateJobModelAndView(String viewName, JobModel jobModel, List<ZoneUtilization> zonesUtilization) {

        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject(JOB_MODEL_ATTRIBUTE, jobModel);
        modelAndView.addObject(JOB_MODEL_ZONE_UTILIZATION, zonesUtilization);
        return modelAndView;
    }

    @RequestMapping(value = "saveNewJob" , method = RequestMethod.POST)
    public ModelAndView saveNewJob(@ModelAttribute("job") @Valid JobModel jobModel, BindingResult errors) {

        ModelAndView modelAndView;
        jobModel.setJobAction(JobAction.SAVE_JOB.toString());

        jobValidator.validate(jobModel, errors);
        if (errors.hasErrors()) {
            modelAndView = populateJobModelAndView(JOB_DETAILS_VIEW, jobModel , new ArrayList<ZoneUtilization>());
            modelAndView.addObject(PAGE_TITLE_MODEL_ATTRIBUTE, PAGE_TITLE_ADD);
            if (StringUtils.hasText(jobModel.getJobLevel())) {
                if (jobModel.getJobLevel().equals(JobLevel.SYSTEM_LEVEL.name())) {
                    jobModel.getJob().setSystemNode(null);
                } else if (jobModel.getJobLevel().equals(JobLevel.NODE_LEVEL.name())) {
                    modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE,
                            getSystemNodes(jobModel.getJob().getSystemName()));
                }
            }
            return modelAndView;

        } else {
            if (StringUtils.hasText(jobModel.getJobLevel())
                    && jobModel.getJobLevel().equals(JobLevel.SYSTEM_LEVEL.name())) {
                jobModel.getJob().setSystemNode(null);
            }

            SSHResult result = dcJobService.addJob(jobModel.getJob());

            modelAndView = new ModelAndView(MANAGE_JOBS_VIEW);
            if (result.isSuccess()) {

                modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                        ADD_JOB_SUCCESS_MESSAGE);
            } else {
                modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                        String.format(ADD_JOB_ERROR_MESSAGE, result.getErrorMessage()));
            }
        }

        JobSearchCriteria jobSearchCriteria = new JobSearchCriteria();
        prepareGridData(modelAndView, jobSearchCriteria);

        return modelAndView;

    }

    // Added by Awad
    // Job utilization 
    @RequestMapping(value = "getZoneUtilization" , method = RequestMethod.POST)
    public ModelAndView getZoneUtilization(@ModelAttribute("job") JobModel jobModel, BindingResult errors) {

        ModelAndView modelAndView;
        List<ZoneUtilization> zonesUtilization = new ArrayList<ZoneUtilization>();
        
        jobModel.setJobAction(JobAction.SAVE_JOB.toString());
        jobValidatorForUtilization.validate(jobModel, errors);

        if (!errors.hasErrors()) {
            zonesUtilization = dcJobService.getZoneUtilization(jobModel.getJob().getCronExpression());
        }

        modelAndView = populateJobModelAndView(JOB_DETAILS_VIEW, jobModel, zonesUtilization);
        modelAndView.addObject(PAGE_TITLE_MODEL_ATTRIBUTE, PAGE_TITLE_ADD);
        if (StringUtils.hasText(jobModel.getJobLevel())) {
            if (jobModel.getJobLevel().equals(JobLevel.SYSTEM_LEVEL.name())) {
                jobModel.getJob().setSystemNode(null);
            } else if (jobModel.getJobLevel().equals(JobLevel.NODE_LEVEL.name())) {
                modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE,
                        getSystemNodes(jobModel.getJob().getSystemName()));
            }
        }
        return modelAndView;

    }

    @RequestMapping(value = "initUpdateJob", method = RequestMethod.POST)
    public ModelAndView initUpdateJob(@ModelAttribute("jobName") String jobName) {

        ModelAndView modelAndView = null;
        JobModel jobModel = new JobModel();
        Job job = dcJobService.getJobByName(jobName);
        if (job != null) {
            jobModel.setJob(job);
            jobModel.setJobAction(JobAction.UPDATE_JOB.toString());
            modelAndView = populateJobModelAndView(JOB_DETAILS_VIEW, jobModel , new ArrayList<ZoneUtilization>());

            List<String> systemNodes = getSystemNodes(job.getSystemName());
            if (systemNodes != null && !systemNodes.isEmpty()) {
                modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, systemNodes);
            }

        } else {
            modelAndView = new ModelAndView(MANAGE_JOBS_VIEW);
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, JOB_NOT_FOUND_MSG);
        }

        modelAndView.addObject(PAGE_TITLE_MODEL_ATTRIBUTE, PAGE_TITLE_UPDATE);
        return modelAndView;
    }

    @RequestMapping(value = "manageJobs", method = RequestMethod.GET)
    public ModelAndView manageJobs() {
        ModelAndView modelAndView = new ModelAndView(MANAGE_JOBS_VIEW);
        JobSearchCriteria jobSearchCriteria = new JobSearchCriteria();

        //prepare Grid
        prepareGridData(modelAndView, jobSearchCriteria);

        modelAndView.addObject(SEARCH_CRITERIA_MODEL_ATTRIBUTE, jobSearchCriteria);
        return modelAndView;
    }

    @RequestMapping(value = "updateJob", method = RequestMethod.POST)
    public ModelAndView updateJob(@ModelAttribute("job") @Valid JobModel jobModel, BindingResult errors) {
        ModelAndView modelAndView;

        jobModel.setJobAction(JobAction.UPDATE_JOB.toString());
        jobValidator.validate(jobModel, errors);
        if (errors.hasErrors()) {
            modelAndView = populateJobModelAndView(JOB_DETAILS_VIEW, jobModel , new ArrayList<ZoneUtilization>());
            modelAndView.addObject(PAGE_TITLE_MODEL_ATTRIBUTE, PAGE_TITLE_UPDATE);
            if (StringUtils.hasText(jobModel.getJobLevel())) {
                if (jobModel.getJobLevel().equals(JobLevel.SYSTEM_LEVEL.name())) {
                    jobModel.getJob().setSystemNode(null);
                }

                List<String> systemNodes = getSystemNodes(jobModel.getJob().getSystemName());
                if (systemNodes != null && !systemNodes.isEmpty()) {
                    modelAndView.addObject(SYSTEM_NODES_MODEL_ATTRIBUTE, systemNodes);
                }
            }
            return modelAndView;
        } else {
            if (StringUtils.hasText(jobModel.getJobLevel())
                    && jobModel.getJobLevel().equals(JobLevel.SYSTEM_LEVEL.name())) {
                jobModel.getJob().setSystemNode(null);
            }

            SSHResult result = dcJobService.updateJob(jobModel.getJob());
            modelAndView = new ModelAndView(MANAGE_JOBS_VIEW);
            if (result.isSuccess()) {

                modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY,
                        UPDATE_JOB_SUCCESS_MSG);
            } else {

                modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                        String.format(UPDATE_JOB_ERROR_MSG, result.getErrorMessage()));
            }
        }

        JobSearchCriteria jobSearchCriteria = new JobSearchCriteria();
        prepareGridData(modelAndView, jobSearchCriteria);

        return modelAndView;
    }

    @RequestMapping(value = "deleteJob", method = RequestMethod.POST)
    public ModelAndView deleteJob(@ModelAttribute("jobName") String jobName) {
        ModelAndView modelAndView;

        Job job = dcJobService.getJobByName(jobName);
        modelAndView = new ModelAndView(MANAGE_JOBS_VIEW);
        if (null != job) {
            SSHResult result = dcJobService.deleteJob(jobName);
            if (result.isSuccess()) {

                modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY, DELETE_SUCCESS_MSG);
            } else {

                modelAndView.addObject(CMTConstants.ERROR_MSG_KEY,
                        String.format(DELETE_ERROR_MSG, result.getErrorMessage()));
            }
        } else {

            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, DELETE_UNFOUND_MSG);
        }

        JobSearchCriteria jobSearchCriteria = new JobSearchCriteria();
        prepareGridData(modelAndView, jobSearchCriteria);

        return modelAndView;
    }

    @RequestMapping(value = "getSystemNodes", method = RequestMethod.GET)
    public ModelAndView populateSystemNodes(@ModelAttribute(value = "systemName") String systemName) {

        ModelAndView modelAndView = new ModelAndView(NODE_LIST_VIEW);
        List<String> nodeNames = getSystemNodes(systemName);
        JobModel jobModel = new JobModel();
        jobModel.setJob(new Job());
        modelAndView.addObject(NODE_LIST_MODEL_ATTRIBUTE, nodeNames);
        modelAndView.addObject(JOB_MODEL_ATTRIBUTE, jobModel);
        return modelAndView;

    }

    @RequestMapping(value = "list", method = {RequestMethod.GET})
    public ModelAndView listJobs(@Valid JobSearchCriteria searchCriteria, BindingResult result) {

        ModelAndView modelAndView = new ModelAndView(MANAGE_JOBS_VIEW);
        prepareGridData(modelAndView, searchCriteria);

        if (result.hasErrors()) {
            return modelAndView;
        }

        return modelAndView;
    }

    @RequestMapping(value = "goToForceJobExecution", method = RequestMethod.POST)
    public ModelAndView goToForceJobExecution(@ModelAttribute("jobName") String jobName) {
        if (StringUtils.hasText(jobName)) {
            ModelAndView modelAndView
                    = new ModelAndView(FORCE_JOB_PAGE);
            ForceJobFormBean formBean = new ForceJobFormBean();
            Job job = dcJobService.getJobByName(jobName);
            if (job != null && StringUtils.hasText(job.getJobName())) {
                formBean.setJob(job);
                modelAndView.addObject(FORCE_JOB_MODEL_ATTRIBUTE, formBean);
                return modelAndView;
            } else {
                throw new GenericException("No job found with name: " + jobName);
            }
        } else {
            throw new GenericException("No job found with name: " + jobName);
        }
    }

    @RequestMapping(value = "forceJobExecution", method = RequestMethod.POST)
    public ModelAndView forceJobExecution(
            @Valid @ModelAttribute(value = FORCE_JOB_MODEL_ATTRIBUTE) ForceJobFormBean formBean,
            BindingResult result) {
        ModelAndView modelAndView
                = new ModelAndView(FORCE_JOB_PAGE);

        forceJobValidator.validate(formBean, result);
        modelAndView.addObject(FORCE_JOB_MODEL_ATTRIBUTE, formBean);
        if (result.hasErrors()) {
            return modelAndView;
        }

        modelAndView = new ModelAndView(MANAGE_JOBS_VIEW);
        JobSearchCriteria jobSearchCriteria = new JobSearchCriteria();
        prepareGridData(modelAndView, jobSearchCriteria);

        String fromDate = formBean.getFromDate()
                + ((formBean.getJob().getExecutionPeriod().equals(JobExecutionPeriod.HOURLY)) ? ':'
                + String.format("%02d", Integer.parseInt(formBean.getFromHour())) : "");

        String toDate = formBean.getToDate()
                + ((formBean.getJob().getExecutionPeriod().equals(JobExecutionPeriod.HOURLY)) ? ':'
                + String.format("%02d", Integer.parseInt(formBean.getToHour())) : "");

        LOGGER.debug("Final dates:\nFrom Date:{}\nTo date:{}", fromDate, toDate);
        SSHResult resultForce = dcJobService.forceJobExecution(formBean.getJob(),
                fromDate, toDate);
        if (resultForce.isSuccess()) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY, FORCE_JOB_SUCCESS_MSG);
        } else if (resultForce.getErrorMessage().equals(SSHUtilityService.TIMEOUT_MESSAGE)) {
            modelAndView.addObject(CMTConstants.SUCCESS_MSG_KEY, String.format(FORCE_JOB_TIMEOUT_MSG,
                    resultForce.getErrorMessage()));
        } else {
            modelAndView.addObject(CMTConstants.ERROR_MSG_KEY, String.format(FORCE_JOB_ERROR_MSG,
                    resultForce.getErrorMessage()));
        }

        return modelAndView;
    }

    @ModelAttribute(value = "systemNames")
    private List<String> getSystemNames() {
        List<String> systemNamesList = dataCollectionService.listAllSystems();
        return systemNamesList;
    }

    @ModelAttribute(value = "systemNodes")
    private List<String> getSystemNodes() {
        return getSystemNodes(null);
    }

    private List<String> getSystemNodes(String systemName) {

        List<String> nodeNamesList = new ArrayList<String>();

        if (systemName != null) {
            nodeNamesList = this.listNodeNames(dataCollectionService.getSystemNodes(systemName, false));
        }

        Collections.sort(nodeNamesList);

        return nodeNamesList;
    }

    @ModelAttribute(value = "zones")
    private List<String> getZones() {
        List<String> zones;
        zones = jobExecutionService.getZonesID();
        return zones;

    }

    @ModelAttribute(value = "executionPeriods")
    private Map<String, String> getExecutionPeriods() {
        Map<String, String> executionPeriodsMap = new HashMap<String, String>(2);
        executionPeriodsMap.put(JobExecutionPeriod.HOURLY.toString(), JobExecutionPeriod.HOURLY.toString());
        executionPeriodsMap.put(JobExecutionPeriod.DAILY.toString(), JobExecutionPeriod.DAILY.toString());
        return executionPeriodsMap;
    }

    @ModelAttribute(value = "jobLevels")
    private Map getJobLevels() {
        Map jobLevelsMap = new HashMap();
        jobLevelsMap.put(JobLevel.SYSTEM_LEVEL.toString(), JobLevel.SYSTEM_LEVEL.toString());
        jobLevelsMap.put(JobLevel.NODE_LEVEL.toString(), JobLevel.NODE_LEVEL.toString());
        return jobLevelsMap;
    }

    private JobExecutionPeriod mapExecutionPeriod(String executionPeriod) {
        return JobExecutionPeriod.valueOf(executionPeriod);
    }

    private List<String> listNodeNames(List<VNode> nodes) {
        List<String> nodeNamesList;
        nodeNamesList = new ArrayList<String>();

        for (VNode node : nodes) {
            nodeNamesList.add(node.getName());
        }
        return nodeNamesList;
    }

    @SuppressWarnings("unchecked")
    private void prepareGridData(ModelAndView modelAndView,
            JobSearchCriteria jobSearchCriteria) {
        int jobsCounts = this.getCount(jobSearchCriteria);
        if (jobsCounts > 0) {
            GridBean gridBean = GridUtils.createGridBean(jobsCounts);
            gridBean.setSearchCriteria(jobSearchCriteria);
            GridUserInterface viewTaskGrid = GridUtils.createGridUserInterface(GRID_XML_CONFIG);
            gridBean.setGridUserInterface(viewTaskGrid);
            GridUtils.populateSortColumns(gridBean);

            List<JobSearchResult> jobSearchResults
                    = (List<JobSearchResult>) this.getResultsList(jobSearchCriteria, 1,
                            GridConstants.GRID_DEFAULT_NO_ROWS_PER_PAGE);

            modelAndView.addObject(GRID_RESULTS_MODEL_NAME, jobSearchResults);
            modelAndView.addObject(GRID_MODEL_NAME, gridBean);
            modelAndView.addObject(SEARCH_CRITERIA, jobSearchCriteria);

        } else {
            modelAndView.addObject(SEARCH_CRITERIA, jobSearchCriteria);
        }
    }

    @Override
    public int getCount(Object... args) {
        return dcJobService.getAllJobNameCount(((JobSearchCriteria) args[0]).getKeyword());
    }

    @Override
    public List<JobSearchResult> getResultsList(
            SearchCriteria searchCriteria, int startRowIndex, int endRowIndex) {
        SortField sortField = null;
        if ((searchCriteria).getSortFields() != null
                && !(searchCriteria).getSortFields().isEmpty()) {
            sortField = (searchCriteria).getSortFields().get(0);
        }
        List<String> jobList
                = dcJobService.getAllJobNameList(((JobSearchCriteria) searchCriteria).getKeyword(),
                        ((sortField != null) ? sortField.getOrder() : "ASC"),
                        startRowIndex, endRowIndex);

        return mapSearchResultToGrid(jobList);

    }

    /**
     * @param searchResult
     * @return
     */
    private List<JobSearchResult> mapSearchResultToGrid(List<String> searchResult) {
        if (searchResult != null && !searchResult.isEmpty()) {

            List<JobSearchResult> jobSearchResults = new ArrayList<JobSearchResult>();
            JobSearchResult jobSearchResult = new JobSearchResult();

            for (String jobName : searchResult) {
                jobSearchResult.setJobName(jobName);
                jobSearchResults.add(jobSearchResult);
                jobSearchResult = new JobSearchResult();
            }
            return jobSearchResults;
        }
        return null;
    }

}
