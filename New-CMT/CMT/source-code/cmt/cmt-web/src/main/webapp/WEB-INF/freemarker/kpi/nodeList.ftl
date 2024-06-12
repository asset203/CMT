[#ftl/]
[#import "../spring.ftl" as spring /]
[#import "../macros.ftl" as cmt /]
[#--
    Author: Alia.Adel

    This page is common between both KPI threshold & manage KPI notification list modules,
    please don't update without retest

--]
[#if nodeList??]
    [#if nodeList?has_content]
        <td class="Labeltd"> <label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
        <td class="inputltd RadioInputs" colspan="2">
            [#assign selectedNode][#if formBean.nodeID??]${formBean.nodeID?c}[#else]-1[/#if][/#assign]
            [@spring.bind path="formBean.nodeID"/]
            <select id="cd-dropdown1" class="cd-select required error " name="${spring.status.expression}">
                <option value="-1">Please Select</option>
                [#list nodeList as node]
                <option value="${node.id?c}" [#if selectedNode == (node.id?c)]selected[/#if]>${node.name}</option>
                [/#list]
            </select>
            [@spring.showErrors "<br>" "error"/]
        </td>
    [#else]
        <td class="Labeltd"> <label for="cd-dropdown1">Select Node</label> <span class="RequiredField">*</span></td>
        <td class="inputltd RadioInputs" colspan="2">
            <div class="NoResults">The selected system has no nodes</div>
            [@spring.formHiddenInput path="formBean.nodeID"/]
            [@spring.showErrors "<br>" "error"/]
        </td>
    [/#if]
[/#if]
