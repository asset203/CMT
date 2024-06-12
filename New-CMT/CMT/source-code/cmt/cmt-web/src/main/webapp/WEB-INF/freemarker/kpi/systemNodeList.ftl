[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]

[#if nodeList??]
  [#if nodeList?has_content]
  <td class="Labeltd"><label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
  <td class="inputltd RadioInputs" colspan="2">
    [#assign selectedNode][#if formBean.nodeID??]${formBean.nodeID?c}[#else]-1[/#if][/#assign]
    [@spring.bind path="formBean.nodeID"/]
    <select id="cd-dropdown1" class="cd-select required error " name="${spring.status.expression}">
      <option value="-1">Please Select</option>
      [#list nodeList as node]
        <option value="${node.systemNodeId?c}"
                [#if selectedNode == (node.systemNodeId?c)]selected[/#if]>${node.nodeName}</option>
      [/#list]
    </select>
    [@spring.showErrors "<br>" "error"/]
  </td>
  [#else]
  <td class="Labeltd"><label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
  <td class="inputltd RadioInputs" colspan="2">
    <div class="NoResults" id="noNode">The selected system has no nodes</div>
    [@spring.formHiddenInput path="formBean.nodeID"/]
    [@spring.showErrors "<br>" "error"/]
  </td>
  [/#if]
[/#if]
