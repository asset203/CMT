package eg.com.vodafone.web.mvc.controller;

import eg.com.vodafone.model.SystemEvent;
import eg.com.vodafone.model.enums.EventLevel;
import eg.com.vodafone.service.DataCollectionSystemServiceInterface;
import eg.com.vodafone.service.impl.SystemEventService;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.SystemEventFormBean;
import eg.com.vodafone.web.mvc.validator.SystemEventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static eg.com.vodafone.web.mvc.util.CMTConstants.*;

/**
 * @author Alia.Adel
 * @since 03/03/2013
 */
@Controller
@RequestMapping("/systemEvents/*")
public class SystemEventsController extends AbstractController {

  @Autowired
  private SystemEventService systemEventService;

  @Autowired
  private DataCollectionSystemServiceInterface dataCollectionSystemService;

  @Autowired
  private SystemEventValidator systemEventValidator;

  private final static Logger logger = LoggerFactory.getLogger(SystemEventsController.class);

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
    new SimpleDateFormat(DATE_DB_PATTERN, Locale.US);
  private static final SimpleDateFormat VIEW_SIMPLE_DATE_FORMAT =
    new SimpleDateFormat("MM_dd_yyyy", Locale.US);

  private static final String SYSTEM_LIST = "systemList";
  private static final String FORM_BEAN = "formBean";
  private static final String PAGE_TITLE = "pageTitle";
  private static final String FORM_ACTION = "formAction";
  private static final String EVENT_LEVELS = "eventLevels";
  private static final String ACTION_VIEW_EVENTS = "viewEvents";
  private static final String PAGE_EDIT_ADD_EVENT = "editAddEventDetails";


  @RequestMapping(value = ACTION_VIEW_EVENTS)
  public ModelAndView viewEvents() throws GenericException {

    logger.debug("Entered View events");

    ModelAndView modelAndView = new ModelAndView(ACTION_VIEW_EVENTS);

    List<String> systemNames = getAllSystems();

    //Fill Model&View
    modelAndView.addObject(SYSTEM_LIST, systemNames);
    modelAndView.addObject(FORM_BEAN, new SystemEventFormBean());


    return modelAndView;
  }

  @RequestMapping(value = "viewSelectedSystemEvents", method = RequestMethod.POST)
  public ModelAndView viewSelectedSystemEvents(
    @ModelAttribute(value = FORM_BEAN) SystemEventFormBean systemEventFormBean,
    BindingResult result) throws GenericException {

    ModelAndView modelAndView = new ModelAndView(ACTION_VIEW_EVENTS);
    modelAndView.addObject(SYSTEM_LIST, getAllSystems());

    systemEventValidator.validateSystem(systemEventFormBean, result);

    modelAndView.addObject(FORM_BEAN, systemEventFormBean);
    if (result.hasErrors()) {
      return modelAndView;
    }

    logger.debug("Entered View selected system events for System {}",
      systemEventFormBean.getSelectedSystem());

    modelAndView.addObject(FORM_BEAN, viewSystemEvents(systemEventFormBean.getSelectedSystem(), new Date()));


    return modelAndView;
  }

  @RequestMapping(value = "loadEventDetails/{systemName}/{date}", method = RequestMethod.GET)
  public ModelAndView loadEventDetails(@PathVariable String systemName, @PathVariable String date) {
    ModelAndView modelAndView = new ModelAndView("systemEventDetails");
    if (StringUtils.hasText(systemName)
      && StringUtils.hasText(date)) {
      SystemEvent systemEvent = null;
      try {
        synchronized (VIEW_SIMPLE_DATE_FORMAT) {
          systemEvent = systemEventService.getSystemEvent(systemName,
            VIEW_SIMPLE_DATE_FORMAT.parse(date));
        }
        SystemEventFormBean systemEventFormBean = new SystemEventFormBean();
        systemEventFormBean.setSelectedSystemEvent(systemEvent);
        modelAndView.addObject(FORM_BEAN, systemEventFormBean);
      } catch (ParseException e) {
        logger.error("Parse Exception while trying to parse date {}", date, e);
      }

    }

    return modelAndView;
  }

  @RequestMapping(value = "goToEditEvent", method = RequestMethod.POST)
  public ModelAndView goToEditSystemEvent(
    @ModelAttribute(value = FORM_BEAN) SystemEventFormBean systemEventFormBean) {
    if (systemEventFormBean == null || (systemEventFormBean != null
      && systemEventFormBean.getSelectedSystemEvent() == null)) {
      throw new GenericException("Empty system event form bean");
    } else {
      logger.debug("going to edit system event page, values passed:" +
        "\nSystem Name:{}\nEvent details:{}\nSystem level:{}\nDate:{}",
        new Object[]{systemEventFormBean.getSelectedSystemEvent().getSystemName(),
          systemEventFormBean.getSelectedSystemEvent().getCommentDesc(),
          systemEventFormBean.getSelectedSystemEvent().getLevelType(),
          systemEventFormBean.getSelectedSystemEvent().getDateTime()});
      synchronized (SIMPLE_DATE_FORMAT) {
        systemEventFormBean.setSelectedSystemEventDateStr(SIMPLE_DATE_FORMAT.format(
          systemEventFormBean.getSelectedSystemEvent().getDateTime()));
      }
    }

    ModelAndView modelAndView = new ModelAndView(PAGE_EDIT_ADD_EVENT);
    modelAndView.addObject(FORM_BEAN, systemEventFormBean);
    modelAndView.addObject(EVENT_LEVELS, EventLevel.values());
    modelAndView.addObject(PAGE_TITLE, "Edit Existing Event");
    modelAndView.addObject(FORM_ACTION, EDIT_SYSTEM_EVENT);
    return modelAndView;
  }

  @RequestMapping(value = "editSystemEvent", method = RequestMethod.POST)
  public ModelAndView editSystemEvent(
    @Valid @ModelAttribute(value = FORM_BEAN) SystemEventFormBean systemEventFormBean,
    BindingResult result) {
    if (systemEventFormBean == null ||
      (systemEventFormBean != null
        && systemEventFormBean.getSelectedSystemEvent() == null)) {
      throw new GenericException("Form bean is null");
    }

    ModelAndView modelAndView = new ModelAndView(PAGE_EDIT_ADD_EVENT);
    modelAndView.addObject(FORM_BEAN, systemEventFormBean);
    modelAndView.addObject(EVENT_LEVELS, EventLevel.values());
    modelAndView.addObject(PAGE_TITLE, "Edit Existing Event");
    modelAndView.addObject(FORM_ACTION, EDIT_SYSTEM_EVENT);
    systemEventValidator.validate(systemEventFormBean, result);

    if (result.hasErrors()) {
      return modelAndView;
    }

    return editSystemEventAction(modelAndView, systemEventFormBean);
  }

  @RequestMapping(value = "goToAddEvent", method = RequestMethod.POST)
  public ModelAndView goToAddSystemEvent(
    @ModelAttribute(value = FORM_BEAN) SystemEventFormBean systemEventFormBean) {
    if (systemEventFormBean != null
      && StringUtils.hasText(systemEventFormBean.getSelectedSystem())) {

      logger.debug("System passed to add an event is: {}",
        new Object[]{systemEventFormBean.getSelectedSystem()});

      systemEventFormBean.setSelectedSystemEvent(new SystemEvent());
      systemEventFormBean.getSelectedSystemEvent().setSystemName(
        systemEventFormBean.getSelectedSystem());

    } else {
      throw new GenericException("No system was passed with the add button");
    }
    ModelAndView modelAndView = new ModelAndView(PAGE_EDIT_ADD_EVENT);
    modelAndView.addObject(FORM_BEAN, systemEventFormBean);
    modelAndView.addObject(EVENT_LEVELS, EventLevel.values());
    modelAndView.addObject(PAGE_TITLE, "Add New Event");
    modelAndView.addObject(FORM_ACTION, ACTION_ADD_EVENT);
    return modelAndView;
  }

  @RequestMapping(value = ACTION_ADD_EVENT, method = RequestMethod.POST)
  public ModelAndView addSystemEvent(
    @Valid @ModelAttribute(value = FORM_BEAN) SystemEventFormBean systemEventFormBean,
    BindingResult result) {
    if (systemEventFormBean == null ||
      (systemEventFormBean != null
        && systemEventFormBean.getSelectedSystemEvent() == null)) {
      throw new GenericException("Form bean is null");
    }

    ModelAndView modelAndView = new ModelAndView(PAGE_EDIT_ADD_EVENT);
    modelAndView.addObject(FORM_BEAN, systemEventFormBean);
    modelAndView.addObject(EVENT_LEVELS, EventLevel.values());
    modelAndView.addObject(PAGE_TITLE, "Add New Event");
    modelAndView.addObject(FORM_ACTION, ACTION_ADD_EVENT);

    systemEventValidator.validate(systemEventFormBean, result);
      /**
       * This validation was commented by Alia.Adel on 2013.09.04 as per UAT comments
       */
    //systemEventValidator.validateDateValue(systemEventFormBean, ACTION_ADD_EVENT, result);

    if (result.hasErrors()) {
      return modelAndView;
    }


    /**
     * Alert the user that another event exists on the same day
     * & request his approval to replace the event.
     */
    logger.debug("Checking if another event exists on same day");
    SystemEvent systemEvent =
      systemEventService.getSystemEvent(systemEventFormBean.getSelectedSystemEvent().getSystemName(),
        systemEventFormBean.getSelectedSystemEvent().getDateTime());
    if (systemEvent != null) {
      systemEventFormBean.getSelectedSystemEvent().setId(systemEvent.getId());
      modelAndView.addObject(FORM_BEAN, systemEventFormBean);
      //Another Event exists on that day
      modelAndView.addObject("CONFIRM_EVENT_REPLACEMENT", systemEvent.getCommentDesc());
      return modelAndView;
    }

    modelAndView = new ModelAndView(ACTION_VIEW_EVENTS);
    modelAndView.addObject(SYSTEM_LIST, getAllSystems());

    if (StringUtils.hasText(systemEventFormBean.getSelectedSystemEvent().getSystemName())) {
      logger.debug("Values passed for new system event addition:\nSubject:{}\nLevel:{}\nDate:{}\nSystem name:{}",
        new Object[]{systemEventFormBean.getSelectedSystemEvent().getCommentDesc(),
          systemEventFormBean.getSelectedSystemEvent().getLevelType(),
          systemEventFormBean.getSelectedSystemEvent().getDateTime(),
          systemEventFormBean.getSelectedSystemEvent().getSystemName()});

      //add new system event
      int resultVal = systemEventService.saveEvent(systemEventFormBean.getSelectedSystemEvent());

      modelAndView.addObject(FORM_BEAN, viewSystemEvents(
        systemEventFormBean.getSelectedSystemEvent().getSystemName(), systemEventFormBean.getSelectedSystemEvent().getDateTime()));

      logger.debug("System Event addition result: {}", new Object[]{resultVal});

      if (resultVal == 1) {
        modelAndView.addObject(SUCCESS_MSG_KEY,
          "<div class=\"SuccessMsg\">Event has been added successfully</div>");
      } else {
        modelAndView.addObject(ERROR_MSG_KEY,
          "<div class=\"ErrorMsg\">The action has not been performed</div>");
      }

    } else {
      throw new GenericException("No system was submitted for search");
    }

    return modelAndView;
  }

  @RequestMapping(value = "confirmEventReplacement", method = RequestMethod.POST)
  public ModelAndView confirmEventReplacement(
    @Valid @ModelAttribute(value = FORM_BEAN) SystemEventFormBean systemEventFormBean, BindingResult result) {
    ModelAndView modelAndView = new ModelAndView(PAGE_EDIT_ADD_EVENT);
    modelAndView.addObject(FORM_BEAN, systemEventFormBean);
    modelAndView.addObject(EVENT_LEVELS, EventLevel.values());
    modelAndView.addObject(PAGE_TITLE, "Add New Event");
    modelAndView.addObject(FORM_ACTION, ACTION_ADD_EVENT);
    systemEventValidator.validate(systemEventFormBean, result);
    if (result.hasErrors()) {
      return modelAndView;
    }

    return editSystemEventAction(modelAndView, systemEventFormBean);
  }


  @RequestMapping(value = "deleteSystemEvent", method = RequestMethod.POST)
  public ModelAndView deleteSystemEvent(
    @Valid @ModelAttribute(value = FORM_BEAN) SystemEventFormBean systemEventFormBean,
    BindingResult result) {
    ModelAndView modelAndView = new ModelAndView(ACTION_VIEW_EVENTS);
    if (systemEventFormBean == null || (systemEventFormBean != null
      && systemEventFormBean.getSelectedSystemEvent() == null)) {
      throw new GenericException("Form bean passed is null");
    } else {
      logger.debug("Values received for deletion:\nSystem Name:{}\nEvent ID:{}",
        systemEventFormBean.getSelectedSystem(),
        systemEventFormBean.getSelectedSystemEvent().getId());

      if (StringUtils.hasText(systemEventFormBean.getSelectedSystem())
        && systemEventFormBean.getSelectedSystemEvent().getId() > 0) {
        int resultVal = systemEventService.deleteEvent(systemEventFormBean.getSelectedSystemEvent().getId());
        logger.debug("System Event deletion result: {}", new Object[]{resultVal});

        if (resultVal == 1) {
          modelAndView.addObject(SUCCESS_MSG_KEY,
            "<div class=\"SuccessMsg\">Event has been deleted successfully</div>");
        } else {
          logger.debug("Failed to delete system event");
          modelAndView.addObject(ERROR_MSG_KEY,
            "<div class=\"ErrorMsg\">The action has not been performed</div>");
        }

        modelAndView.addObject(SYSTEM_LIST, getAllSystems());
        modelAndView.addObject(FORM_BEAN, viewSystemEvents(systemEventFormBean.getSelectedSystem(), new Date()));

      } else {
        throw new GenericException("Null values received for deletion");
      }
    }

    return modelAndView;
  }

  /**
   * Add system event & update modelAndView to return to view events
   * with success message
   *
   * @param modelAndView
   * @return
   */
  private ModelAndView editSystemEventAction(ModelAndView modelAndView, SystemEventFormBean systemEventFormBean) {
    modelAndView = new ModelAndView(ACTION_VIEW_EVENTS);
    modelAndView.addObject(SYSTEM_LIST, getAllSystems());

    if (StringUtils.hasText(systemEventFormBean.getSelectedSystemEvent().getSystemName())) {
      logger.debug("Values passed for system event update:\nSubject:{}\nLevel:{}\nDate:{}\nSystem name:{}",
        new Object[]{systemEventFormBean.getSelectedSystemEvent().getCommentDesc(),
          systemEventFormBean.getSelectedSystemEvent().getLevelType(),
          systemEventFormBean.getSelectedSystemEvent().getDateTime(),
          systemEventFormBean.getSelectedSystemEvent().getSystemName()});
      //updating system event
      int resultVal = systemEventService.updateEvent(systemEventFormBean.getSelectedSystemEvent());

      modelAndView.addObject(FORM_BEAN, viewSystemEvents(
        systemEventFormBean.getSelectedSystemEvent().getSystemName(),
        systemEventFormBean.getSelectedSystemEvent().getDateTime()));

      logger.debug("System Event update result: {} ", new Object[]{resultVal});
      if (resultVal == 1) {
        modelAndView.addObject(SUCCESS_MSG_KEY,
          "<div class=\"SuccessMsg\">Event has been updated successfully</div>");
      } else {
        modelAndView.addObject(ERROR_MSG_KEY,
          "<div class=\"ErrorMsg\">The action has not been performed</div>");
      }

    } else {
      throw new GenericException("No system was submitted for search");
    }
    return modelAndView;
  }

  /**
   * Common method to select all events for the given System
   *
   * @param systemName
   * @return
   */
  private SystemEventFormBean viewSystemEvents(String systemName, Date date) {
    List<SystemEvent> systemEvents
      = systemEventService.getSystemEvents(systemName);

    SystemEventFormBean systemEventFormBean = new SystemEventFormBean();
    systemEventFormBean.setSelectedSystem(systemName);
    systemEventFormBean.setSystemEvents(systemEvents);

    if (systemEvents != null) {
      logger.debug("Events for system {} are: {}",
        systemName, systemEvents);

      SystemEvent systemEvent = systemEventService.getSystemEvent(systemName,
        date);
      if (systemEvent != null) {
        systemEventFormBean.setSelectedSystemEvent(systemEvent);
        logger.debug("Event was found for " + date.toString() + " :\nDescription:{}\nLevel:{}\nDate:{}",
          new Object[]{systemEvent.getCommentDesc(),
            systemEvent.getLevelType(), systemEvent.getDateTime()});
      }
    }
    return systemEventFormBean;
  }


  /**
   * Get All system names
   *
   * @return
   */
  private List<String> getAllSystems() {
    List<String> systemNames
      = dataCollectionSystemService.listAllSystems();
    if (systemNames == null
      || (systemNames != null && systemNames.isEmpty())) {
      throw new GenericException("System list size is Zero");
    }

    return systemNames;
  }


}
