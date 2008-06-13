package org.pentaho.pac.client.common.ui;


public interface ICallback<T extends Object> {

  public void onHandle( T o );
}
