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
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.pac.common.PacServiceException;


public class ThreadSafeHttpClient {
    private static final Log logger = LogFactory.getLog(ThreadSafeHttpClient.class);
    
    /*
     * see: http://hc.apache.org/httpclient-3.x/threading.html
     */
    private static final HttpClient CLIENT;
    static {
      MultiThreadedHttpConnectionManager connectionManager = 
        new MultiThreadedHttpConnectionManager();
      CLIENT = new HttpClient( connectionManager );
    }
    private String proxyUrl = null;
    
    /**
     * Base Constructor
     */
    public ThreadSafeHttpClient( String proxyUrl ) {
      super();
      this.proxyUrl = proxyUrl;
    }

    public String execRemoteMethod( String serviceName, Map<String, String> mapParams )
        throws PacServiceException {
      return execRemoteMethod( serviceName, mapParams, "text/xml" ); //$NON-NLS-1$
    }

    /**
     * 
     * @param serviceName String can be null or empty string.
     * @param mapParams
     * @param contentType
     * @return
     * @throws PacServiceException
     */
    public String execRemoteMethod( String serviceName, Map<String, String> mapParams, String contentType )
        throws PacServiceException {
      
      InputStream responseStrm = null;
      NameValuePair[] params = ( null != mapParams )
        ? mapToNameValuePair( mapParams )
        : null;
      String serviceUrl = proxyUrl 
        + ( ( StringUtils.isEmpty( serviceName ) ) ? "" : "/" + serviceName ); //$NON-NLS-1$ //$NON-NLS-2$
      GetMethod method = new GetMethod( serviceUrl );

      method.addRequestHeader( "Content-Type", contentType ); //$NON-NLS-1$
      
// TODO sbarkdull, do retries? if so, put number of retries in config file.
      //method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
      
      if ( null != params ) {
        method.setQueryString(params);
      }
      try {
        int httpStatus = CLIENT.executeMethod(method);
        if ( httpStatus != HttpStatus.SC_OK) {
          String status = method.getStatusLine().toString();
          throw new PacServiceException(status);
        }
        // trim() is necessary because SchedulerAdmin.jsp puts \n\r at the beginning of
        // the returned text, and the xml processor chokes on \n\r at the beginning.
        responseStrm = method.getResponseBodyAsStream();
        return IOUtils.toString( responseStrm ).trim();
      } catch (IOException e) {
        throw new PacServiceException(e);
      } finally {
        method.releaseConnection();
      }
    }
    
    private static NameValuePair[] mapToNameValuePair( Map<String, String> paramMap )
    {
      NameValuePair[] pairAr = new NameValuePair[ paramMap.size() ];
      int idx = 0;
      for ( Map.Entry<String,String> me : paramMap.entrySet() ) {
        pairAr[ idx ] = new NameValuePair( me.getKey(), me.getValue() );
        idx++;
      }
      return pairAr;
    }
  }