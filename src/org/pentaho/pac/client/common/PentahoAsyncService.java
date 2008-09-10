package org.pentaho.pac.client.common;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PentahoAsyncService<E> {
	public void initialze(AsyncCallback<E> callback);
}