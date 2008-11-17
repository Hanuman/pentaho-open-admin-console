package org.pentaho.pac.common.config;

import java.io.Serializable;

public class PublisherConfig implements IPublisherConfig, Serializable {

  String password;

  public PublisherConfig() {
    
  }
  
  public PublisherConfig(IPublisherConfig publisherConfig) {
    setPassword(publisherConfig.getPassword());
  }
  
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
