package org.pentaho.pac.client.scheduler.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Data available to folder:
 * <file
 *     description="Links for updating the system settings and web service examples"
 *     isDirectory="true" lastModifiedDate="1213904824687"
 *     localized-name="Admin Services" name="admin" visible="false">
 *     
 * @author Steven Barkdull
 *
 */
public class SolRepFolder extends SolRepFile implements ISolRepFile {

  private Map<String,ISolRepFile> children = new HashMap<String,ISolRepFile>();
  
  public SolRepFolder( String name, String friendlyName, String description ) {
    super( name, friendlyName, description );
  }

  public void addChild( ISolRepFile file ) {
    children.put( file.getName(), file );
  }
  
  public ISolRepFile getChild( String name ) {
    return children.get( name );
  }
  
  public boolean hasChildren() {
    return children.size() > 0;
  }
}
