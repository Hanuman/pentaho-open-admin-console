package org.pentaho.pac.common.config;

import java.io.Serializable;

public class GoogleMapsConfig implements IGoogleMapsConfig, Serializable {

  String googleMapsKey;

  public String getGoogleMapsKey() {
    return googleMapsKey;
  }

  public void setGoogleMapsKey(String googleMapsKey) {
    this.googleMapsKey = googleMapsKey;
  }
  


}
