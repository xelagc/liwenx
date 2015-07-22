/**
 * 
 */
package net.sf.liwenx.config;

/**
 * @author Alejandro Guerra Cabrera
 *
 */
public class ErrorPage extends Page{
	
	private Class<? extends Exception> exception;

	private Integer statusCode;
	
	/**
	 * @return the exception
	 */
	public Class<? extends Exception> getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Class<? extends Exception> exception) {
		this.exception = exception;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusCode
	 */
	public Integer getStatusCode() {
		return statusCode;
	}

}
