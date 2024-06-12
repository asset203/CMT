package eg.com.vodafone.web.mvc.formbean.dataCollection;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Radwa Osama
 * @since 4/16/13
 */
public class ExtractXMLSourceColumns {

  private String fileName;

  private MultipartFile uploadFile;

  private String xmlVendor;

  private String xmlConverter;

  private XMLComplexity xmlComplexity;

  private Map<String, XMLComplexity> xmlComplexityTypes;

  private Map<String, String> xmlVendors;

  private Map<String, String> xmlConverterTypes;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public MultipartFile getUploadFile() {
    return uploadFile;
  }

  public void setUploadFile(MultipartFile uploadFile) {
    this.uploadFile = uploadFile;
  }

  public XMLComplexity getXmlComplexity() {
    return xmlComplexity;
  }

  public void setXmlComplexity(XMLComplexity xmlComplexity) {
    this.xmlComplexity = xmlComplexity;
  }

  public Map<String, XMLComplexity> getXmlComplexityTypes() {
    return xmlComplexityTypes;
  }

  public void setXmlComplexityTypes(Map<String, XMLComplexity> xmlComplexityTypes) {
    this.xmlComplexityTypes = xmlComplexityTypes;
  }

  public String getXmlVendor() {
    return xmlVendor;
  }

  public void setXmlVendor(String xmlVendor) {
    this.xmlVendor = xmlVendor;
  }

  public String getXmlConverter() {
    return xmlConverter;
  }

  public void setXmlConverter(String xmlConverter) {
    this.xmlConverter = xmlConverter;
  }

  public Map<String, String> getXmlVendors() {
    return xmlVendors;
  }

  public void setXmlVendors(Map<String, String> xmlVendors) {
    this.xmlVendors = xmlVendors;
  }

  public Map<String, String> getXmlConverterTypes() {
    return xmlConverterTypes;
  }

  public void setXmlConverterTypes(Map<String, String> xmlConverterTypes) {
    this.xmlConverterTypes = xmlConverterTypes;
  }
}
