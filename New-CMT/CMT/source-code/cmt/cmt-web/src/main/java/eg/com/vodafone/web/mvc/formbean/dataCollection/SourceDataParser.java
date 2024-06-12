package eg.com.vodafone.web.mvc.formbean.dataCollection;

import eg.com.vodafone.web.exception.GenericException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Create By: Radwa Osama
 * Date: 4/5/13, 5:14 PM
 */
public class SourceDataParser {

  MultipartFile multipartFile;

  public SourceDataParser(MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
  }

  public String parseResult(int limit) throws IOException {

    List<String> lines = getLines();

    List<String> sampleLines;

    if (lines.size() < limit) {
      sampleLines = lines.subList(0, lines.size());
    } else {
      sampleLines = lines.subList(0, limit);
    }

    return StringUtils.join(sampleLines, '\n');
  }

  public int size() {
    try {
      return getLines().size();
    } catch (IOException e) {

      return -1;
    }
  }

  public List<String> getLines() throws IOException {
    try {

      StringWriter writer = getStringWriter();
      String result = writer.toString();

      if (StringUtils.isEmpty(result)) {
        return new ArrayList<String>();
      }

      List<String> lines = Arrays.asList(result.split("\\n"));

      List<String> output = new ArrayList<String>();

      // Remove white spaces
      for (String line : lines) {
        if (StringUtils.isNotEmpty(line) && !StringUtils.isWhitespace(line)) {
          output.add(line);
        }
      }

      return output;

    } finally {

      IOUtils.closeQuietly(multipartFile.getInputStream());
    }
  }

  private StringWriter getStringWriter() throws IOException {
    StringWriter writer = new StringWriter();
    IOUtils.copy(multipartFile.getInputStream(), writer);
    return writer;
  }

}
