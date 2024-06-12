package eg.com.vodafone.web.mvc.validator;

import eg.com.vodafone.service.BusinessException;
import eg.com.vodafone.web.exception.GenericException;
import eg.com.vodafone.web.mvc.formbean.dataCollection.Delimiter;
import eg.com.vodafone.web.mvc.formbean.dataCollection.ExtractSourceDataFormBean;
import eg.com.vodafone.web.mvc.formbean.dataCollection.Header;
import eg.com.vodafone.web.mvc.formbean.dataCollection.SourceDataParser;
import eg.com.vodafone.web.mvc.util.CacheService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eg.com.vodafone.web.mvc.util.CacheName.TEXT_FILE_CACHE;

/**
 * Create By: Radwa Osama Date: 4/5/13, 5:18 PM
 */
/**
 * update by : Marwa Goda Date: 3/6/2013 Adding file contentType validation
 */
@Component
public class ExtractSourceDataFormBeanValidator implements Validator {

    // accepted content types for uploaded files.
    private static final String TEXT_CSV = "text/csv";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String APPLICATION_CSV = "application/csv";
    private static final String TEXT_COMMA_SEPARATED_VALUES = "text/comma-separated-values";
    private static final String APPLICATION_EXCEL = "application/excel";
    private static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
    private static final String APPLICATION_VND_MSEXCEL = "application/vnd.msexcel";
    private static final String TEXT_ANYTEXT = "text/anytext";
    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    private static final String APPLICATION_TXT = "application/txt";

    private static final Logger logger = LoggerFactory.getLogger(ExtractSourceDataFormBeanValidator.class);
    @Autowired
    CacheService cacheService;

    @Value("#{myProps['text.supported.fileTypes']}")
    private String supportedTextFileTypes;

    @Override
    public boolean supports(Class<?> aClass) {
        return ExtractSourceDataFormBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    @SuppressWarnings("unchecked")
    public void validate(Object target, Errors errors, String uploadedFileId) {

        if (!(target instanceof ExtractSourceDataFormBean)) {
            throw new GenericException("Invalid bean type");
        }

        ExtractSourceDataFormBean formBean = (ExtractSourceDataFormBean) target;

        // Validate delimiter
        if (Delimiter.OTHER.equals(formBean.getDelimiter())) {
            ValidationUtils.
                    rejectIfEmptyOrWhitespace(errors, "extractSourceDataFormBean.otherDelimiter",
                            "ExtractSourceDataFormBean.field.required", "Please, Enter delimiter value");
        }

        // Validate ignore lines value
        if (!StringUtils.isEmpty(formBean.getIgnoreLines()) && !StringUtils.isNumeric(formBean.getIgnoreLines())) {
            errors.rejectValue("extractSourceDataFormBean.ignoreLines",
                    "ExtractSourceDataFormBean.field.required", "Ignored lines value must be a number");
        }

        List<String> lines = (List<String>) cacheService.get(TEXT_FILE_CACHE, uploadedFileId);

        // Validate file uploaded
        if (lines == null) {

            errors.rejectValue("extractSourceDataFormBean.fileName",
                    "field.fileName", "Please upload source data file");

        } else if (lines.isEmpty()) {

            errors.rejectValue("extractSourceDataFormBean.fileName",
                    "field.fileName", "Please upload source data");

        } else {
            int expectedNumberOfLines;
            int ignoreLines = 0;
            if (StringUtils.isNotEmpty(formBean.getIgnoreLines())
                    && StringUtils.isNumeric(formBean.getIgnoreLines())) {
                ignoreLines = Integer.parseInt(formBean.getIgnoreLines());
            }
            if (Header.DONT_USE.equals(formBean.getHeader())) {
                expectedNumberOfLines = ignoreLines + 1;
            } else {
                expectedNumberOfLines = ignoreLines + 2;
            }

            // Validate ignored lines less than file size Or use headers selected and not valid number of lines
            if (lines.size() < expectedNumberOfLines) {
                errors.rejectValue("extractSourceDataFormBean.ignoreLines",
                        "ExtractSourceDataFormBean.field.required", "incorrect number of lines in the uploaded file");
            }

        }

    }

    public void validateUploadedFile(Object target, Errors errors) {

        if (!(target instanceof ExtractSourceDataFormBean)) {
            throw new GenericException("Invalid bean type");
        }

        ExtractSourceDataFormBean formBean = (ExtractSourceDataFormBean) target;
        String[] supportedFileTypes = supportedTextFileTypes.split(",");

        // Validate file extension
        String fileName = formBean.getUploadFile().getOriginalFilename();

        if (StringUtils.isEmpty(fileName) || StringUtils.isWhitespace(fileName)) {

            errors.rejectValue("extractSourceDataFormBean.fileName",
                    "field.fileName", "Please upload source data file");

        } else {
            boolean validType = false;
            for (String type : supportedFileTypes) {
                
                type = "(.*)" + type ;
                
                if(type.endsWith("*")){
                    type = type.substring(0, type.length()-1);
                    type = type + "(\\d+$)";
                }
                if (fileName.matches(type)) {
                    validType = true;
                    break;
                }
            }
            if (!validType) {
                errors.rejectValue("extractSourceDataFormBean.fileName",
                        "ExtractSourceDataFormBean.field.required", "Invalid file type.");
            } else {
                Map<String, String> validContentTypesMap = prepareValidContentTypesMap();

                boolean isValidContent = validateFileContentType(formBean.getUploadFile(), validContentTypesMap);

                if (!isValidContent) {
                    errors.rejectValue("extractSourceDataFormBean.fileName",
                            "ExtractSourceDataFormBean.field.required", "Invalid file content type");
                }
            }
            // Validate file size
            long MAX_FILE_SIZE = 4194304;
            if (formBean.getUploadFile() != null
                    & formBean.getUploadFile().getSize() > MAX_FILE_SIZE) {

                errors.rejectValue("extractSourceDataFormBean.fileName",
                        "ExtractSourceDataFormBean.field.required", "File size must not be larger than 4 MB (4,194,304 bytes)");
            }

            if (formBean.getUploadFile() != null) {

                SourceDataParser parser = new SourceDataParser(formBean.getUploadFile());
                try {

                    if (parser.getLines().isEmpty()) {
                        errors.rejectValue("extractSourceDataFormBean.fileName",
                                "ExtractSourceDataFormBean.field.required", "Invalid file. The file is empty");
                    }

                } catch (IOException e) {

                    throw new BusinessException("Error parsing sheets, " + e.getMessage());
                }
            }
        }
    }

    private Map<String, String> prepareValidContentTypesMap() {
        Map<String, String> validContentTypesMap = new HashMap<String, String>();

        validContentTypesMap.put(TEXT_CSV, TEXT_CSV);
        validContentTypesMap.put(TEXT_PLAIN, TEXT_PLAIN);
        validContentTypesMap.put(APPLICATION_CSV, APPLICATION_CSV);
        validContentTypesMap.put(TEXT_COMMA_SEPARATED_VALUES, TEXT_COMMA_SEPARATED_VALUES);
        validContentTypesMap.put(APPLICATION_EXCEL, APPLICATION_EXCEL);
        validContentTypesMap.put(APPLICATION_VND_MS_EXCEL, APPLICATION_VND_MS_EXCEL);
        validContentTypesMap.put(APPLICATION_VND_MSEXCEL, APPLICATION_VND_MSEXCEL);
        validContentTypesMap.put(TEXT_ANYTEXT, TEXT_ANYTEXT);
        validContentTypesMap.put(APPLICATION_OCTET_STREAM, APPLICATION_OCTET_STREAM);
        validContentTypesMap.put(APPLICATION_TXT, APPLICATION_TXT);
        return validContentTypesMap;
    }

    private boolean validateFileContentType(MultipartFile uploadFile, Map<String, String> validContentTypesMap) {

        boolean valid = false;
        Metadata fileMetadata = new Metadata();
        fileMetadata.set(Metadata.RESOURCE_NAME_KEY, uploadFile.getOriginalFilename());

        Parser parser = null;
        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
            parser = new AutoDetectParser();
            parser.parse(inputStream, new BodyContentHandler(), fileMetadata);
            String contentType = fileMetadata.get(Metadata.CONTENT_TYPE);
            valid = validContentTypesMap.containsKey(contentType);
        } catch (Exception e) {
            logger.error("ExtractSourceDataFormBeanValidator.validateFileContentType() : " + e);
            valid = false;
        } finally {

            IOUtils.closeQuietly(inputStream);
        }
        return valid;
    }

}
