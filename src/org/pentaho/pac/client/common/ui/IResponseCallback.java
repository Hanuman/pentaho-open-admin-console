package org.pentaho.pac.client.common.ui;

public interface IResponseCallback<PARAM extends Object, RETVAL extends Object> {
  public RETVAL onHandle( PARAM p );
}
