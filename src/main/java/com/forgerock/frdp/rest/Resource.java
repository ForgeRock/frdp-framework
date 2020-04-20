/*
 * Copyright (c) 2015-2020, ForgeRock, Inc., All rights reserved
 * Use subject to license terms.
 */
package com.forgerock.frdp.rest;

import com.forgerock.frdp.common.BasicData;
import com.forgerock.frdp.common.ConstantsIF;
import com.forgerock.frdp.common.Core;
import com.forgerock.frdp.common.CoreIF;
import com.forgerock.frdp.common.DataIF;
import com.forgerock.frdp.dao.OperationIF;
import com.forgerock.frdp.security.Credential;
import com.forgerock.frdp.security.CredentialIF;
import com.forgerock.frdp.utils.JSON;
import com.forgerock.frdp.utils.STR;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Abstract class for REST Web Service that implements ResourceIF interface
 *
 * @author Scott Fehrman, ForgeRock, Inc.
 */
public abstract class Resource extends Core implements ResourceIF {

   private final String CLASS = this.getClass().getName();
   private final Map<STATE, Response.Status> _mapStateStatus = new HashMap<>();
   protected static final String CTX_ATTR_PARSER = "com.forgerock.frdp.parser";
   protected final Logger _logger = Logger.getLogger(this.getClass().getName());
   protected final Level DEBUG_LEVEL = Level.FINE;

   /**
    * Default constructor
    */
   public Resource() {
      String METHOD = "Resource()";

      _logger.entering(CLASS, METHOD);

      for (int i = 0; i < _arrayState.length; i++) {
         _mapStateStatus.put(_arrayState[i], _arrayStatus[i]);
      }

      _logger.exiting(CLASS, METHOD);

      return;
   }

   /**
    * This class does not implement the copy() method from the Core interface
    */
   @Override
   public CoreIF copy() {
      throw new UnsupportedOperationException("Not supported.");
   }

   /*
    * ================= PROTECTED METHODS =================
    */
   /**
    * Create CredentialIF object for proxy user
    *
    * @param propUserName proxy user name
    * @param propPassword proxy user password
    * @return CredentialIF object
    */
   protected CredentialIF getCredentialFromProxy(final String propUserName, final String propPassword) {
      boolean error = false;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String msg = null;
      String username = null;
      String password = null;
      CredentialIF cred = null;

      _logger.entering(CLASS, METHOD);

      cred = new Credential(CredentialIF.Type.PROXY);

      _logger.log(Level.FINE, "propUserName=''{0}'', propPassword=''{1}''",
         new Object[]{
            propUserName != null ? propUserName : NULL,
            propPassword != null ? propPassword : NULL});

      if (!STR.isEmpty(propUserName)) {
         username = this.getParam(propUserName);
         if (!STR.isEmpty(username)) {
            cred.setParam(ConstantsIF.PROXY_USERNAME, username);
            if (!STR.isEmpty(propPassword)) {
               password = this.getParam(propPassword);
               if (!STR.isEmpty(password)) {
                  cred.setParam(ConstantsIF.PROXY_PASSWORD, password);
                  cred.setStatus("PROXY credential created for username: " + username);
                  cred.setState(STATE.SUCCESS);
               } else {
                  error = true;
                  msg = "Value for property '" + propPassword + "' is empty";
               }
            } else {
               error = true;
               msg = "Property name for proxy password is empty";
            }
         } else {
            error = true;
            msg = "Value for property '" + propUserName + "' is empty";
         }
      } else {
         error = true;
         msg = "Property name for proxy username is empty";
      }

      if (error) {
         cred.setState(STATE.ERROR);
         cred.setStatus(msg);
      }

      _logger.exiting(CLASS, METHOD);

      return cred;
   }

   /**
    * Get HTTP Header cookie value
    *
    * @param headers HttpHeaders
    * @param name Header name
    * @return String Header value
    */
   protected String getCookieValue(final HttpHeaders headers, final String name) {
      boolean error = false;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String value = null;
      String msg = null;
      Cookie cookie = null;
      Map<String, Cookie> mapCookies = null;

      _logger.entering(CLASS, METHOD);

      if (headers != null) {

         if (!STR.isEmpty(name)) {
            mapCookies = headers.getCookies();
            if (mapCookies != null && mapCookies.containsKey(name)) {
               cookie = mapCookies.get(name);
               if (cookie != null) {
                  value = cookie.getValue();
                  if (STR.isEmpty(value)) {
                     error = true;
                     msg = "Cookie '" + name + "' has an empty value";
                  }
               } else {
                  error = true;
                  msg = "Cookie '" + name + "' is null";
               }
            } else {
               error = true;
               msg = "Cookie '" + name + "' does not exist";
            }
         } else {
            error = true;
            msg = "Cookie name is empty";
         }
      } else {
         error = true;
         msg = "HttpHeaders are null";
      }

      if (error) {
         _logger.log(Level.SEVERE, msg);
      }

      _logger.exiting(CLASS, METHOD);

      return value;
   }

   /**
    * Create CredentialIF object from Cookie value in the HTTP Headers
    *
    * @param headers HttpHeaders
    * @param cookieName name of the cookie in the headers
    * @param type credential type
    * @return CredentialIF object
    */
   protected CredentialIF getCredentialFromCookie(final HttpHeaders headers, final String cookieName,
      final CredentialIF.Type type) {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String cookieValue = null;
      CredentialIF cred = null;
      Cookie cookie = null;
      Map<String, Cookie> mapCookies = null;

      _logger.entering(CLASS, METHOD);

      mapCookies = headers.getCookies();

      if (!STR.isEmpty(cookieName)
         && mapCookies != null
         && mapCookies.containsKey(cookieName)) {
         cookie = mapCookies.get(cookieName);
         cookieValue = cookie.getValue();
         if (!STR.isEmpty(cookieValue)) {
            cred = new Credential(type);
            cred.setParam(ConstantsIF.COOKIE, cookieName);
            cred.setParam(ConstantsIF.TOKEN, cookieValue);
         } else {
            _logger.log(Level.WARNING, "Anon credential created, missing cookie name");
            cred = new Credential(CredentialIF.Type.ANON);
            cred.setState(STATE.WARNING);
            cred.setStatus("Missing cookie value");
         }
      } else {
         _logger.log(Level.WARNING, "Anon credential created, missing cookie name");
         cred = new Credential(CredentialIF.Type.ANON);
         cred.setState(STATE.WARNING);
         cred.setStatus("Missing cookie name");
      }

      if (_logger.isLoggable(Level.FINE)) {
         _logger.log(Level.FINE, "cookieName=''{0}'', cookieValue=''{1}'', cred=''{2}''",
            new Object[]{
               cookieName != null ? cookieName : NULL,
               cookieValue != null ? cookieValue : NULL, cred.toString()});
      }

      _logger.exiting(CLASS, METHOD);

      return cred;
   }

   /**
    * Get Response object from the DataIF object
    *
    * @param data DataIF object
    * @return Response
    */
   protected Response getResponseFromData(DataIF data) {
      Object obj = null;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String str = null;
      Response response = null;

      _logger.entering(CLASS, METHOD);

      if (_logger.isLoggable(Level.FINE)) {
         _logger.log(Level.FINE, "data=''{0}''",
            new Object[]{data != null ? data.toString() : NULL});
      }

      if (data == null) {
         data = new BasicData();
         data.setState(STATE.ERROR);
         data.setData("Input Data object is null");
         data.setStatus("Input Data object is null");
      }

      switch (data.getState()) {
         case ERROR:
            _logger.log(Level.SEVERE, "{0} : {1}",
               new Object[]{data.getState().toString(), data.getStatus()});
            break;
         case FAILED:
         case WARNING:
         case NOTAUTHORIZED:
         case NOTEXIST:
         case NOTREADY:
            _logger.log(Level.WARNING, "{0} : {1}",
               new Object[]{data.getState().toString(), data.getStatus()});
            break;
         case NEW:
         case READY:
         case SUCCESS:
            if (_logger.isLoggable(Level.FINE)) {
               _logger.log(Level.FINE, "{0} : {1}",
                  new Object[]{data.getState().toString(), data.getStatus()});
            }
            break;
      }

      obj = data.getObject();
      if (obj != null && obj instanceof JSONObject) {
         str = ((JSONObject) obj).toJSONString();
      } else {
         str = data.getData();
      }

      response = Response.status(this.getStatusFromState(data.getState())).type(MediaType.APPLICATION_JSON_TYPE)
         .entity((str != null ? str : data.getStatus())).build();

      _logger.exiting(CLASS, METHOD);

      return response;
   }

   /**
    * Read property file and load values as parameters
    *
    * @param propFileName name of the property file
    * @throws Exception could not load parameters from properties file
    */
   protected final void loadParamsFromProperties(final String propFileName) throws Exception {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      Properties props = null;
      InputStream is = null;

      _logger.entering(CLASS, METHOD);

      props = new Properties();
      is = getClass().getClassLoader().getResourceAsStream(propFileName);

      if (is != null) {
         props.load(is);

         if (!props.isEmpty()) {
            this.setParams(props);
         }
      } else {
         throw new Exception("Could not get Properties: '" + propFileName
            + "', InputStream is null");
      }

      _logger.exiting(CLASS, METHOD);

      return;
   }

   /**
    * Read JSON file and load values as parameters
    *
    * @param fileName pathname/filename from deployment locations
    * (FILESYSTEM/themes/themes.json) { "id" : "themes/default" }
    * @throws Exception could not load parameters from JSON file
    */
   protected final void loadParamsFromJSON(final String fileName) throws Exception {
      byte[] bytes = null;
      Object objParse = null;
      Object objTheme = null;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String jsonFileName = null;
      String filesystemPath = null;
      Path path = null;
      JSONParser parser = null;
      JSONObject joConfig = null;

      _logger.entering(CLASS, METHOD);

      if (!STR.isEmpty(fileName)) {
         filesystemPath = this.getParamNotEmpty(PATH_FILESYSTEM);
         jsonFileName = filesystemPath + ConstantsIF.THEMES + "/" + fileName;

         if (_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, "filesystemPath=''{0}'', fileName=''{1}''",
               new Object[]{filesystemPath, fileName});
         }

         parser = new JSONParser();

         path = Paths.get(jsonFileName);
         bytes = Files.readAllBytes(path);
         objParse = parser.parse(new String(bytes));
         joConfig = (JSONObject) objParse;

         objTheme = joConfig.get(ConstantsIF.THEME);

         if (objTheme != null && objTheme instanceof String) {
            this.setParam(ConstantsIF.THEME, ConstantsIF.THEMES + "/" + (String) objTheme);
         } else {
            throw new Exception("JSON Object for config '"
               + path.getFileName().toString()
               + "' does not have a 'theme' String attribute, JSON='"
               + joConfig.toJSONString() + "'");
         }
      } else {
         throw new Exception("Argument 'filename' is empty.");
      }

      _logger.exiting(CLASS, METHOD);

      return;
   }

   /**
    * Get the Response Status related to the STATE
    *
    * @param state enum value for STATE
    * @return Response.Status
    */
   protected Response.Status getStatusFromState(final STATE state) {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      Response.Status status = null;

      _logger.entering(CLASS, METHOD);

      if (state != null) {
         if (_mapStateStatus.containsKey(state)) {
            status = _mapStateStatus.get(state);
         }
      }

      if (status == null) {
         status = Response.Status.NOT_ACCEPTABLE; // 406
      }

      _logger.exiting(CLASS, METHOD);

      return status;
   }

   /**
    * Abort processing of the web request
    *
    * @param method name of the method that invoked this method
    * @param msg message describing the situation
    * @param status HTTP Response status
    * @throws WebApplicationException could not continue execution
    */
   protected void abort(final String method, final String msg, final Response.Status status)
      throws WebApplicationException {
      Response response = null;

      _logger.log(Level.SEVERE, "{0}:{1}: {2}",
         new Object[]{CLASS, (method == null ? "" : method),
            (msg == null ? "null message" : msg)});

      response = Response.status(status).type(MediaType.TEXT_PLAIN)
         .entity(method + ": " + msg)
         .build();

      throw new WebApplicationException(response);
   }

   /**
    * Get the JSONParser object from the ServletContext
    *
    * @param ctx ServletContext
    * @return JSONParser
    */
   protected JSONParser getParserFromCtx(final ServletContext ctx) {
      Object obj = null;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      JSONParser parser = null;

      _logger.entering(CLASS, METHOD);

      obj = ctx.getAttribute(CTX_ATTR_PARSER);
      if (obj != null && obj instanceof JSONParser) {
         parser = (JSONParser) obj;
      } else {
         parser = new JSONParser();
         ctx.setAttribute(CTX_ATTR_PARSER, parser);
      }

      _logger.exiting(CLASS, METHOD);

      return parser;
   }

   /**
    * Get HTTP Response object from JSON object in the OperationIF object
    *
    * @param uri HTTP UriInfo
    * @param oper OperationIF
    * @return Response
    */
   protected Response getResponseFromJSON(final UriInfo uri, final OperationIF oper) {
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String str = null;
      JSONObject jsonOutput = null;
      JSONObject jsonUids = null;
      Response response = null;
      UriBuilder builder = null;

      _logger.entering(CLASS, METHOD);

      if (_logger.isLoggable(Level.FINE)) {
         _logger.log(Level.FINE, METHOD + ": oper=''{0}''",
            new Object[]{oper != null ? oper.toString() : NULL});
      }

      if (oper == null) {
         this.abort(METHOD, "Input Operation is null",
            Response.Status.INTERNAL_SERVER_ERROR);
      }

      if (oper.isError()) {
         if (oper.getState() == STATE.ERROR) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
               .type(MediaType.TEXT_PLAIN)
               .entity(oper.getStatus())
               .build();
         } else {
            response = Response.status(Response.Status.BAD_REQUEST)
               .type(MediaType.TEXT_PLAIN)
               .entity(oper.getStatus())
               .build();
         }
      } else {
         jsonOutput = oper.getJSON();

         if (_logger.isLoggable(Level.FINE)) {
            _logger.log(Level.FINE, METHOD + ": json=''{0}''",
               new Object[]{jsonOutput != null ? jsonOutput.toString() : NULL});
         }

         if (jsonOutput == null) {
            this.abort(METHOD, "JSON output is null",
               Response.Status.INTERNAL_SERVER_ERROR);
         }

         switch (oper.getType()) {
            case CREATE: {
               builder = uri.getAbsolutePathBuilder();
               /*
             * If the operation was initially a PUT, the URI has the UID The operation
             * object will be non-null if the CREATE was a REPLACE
                */
               if (oper.getObject() == null) {
                  str = this.getUidFromOperation(oper);
                  builder.path(str);
               }

               response = Response.created(builder.build()).build();
               break;
            }
            case READ: {
               response = Response.status(this.getStatusFromState(oper.getState()))
                  .type(MediaType.APPLICATION_JSON)
                  .entity(jsonOutput.toString())
                  .build();
               break;
            }
            case REPLACE: {
               switch (oper.getState()) {
                  case FAILED: {
                     response = Response.status(this.getStatusFromState(oper.getState()))
                        .entity(oper.getStatus())
                        .build();
                     break;
                  }
                  case NOTEXIST: {
                     response = Response.status(Response.Status.NOT_FOUND)
                        .build();
                     break;
                  }
                  default: {
                     response = Response.noContent().build();
                     break;
                  }
               }
               break;
            }
            case DELETE: {
               response = Response.noContent().build();
               break;
            }
            case SEARCH: {
               jsonUids = this.getUidsFromSearch(jsonOutput);
               str = jsonUids.toJSONString();
               response = Response.ok().type(MediaType.APPLICATION_JSON)
                  .entity(str)
                  .build();
               break;
            }
            default: {
               break;
            }
         }
      }

      _logger.exiting(CLASS, METHOD);

      return response;
   }

   /**
    * Get the Uid value from the JSON data in the OperationIF object
    *
    * @param oper OperationIF object
    * @return String Uid value
    */
   protected String getUidFromOperation(final OperationIF oper) {
      Object obj = null;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String uid = null;
      JSONObject json = null;

      _logger.entering(CLASS, METHOD);

      json = oper.getJSON();

      if (json == null) {
         this.abort(METHOD, "JSON is null", Response.Status.INTERNAL_SERVER_ERROR);
      }

      uid = JSON.getString(json, ConstantsIF.UID);

      if (STR.isEmpty(uid)) {
         this.abort(METHOD, "Attribute '" + ConstantsIF.UID + "' is empty",
            Response.Status.INTERNAL_SERVER_ERROR);
      }

      _logger.exiting(CLASS, METHOD);

      return uid;
   }

   /**
    * Get unique ids from JSON results object
    *
    * The search response may have different formatting options:
    *
    * <pre>
    * 1) The "array" could be named "result" or "results" ...
    *    { "result": [ {...} ] }
    *    { "results": [ * {...} ] }
    * 2) The unique id for a entry could be "_id" or "uid" ...
    *    { "_id": "..." }
    *    { "uid": "..." }
    * </pre>
    *
    * @param jsonInput JSONObject
    * @return JSONObject
    */
   protected JSONObject getUidsFromSearch(final JSONObject jsonInput) {
      Object obj = null;
      String METHOD = Thread.currentThread().getStackTrace()[1].getMethodName();
      String uid = null;
      JSONObject jsonOutput = null;
      JSONObject jsonEntry = null;
      JSONArray jsonResults = null;
      JSONArray jsonUids = null;

      _logger.entering(CLASS, METHOD);

      jsonOutput = new JSONObject();
      jsonUids = new JSONArray();

      if (jsonInput.containsKey(ConstantsIF.RESULTS)) {
         obj = jsonInput.get(ConstantsIF.RESULTS);

      } else if (jsonInput.containsKey(ConstantsIF.RESULT)) {
         obj = jsonInput.get(ConstantsIF.RESULT);
      }

      if (obj != null
         && obj instanceof JSONArray 
         && ((JSONArray) obj).size() > 0) {
         jsonResults = (JSONArray) obj;
         for (Object o : jsonResults) {
            if (o != null) {
               if (o instanceof JSONObject) {
                  jsonEntry = (JSONObject) o;
                  uid = null;
                  if (jsonEntry.containsKey(ConstantsIF.UID)) {
                     uid = (String) ((JSONObject) o).get(ConstantsIF.UID);
                  } else if (jsonEntry.containsKey(ConstantsIF._ID)) {
                     uid = (String) ((JSONObject) o).get(ConstantsIF._ID);
                  }

                  if (!STR.isEmpty(uid)) {
                     jsonUids.add(uid);
                  }
               } else if (o instanceof String && !STR.isEmpty((String) o)) {
                  uid = (String) o;
                  jsonUids.add(uid);
               }
            }
         }
      }

      jsonOutput.put(ConstantsIF.RESULTS, jsonUids);
      jsonOutput.put(ConstantsIF.QUANTITY, jsonUids.size());

      _logger.exiting(CLASS, METHOD);

      return jsonOutput;
   }

}
