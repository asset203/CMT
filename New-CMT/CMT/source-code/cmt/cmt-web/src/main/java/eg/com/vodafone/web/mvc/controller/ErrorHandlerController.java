package eg.com.vodafone.web.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 * User: Alia.Adel
 * Date: 4/1/13
 * Time: 5:56 PM
 */
@Controller
public class ErrorHandlerController {

    final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(ErrorHandlerController.class);

    @RequestMapping(value = "error403")
    public ModelAndView handleError403() {
        logger.info("Entered error403");
        return new ModelAndView("/general/error403");
    }

    @RequestMapping(value = "error404")
    public ModelAndView handleError404() {
        logger.info("Entered error404");
        return new ModelAndView("/general/error404");
    }

    @RequestMapping(value = "error405")
    public ModelAndView handleError405() {
        logger.info("Entered error405");
        return new ModelAndView("/general/error405");
    }

    @RequestMapping(value = "error500")
    public ModelAndView handleError500() {
        logger.info("Entered error500");
        return new ModelAndView("/general/error500");
    }

}
