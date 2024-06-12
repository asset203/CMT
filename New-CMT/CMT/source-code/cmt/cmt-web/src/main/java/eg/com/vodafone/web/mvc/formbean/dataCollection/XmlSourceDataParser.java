package eg.com.vodafone.web.mvc.formbean.dataCollection;

import eg.com.vodafone.service.BusinessException;
import org.apache.commons.io.IOUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Radwa Osama
 * @since 4/17/13
 */
public class XmlSourceDataParser {

  InputStream inputXml;

  public XmlSourceDataParser(InputStream inputXml) {
    this.inputXml = inputXml;
  }

  public List<SourceColumn> extractSourceData() {

    List<SourceColumn> sourceColumns = new ArrayList<SourceColumn>();

    try {

      XMLInputFactory inputFactory = XMLInputFactory.newInstance();

      XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(inputXml);

      while (xmlEventReader.hasNext()) {

        XMLEvent documentStart = xmlEventReader.nextEvent();

        if (documentStart.isStartDocument()) {
          continue;
        }

        if (documentStart.isStartElement()) {
          // Skip Root Element
          xmlEventReader.nextEvent();
          return extractSourceData(xmlEventReader);
        }
      }

    } catch (XMLStreamException e) {
      throw new BusinessException("Error parsing input xml source data: " + e.getMessage());

    } finally {

      IOUtils.closeQuietly(inputXml);
    }

    return sourceColumns;
  }

  private List<SourceColumn> extractSourceData(XMLEventReader xmlEventReader) {

    List<SourceColumn> sourceColumns = new ArrayList<SourceColumn>();

    try {

      XMLEvent firstNode = xmlEventReader.nextEvent();

      if (firstNode.isStartElement()) {

        String START_NODE = firstNode.asStartElement().getName().getLocalPart();
        int index =0;
        while (xmlEventReader.hasNext()) {

          XMLEvent event = xmlEventReader.nextEvent();

          if (event.isEndElement()) {
            String name = event.asEndElement().getName().getLocalPart();
            if (START_NODE.equals(name)) {
              break;
            }
          }

          if (event.isStartElement()) {
            String name = event.asStartElement().getName().getLocalPart();
            if (START_NODE.equals(name)) {
              break;
            }

            String VAR_CHAR_MAX_LENGTH = "4000";
            String value = xmlEventReader.getElementText();

            SourceColumn sourceColumn = new SourceColumn();
            sourceColumn.setName(name);
            sourceColumn.setDefaultValue(value);
            sourceColumn.setSampleValue(value);
            sourceColumn.setType(Type.STRING);
            sourceColumn.setCustomType(VAR_CHAR_MAX_LENGTH);
            sourceColumn.setIndex(index);
            sourceColumns.add(sourceColumn);
            index++;
          }
        }
      }

    } catch (XMLStreamException e) {

      e.printStackTrace();
      throw new BusinessException("Error parsing input xml source data: " + e.getMessage());

    }

    return sourceColumns;
  }
}