package eg.com.vodafone.web.mvc.formbean.dataCollection;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Create By: Radwa Osama
 * Date: 4/5/13, 1:43 PM
 */
public class ExtractSourceDataFormBean {

  private String fileName;

  private MultipartFile uploadFile;

  private String sampleLines;

  private Delimiter delimiter;

  private Header header;

  private String otherDelimiter;

  Map<String, String> delimiterTypes;

  Map<String, Header> headerTypes;

  private String ignoreLines;


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

  public String getSampleLines() {
    return sampleLines;
  }

  public void setSampleLines(String sampleLines) {
    this.sampleLines = StringEscapeUtils.escapeHtml(sampleLines);
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }

  public void setDelimiter(Delimiter delimiter) {
    this.delimiter = delimiter;
  }

  public Map<String, String> getDelimiterTypes() {
    return delimiterTypes;
  }

  public void setDelimiterTypes(Map<String, String> delimiterTypes) {
    this.delimiterTypes = delimiterTypes;
  }

  public String getIgnoreLines() {
    return ignoreLines;
  }

  public void setIgnoreLines(String ignoreLines) {
    this.ignoreLines = ignoreLines;
  }

  public String getOtherDelimiter() {
    return otherDelimiter;
  }

  public void setOtherDelimiter(String otherDelimiter) {
    this.otherDelimiter = otherDelimiter;
  }

  public Header getHeader() {
    return header;
  }

  public void setHeader(Header header) {
    this.header = header;
  }

  public Map<String, Header> getHeaderTypes() {
    return headerTypes;
  }

  public void setHeaderTypes(Map<String, Header> headerTypes) {
    this.headerTypes = headerTypes;
  }

}
