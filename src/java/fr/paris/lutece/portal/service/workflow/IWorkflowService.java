package fr.paris.lutece.portal.service.workflow;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workflow.Action;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.util.ReferenceList;
/**
 * WorkflowService
 */
public interface IWorkflowService {

	

	/**
	 * returns a list of actions possible for a given document based on the status
	 * of the document in the workflow and the user role
	 * @param nIdResource the document id
	 * @param strResourceType the document type
	 * @param user the adminUser
	 * @param nIdWorkflow the workflow id
	 * @return a list of Action
	 */
	Collection<Action> getActions(int nIdResource, String strResourceType,
			int nIdWorkflow, AdminUser user);

	/**
	 * returns the state of a  given document 
	 * of the document in the workflow and the user role
	 * @param nIdResource the document id
	 * @param strResourceType the document type
	 * @param user the adminUser
	 * @param nIdWorkflow the workflow id
	 * @return the state of a given document
	 */
	State getState(int nIdResource, String strResourceType, int nIdWorkflow,
			AdminUser user);

	/**
	 * return true if a form is associate to the action
	 *
	 * @param nIdAction the action id
	 * @param locale the loacle
	 * @return true if a form is associate to the action
	 */
	boolean isDisplayTasksForm(int nIdAction, Locale locale);

	/**
	 * Proceed action given in parameter
	 * @param nIdResource the resource id
	 * @param strResourceType the resource type
	 * @param request the request
	 * @param nIdAction the action id
	 * @param locale locale
	 *
	 */
	void doProcessAction(int nIdResource, String strResourceType,
			int nIdAction, HttpServletRequest request, Locale locale);

	/**
	 * returns the  actions history performed on a resource
	 * @param nIdResource the resource id
	 * @param strResourceType the resource type
	 * @param request the request
	 * @param nIdWorkflow the workflow id
	 * @param locale the locale
	 * @return the history of actions performed on a resource
	 */
	String getDisplayDocumentHistory(int nIdResource, String strResourceType,
			int nIdWorkflow, HttpServletRequest request, Locale locale);

	/**
	 * Perform the information on the various tasks associated with the given action specified in parameter
	 * @param nIdResource the resource id
	 * @param strResourceType the resource type
	 * @param request the request
	 * @param nIdAction the action id
	 * @return null if there is no error in the task form
	 *                    else return the error message url
	 
	 */
	String doSaveTasksForm(int nIdResource, String strResourceType,
			int nIdAction, HttpServletRequest request, Locale locale);

	

	/**
	 * Remove in all workflows the resource specified in parameter
	 * @param nIdResource the resource id
	 * @param strResourceType the resource type
	 */
	void doRemoveWorkFlowResource(int nIdResource, String strResourceType);

	
	/**
	 * returns the tasks form
	 * @param nIdResource the document id
	 * @param strResourceType the document type
	 * @param request the request
	 * @param nIdAction the action id
	 * @param locale the locale
	 * @return the tasks form associated to the action
	 *
	 */
	String getDisplayTasksForm(int nIdResource, String strResourceType,
			int nIdAction, HttpServletRequest request, Locale locale);

	/**
	 * Check that a given user is allowed to view a resource depending the state of the resource
	 * @param nIdResource the document id
	 * @param strResourceType the document type
	 * @param  user the AdminUser
	 * @param nIdWorkflow the workflow id
	 * @return a list of Action
	 */
	boolean isAuthorized(int nIdResource, String strResourceType,
			int nIdWorkflow, AdminUser user);

	
	/**
	 * return a referencelist wich contains a list enabled workflow
	 * @param user the AdminUser
	 * @param locale the locale
	 * @return a referencelist wich contains a list enabled workflow
	 */
	 ReferenceList getWorkflowsEnabled( AdminUser user,Locale locale);
}