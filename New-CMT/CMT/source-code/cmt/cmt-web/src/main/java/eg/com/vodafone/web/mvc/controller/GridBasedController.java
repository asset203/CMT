package eg.com.vodafone.web.mvc.controller;

import java.util.List;

import eg.com.vodafone.web.mvc.model.searchcriteria.SearchCriteria;
import eg.com.vodafone.web.mvc.model.searchresult.SearchResult;
import org.springframework.web.bind.annotation.SessionAttributes;


@SessionAttributes("gridBean")
public interface GridBasedController {
			
	int getCount (Object ... args);
	
	List <? extends SearchResult> getResultsList (SearchCriteria searchCriteria ,int startRowIndex , int endRowIndex);
	
	
}
