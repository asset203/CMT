package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.dataCollection.*;
import eg.com.vodafone.web.mvc.util.CMTConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * @author Radwa Osama
 * @since 4/17/13
 */
@Component
public class ExtractXMLSourceColumnsValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ExtractXMLSourceColumns.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (!(target instanceof ExtractXMLSourceColumns)) {
            throw new GenericException("Invalid bean type");
        }

        ExtractXMLSourceColumns formBean = (ExtractXMLSourceColumns) target;

        if (XMLComplexity.VENDOR_SPECIFIC.equals(formBean.getXmlComplexity())) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "extractXMLSourceColumns.xmlVendor",
                    "extractXMLSourceColumns.field.required", "Please, select a vendor");

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "extractXMLSourceColumns.xmlConverter",
                    "extractXMLSourceColumns.field.required", "Please, select a Converter");
            if (StringUtils.isNotEmpty(formBean.getXmlVendor()) && formBean.getXmlVendor().equals(CMTConstants.DEFAULT_SELECT)) {
                errors.rejectValue("extractXMLSourceColumns.xmlVendor",
                        "extractXMLSourceColumns.field.required", "Please, select a Vendor");
            }
            if (StringUtils.isNotEmpty(formBean.getXmlConverter()) && formBean.getXmlConverter().equals("-1")) {
                errors.rejectValue("extractXMLSourceColumns.xmlConverter",
                        "extractXMLSourceColumns.field.required", "Please, select a Converter");
            }

        }

    }

    public void validateSourceColumnsWhereExtracted(ExtractSourceColumnFormBean extractSourceColumnFormBean, Errors errors) {

        if (extractSourceColumnFormBean == null) {
            errors.rejectValue("extractXMLSourceColumns.uploadFile",
                    "extractXMLSourceColumns.field.required", "Please, upload a file first");
        } else {

            List<SourceColumn> sourceColumnList = extractSourceColumnFormBean.getColumns();

            if (sourceColumnList == null || sourceColumnList.isEmpty()) {
                errors.rejectValue("extractXMLSourceColumns.uploadFile",
                        "extractXMLSourceColumns.field.required", "Please, select a file to upload");
            }
        }
    }

    public void validateFileUpload(Object target, Errors errors) {

        if (!(target instanceof ExtractXMLSourceColumns)) {
            throw new GenericException("Invalid bean type");
        }

        ExtractXMLSourceColumns formBean = (ExtractXMLSourceColumns) target;

        if (formBean.getUploadFile() == null) {
            errors.rejectValue("extractXMLSourceColumns.uploadFile",
                    "extractXMLSourceColumns.field.required", "Please, select a file to upload");
        } else {

            String XML_FILE = ".xml";

            String fileName = formBean.getUploadFile().getOriginalFilename();

            if (StringUtils.isEmpty(fileName) || StringUtils.isWhitespace(fileName)) {

                errors.rejectValue("extractXMLSourceColumns.uploadFile",
                        "extractXMLSourceColumns.field.required", "Please, select a file to upload");

            } else if (!fileName.toLowerCase().endsWith(XML_FILE)) {

                errors.rejectValue("extractXMLSourceColumns.uploadFile",
                        "extractXMLSourceColumns.field.required", "Invalid file type. You can only upload XML files");

            } else {

                // Validate file size
                long MAX_FILE_SIZE = 4194304;
                if (formBean.getUploadFile() != null
                        & formBean.getUploadFile().getSize() > MAX_FILE_SIZE) {

                    errors.rejectValue("extractXMLSourceColumns.uploadFile",
                            "extractXMLSourceColumns.field.required", "File size must not be larger than 4 MB (4,194,304 bytes)");
                } else {

                    // Parse file
                    try {

                        XmlSourceDataParser xmlSourceDataParser =
                                new XmlSourceDataParser(formBean.getUploadFile().getInputStream());

                        List<SourceColumn> sourceColumns = xmlSourceDataParser.extractSourceData();

                        if (sourceColumns == null || sourceColumns.isEmpty()) {

                            errors.rejectValue("extractXMLSourceColumns.uploadFile",
                                    "extractXMLSourceColumns.field.required", "Invalid file content.");

                        }

                    } catch (Exception e) {

                        errors.rejectValue("extractXMLSourceColumns.uploadFile",
                                "extractXMLSourceColumns.field.required", "Invalid file content.");

                    }
                }


            }

        }

    }
}
