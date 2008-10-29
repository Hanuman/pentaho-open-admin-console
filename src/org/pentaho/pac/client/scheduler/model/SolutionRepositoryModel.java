package org.pentaho.pac.client.scheduler.model;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class SolutionRepositoryModel {

  Document solutionRepositoryDocument = null;
  
  public SolutionRepositoryModel( Document solutionRepositoryDocument ) {
    this.solutionRepositoryDocument = solutionRepositoryDocument;
    buildModel();
  }
  
  public Document getSolutionRepositoryDocument() {
    return solutionRepositoryDocument;
  }
 
  public Document getDocument() {
    return solutionRepositoryDocument;
  }
  
  private void buildModel() {
    
  }
  
  private static String SOLUTION_REPOSITORY_PATH_SEPARATOR = "/"; //$NON-NLS-1$
  
  /**
   * NOTE: path must never have a leading "/". The paths returned from the PCI
   * will NOT have a leading slash.
   * NOTE: this method is necessary because we don't have any xpath code
   * to find the node of interest in the document.
   * 
   * @param path
   * @return
   */
  public String getFriendlyNameFromName( String path ) {
    
    Element el = solutionRepositoryDocument.getDocumentElement();
    return getFriendlyNameFromName( SOLUTION_REPOSITORY_PATH_SEPARATOR + path, el );
  }
  
  /**
   * NOTE: path must ALWAYS have a leading "/"
   * @param path
   * @param node
   * @return
   */
  private String getFriendlyNameFromName( String path, Node node ) {
    int endIdx = path.indexOf( SOLUTION_REPOSITORY_PATH_SEPARATOR, 1 );
    if ( -1 != endIdx ) {
      String leftPathSegment = path.substring( 1, endIdx );
      String remainingPathSegment = path.substring( endIdx );
      Node nodeWithNameAttribute = findChildNodeWithNameAttributeValue( leftPathSegment, node );
      String friendlySegmentName = getFriendlyNameFromNode( nodeWithNameAttribute );
      return friendlySegmentName + SOLUTION_REPOSITORY_PATH_SEPARATOR + getFriendlyNameFromName( remainingPathSegment, nodeWithNameAttribute );
    } else {
      String leftPathSegment = path.substring( 1 );
      Node nodeWithNameAttribute = findChildNodeWithNameAttributeValue( leftPathSegment, node );
      String friendlySegmentName = getFriendlyNameFromNode( nodeWithNameAttribute );
      return friendlySegmentName;
    }
  }
  
  // TODO sbarkdull, can return null, callers need to check for it
  private String getFriendlyNameFromNode( Node nd ) {
    Node friendlyAttrNd = nd.getAttributes().getNamedItem( "localized-name" ); //$NON-NLS-1$
    return friendlyAttrNd.getNodeValue();
  }
  
  private Node findChildNodeWithNameAttributeValue( String nameAttributeValue, Node node ) {
    NodeList nds = node.getChildNodes();
    for ( int ii=0; ii<nds.getLength(); ++ii ) {
      Node nd = nds.item( ii );
      Node attrNd = nd.getAttributes().getNamedItem( "name" ); //$NON-NLS-1$
      if ( (null != attrNd ) && attrNd.getNodeValue().equals( nameAttributeValue ) ) {
        return nd;
      }
    }
    return null;  // should never happen
  }
  
//  public String getNameFromFriendlyName( String friendlyName ) {
//    String[] nameParts = friendlyName.split( SOLUTION_REPOSITORY_PATH_SEPARATOR );
//    return "simpson";
//    
//  }
}
