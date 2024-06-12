[#ftl/]
[#import "spring.ftl" as spring /]

[#-- Progress image--]
[#assign progressImg]
   '<div class="loading" id="progressBar"></div>'
[/#assign]
<div class="Paging">
    <div class="Paging pager green" id="Pager">
        [#if gridBean.paginationBean.currentPageIndex == 1]
            <div class="btn disabled" title="First">First</div>
        [#else]
            <div class="btn"
                id="paginator-first" title="First" onclick="first();" style="cursor:pointer">
                First
            </div>
        [/#if]
        [#if  (gridBean.paginationBean.currentPageIndex == 1)]
            <div class="btn disabled" title="Previous">Previous</div>
        [#else]
            <div class="btn" title="Previous"
                id="paginator-previous" style="cursor:pointer" onclick="previous();">
                Previous
            </div>
        [/#if]
        <ul style="width: 60px;">
            [#list gridBean.paginationBean.startPageIndex..gridBean.paginationBean.endPageIndex as pageLinkIndex]
                <li>
                    <a  style="cursor:pointer" onclick="paginate(${pageLinkIndex});"
                        [#if pageLinkIndex == gridBean.paginationBean.currentPageIndex]
                            class="green normal active"
                        [#else]
                            class="green normal"
                        [/#if]
                        id="paginator-page">
                        ${pageLinkIndex}
                    </a>
                </li>
            [/#list]
        </ul>

        [#if (gridBean.paginationBean.totalNoPages == gridBean.paginationBean.currentPageIndex)]
            <div class="btn disabled" title="Next">Next</div>
        [#else]
            <div class="btn"
                title="Next" id="paginator-next"
                onclick="next();" style="cursor:pointer">
                Next
            </div>
        [/#if]
        [#if gridBean.paginationBean.currentPageIndex == gridBean.paginationBean.totalNoPages]
            <div class="btn disabled" title="Last">Last</div>
        [#else]
            <div class="btn"
                title="Last" id="paginator-last"
                onclick="last();" style="cursor:pointer">
                Last
            </div>
        [/#if]
        <br>
        <strong> Page ${gridBean.paginationBean.currentPageIndex}/ ${gridBean.paginationBean.totalNoPages} </strong>
    </div>
</div>

<script>
    $.ajaxSetup({
      cache: false
    });
	function next (){
	    $('#table-div').html(${progressImg});
		$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/next.htm'/]',{newSelections : trackSelectedIds()  ,cancelledSelections: trackNotSelectedIds()} ,function(data) {
		  	$('#table-div').html(data);
		  	return false;
		});
	}
	
	function previous (){
	    $('#table-div').html(${progressImg});
		$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/previous.htm'/]',{newSelections : trackSelectedIds()  ,cancelledSelections: trackNotSelectedIds()} ,function(data) {
		  	$('#table-div').html(data);
		  	return false;
		});
	}


	function first (){
	    $('#table-div').html(${progressImg});
		$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/first.htm'/]', {newSelections : trackSelectedIds()  ,cancelledSelections: trackNotSelectedIds()} ,function(data) {
		  	$('#table-div').html(data);
		  	return false;
		});
	}
	
	function last (){
	    $('#table-div').html(${progressImg});
		$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/last.htm'/]',{newSelections : trackSelectedIds()  ,cancelledSelections: trackNotSelectedIds()} ,function(data) {
		  	$('#table-div').html(data);
		  	return false;
		});
	}

	function paginate (index){
	    $('#table-div').html(${progressImg});
		$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/refresh.htm'/]',{pageIndex : index ,newSelections : trackSelectedIds()  ,cancelledSelections: trackNotSelectedIds()} ,function(data) {
		  	$('#table-div').html(data);
		  	return false;
		});
	}
	
	function sortGrid (sortField , sortOrder ){
	    $('#table-div').html(${progressImg});
		$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/sort.htm'/]',{name : sortField , order :sortOrder ,newSelections : trackSelectedIds()  ,cancelledSelections: trackNotSelectedIds()} ,function(data) {
		  	$('#table-div').html(data);
		  	return false;
		});
			
	}
	
	 function updateRowsPerPage(elem, currentRowsPerPage){	    	 	
		var numericExpression = /^[0-9]+$/;
		if(elem.value.match(numericExpression) && elem.value!=0){
			var rows = $("#rows-txt").val();
			$('#table-div').html(${progressImg});
			$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/update.htm'/]',{rowsPerPage : rows ,newSelections : trackSelectedIds()  ,cancelledSelections: trackNotSelectedIds()} ,function(data) {
				$('#table-div').html(data);
				return false;
			});
			return true;
		}else{
			$("#table-error").show();
			$("#table-error").html('Numbers only allowed in view per page and should be more than 0');
			$("#rows-txt").val(currentRowsPerPage);
			elem.focus();
			return false;
		}
	}

	
	[#--Checkbox handling--]
	selectOne();
	
	function selectAll (checked){
        $('.gridCheckBox').attr('checked', checked);
	}

	function selectOne (){
        $('#select-all').attr('checked', $(".gridCheckBox").length == $(".gridCheckBox:checked").length);
	}

	function updateSelections (){

		var selected_ids = null;
		var not_selected_ids = null;
		if($(".gridCheckBox:checked").length > 0) {
             selected_ids = [];
            
             $(".gridCheckBox:checked").each(function() {
             
                selected_ids.push($(this).val());
            });
        }
        
        if($(".gridCheckBox:not(:checked)").length > 0) {
             not_selected_ids = [];
            $(".gridCheckBox:not(:checked)").each(function() {
                not_selected_ids.push($(this).val());
            });
        }
        
        if (($(".gridCheckBox:checked").length > 0)||($(".gridCheckBox:not(:checked)").length > 0)){
        	$.post('[@spring.url '/${gridBean.gridUserInterface.controllerUrlMapping}/updateSelection.htm'/]', {newSelections : selected_ids , cancelledSelections : not_selected_ids});
        }
        
	}
	
	function trackSelectedIds(){
        if($(".gridCheckBox:checked").length > 0) {
            var selected_ids = [];
            $(".gridCheckBox:checked").each(function() {
                selected_ids.push($(this).val());
            });
            return selected_ids.toString();
        }
        return "";
    }

    function trackNotSelectedIds(){
        if($(".gridCheckBox:not(:checked)").length > 0) {
            var not_selected_ids = [];
            $(".gridCheckBox:not(:checked)").each(function() {
                not_selected_ids.push($(this).val());
            });
            return not_selected_ids.toString();
        }
        return "";
    }

</script>


