package org.pentaho.pac.client.scheduler.model;

import com.google.gwt.xml.client.Document;

public class SolutionRepositoryModel {

  Document solutionRepositoryDocument = null;
  
  public SolutionRepositoryModel( Document solutionRepositoryDocument ) {
    this.solutionRepositoryDocument = solutionRepositoryDocument;
    buildModel();
  }
  
  public Document getSolutionRepositoryDocument() {
    return solutionRepositoryDocument;
  }
  
  private void buildModel() {
    
  }
}
