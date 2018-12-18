package ph.codegum.mvcblog.services;

/**
 * @author Ken on 12/13/2018
 * @project Spring-MVC-Blog
 */
public interface NotificationService {
    void addInfoMessage(String msg);
    void addErrorMessage(String msg);
}
