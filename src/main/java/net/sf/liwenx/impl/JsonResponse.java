/**
 * 
 */
package net.sf.liwenx.impl;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Json Response
 * 
 * @author Alejandro Guerra Cabrera
 *
 */
public class JsonResponse extends AbstractLiwenxResponse {

	public static final String LIWENX_JSON_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss Z";
	
	private static final String KEY_IS_OK = "isOk";
	private static final String KEY_ERROR_CAUSE = "errorCause";
	
	private Map<String, Object> json;
	private GsonBuilder builder;
	
	public JsonResponse() {
		super();
		init();
		this.builder = createDefaultBuilder();
	}
	
	/**
	 * 
	 * @param builder - GsonBuilder
	 */
	public JsonResponse(GsonBuilder builder){
		super();
		init();
		this.builder = builder;
	}
	
	/**
	 * @param statusCode - status code
	 */
	public JsonResponse(int statusCode) {
		super(statusCode);
		init();
		this.builder = createDefaultBuilder();
	}

	/**
	 * @param statusCode - status code
	 * @param builder - GsonBuilder
	 */
	public JsonResponse(int statusCode, GsonBuilder builder) {
		super(statusCode);
		init();
		this.builder = builder;
	}

	private GsonBuilder createDefaultBuilder(){
		return new GsonBuilder().registerTypeAdapter(GregorianCalendar.class, new JsonSerializer<GregorianCalendar>() {
		    public JsonElement serialize(GregorianCalendar date, Type type, JsonSerializationContext context) {
		    	SimpleDateFormat sdf = new SimpleDateFormat(LIWENX_JSON_DATE_FORMAT);
		        return new JsonPrimitive(sdf.format(date.getTime()));
		      }               
		  });
	}
	
	private void init(){
		json = new LinkedHashMap<String, Object>();
		json.put(KEY_IS_OK, false);		
	}
	
	/**
	 * It adds an object to json object
	 * @param key - object key
	 * @param obj - object
	 */
	public void addObject(String key, Object obj){
		json.put(key, obj);
	}
	
	public void setOk(boolean ok){
		addObject(KEY_IS_OK, ok);
	}
	
	public void setErrorCause(String errorCause){
		addObject(KEY_ERROR_CAUSE, errorCause);
	}

	/**
	 * @return the json
	 */
	Map<String, Object> getJson() {
		return json;
	}

	/**
	 * @return the builder
	 */
	public GsonBuilder getBuilder() {
		return builder;
	}
}
