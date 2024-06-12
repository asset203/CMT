package eg.com.vodafone.web.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Radwa Osama
 * @since 3/3/13
 */
@Service
public class DefaultUserContextService implements UserContextService {

  @Override
  public String getCurrentUser() {
    return ((SecurityContextHolder.getContext().getAuthentication() != null)?
            SecurityContextHolder.getContext().getAuthentication().getName():"");
  }
}
