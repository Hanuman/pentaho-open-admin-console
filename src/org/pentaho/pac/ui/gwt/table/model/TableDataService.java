package org.pentaho.pac.ui.gwt.table.model;

import java.util.List;
import java.util.Map;

import org.pentaho.pac.ui.gwt.table.NameValue;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TableDataService extends RemoteService {
	public Map<String, String[]> getData(String[] colNames,NameValue[] discriminators) ;	
	public List<String[]> getData(NameValue[] discriminators);
	public int getRowCount();

}
