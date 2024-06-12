[#ftl/]
[#import "../../spring.ftl" as spring /]
[#import "../../macros.ftl" as cmt /]
[#import "wizard-macros.ftl" as wizard /]
[#global title="Capacity Management Tool"/]
[#global isDCSelected="selected"/]

[@cmt.htmlHead ""/]
[@cmt.navigation/]

<script src="[@spring.url '/js/data-collection.js'/]" type="text/javascript"></script>

[@wizard.body]

<form id="extractXmlSourceColumnsForm" method="POST" action="javascript:void(0);" enctype="multipart/form-data">

[@wizard.defineDataCollectionMappingFormBeanHiddenFields/]
[@wizard.defineOutputTableHiddenFields/]
[@wizard.dataCollectionWizardHiddenFields/]
[@wizard.extractSourceColumnFormBeanHiddenFields/]
[@wizard.dataCollectionTypeHiddenFields/]


[@spring.formHiddenInput "dataCollectionWizardFormBean.extractXMLSourceColumns.fileName"/]

[@spring.bind path="dataCollectionWizardFormBean.extractXMLSourceColumns.xmlVendor"/]
[#assign xmlVendorErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.extractXMLSourceColumns.xmlConverter"/]
[#assign xmlConverterErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

[@spring.bind path="dataCollectionWizardFormBean.extractXMLSourceColumns.uploadFile"/]
[#assign uploadFileErrors][@spring.showErrors "<br>" "validationerror"/][/#assign]

<div class="CustomContent2">
    Data Collection Type:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionType.description}</span> <span
        class="Space50"></span> Generic Process Name:
    <span>${dataCollectionWizardFormBean.dataCollectionTypeFormBean.dataCollectionName}</span>
</div>
<h2>Extract Source Columns for XML </h2>



[#if xmlVendorErrors?has_content || xmlConverterErrors?has_content || uploadFileErrors?has_content]
<div class="CustomContent3 ErrorMessages">
    <h4>Error Message </h4>

    [#if xmlVendorErrors?has_content]
    <p>${xmlVendorErrors}</p>
    [/#if]

    [#if xmlConverterErrors?has_content]
    <p>${xmlConverterErrors}</p>
    [/#if]

    [#if uploadFileErrors?has_content]
    <p>${uploadFileErrors}</p>
    [/#if]

</div>
[#else]
[#if dataCollectionWizardFormBean.extractXMLSourceColumns.fileName?has_content]
<div class="SuccessMsg">
    ${dataCollectionWizardFormBean.extractXMLSourceColumns.fileName} was uploaded successfully
</div>
[/#if]
[/#if]

<div class="CustomContent1">
    <h4>Instructions</h4>
    <a href="#d" class="Expandable">
        <span class="HideContent"> [ - ] Hide</span>
        <span class="ShowContent" style="display:none"> [ + ] Show</span>
    </a>

    <div class=" ExpandableContent FirstRadioOptionRow "
         style="[#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity != 'SIMPLE']display:none;[/#if]">
        <p>Please upload a sample file and configure data extraction parameters. The file should have a single level of nesting tags.</p>
    </div>
    <div class="ExpandableContent SecondRadioOptionRow"
         style="[#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity != 'VENDOR_SPECIFIC']display:none;[/#if]">
        <p>Please select the vendor converter for the data source files.</p>
    </div>
</div>

<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
    <tr>
        <td class="Labeltd"><label for="">XML Complexity</label> <span class="RequiredField">*</span></td>
        <td colspan="2" class="inputltd RadioInputs ">
            <label class="FirstRadioOption">
                <input type="radio"
                       name="extractXMLSourceColumns.xmlComplexity"
                       id="extractXMLSourceColumns.xmlComplexity0"
                       value="SIMPLE"
                [#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity ==
                "SIMPLE"]checked="checked"[/#if]/>Simple
            </label>


            <label class="SecondRadioOption">
                <input type="radio"
                       name="extractXMLSourceColumns.xmlComplexity"
                       id="extractXMLSourceColumns.xmlComplexity1"
                       value="VENDOR_SPECIFIC"
                [#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity ==
                "VENDOR_SPECIFIC"]checked="checked"[/#if]/>Vendor Specific
            </label>
        </td>
    </tr>
</table>

<div class="FirstRadioOptionRow"
     style="[#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity != "SIMPLE"]display:none;[/#if]padding:10px 0">

<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
    <tr>
        <td class="Labeltd"> <label for="">Upload file</label> <span class="RequiredField">*</span></td>
        <td colspan="2" class="inputltd">
			<span class="text largeInput FileUpload"> 
				<input type="text" id="" >
				[@spring.formInput path="dataCollectionWizardFormBean.extractXMLSourceColumns.uploadFile"
					fieldType="file" attributes="accept=\".xml\""/]								
			</span>						
			<span class="SubmitButtons"> 
				 <a class="SubmitButton BtnWidth1" href="#D">Browse</a> 
			</span>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"></td>
        <td colspan="2" class="inputltd">
            Type must be xml, size must not be larger than 4 MB<br><br>
                <span class="SubmitButtons FloatLeft">
                    <span class="SubmitButton BtnWidth1">
                        <input type="submit" value="Upload" class="submit BtnWidth1"
                               onclick="updateForm('[@spring.url '/dataCollection/define/extractXMLSourceColumns/uploadSource'/]', 'POST', 'extractXmlSourceColumnsForm');">
                    </span>
                </span>
        </td>
    </tr>
</table>

<div class="WizardBtns">

               <span class="SubmitButtons FloatLeft Gray">
                   <span class="SubmitButton BtnWidth1">
                       <input type="submit" value="Back" class="submit BtnWidth1"
                              onclick="updateForm('[@spring.url '/dataCollection/define/type?back=true'/]', 'POST', 'extractXmlSourceColumnsForm');">
                   </span>
               </span>

               <span class="SubmitButtons FloatLeft Gray">
                    <a href="[@spring.url '/dataCollection/manage'/]" class="SubmitButton BtnWidth1">Cancel</a>
               </span>

               <span class="SubmitButtons FloatRight">
                   <span class="SubmitButton BtnWidth1">
                       <input type="submit" value="Next" class="submit BtnWidth1 "
                              onclick="updateForm('[@spring.url '/dataCollection/define/extractXMLSourceColumns'/]', 'POST', 'extractXmlSourceColumnsForm');">
                   </span>
               </span>
</div>
</div>

<div class="SecondRadioOptionRow"
     style="[#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlComplexity == "SIMPLE"]display:none;[/#if]padding:10px 0">

<table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable ">
    <tr>
        <td class="Labeltd"><label for="name">Vendor Specific Name </label> <span class="RequiredField">*</span>
        </td>
        <td class="inputltd" colspan="2">
            <select class="cd-select required error" name="extractXMLSourceColumns.xmlVendor"
                    onchange="navigateTo('[@spring.url '/dataCollection/define/extractXMLSourceColumns/reloadConverters'/]', 'POST', 'extractXmlSourceColumnsForm');"
                    id="extractXMLSourceColumns.xmlVendor">
                [#list dataCollectionWizardFormBean.extractXMLSourceColumns.xmlVendors?keys as vendor]
                <option value="${vendor}"
                [#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlVendor?has_content &&
                vendor == dataCollectionWizardFormBean.extractXMLSourceColumns.xmlVendor]selected="selected"[/#if]>
                ${dataCollectionWizardFormBean.extractXMLSourceColumns.xmlVendors[vendor]}
                </option>
                [/#list]
            </select>
        </td>
    </tr>
    <tr>
        <td class="Labeltd"><label for="name">Vendor Specific Converter Name </label> <span
                class="RequiredField">*</span></td>
        <td class="inputltd" colspan="2">
            <select class="cd-select required error" name="extractXMLSourceColumns.xmlConverter"
                    id="extractXMLSourceColumns.xmlConverter">
                <option value="-1">Select ...</option>
                [#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlConverterTypes?has_content]
                [#list dataCollectionWizardFormBean.extractXMLSourceColumns.xmlConverterTypes?keys as converter]
                <option value="${converter}"
                [#if dataCollectionWizardFormBean.extractXMLSourceColumns.xmlConverter?has_content &&
                converter == dataCollectionWizardFormBean.extractXMLSourceColumns.xmlConverter]selected="selected"[/#if]>
                ${dataCollectionWizardFormBean.extractXMLSourceColumns.xmlConverterTypes[converter]}
                </option>
                [/#list]
                [/#if]
            </select>
        </td>
    </tr>
</table>

<div class="WizardBtns">

               <span class="SubmitButtons FloatLeft Gray">
                   <span class="SubmitButton BtnWidth1">
                       <input type="submit" value="Back" class="submit BtnWidth1"
                              onclick="updateForm('[@spring.url '/dataCollection/define/type?back=true'/]', 'POST', 'extractXmlSourceColumnsForm');">
                   </span>
               </span>

               <span class="SubmitButtons FloatLeft Gray">
                    <a href="[@spring.url '/dataCollection/manage'/]" class="SubmitButton BtnWidth1">Cancel</a>
               </span>

               <span class="SubmitButtons FloatRight">
                   <span class="SubmitButton BtnWidth1">
                       <input type="submit" value="Next" class="submit BtnWidth1"
                              onclick="updateForm('[@spring.url '/dataCollection/define/extractXMLSourceColumns'/]', 'POST', 'extractXmlSourceColumnsForm');">
                   </span>
               </span>
</div>
</div>

</form>
[/@wizard.body]