/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * Created  
 * @author Steven Barkdull
 */

package org.pentaho.pac.server.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.client.PentahoAdminConsole;
import org.pentaho.pac.client.i18n.PacLocalizedMessages;
import org.pentaho.pac.common.PacServiceException;

public class ThreadSafeHttpClient {
  private static final PacLocalizedMessages MSGS = PentahoAdminConsole.getLocalizedMessages();
  private static final Log logger = LogFactory.getLog(ThreadSafeHttpClient.class);

  private static final String REQUESTED_MIME_TYPE = "requestedMimeType"; //$NON-NLS-1$

  public enum HttpMethodType {
    POST, GET
  };

  /*
   * see: http://hc.apache.org/httpclient-3.x/threading.html
   */
  private static final HttpClient CLIENT;
  static {
    MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
    CLIENT = new HttpClient(connectionManager);
    CLIENT.getParams().setParameter("http.useragent", ThreadSafeHttpClient.class.getName() ); //$NON-NLS-1$
  }

  private String baseUrl = null;

  /**
   * Base Constructor
   */
  public ThreadSafeHttpClient() {
    super();
  }

  public ThreadSafeHttpClient(String baseUrl) {
    this();
    setBaseUrl(baseUrl);
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String execRemoteMethod(String serviceName, HttpMethodType methodType, Map<String, Object> mapParams)
      throws ProxyException {
    return execRemoteMethod(serviceName, methodType, mapParams, "text/xml"); //$NON-NLS-1$
  }

  /**
   * 
   * @param serviceName String can be null or empty string.
   * @param mapParams
   * @param requestedMimeType
   * @return
   * @throws PacServiceException
   */
  public String execRemoteMethod(String serviceName, HttpMethodType methodType, Map<String, Object> mapParams,
      String requestedMimeType) throws ProxyException {

    assert null != baseUrl : "baseUrl cannot be null"; //$NON-NLS-1$
    
    String serviceUrl = baseUrl + ((StringUtils.isEmpty(serviceName)) ? "" : "/" + serviceName); //$NON-NLS-1$ //$NON-NLS-2$
    if (null == mapParams) {
      mapParams = new HashMap<String, Object>();
    }
    mapParams.put(REQUESTED_MIME_TYPE, requestedMimeType);
    
    HttpMethodBase method = null;
    switch (methodType) {
      case POST:
        method = new PostMethod(serviceUrl);
        setPostMethodParams( (PostMethod)method, mapParams );
        method.setFollowRedirects( false );
        break;
      case GET:
        method = new GetMethod(serviceUrl);
        setGetMethodParams( (GetMethod)method, mapParams );
        method.setFollowRedirects( true );
        break;
      default:
        throw new RuntimeException( MSGS.invalidHttpMethodType( methodType.toString() ) );  // can never happen
    }
    return executeMethod( method );
  }

  private String executeMethod( HttpMethod method ) throws ProxyException {
    InputStream responseStrm = null;
    try {
      int httpStatus = CLIENT.executeMethod(method);
      if (httpStatus != HttpStatus.SC_OK) {
        String status = method.getStatusLine().toString();
        throw new ProxyException(status);  // TODO
      }
      responseStrm = method.getResponseBodyAsStream();
      // trim() is necessary because some jsp's puts \n\r at the beginning of
      // the returned text, and the xml processor chokes on \n\r at the beginning.
      String tmp = IOUtils.toString(responseStrm).trim();
      return tmp;
    } catch (IOException e) {
      throw new ProxyException(e);
    } finally {
      method.releaseConnection();
    }
  }

  private static void setGetMethodParams( GetMethod method, Map<String, Object> mapParams ) {
    NameValuePair[] params = mapToNameValuePair( mapParams );
    method.setQueryString( params );
  }

  private static void setPostMethodParams( PostMethod method, Map<String, Object> mapParams ) {
    for ( Map.Entry<String,Object> entry : mapParams.entrySet() ) {
      Object o = entry.getValue();
      if ( o instanceof String[] ) {
        for ( String s : (String[])o ) {
          method.addParameter( entry.getKey(), s );
        }
      } else {
        method.setParameter( entry.getKey(), (String)o );
      }
    }
  }
  
  private static NameValuePair[] mapToNameValuePair(Map<String, Object> paramMap) {
    NameValuePair[] pairAr = new NameValuePair[paramMap.size()];
    int idx = 0;
    for (Map.Entry<String, Object> me : paramMap.entrySet()) {
      pairAr[idx] = new NameValuePair(me.getKey(), (String)me.getValue());
      idx++;
    }
    return pairAr;
  }
}