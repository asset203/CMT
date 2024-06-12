[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#global title="Capacity Management Tool"/]
[#global isKPISelected="selected"/]
[#assign javaScript]
<script type="text/javascript" src="[@spring.url '/js/system-kpi-properties-edit.js'/]"></script>
<script type="text/javascript" src="[@spring.url '/js/common-validation.js'/]"></script>
<script>
  $(document).ready(function () {
    bindDeleteClickHandler();
  });


</script>
[/#assign]
[@cmt.htmlHead javaScript/]
[@cmt.navigation/]
<div class="TabContent">
  <h1>Edit Thresholds & KPIs </h1>

  <div class="FormContainer">
    <form action="[@spring.url '/manageKpis/updateKpi'/]" method="post">
      <table width="100%" cellspacing="0" cellpadding="0" border="0" class="FormTable">
        <tr>
          <td class="Labeltd"><label for="">System Name </label> <span class="RequiredField">*</span>
          </td>
          <td class="inputltd">

          [@spring.formInput path="formBean.systemName"
          attributes=" readonly size=\"25\" class=\"CustomInput readonly \"   " /]
            <br>
            <label class=" systemNameError" id="systemNameError"></label>
            <br>
          [@spring.showErrors "<br>" "error"/]
          </td>
        </tr>
        <tr>
          <td class="Labeltd"><label>System Node</label> <span class="RequiredField">*</span></td>
        [#assign nodeName=""/]
        [#if systemNode.nodeName??][#assign nodeName=systemNode.nodeName][/#if]
          <td class="inputltd">
            <input id="" name="" size="25" class="CustomInput readonly"
                   type="text" readonly value="${nodeName}">
          </td>
        </tr>
        <tr>
          <td class="Labeltd"><label for="">In Use</label> <span class="RequiredField">*</span></td>
          <td class="inputltd RadioInputsSmall">
            <label>
            [@cmt.formCheckbox path="formBean.inUse" /]

              Yes
            </label>

          </td>
        </tr>
      </table>
      <hr>
      <div id="nodePropertiesDiv">
      [#include "nodeProperties.ftl"/]
      </div>

      <div class="WizardBtns">
					<span class="SubmitButtons ">
						<span class="SubmitButton BtnWidth1"><input type="submit" value="Save" class="submit"> </span></span>

        <span class="SubmitButtons Gray "> <span class="SubmitButton BtnWidth1">
          <input type="reset" value="Cancel" onClick="location.href='[@spring.url '/manageKpis/systemKpis'/]'"> </span></span>
      </div>

    </form>
  </div>

</div>
[@cmt.footer/]