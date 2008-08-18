package org.pentaho.pac.client.scheduler.model;

/**
 * Data available to file:
 * <file
 *       description="Removes files from the content repository that are more than 180 days old."
 *       isDirectory="false" lastModifiedDate="1213904782718"
 *       localized-name="Clean Repository" name="clean_repository.xaction"
 *       visible="true" />
 * @author Steven Barkdull
 *
 */
public class SolRepFile implements ISolRepFile {

  private String name;
  private String friendlyName;
  private String description;
  
  public SolRepFile( String name, String friendlyName, String description ) {
    this.name = name;
    this.friendlyName = friendlyName;
    this.description = description;
  }

  public boolean hasChildren() {
    return false;
  }
  
  public String getName() {
    return name;
  }

  public String getFriendlyName() {
    return friendlyName;
  }

  public String getDescription() {
    return description;
  }
}
