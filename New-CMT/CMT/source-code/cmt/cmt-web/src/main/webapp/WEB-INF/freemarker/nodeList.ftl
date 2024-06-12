[#ftl/]
[#import "spring.ftl" as spring /]
[#import "macros.ftl" as cmt /]

[#if nodeList??]
[#if nodeList?has_content]
[@spring.bind path="job.job.systemNode"/]
<td class="Labeltd"><script>$(".SecondRadioOption").show();</script><label>System Node</label> <span class="RequiredField">*</span></td>
<td class="inputltd RadioInputs" colspan="2">

    <select id="cd-dropdown2" class="cd-select required error" name="${spring.status.expression}">
        <option value="system_nodes" selected>All Nodes</option>
        [#list nodeList as node]
        [#assign selectedStatus][/#assign]
        [#if job.job.systemNode??]
        [#if job.job.systemNode == node]
        [#assign selectedStatus]selected[/#assign]
        [/#if]
        [/#if]
        <option value="${node}" ${selectedStatus}>${node}</option>
        [/#list]
    </select>
</td>
[#else]
        <script>$(".SecondRadioOption").hide();</script>
[/#if]
[#else]
        <script>$(".SecondRadioOption").hide();</script>
[/#if]
