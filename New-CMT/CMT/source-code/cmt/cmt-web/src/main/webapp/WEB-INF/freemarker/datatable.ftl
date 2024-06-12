[#ftl/]
[#import "spring.ftl" as spring /]


[#if value?has_content]

[#setting url_escaping_charset="UTF-8"/]

[#if (gridBean.gridUserInterface.width)?has_content]
[#assign gridWidth=gridBean.gridUserInterface.width]
[#else]
[#assign gridWidth="100%"]
[/#if]

[#if (gridBean.gridUserInterface.id)?has_content]
[#assign gridId=gridBean.gridUserInterface.id]
[#else]
[#assign gridId="grid-id"]
[/#if]

<div class="ErrorMsg" style="display:none;" id="table-error"></div>

[#if gridBean.gridUserInterface.scrollable?has_content]
[#if gridBean.gridUserInterface.scrollable?string == "true"]
<div id="h-scroll-div">
    [/#if]
    [/#if]
    [#--<script type="text/javascript">
    $(document).ready( function(){
    $('#${gridId}').tableScroll();
    });
</script>
    --]
    [#-- Sorting script --]
    <script type="text/javascript">
        $(".SortingImg").click(function() {
        $(this).next(".SortContent").show();
        $('.SortContent').on("mouseleave",function(){
        $(this).hide();
        });
        });
    </script>

    <div class="GridHeader">
        <div class="GridControl">
            <div>
                Total number of rows: ${gridBean.paginationBean.totalNoRows},
                Display <input type="text"  value="${gridBean.paginationBean.rowsPerPage}" id="rows-txt"> per page
                <a href="#d" class="RefreshGrid btn" id="update-rows"
                   onclick="updateRowsPerPage(document.getElementById('rows-txt'),
                        ${gridBean.paginationBean.rowsPerPage});">Refresh Grid</a>
            </div>
        </div>
    </div>
    <div class="Grid">
        <table cellspacing="0" border="0" cellpadding="0" width="100%" id="${gridId}">

            [#-- Table Column Header--]
            [#list gridBean.gridUserInterface.columns as column]
            <col [#if column.width?? && column.width?has_content]width="${column.width}"[/#if]>
            [/#list]
            <thead>
            <tr>[#list gridBean.gridUserInterface.columns as column]<th [#if column.width?? && column.width?has_content]width="${column.width}"[/#if]>
                [#if column.columnType =='CHECKBOX']
                <input type="checkbox" id="select-all" name="select-all" onclick="selectAll(this.checked);"/>
                [#else]
                <span>${column.label}</span>
                [/#if]
                [#if column.sortable]
                <div class="SortingIcon">
                    <a href="#d" class="SortingImg">Sort</a>
                    <div class="SortContent">
                        <a href="#d" class="ascending"
                           onclick=" sortGrid('${column.dbColumn}', 'true');">Ascending</a>
                        <a href="#d" class="descending"
                           onclick=" sortGrid('${column.dbColumn}', 'false');">Descending</a>
                    </div>
                </div>
                [/#if]
                </th>[/#list]</tr>
            </thead>
            [#-- Table Body--]
            <tbody>
            [#list value as var]
            <tr>[#list gridBean.gridUserInterface.columns as column]<td>
                [#if column.dataAvailable?has_content]
                [#assign showData=(column.dataAvailable)?eval]
                [#else]
                [#assign showData="true"]
                [/#if]
                [#if showData?string == "true"]
                [#if column.columnType == 'LINK']
                <a
                [#if (column.href)??]
                href ="[@spring.url '${column.href}'/]"
                [#elseif (column.dynamicHref)?has_content]
                href ="[@spring.url '${(column.dynamicHref?eval)}'/]"
                [/#if]

                [#if (column.onClick)?has_content]
                onclick="${column.onClick}"
                [#elseif (column.dynamicOnClick)?has_content]
                onclick="${(column.dynamicOnClick?eval)}"
                [/#if]
                [#if column.linkStyle??]
                class="${column.linkStyle}"
                [/#if]
                >
                [#if (column.linkLabel)?has_content]
                ${column.linkLabel}
                [#elseif (column.dynamicLinkLabel)?has_content]
                ${(""+column.dynamicLinkLabel+"")?eval}
                [/#if]
                </a>
                [#elseif column.columnType == 'LINK_LIST']
                [#list column.linkList as link]
                [#assign href]
                [#if (((link.enable)?has_content) == false) || (link.enable?eval)]
                [#if link.href?has_content && (link.dynamicHref)?has_content]
                ${link.href}${(link.dynamicHref?eval)}
                [#elseif link.href?has_content]
                ${link.href}
                [/#if]
                [/#if]
                [/#assign]
                [#if link.enable?? && !(link.enable?eval)]
                <label
                [#if link.linkStyle??]
                class="${link.linkStyle}"
                [/#if]>
                [#if (link.linkLabel)?has_content]
                ${link.linkLabel}
                [/#if]
                </label>
                [#else]
                <a
                [#if href?trim?has_content]
                href ="[@spring.url '${href?trim}'/]"
                [#else]href ="#DeleteUser"[/#if]

                [#if ((((link.enable)?has_content) == false) || (link.enable?eval)) && ((link.onClick)?has_content)]
                onclick="${link.onClick}([#if (link.dynamicHref)?has_content]'${(link.dynamicHref?eval)}'[/#if])"
                [/#if]

                [#if link.linkStyle??]
                class="${link.linkStyle}"
                [/#if]
                >
                [#if (link.linkLabel)?has_content]
                ${link.linkLabel}

                [/#if]
                </a>
                [/#if]
                [/#list]
                [#elseif column.columnType == 'LABEL']
                [#if (column.modelProperty)?has_content]
                ${("var."+column.modelProperty)?eval}
                [#elseif (column.expression)?has_content]
                [#if ((column.expression)?eval)??]
                [#if column.splitAfter?? && column.splitAfter?has_content]
                [#assign split=(column.splitAfter?number)/]
                [#assign labelContent]${(column.expression)?eval}[/#assign]
                [#if labelContent?length > split]
                [#assign loopCount = ((((labelContent?length)/split)?round)?number -1)/]
                [#assign labelLength]${labelContent?length}[/#assign]
                [#list 1..loopCount as count]
                [#if labelContent?length > split-1]
                ${labelContent?substring(0, split)} <br>
                [#assign labelContent= labelContent?replace(labelContent?substring(0, split),"")/]
                [#else]
                ${labelContent}[#break]
                [/#if]
                [/#list]
                [#if labelContent?length > split]
                ${labelContent?substring(0, split)} <br>
                [/#if]
                ${labelContent}
                [#else]
                ${labelContent}
                [/#if]
                [#else]
                ${(column.expression)?eval}
                [/#if]
                [/#if]
                [/#if]

                [#elseif column.columnType == 'CHECKBOX']
                [#assign rowId="${(column.checkBoxRowId)?eval}"]
                <input type="checkbox" class="gridCheckBox" name="selectedKeys" value="${rowId}" onclick="selectOne();"
                [#if (gridBean.selectedKeys?seq_contains(rowId)) ]checked [/#if]
                [/#if]
                [#else]
                &nbsp;
                [/#if]
            </td>[/#list]</tr>
            [/#list]
            </tbody>
        </table>
    </div>
    [#if (gridBean.gridUserInterface.scrollable)?has_content]
    [#if gridBean.gridUserInterface.scrollable?string == "true"]
</div>
[/#if]
[/#if]
[#include "paginator.ftl"]

[#else]
<div class="NoResults">No results found</div>
[/#if]