var scenarioCounter = 0;
var currentScenario = 0; 
var menucounter = 1;
var tabTemplate;
var tabs;
var tabs2;
var lastAction;
var readyCnt = 0;
var savedSchemata;
var savedTasks;
var openedTasks = new Array();
var duplicates = new Object();
var sourceForConn = new Object();
/* variables for constant/function icons positioning */
var shiftXpixels = 10;
var shiftYpixels = 40;
var maxIconWidth = 150;
var csrftoken = getCookie('XSRF-TOKEN');
//common options for source arrow connections
var commonSource = {
    connector:"Straight",
    isSource:true,
    anchor:"Right",
    endpoint:"Blank",
    connectorStyle : { strokeStyle:"black", lineWidth:2 },
    connectorOverlays: [ [ "Arrow", {
      width: 10,
      length: 12,
      foldback: 0,
      location: 1,
      id: "arrow"
    } ] ]
};
//common options for target arrow connections
var commonTarget = {
    isTarget:true, 
    anchor:"Left",
    endpoint:"Blank"
};

//takes a JSON object as input and creates the source and target schema trees
//the existing connections between them and the corresponding visual components
function loadSchemaTrees(taskName, JSONTreesData){
    
    //create a new instance of jPlumb for each tab
    var newplumb = jsPlumb.getInstance();
    scenarioCounter++;
    //on window resize, re-draw connections (only for the visible tab)
    $( window ).resize(function() {
        var visibleTab = $(".ui-tabs-panel:visible").attr("id");
        var container = $(newplumb.getContainer()).attr('id');
        if(container === visibleTab){
            newplumb.repaintEverything();
        }
    });

    var sourceTreeArray =[];   
    for (var i=0;i<JSONTreesData.trees[0].data.length;i++){
       sourceTreeArray.push({id: JSONTreesData.trees[0].data[i].id, 
        parent:JSONTreesData.trees[0].data[i].parent, 
        text:JSONTreesData.trees[0].data[i].text, 
        icon:"css/images/"+JSONTreesData.trees[0].data[i].icon,
        data: {selectionCondition: "", duplicate : false}
       });
    }
    var targetTreeArray =[];   
    for (var k=0;k<JSONTreesData.trees[1].data.length;k++){
       targetTreeArray.push({id: JSONTreesData.trees[1].data[k].id, 
        parent:JSONTreesData.trees[1].data[k].parent, 
        text:JSONTreesData.trees[1].data[k].text, 
        icon:"css/images/"+JSONTreesData.trees[1].data[k].icon,
        data: {selectionCondition: "", duplicate : false}
       });
    } 
    
    addTreeTab(taskName);
    makeTrees(sourceTreeArray, targetTreeArray, "jstreeSource"+ scenarioCounter, "jstreeTarget"+ scenarioCounter, newplumb, JSONTreesData); 
    addProjectTree(scenarioCounter);              
    currentScenario=scenarioCounter;  
}

//adds the Tab with the source and target tree
function addTreeTab(taskName) {
    var label = scenarioCounter+". "+taskName,
    id = "schemaTabs-" + scenarioCounter,
    li = $( tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) ),
    tabContentHtml = '<div id="maindivleft'+scenarioCounter+'" class="maindivleft maindivchild">\
        <div id="jstreeSource'+ scenarioCounter+ '" class="jstreeSource myJsTree">\
        </div>\
    </div>\
    <div id="maindivcenter'+scenarioCounter+'" class="maindivcenter maindivchild">\
    </div>\
    <div id="maindivright'+scenarioCounter+'" class="maindivright maindivchild">\
         <div id="jstreeTarget'+ scenarioCounter+ '" class="jstreeTarget myJsTree">\
        </div>\
    </div>';
    tabs.find( ".ui-tabs-nav" ).append( li );
    tabs.append( "<div id='" + id + "' class='treeTab'><p>" + tabContentHtml + "</p></div>" );
      
    //tabs.tabs({heightStyle: "fill"});
    tabs.tabs( "refresh" );
    tabs.tabs({active: -1});
}

//sets max height among divs in the current maindiv panel
function setMaxHeight(counter){
    //minimum height
    var maxHeight = 300;
    $('#schemaTabs-'+counter+' .maindivchild').each(function() { 
        maxHeight = maxHeight > $(this).height() ? maxHeight : $(this).height();
    });
    $('#schemaTabs-'+counter+' .maindivchild').each(function() {
        $(this).height(maxHeight);
    });
    //tabs.tabs( "refresh" );
 }

//adds the tab with Transformations' info
function addViewTransformationsTab(scenarioCnt) {
     $.ajax( {
        url: 'ShowMappingTaskInfo',
        type: 'POST',
	data: {scenarioNo: scenarioCnt},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{ 
            var information = obj.info;
            information = information.replace(/\r?\n/g, '<br>');
            var label = scenarioCnt+". View Transformations",
            id = "viewTransformationsTabs-" + scenarioCnt,
            li = $( tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) ),
            tabContentHtml = information;        
            tabs.find( ".ui-tabs-nav" ).append( li );
            tabs.append( "<div id='" + id + "'><p>" + tabContentHtml + "</p></div>" );
            tabs.tabs( "refresh" );
            $("#"+id).addClass("viewTab"); //for css
            ///////$( '#maindiv a[href="#viewTransformationsTabs-'+scenarioCnt+'"]').trigger( "click" ); ///////
        }
      });    
}

function refreshViewTransformationsTab(scenarioCnt){
    $.ajax( {
        url: 'ShowMappingTaskInfo',
        type: 'POST',
	data: {scenarioNo: scenarioCnt},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{ 
            var information = obj.info;   
            information = information.replace(/\r?\n/g, '<br>');
            var tabContentHtml = information;
            var parElement = tabs.find("#viewTransformationsTabs-" + scenarioCnt);
            parElement.html("<p>" + tabContentHtml + "</p>");
            tabs.tabs( "refresh" );
        }
    });  
}

//adds the SQL Output tab
function addViewSqlTab(scenarioCnt) {
    $.ajax( {
        url: 'SqlOutput',
        type: 'POST',
        data: {scenarioNo: scenarioCnt},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{
            var sqlScript = obj.sqlScript;
            sqlScript = sqlScript.replace(/\r?\n/g, '<br>');
            var label = scenarioCnt+". View Sql",
            id = "viewSqlTabs-" + scenarioCnt,
            li = $( tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) ),
            tabContentHtml = sqlScript;        
            tabs.find( ".ui-tabs-nav" ).append( li );
            tabs.append( "<div id='" + id + "' class='sqlTab'><p>" + tabContentHtml + "</p></div>" );
            tabs.tabs( "refresh" );
            $("#"+id).addClass("viewTab");
            ///////$( '#maindiv a[href="#viewSqlTabs-'+scenarioCnt+'"]').trigger( "click" ); ///////
        }
    });
}

//checks if mapping data has been modified and refreshes the SQL output
function checkSql(tabId){
    var findString = "viewSqlTabs-";
    var scenarioCnt = tabId.substring(tabId.length, tabId.indexOf(findString)+findString.length);
    $.ajax( {
        url: 'SqlOutput',
        type: 'POST',
	data: {scenarioNo: scenarioCnt},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{
            var sqlScript = obj.sqlScript;
            sqlScript = sqlScript.replace(/\r?\n/g, '<br>');
            var newSql = "<p>" + sqlScript + "</p>";
            var oldSql = $("#"+tabId).html();
            if(newSql!==oldSql){
               var r = confirm("The SQL Queries Have Been Modified, Refresh ?");
               if (r === true) {
                   $("#"+tabId).html(newSql);
                   tabs.tabs( "refresh" );
               } 
            }
        }
    });  
}

//adds the XQuery Output tab
function addViewXQueryTab(scenarioCnt) {
    $.ajax( {
        url: 'XqueryOutput',
        type: 'POST',
	data: {scenarioNo: scenarioCnt},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{
            var xQueryScript = obj.xQueryScript;
            xQueryScript = xQueryScript.replace(/\r?\n/g, '<br>');
            var label = scenarioCnt+". View XQuery",
            id = "viewXQueryTabs-" + scenarioCnt,
            li = $( tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) ),
            tabContentHtml = xQueryScript;        
            tabs.find( ".ui-tabs-nav" ).append( li );
            tabs.append( "<div id='" + id + "' class='xqueryTab'><p>" + tabContentHtml + "</p></div>" );
            tabs.tabs( "refresh" );
            $("#"+id).addClass("viewTab");
            ///////$( '#maindiv a[href="#viewXQueryTabs-'+scenarioCnt+'"]').trigger( "click" ); ///////
        }
    });  
}

//checks if mapping data has been modified and refreshes the XQuery output
function checkXQuery(tabId){
    var findString = "viewXQueryTabs-";
    var scenarioCnt = tabId.substring(tabId.length, tabId.indexOf(findString)+findString.length);
    $.ajax( {
        url: 'XqueryOutput',
        type: 'POST',
	data: {scenarioNo: scenarioCnt},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{
            var xQueryScript = obj.xQueryScript;
            xQueryScript = xQueryScript.replace(/\r?\n/g, '<br>');
            var newXQuery = "<p>" + xQueryScript + "</p>";
            var oldXQuery = $("#"+tabId).html();
            if(newXQuery!==oldXQuery){
               var r = confirm("The XQuery Queries Have Been Modified, Refresh ?");
               if (r === true) {
                   $("#"+tabId).html(newXQuery);
                   tabs.tabs( "refresh" );
               } 
            }
        }
    });  
}

//creates the TGDs Tabs for the selected scenario
function addTGDsTabs(obj) {
    //find all tabs with the same scenario number as the current one...
    var list = $("#tgd_div .tgdTab");
    list.each(function() {
        var tabId = $(this).attr('id');
        var findString1 = 'gdTabs-';
        var findString2 = 'no';
        var scenarioNo = tabId.substring(tabId.indexOf(findString1)+findString1.length, tabId.indexOf(findString2));
        //do NOT change to "===", they are not of the same type
        if(scenarioNo==currentScenario){
            //...and replace the previous tabs
            $('a[href$="'+tabId+'"]').parent().remove();
            $(this).remove();
            tabs2.tabs( "refresh" );
       }
    });  
    for (var i=0;i<obj.tgds.length;i++){
        //replace line breaks with the <br> element
        addTGDTab(i+1, obj.tgds[i].tgd.replace(/\r?\n/g, '<br>'));
    }
    var onlyConstant = false;
    if(obj.tgds.length===0 && obj.constantTgds.length>0){
        onlyConstant = true;
    }
    for (var i=0;i<obj.constantTgds.length;i++){
        addConstantTGDTab(i+1, obj.constantTgds[i].constantTgd.replace(/\r?\n/g, '<br>'), onlyConstant);
    }
    
    var list2 = $("#maindiv .viewTab");
    var existing = false;
    list2.each(function() {
        if($(this).attr('id')==='viewTransformationsTabs-' + currentScenario){
            existing=true;
        }
    });
    //open the View Transformations Tab if it doesn't exist already
    if (!existing){
        addViewTransformationsTab(currentScenario);
    }
    //or else update the information in it
    else{
        refreshViewTransformationsTab(currentScenario);
    }        
}

//creates a TGD Tab
function addTGDTab(number, content) { 
    //tgd tab configuration
    var label = currentScenario+". TGD"+number,
        id = "tgdTabs-"+currentScenario+"no"+number,
        li = $( tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) ),
        tabContentHtml = "<p>"+content+"</p>";
    tabs2.find( ".ui-tabs-nav" ).append( li );
    tabs2.append( "<div id='" + id + "' class='tgdTab'><p>" + tabContentHtml + "</p></div>" );
    tabs2.tabs( "refresh" );
    //activate the first tgd tab of the current scenario
    if (number===1){
        tabs2.tabs({active: -1});
    }            
}

//creates a ConstantTGD Tab
function addConstantTGDTab(number, content, onlyConstant) { 
    //tgd tab configuration
    var label = currentScenario+". Constant TGD"+number,
        id = "constantTgdTabs-"+currentScenario+"no"+number,
        li = $( tabTemplate.replace( /#\{href\}/g, "#" + id ).replace( /#\{label\}/g, label ) ),
        tabContentHtml = "<p>"+content+"</p>";
    tabs2.find( ".ui-tabs-nav" ).append( li );
    tabs2.append( "<div id='" + id + "' class='tgdTab'><p>" + tabContentHtml + "</p></div>" );
    tabs2.tabs( "refresh" );
    //activate the first tgd tab of the current scenario
    if (number===1 && onlyConstant){
        tabs2.tabs({active: -1});
    }            
}

//function that removes TGD Tabs with the same scenario number as the one removed
function removeTGDTabs(scenarioNo){
    var list = $("#tgd_div .tgdTab");
    list.each(function() {
        var tabId = $(this).attr('id');
        var findString1 = 'gdTabs-';
        var findString2 = 'no';
        var tabScenarioNo = tabId.substring(tabId.indexOf(findString1)+findString1.length, tabId.indexOf(findString2));
        if(tabScenarioNo==scenarioNo){
            $('a[href$="'+tabId+'"]').parent().remove();
            $(this).remove();
       }
    });
}

//function that checks if there are no TGD tabs left in the lower area
function checkTGDTabArea(){
    var tabCount = $('#tgd_div >ul >li').size();
    if(tabCount===0){
        //remove TGD Tab Area
        $("#tgd_div").css("display","none");
        //set the main area to its default size
        $("#maindiv").css("height","100%");
        tabs.tabs( "refresh" );
    }
}

//creates the tree to the left area with links to each tab
function addProjectTree(scenarioCnt){
    $("#leftdiv").append("<div id='leftdiv"+scenarioCnt+"' class='treediv'></div>");

    data_arr=[
     { "id" : "projectTreeRoot"+scenarioCnt, "parent" : "#", "text" : "Scenario "+scenarioCnt },
     { "id" : "schemaProjectTreeNode"+scenarioCnt, "parent" : "projectTreeRoot"+scenarioCnt, "text" : "Schema Tree"},
     { "id" : "viewTransformationsProjectTreeNode"+scenarioCnt, "parent" : "projectTreeRoot"+scenarioCnt, "text" : "View Transformations Window"},
     { "id" : "viewSqlProjectTreeNode"+scenarioCnt, "parent" : "projectTreeRoot"+scenarioCnt, "text" : "View Sql"},
     { "id" : "viewXQueryProjectTreeNode"+scenarioCnt, "parent" : "projectTreeRoot"+scenarioCnt, "text" : "View XQuery"}
    ];        


    $("#leftdiv"+scenarioCnt).bind("ready.jstree", function(){
        var treeSource = $(this);
        treeSource.jstree("open_all");
        //do not show expand/colapse icons
        treeSource.find('.jstree-node').children('.jstree-ocl').css( "display", "none" );
        treeSource.find('#projectTreeRoot'+scenarioCnt).addClass("projectTreeRoot");
        treeSource.find('#schemaProjectTreeNode'+scenarioCnt).addClass("schemaProjectTreeNode");
        treeSource.find('#viewTransformationsProjectTreeNode'+scenarioCnt).addClass("projectTreeNode");
        treeSource.find('#viewSqlProjectTreeNode'+scenarioCnt).addClass("projectTreeNode");
        treeSource.find('#viewXQueryProjectTreeNode'+scenarioCnt).addClass("projectTreeNode");
        
        treeSource.find('#projectTreeRoot'+scenarioCnt).data("scenarioNo",scenarioCnt);
        
        treeSource.find('#viewTransformationsProjectTreeNode'+scenarioCnt).data("type","viewTransformations");
        treeSource.find('#viewSqlProjectTreeNode'+scenarioCnt).data("type","viewSql");
        treeSource.find('#viewXQueryProjectTreeNode'+scenarioCnt).data("type","viewXQuery");        
    });

    //wholerow plugin makes the dots disappear
    $("#leftdiv"+scenarioCnt).jstree({'plugins' : ['themes','wholerow','contextmenu'],contextmenu: {items: customMenu},'core' : {'themes':{'icons': false},'multiple' : false,'check_callback' : true,'data': data_arr}});

    $('#leftdiv').find(".treediv").css("background-color","#E0E0EB");
    $('#leftdiv'+scenarioCnt).css("background-color","#808080");
}

//on doubleclicking on the constant menu, open options menu for the constant
function createConstantOptionsPopup(item_id, newplumb){
    var previous_txt = $("#"+item_id).find(".span_hidden").text();
    var constant_form_text = '<form id="constant-options" class="" action="#" title="Constant options">\
        <input type="radio" name="type" class="constantOption" id="stringOption" value="string" checked>String<br>\
        <input type="radio" name="type" class="constantOption" id="numberOption" value="number">Number<br>\
        <input type="radio" name="type" class="constantOption" id="funcOption" value="function">Function<br><br>\
        <input autofocus id="text_field" type="text" name="constant_value" value='+previous_txt+'><br><br>\
        <select id="func_selection" disabled>\
          <option selected disabled hidden value=""></option>\
          <option value="newId()">newId()</option>\
          <option value="date()">date()</option>\
          <option value="datetime()">datetime()</option>\
        </select> \
        </form>';
    
    function checkRegexp( o, regexp) {
        if ( !( regexp.test( o ) ) ) {
        return false;
        } else {
        return true;
        }
    }
    
    $('#dialog_container').append(constant_form_text);
    //Dialog Setup
        var dialog = $("#constant-options").dialog({
          width : 'auto',
          height : 'auto',
          minHeight: 260,
          modal: true,
           buttons: {
            "OK": function(){
                var valid = true;
                var error_msg;                
                var radio_val = $("input:radio[name=type]:checked").val();
                var result_string;
                if(radio_val==="string"){  
                   var txt_val = $("#text_field").val();
                   valid = (txt_val!=="");
                   if (valid){                       
                        txt_val = '"'+txt_val+'"';
                        result_string = txt_val;;
                    }
                    else{
                        $("#text_field").addClass("ui-state-error");
                        error_msg="Please do not enter a blank value";
                    }                   
                }
                else if(radio_val==="number"){
                    var txt_val = $("#text_field").val();
                    valid = valid && checkRegexp(txt_val, /^[0-9]+$/);
                    if (valid){
                        result_string = txt_val;
                    }
                    else{
                        $("#text_field").addClass("ui-state-error");
                        error_msg="Please enter a numeric value";
                    }
                }
                else if (radio_val==="function"){
                   result_string = $("#func_selection").val();
                }
                
                if(valid){
                    var shown_result = result_string;
                    if(shown_result.length>15){
                        shown_result = shown_result.substring(0,15)+"...";
                    }
                    $("#"+item_id).find(".span_hidden").html(result_string);
                    $("#"+item_id).find(".span_shown").html(shown_result);
                    $("#"+item_id).attr('title',result_string);
                    updateConstantConnection(item_id, newplumb, result_string);
                    newplumb.repaintEverything();
                    dialog.dialog("close");
                }
                else{
                    alert(error_msg);
                }
            },
            Cancel: function() {
            dialog.dialog("close");
            }
          },
          create: function(event, ui) { 
            var widget = $(this).dialog("widget");
          $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
          }
          ,close: function(event, ui) { $(this).remove(); }
        });  
}

//on doubleclicking on the function menu, open options menu for the function
function createFunctionOptionsPopup(newplumb, item_id){
    var previous_txt = $("#"+item_id).find(".span_hidden").text();
    var src_attributes="";
    var idSourceConnection;
    var sourcePath;
    var relatedConnections = newplumb.getConnections({target: $('#'+item_id).find('img').attr('id')});
    for(var c=0; c<relatedConnections.length; c++) {
        idSourceConnection = $('#'+relatedConnections[c].sourceId).closest('.myJsTree').attr('id');
        sourcePath = $('#'+idSourceConnection).jstree(true).get_path('#' + relatedConnections[c].sourceId,".");
        if (sourcePath.lastIndexOf(" (")>=0){
            sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(" ("));
            src_attributes += sourcePath;
        }  
    }

    var constant_form_text1 = '<form id="function-options" class="" action="#" title="Function options">\
        <label for="src_attr_listbox">Source Attributes: </label>\
        <select id="src_attr_listbox" title="Doubleclick on a value on this area to add it to the \'Function Value\'" name="src_attributes" size="4"\
            ondblclick="$(\'#func_val_textarea\').replaceSelectedText($(this).children(\'option:selected\').text());">';
    var constant_form_text2='';
        for(var c=0; c<relatedConnections.length; c++) {
            idSourceConnection = $('#'+relatedConnections[c].sourceId).closest('.myJsTree').attr('id');
            sourcePath = $('#'+idSourceConnection).jstree(true).get_path('#' + relatedConnections[c].sourceId,".");
            if (sourcePath.lastIndexOf(" (")>=0){
                sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(" ("));
            }
            constant_form_text2+='<option value="'+sourcePath+'">'+sourcePath+'</option>';
        }
    var constant_form_text3='</select><br><br>\
        <label for="func_val_textarea">Insert Function: </label>\
        <select id="func_selection" onchange="$(\'#func_val_textarea\').replaceSelectedText($(this).val());" >\
          <option selected disabled hidden value=""></option>';                  
    var constant_form_text4='';
        for (var i=0;i<functionArray.length;i++){
            constant_form_text4+='<option class="function-selection" value="'+functionArray[i].func_name+'"title="'+functionArray[i].hint+'">'+functionArray[i].name+'</option>';
        }                        
    var constant_form_text5 ='</select><br><br>\
        <label for="func_val_textarea">Function Value: </label>\
        <textarea autofocus id="func_val_textarea" rows="4" name="src_attr" form="function-options">'+previous_txt+'</textarea><br>\
        </form>';
    var constant_form_text1=constant_form_text1+constant_form_text2+constant_form_text3+constant_form_text4+constant_form_text5;

    $('#dialog_container').append(constant_form_text1);
    //Dialog Setup
    var dialog = $( "#function-options" ).dialog({
      width : 'auto',
      height : 'auto',
      minWidth: 300,
      minHeight: 410,
      modal: true,
       buttons: {
        "OK": function(){
            var result_string = $("#func_val_textarea").val();
            var shown_result = result_string;
            if(shown_result.length>15){
                shown_result = shown_result.substring(0,15)+"...";
            }
            $("#"+item_id).find(".span_hidden").html(result_string);
            $("#"+item_id).find(".span_shown").html(shown_result);
            $("#"+item_id).attr('title',result_string);
            updateFunctionConnection(item_id, newplumb, result_string);
            newplumb.repaintEverything();
            dialog.dialog("close");
        },
        Cancel: function() {
        dialog.dialog("close");
        }
      },
      create: function(event, ui) { 
        var widget = $(this).dialog("widget");
      $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
      }
      ,close: function(event, ui) { $(this).remove(); }
    }); 
}

//pop up window with save options
function createSaveMappingTaskPopup(){
    var save_task_text = '<form id="save-task" class="" action="#" title="Save Mapping Task">\
                          <label for="save_name">Please enter the name of the mapping task to save:</label><br><br>\
                          <input autofocus id="save_name" type="text" name="save_name" value=""><br><br>\
                          </form>';
    $('#dialog_container').append(save_task_text);
    var dialog = $( "#save-task" ).dialog({
      width : 500,
      height : 190,
      minWidth: 500,
      minHeight: 190,
      modal: true,
       buttons: {
        "OK": function(){                
            var saveName=$("#save_name").val();
            var valid = (saveName!=="");
               if (valid){
                   //if a mapping task with the same name exists
                   if($.inArray(saveName, savedTasks) !== -1){
                       var r = confirm("Mapping task "+saveName+" already exists. Overwrite?");
                       if (r === true) {
                            saveTask(saveName);
                            dialog.dialog("close");
                       }
                   }
                   else{
                        saveTask(saveName);
                        dialog.dialog("close");
                   }
                }
                else{
                    $("#save_name").addClass( "ui-state-error" );
                    var error_msg="Please do not enter a blank value";
                    alert(error_msg);
                } 
        },
        Cancel: function() {
            dialog.dialog("close");
        }
      },
      create: function(event, ui) { 
        var widget = $(this).dialog("widget");
      $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
      }
      ,close: function(event, ui) { $(this).remove(); }
    }); 
}

function saveTask(saveName){
     $.ajax( {
        url: 'SaveMappingTask',
        type: 'POST',
        data: {saveName: saveName, scenarioNo : currentScenario},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{
            if($.inArray(saveName, savedTasks) === -1){
                savedTasks.push(saveName);
            }
            openedTasks.push(saveName);
            $('a[href$="schemaTabs-'+currentScenario+'"]').text(currentScenario + ". "+saveName);
            alert("Mapping task "+currentScenario+ " successfully saved as \"" + saveName + "\"");
        }
      }); 
}

//pop up window with open task options
function createOpenMappingTaskPopup(){
    var open_task_text = '<form id="open-task" class="" action="#" title="Open Mapping Task">\
                          <label for="open_name">Please choose which mapping task to open:</label><br><br>\n\
                          <select id="open_name" name="open_name">';
    for(var task = 0; task < savedTasks.length; task++){
        open_task_text +='<option value="'+savedTasks[task]+'">'+savedTasks[task]+'</option>';
    }
    open_task_text +=  '</select><br><br></form>';
    $('#dialog_container').append(open_task_text);
    var dialog = $( "#open-task" ).dialog({
      width : 410,
      height : 190,
      minWidth: 410,
      minHeight: 190,
      modal: true,
       buttons: {
        "OK": function(){                
            var openName=$("#open_name").val();                         
            lastAction = "open";
            $.ajax( {
                url: 'OpenMappingTask',
                type: 'POST',
                data: {openName: openName, scenarioNo : scenarioCounter+1},
                beforeSend: function(xhr){
                        xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                }
              } ).done(function(responseText) {
                var obj = $.parseJSON(responseText); 
                if(obj.hasOwnProperty("exception")){
                    alert(obj.exception);                    
                } 
                else{
                    loadSchemaTrees(openName, obj);
                    openedTasks.push(openName);
                }
            });              
            dialog.dialog("close");                              
        },
        Cancel: function() {
            dialog.dialog("close");
        }
      },
      create: function(event, ui) { 
        var widget = $(this).dialog("widget");
      $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
      }
      ,close: function(event, ui) { $(this).remove(); }
    }); 
}

//pop up window with delete task options
function createDeleteMappingTaskPopup(){
    var delete_task_text = '<form id="delete-task" class="" action="#" title="Delete Mapping Task">\
                          <label for="delete_name">Choose which mapping task to delete:</label><br><br>\n\
                          <select id="delete_name" name="delete_name">';
    for(var task = 0; task < savedTasks.length; task++){
        delete_task_text +='<option value="'+savedTasks[task]+'">'+savedTasks[task]+'</option>';
    }
    delete_task_text +=  '</select><br><br></form>';
    $('#dialog_container').append(delete_task_text);
    var dialog = $( "#delete-task" ).dialog({
      width : 410,
      height : 190,
      minWidth: 410,
      minHeight: 190,
      modal: true,
       buttons: {
        "OK": function(){ 
            var deleteName=$("#delete_name").val();
            //if the mapping task that is going to be deleted is among the opened tasks
            if($.inArray(deleteName, openedTasks) !== -1){
                alert("Mapping task \""+deleteName+"\" is open. \nPlease, close the task before deleting it.");
                dialog.dialog("close");
            }
            else{
                var r = confirm("Are you sure you want to delete mapping task \""+deleteName+"\"?");
                if (r === true) {
                     $.ajax( {
                        url: 'DeleteMappingTask',
                        type: 'POST',
                        data: {deleteName: deleteName},
                        beforeSend: function(xhr){
                                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                        }
                      } ).done(function(responseText) {
                        var obj = $.parseJSON(responseText); 
                        if(obj.hasOwnProperty("exception")){
                            alert(obj.exception);                    
                        }
                        else{
                            var deleteName = obj.deleteName;
                            alert("Mapping task \""+deleteName+ "\" was successfully deleted"); 
                        }
                      });  
                    //remove the mapping task from the array of saved ones
                    var index = savedTasks.indexOf(deleteName);
                    if (index > -1) {
                        savedTasks.splice(index, 1);
                    }
                    dialog.dialog("close");
                }
            }
        },
        Cancel: function() {
            dialog.dialog("close");
        }
      },
      create: function(event, ui) { 
        var widget = $(this).dialog("widget");
      $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
      }
      ,close: function(event, ui) { $(this).remove(); }
    }); 
}

//pop up window with download task options
function createDownloadMappingTaskPopup(){
    var download_task_text = '<form id="download-task" class="" action="#" title="Download Mapping Task and Files">\
                          <label for="download_name">Choose a mapping task to download along with its files:</label><br><br>\n\
                          <select id="download_name" name="download_name">';
    for(var task = 0; task < savedTasks.length; task++){
        download_task_text +='<option value="'+savedTasks[task]+'">'+savedTasks[task]+'</option>';
    }
    download_task_text +=  '</select><br><br></form>';
    $('#dialog_container').append(download_task_text);
    var dialog = $( "#download-task" ).dialog({
      width : 410,
      height : 190,
      minWidth: 410,
      minHeight: 190,
      modal: true,
       buttons: {
        "OK": function(){ 
           var downloadName=$("#download_name").val();
           window.location = 'DownloadMappingTask?downloadName='+downloadName;  
           dialog.dialog("close");
            
            
        },
        Cancel: function() {
            dialog.dialog("close");
        }
      },
      create: function(event, ui) { 
        var widget = $(this).dialog("widget");
      $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
      }
      ,close: function(event, ui) { $(this).remove(); }
    }); 
}

//pop up window with selection condition expression
function createSelectionConditionPopup(item_id){
    var node_id = item_id.substring(0, item_id.length-7);    
    var idSource = $("#"+item_id).closest('.myJsTree').attr('id');
    var tree = $("#"+idSource);
    var findString ='jstreeSource';
    var scenarioNo = idSource.substring(idSource.length, idSource.indexOf(findString)+findString.length); 
    var pathTillNode = $('#'+idSource).jstree(true).get_path('#' +item_id,".");
    var previous_txt = tree.jstree(true).get_node(item_id).data.selectionCondition;
    //replace < and > symbols with the corresponding html entities
    previous_txt = previous_txt.replace('<','&lt;');
    previous_txt = previous_txt.replace('>','&gt;');
    var selection_condition_text = '<form id="selection-condition" class="" action="#" title="Edit Selection Condition">\
        <input autofocus id="select_cond_expression" type="text" name="expression_value" value='+previous_txt+'><br><br>\
        </form>';
       
    $('#dialog_container').append(selection_condition_text);
    //Dialog Setup
    var dialog = $( "#selection-condition" ).dialog({
      width : 400,
      height : 160,
      minWidth: 400,
      minHeight: 160,
      modal: true,
       buttons: {
        "Delete": function(){
            $.ajax( {
                url: 'EditSelectionCondition',
                type: 'POST',
                data: {specificAction:"delete", path:pathTillNode, expression:"", scenarioNo: scenarioNo},
                beforeSend: function(xhr){
                        xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                }
              } ).done(function(responseText) {
                var obj = $.parseJSON(responseText); 
                if(obj.hasOwnProperty("exception")){
                    alert(obj.exception);                    
                } 
                else{
                    var image = $("#"+item_id).data('original_img');                 
                    tree.jstree(true).get_node(node_id).data.selectionCondition = "";
                    tree.jstree(true).set_icon(item_id, 'css/images/'+image);
                    $("#"+item_id).contents().filter(function() {return this.nodeType === 3;}).replaceWith($("#"+item_id).data('original_text'));
                }
            });
            dialog.dialog("close");
        },  
        "OK": function(){                
            var expression=$("#select_cond_expression").val();
            var valid = (expression!=="");
               if (valid){
                   $.ajax( {
                        url: 'EditSelectionCondition',
                        type: 'POST',
                        data: {specificAction:"update", path:pathTillNode, expression:expression, scenarioNo: scenarioNo},
                        beforeSend: function(xhr){
                                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                        }
                      } ).done(function(responseText) {
                        var obj = $.parseJSON(responseText); 
                        if(obj.hasOwnProperty("exception")){
                            alert(obj.exception);                    
                        } 
                        else{
                            //change icon
                            tree.jstree(true).set_icon(item_id, 'css/images/selection_condition_set.png');
                            //replace whitespaces with the nbsp special character
                            expression = expression.replace(/\s/g,"&nbsp;"); 
                            tree.jstree(true).get_node(node_id).data.selectionCondition = expression;
                            if (expression.length > 20)
                                //change only child text node (nodeType 3)
                                $("#"+item_id).contents().filter(function() {return this.nodeType === 3;}).replaceWith($("#"+item_id).data('original_text')+" ["+expression.substring(0,20)+"...]");
                            else
                                $("#"+item_id).contents().filter(function() {return this.nodeType === 3;}).replaceWith($("#"+item_id).data('original_text')+" ["+expression+"]");
                        }
                    });                   
                    dialog.dialog("close");
                }
                else{
                    $("#select_cond_expression").addClass( "ui-state-error" );
                    var error_msg="Please do not enter a blank value";
                    alert(error_msg);
                } 
        },
        Cancel: function() {
            dialog.dialog("close");
        }
      },
      create: function(event, ui) { 
        var widget = $(this).dialog("widget");
      $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
      }
      ,close: function(event, ui) { $(this).remove(); }
    }); 
}

//left area context menu
function customMenu(node) {
    var nodeId = ($(node).attr('id'));
    var scenarioNo;
    if($('#'+nodeId).hasClass("projectTreeRoot")){
        scenarioNo = $('#'+nodeId).data("scenarioNo");
    }
    else{
        scenarioNo = $('#'+nodeId).closest(".projectTreeRoot").data("scenarioNo");
    }
    var items = {
        selectItem: {
            label: "Select Scenario",
            action: function () {
                currentScenario = scenarioNo;
                $.ajax( {
                    url: 'SelectMappingTask',
                    type: 'POST',
                    data: {scenarioNo:scenarioNo},
                    beforeSend: function(xhr){
                            xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                    }
                  } ).done(function(responseText) {
                    var obj = $.parseJSON(responseText); 
                    if(obj.hasOwnProperty("exception")){
                        alert(obj.exception);                    
                    } 
                    else{
                        $('#leftdiv').find(".treediv").css("background-color","#E0E0EB");               
                        $('#'+nodeId).closest(".treediv").css("background-color","#808080");
                        $( "#schemaProjectTreeNode"+scenarioNo ).trigger( "click" );
                    }
                });                               
            }
        },
        removeItem: {
            label: "Remove Scenario",
            action: function () {
                var r = confirm("Do you want to close mapping task "+scenarioNo+"?");
                if (r===true){
                    removeMappingTask(scenarioNo);
                }
            }
        }
    };
    return items;
}    

//function that creates the source and target tree given their data
function makeTrees (sourceTreeArray, targetTreeArray, sourceId, targetId, newplumb, JSONData){
  var findString ='jstreeSource';
  var idNo = sourceId.substring(sourceId.length, sourceId.indexOf(findString)+findString.length);    
  //set the current tab as container
  var container = $("#schemaTabs-"+idNo);
  newplumb.setContainer(container);
  container.data("instance", newplumb);

  //connection restrictions
  newplumb.bind('connection',function(info){
    var con=info.connection;
    var idSourceConnection;
    var idTargetConnection;      
    var sourcePath;
    var targetPath;
    con.setParameter("connection",true);
    con.setParameter("join_condition",false);
    con.setParameter("constant",false);
    con.setParameter("fromFunction",false);
    con.setParameter("toFunction",false);
    //do not allow duplicate connections (arr1)
    //neither reverse connections (arr2)
    var arr1 = newplumb.select({source:con.sourceId,target:con.targetId});
    var arr2 = newplumb.select({source: con.targetId, target: con.sourceId});
    /*var arr3 = newplumb.select({target:con.targetId});
    var existingConnections = newplumb.getConnections({target:con.targetId});*/

    if (arr1.length + arr2.length > 1){
      con.setParameter("connection",false);
      newplumb.detach(con);
    }        
    
    //also if the source is on the Target Tree
    //and the target on the Source Tree
    //do not allow the connection
    else if ($('#' + con.targetId).parents(".jstreeSource").length===1
      && $('#' + con.sourceId).parents(".jstreeTarget").length===1){
        con.setParameter("connection",false);
        newplumb.detach(con);
    }
    //if the target is Function
    else if ($('#' + con.targetId).parents(".function-menu").length===1){
        con.setParameter("toFunction",true);
        //allow only connections from source tree
        if(!($('#' + con.sourceId).parents('.jstreeSource').length===1)){
           newplumb.detach(con); 
        }
    }
    //if the source is Function
    else if ($('#' + con.sourceId).parents(".function-menu").length===1){
        con.setParameter("fromFunction",true);
        var arr4 = newplumb.select({ target: $("#"+con.sourceId).siblings('img').attr('id') });
        //if the target is the Source Tree do not allow the connection
        if($('#' + con.targetId).parents(".jstreeSource").length===1){
          con.setParameter("connection",false);
          newplumb.detach(con);
        }
        //if no connection from source tree to this function hasn't been established yet
        //do not allow the connection
        else if(arr4.length  === 0){
            alert('You must provide a source value to the \'Function menu\' first');
            con.setParameter("connection",false);
            newplumb.detach(con);
        }
    }
    //if the source is Constant        
    else if ($('#' + con.sourceId).parents(".constant-menu").length===1){
        con.setParameter("constant",true);
        //if the target is the Source Tree do not allow the connection
        if($('#' + con.targetId).parents(".jstreeSource").length===1){
          con.setParameter("connection",false);
          newplumb.detach(con);
        }
    }    
    //if connection is on the same tree  
    else if (($('#' + con.sourceId).parents(".jstreeSource").length===1 
            && $('#' + con.targetId).parents(".jstreeSource").length===1)
    || ($('#' + con.sourceId).parents(".jstreeTarget").length===1 
            && $('#' + con.targetId).parents(".jstreeTarget").length===1)){
        //make the lines grey and dotted
        con.setPaintStyle({strokeStyle: 'grey', dashstyle:"2 2", lineWidth: 2});
        //change the connection's curviness
        con.setConnector("Flowchart");
        
        /*if ($('#' + con.sourceId).parents(".jstreeSource").length===1){ 
            con.endpoints[0].setAnchor("Left");                        
        }    else{ con.endpoints[1].setAnchor("Right");}*/ 
        //and the position of the anchor
        con.endpoints[1].setAnchor("Right");     
        con.setParameter("connection",false);
        con.setParameter("join_condition",true);
    }

    //send connection data to server whenever a connection is establised
    if (con.getParameter("connection")){
             
        if(!(con.getParameter("toFunction"))){              
            idTargetConnection = $('#'+con.targetId).closest('.myJsTree').attr('id');
            targetPath = $('#'+idTargetConnection).jstree(true).get_path('#' + con.targetId,".");
            if (targetPath.lastIndexOf(" (")>=0){
                targetPath = targetPath.substring(0, targetPath.lastIndexOf(" ("));
            }
            var sourcePathArray = new Array();
            var sourceValue = null;
            var expression = null;
            //constant
            if(con.getParameter("constant")){                
                sourceValue = $("#"+con.sourceId).siblings( ".span_hidden" ).text();  
                expression = sourceValue;                
            }
            //function
            else if(con.getParameter("fromFunction")){
                var relatedConnections = newplumb.getConnections({target: $('#'+con.sourceId).siblings('img').attr('id')});
                for(var c=0; c<relatedConnections.length; c++) {
                    idSourceConnection = $('#'+relatedConnections[c].sourceId).closest('.myJsTree').attr('id');
                    var sourcePath = $('#'+idSourceConnection).jstree(true).get_path('#' + relatedConnections[c].sourceId,".");
                    if (sourcePath.lastIndexOf(" (")>=0){
                        sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(" ("));
                    } 
                    sourcePathArray.push(sourcePath);
                }
                expression = $("#"+con.sourceId).siblings( ".span_hidden" ).text();                    
            }
            //simple connection from tree node to tree node
            else{
                idSourceConnection = $('#'+con.sourceId).closest('.myJsTree').attr('id');   
                var sourcePath = $('#'+idSourceConnection).jstree(true).get_path('#' + con.sourceId,".");
                if (sourcePath.lastIndexOf(" (")>=0){
                    sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(" ("));
                }
                sourcePathArray.push(sourcePath);
                expression = sourcePath;                    
            }
            con.setParameter("sourcePath",expression);
            con.setParameter("targetPath",targetPath);
            con.setParameter("scenarioNo",idNo);
            con.setParameter("sourcePathArray",sourcePathArray);
            
            //do not create new connections on server when loading a mapping task
            if(lastAction!=="open"){
                 $.ajax( {
                    url: 'EstablishedConnection',
                    type: 'POST',
                    data: {'sourcePathArray[]':sourcePathArray, targetPath:targetPath, 
                           sourceValue: sourceValue, expression: expression, scenarioNo: idNo},
                    beforeSend: function(xhr){
                            xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                    }
                  } ).done(function(responseText) {
                    var obj = $.parseJSON(responseText);
                    if(obj.hasOwnProperty("exception")){
                        alert(obj.exception);                    
                    }
                  });           
            }
        }
    }
    //send join condition data to the server
    else if (con.getParameter("join_condition")){
        idSourceConnection = $('#'+con.sourceId).closest('.myJsTree').attr('id');
        idTargetConnection = $('#'+con.targetId).closest('.myJsTree').attr('id');      
        sourcePath = $('#'+idSourceConnection).jstree(true).get_path('#' + con.sourceId,".");
        targetPath = $('#'+idTargetConnection).jstree(true).get_path('#' + con.targetId,".");
        if (sourcePath.lastIndexOf(" (")>=0){
            sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(" ("));
        }                        
        if (targetPath.lastIndexOf(" (")>=0){
            targetPath = targetPath.substring(0, targetPath.lastIndexOf(" ("));
        }
        var isSource;
        if ($('#' + con.sourceId).parents(".jstreeSource").length===1){
            isSource=true;
        }
        else{
            isSource=false;
        }
        con.setParameter("sourcePath",sourcePath);
        con.setParameter("targetPath",targetPath);
        con.setParameter("isSource",isSource);
        con.setParameter("scenarioNo",idNo);
        $.ajax( {
            url: 'NewJoinCondition',
            type: 'POST',
            data: {sourcePath:sourcePath, targetPath:targetPath, scenarioNo: idNo, isSource: isSource},
            beforeSend: function(xhr){
                    xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
            }
          } ).done(function(responseText) {
            var obj = $.parseJSON(responseText); 
            if(obj.hasOwnProperty("exception")){
                alert(obj.exception);                    
            } 
        });  
    }
    
    //right clicking on a connection makes the 'delete connection' context menu appear
    con.bind("contextmenu", function (connection, e) {
        e.preventDefault();
        if(con.getParameter("join_condition")){
            createContextMenu1(newplumb,con,e);
          }
        else if(con.getParameter("connection")){
            createContextMenu2(newplumb,con,e);
          }      
    });
  }); 
  
  //connection detached
  newplumb.bind('connectionDetached',function(info){
    var con=info.connection;
    var arr1 = newplumb.select({source:con.sourceId,target:con.targetId});       
    ///if it is a join condition
    if(con.getParameter("join_condition")){
        $.ajax( {
            url: 'DeleteJoin',
            type: 'POST',
            data: {sourcePath : con.getParameter("sourcePath"), targetPath: con.getParameter("targetPath"), 
                   scenarioNo: idNo, isSource: con.getParameter("isSource")},
            beforeSend: function(xhr){
                    xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
            }
          } ).done(function(responseText) {
            var obj = $.parseJSON(responseText); 
            if(obj.hasOwnProperty("exception")){
                alert(obj.exception);                    
            } 
        });  
    }
    //if it is a detach event due to a duplicate connection 
    //do not delete the existing connection from the server
    else if ((arr1.length===1)
      //or if the "Delete All Connections" button was pressed
      //another request has already been sent to the server to handle it
      ||(con.getParameter("already_deleted"))){
        return;
    }      
    else{        
        $.ajax( {
            url: 'DeleteConnection',
            type: 'POST',
                    data: {sourcePath: con.getParameter("sourcePath"), targetPath: con.getParameter("targetPath"), scenarioNo: idNo},
            beforeSend: function(xhr){
                    xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
            }
          } ).done(function(responseText) {
            var obj = $.parseJSON(responseText); 
            if(obj.hasOwnProperty("exception")){
                alert(obj.exception);                    
            } 
          });          
    }
    
  });

   //bind right-clicking on Yellow area
   //so that the 'constant' and 'function' context menus can be created
   var yellow_area = $("#"+container.find(".maindivcenter").attr('id'));
   $(yellow_area).bind('contextmenu',function (e) {
      e.preventDefault();
      createContextMenu3(newplumb,$(this).attr('id'),e);
   });

   //bind right-clicking on the two Grey areas
   //so that the 'Delete-all-connections' context menu can be created
   var grey_area1 = $("#"+container.find(".maindivleft").attr('id'));
   var grey_area2 = $("#"+container.find(".maindivright").attr('id'));
   $(grey_area1).bind('contextmenu',function (e) {
      e.preventDefault();
      createContextMenu5(newplumb,idNo,e);
   });
   $(grey_area2).bind('contextmenu',function (e) {
      e.preventDefault();
      createContextMenu5(newplumb,idNo,e);
   });
     
   var tree1 = $('#'+sourceId);
   tree1.bind("ready.jstree", function(){
    var treeSource = jQuery(this);
    treeSource.jstree("open_all");
    //do not show expand/collapse icons
    treeSource.find('.jstree-node').children('.jstree-ocl').css( "display", "none" );
    findExistingDuplicates(treeSource, JSONData, true);
    //make the leaf nodes source/targets for arrow connections
    makeLeafNodesSourceTarget(treeSource, newplumb, JSONData, createExistingConnections);
    //give to all second nodes the class setNode
    makeSelectionConditionAndDuplicationNodes(treeSource, true, newplumb, JSONData, loadExistingSelectionConditions);
   }); 
   //load data to the tree
   tree1.jstree({'core' : {'check_callback': true,'data': sourceTreeArray}});  
   
   //when a data node is copied its data are also copied
   tree1.bind("copy_node.jstree", function (e, data) {
        data.node.data = $.extend(true, {}, data.original.data);
    });
   
    //add an event handler so that clicking on a tree node makes the corresponding 
    //connection highlighted while all other connections return to their default colour
    $('#'+sourceId).on("click", ".jstree-anchor" ,function () {
        //first set all connections' color back to their default (black/grey)...
        setConnectionsDefaultColor(newplumb);
        //...then highlight the selected connection
        var selectedId = $(this).attr('id');
        highlightSelectedconnectonsSource(newplumb, selectedId);        
    });
    
    $('#'+targetId).on("click", ".jstree-anchor" ,function () {
        //first set all connections' color back to their default (black/grey)...
        setConnectionsDefaultColor(newplumb);
        //...then highlight the selected connection
        var selectedId = $(this).attr('id');
        highlightSelectedconnectonsTarget(newplumb, selectedId);
    });

   var tree2 = $('#'+targetId);

   tree2.bind("ready.jstree", function(){
    var treeTarget = jQuery(this);    
    treeTarget.jstree("open_all");
    //do not show expand/collapse icons
    treeTarget.find('.jstree-node').children('.jstree-ocl').css( "display", "none" );
    findExistingDuplicates(treeTarget, JSONData, false);
    //make the leaf nodes source/targets for arrow connections
    makeLeafNodesSourceTarget(treeTarget, newplumb, JSONData, createExistingConnections);
    //give to all second nodes the class setNode
    makeSelectionConditionAndDuplicationNodes(treeTarget, false, newplumb, JSONData, loadExistingSelectionConditions);
   }); 
   //load data to the tree
   tree2.jstree({'core' : {'check_callback': true,'data': targetTreeArray}}); 
   
   //when a data node is copied its data are also copied
    tree2.bind("copy_node.jstree", function (e, data) {
        data.node.data = $.extend(true, {}, data.original.data);
    });
};

function findExistingDuplicates(tree, JSONData, source){
    if(JSONData!==null){
        if (source)
            for (var i =0; i < JSONData.sourceDuplications.length; i++){
                tree.jstree(true).get_node(JSONData.sourceDuplications[i].cloneNode).data.duplicate = true; 
                //setting the correct number of duplication
                setDuplicationNo(scenarioCounter, tree.jstree(true).get_node(JSONData.sourceDuplications[i].originalNode).text);                
            }
        else
           for (var i =0; i < JSONData.targetDuplications.length; i++){
                tree.jstree(true).get_node(JSONData.targetDuplications[i].cloneNode).data.duplicate = true;    
                //setting the correct number of duplication
                setDuplicationNo(scenarioCounter, tree.jstree(true).get_node(JSONData.sourceDuplications[i].originalNode).text);              
            }       
    }
    
}

//
function setDuplicationNo(counter, original_text){
    if(duplicates[counter] === undefined){
        duplicates[counter] = new Object();        
    }
    var duplicateForScenario = duplicates[counter];
    if(duplicateForScenario[original_text] === undefined)
        duplicateForScenario[original_text] = 0;     
    duplicateForScenario[original_text] = duplicateForScenario[original_text] + 1; 
    return duplicateForScenario[original_text];
}

//function that checks if a connection from a constant exists and updates its value
function updateConstantConnection(id, newplumb, newValue){   
    var existing_conns = newplumb.getConnections({ source: $('#'+id).find('.span_shown').attr('id') });
    if(existing_conns.length  !== 0){
        for (var i=0; i< existing_conns.length; i++){
            $.ajax( {
                url: 'UpdateConnection',
                type: 'POST',
                data: {sourcePath : existing_conns[i].getParameter("sourcePath"), targetPath: existing_conns[i].getParameter("targetPath"), 
                       scenarioNo: existing_conns[i].getParameter("scenarioNo"), sourceValue:newValue, 'sourcePathArray[]': null, expression:newValue},
                beforeSend: function(xhr){
                        xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                }
              } ).done(function(responseText) {
                var obj = $.parseJSON(responseText); 
                if(obj.hasOwnProperty("exception")){
                    alert(obj.exception);                    
                }
              });
        }
    }
}

//function that checks if a connection from a constant exists and updates its value
function updateFunctionConnection(id, newplumb, newValue){   
    var existing_conns = newplumb.getConnections({ source: $('#'+id).find('.span_shown').attr('id') });
    if(existing_conns.length  !== 0){
        for (var i=0; i< existing_conns.length; i++){         
             $.ajax( {
                url: 'UpdateConnection',
                type: 'POST',
                data: {sourcePath : existing_conns[i].getParameter("sourcePath"), targetPath: existing_conns[i].getParameter("targetPath"), 
                       scenarioNo: existing_conns[i].getParameter("scenarioNo"), sourceValue:null, 'sourcePathArray[]': existing_conns[i].getParameter("sourcePathArray"), expression:newValue},
                beforeSend: function(xhr){
                        xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                }
              } ).done(function(responseText) {
                var obj = $.parseJSON(responseText); 
                if(obj.hasOwnProperty("exception")){
                    alert(obj.exception);                    
                }
              }); 
            existing_conns[i].setParameter(("sourcePath"),newValue);
        }
    }
}

//function that creates existing connections and join conditions when loading a mapping task
function createExistingConnections(connections, joins, newplumb){
    readyCnt++;
    //the readyCnt counter is equal to "2" when both trees (source & target) have been loaded
    //and have made their leaf nodes connection sources & targets
    if (readyCnt === 2){
        if((connections.length+joins.length) > 0){
            var constantValues = new Array();
            var constantMap = new Object();
            var functionValues = new Array();
            var functionMap = new Object();
            //variables for positioning the possible constant and function icons
            //the "+5" is for giving a 5px margin from the yellow div
            var position = $("#maindivcenter"+currentScenario).position();
            var positionX = position.left + 5;
            var positionY = position.top + 5;
            var shift;
            //the maximum number of icons that fit in the X axis of the yellow area
            var maxIcons = 1 + Math.floor(($("#maindivcenter"+currentScenario).width()-maxIconWidth-5)/shiftXpixels);
            //the iconsNo variable counts the number of constant and function icons on the CURRENT tab
            //so that the next icon will not be positioned on top of another
            var iconsNo = 0;
            var currentmenucounter;
            for (var i=0; i < connections.length ;i++){
                //simple 1:1 connection
                if (connections[i].connectionType === "simple"){
                    newplumb.connect({ source: connections[i].sourceNodes[0]+"_anchor", 
                                       target: connections[i].targetNode+"_anchor",
                                       overlays: [ [ "Arrow", {width: 10, length: 12, foldback: 0, location: 1, id: "arrow"} ] ]
                                    });
                }
                else if (connections[i].connectionType === "constant"){
                    //check if a constant icon with the same value has already been created, if not create one
                    if($.inArray(connections[i].sourceValue, constantValues) === -1){
                        if ((iconsNo%maxIcons)===maxIcons){
                            shift = maxIcons;
                        }
                        else{
                            shift = iconsNo%maxIcons;
                        }
                        //the icon's position depends on the number of icons already on the current tab
                        createConstant("maindivcenter"+currentScenario, positionX+shift*shiftXpixels, positionY+iconsNo*shiftYpixels, newplumb); 
                        iconsNo++;
                        //menucounter has already been increased, so the current one is 1 less
                        currentmenucounter = menucounter-1;
                        var constantValueShown = connections[i].sourceValue;
                        if(constantValueShown.length>15){
                            constantValueShown = constantValueShown.substring(0,15)+"...";
                        }
                        $("#constant-menu-span"+currentmenucounter).html(constantValueShown);
                        $("#constant-menu-span_hidden"+currentmenucounter).html(connections[i].sourceValue);
                        $("#constant-menu"+currentmenucounter).attr('title',connections[i].sourceValue);
                        constantValues.push(connections[i].sourceValue);
                        constantMap[connections[i].sourceValue] = currentmenucounter;
                    }
                    //if a constant icon with the same value has already been created find the corresponding one
                    else{
                        currentmenucounter = constantMap[connections[i].sourceValue];
                    }
                    newplumb.connect({ source: "constant-menu-span"+currentmenucounter, 
                                        target: connections[i].targetNode+"_anchor",
                                        overlays: [ [ "Arrow", {width: 10, length: 12, foldback: 0, location: 1, id: "arrow"} ] ]
                                    });
                }
                else if (connections[i].connectionType === "function"){
                    //check if a function icon with the same value has already been created, if not create one
                    if($.inArray(connections[i].transformationFunction, functionValues) === -1){
                        if ((iconsNo%maxIcons)===maxIcons){
                            shift = maxIcons;
                        }
                        else{
                            shift = iconsNo%maxIcons;
                        }
                        createFunction("maindivcenter"+currentScenario, positionX+shift*shiftXpixels, positionY+iconsNo*shiftYpixels, newplumb); 
                        iconsNo++;
                        currentmenucounter = menucounter-1;
                        var functionValueShown = connections[i].transformationFunction;
                        if(functionValueShown.length>15){
                            functionValueShown = functionValueShown.substring(0,15)+"...";
                        }
                        $("#function-menu-span"+currentmenucounter).html(functionValueShown);
                        $("#function-menu-span_hidden"+currentmenucounter).html(connections[i].transformationFunction);
                        $("#function-menu"+currentmenucounter).attr('title',connections[i].transformationFunction);
                        functionValues.push(connections[i].transformationFunction);
                        functionMap[connections[i].transformationFunction] = currentmenucounter;
                    }
                    //if a function icon with the same value has already been created find the corresponding one
                    else{
                        currentmenucounter = functionMap[connections[i].transformationFunction];
                    }
                    for (var j=0; j < connections[i].sourceNodes.length; j++){
                        newplumb.connect({ source: connections[i].sourceNodes[j]+"_anchor", 
                                            target: "function-menu-img"+currentmenucounter,
                                          overlays: [ [ "Arrow", {width: 10, length: 12, foldback: 0, location: 1, id: "arrow"} ] ]
                                         });
                    }
                    newplumb.connect({ source: "function-menu-span"+currentmenucounter, 
                                        target: connections[i].targetNode+"_anchor",
                                        overlays: [ [ "Arrow", {width: 10, length: 12, foldback: 0, location: 1, id: "arrow"} ] ]
                                    });
                }
            }
            for (var i=0; i < joins.length ;i++){
                var con = newplumb.connect({ source: joins[i].sourceNode+"_anchor", target: joins[i].targetNode+"_anchor"});
                if (joins[i].mandatory === true){
                    con.setPaintStyle({strokeStyle: 'grey', dashstyle:"2 2", lineWidth: 4});
                }
                else{
                    con.setPaintStyle({strokeStyle: 'grey', dashstyle:"2 2", lineWidth: 2});
                }
                if(joins[i].fk === true){
                    con.addOverlay([ "Arrow", { foldback:0, location:1, width:10,length: 12, id:"myArrowLabel"+con.id } ]);
                }
                con.setParameter("mandatory",joins[i].mandatory);
                con.setParameter("fk",joins[i].fk);
            }
        }
        lastAction = "";
        readyCnt=0;
        setMaxHeight(scenarioCounter);
    }
}

//creates Constant draggable icon, makes it source for connections
//and binds it with click and doubleclick events
function createConstant(id, relativeX, relativeY, newplumb){
    $("#"+id).append("<div id='constant-menu"+menucounter+"' class='constant-menu ui-widget-content'>\
                        <img src='css/images/constant.jpg' alt='constant_symbol' height='30' width='30'>\
                        <span id='constant-menu-span"+menucounter+"' class='span_shown'></span>\
                        <span id='constant-menu-span_hidden"+menucounter+"' class='span_hidden'></span></div>");
    $("#constant-menu"+menucounter).css("left", relativeX + "px");
    $("#constant-menu"+menucounter).css("top", relativeY + "px");
    var outerwidth= $("#constant-menu"+menucounter).parent(".maindivcenter").css("width");
    $("#constant-menu-span"+menucounter).css("max-width", "calc("+outerwidth+" - 35px)");
    var spanId = $( "#constant-menu"+menucounter).find("span").attr('id');
    newplumb.makeSource($("#"+spanId),commonSource);
    newplumb.draggable($( "#constant-menu"+menucounter));//,{ constrain: true });
    $( "#constant-menu"+menucounter).draggable({ containment: "parent" });

    //when right-clicking on the constant menu open context menu 4
    $("#constant-menu"+menucounter).bind("contextmenu",function (e) {
        e.preventDefault();
        //so that the Yellow page (parent) context menu doesn't appear
        e.stopPropagation();
        createContextMenu4(newplumb,$(this).attr('id'),e);
    });
    $("#constant-menu"+menucounter).bind("dblclick",function (e) {
        createConstantOptionsPopup($(this).attr('id'),newplumb);
    });
    menucounter++; 
}

//creates Function draggable icon, makes it source and target for connections
//and binds it with click and doubleclick events
function createFunction(id, relativeX, relativeY, newplumb){
    $("#"+id).append("<div id='function-menu"+menucounter+"' class='function-menu ui-widget-content' title=''>\
                   <img id='function-menu-img"+menucounter+"' src='css/images/function-2.jpg' alt='function_symbol' height='30' width='30'>\
                   <span id='function-menu-span"+menucounter+"'class='span_shown'></span>\
                   <span id='function-menu-span_hidden"+menucounter+"' class='span_hidden'></span></div>");
    $("#function-menu"+menucounter).css("left", relativeX + "px");
    $("#function-menu"+menucounter).css("top", relativeY + "px");
    var outerwidth= $("#function-menu"+menucounter).parent(".maindivcenter").css("width");
    $("#function-menu-span"+menucounter).css("max-width", "calc("+outerwidth+" - 35px)");

    var spanId = $( "#function-menu"+menucounter).find("span").attr('id');
    var imgId = $( "#function-menu"+menucounter).find("img").attr('id');
    newplumb.makeSource($("#"+spanId),commonSource);
    newplumb.makeTarget($("#"+imgId), {allowLoopback:false, maxConnections: 100} ,commonTarget);
    newplumb.draggable($( "#function-menu"+menucounter));//,{ constrain: true });
    $( "#function-menu"+menucounter).draggable({ containment: "parent" });

    //when right-clicking on the function menu open context menu 4
    $("#function-menu"+menucounter).bind("contextmenu",function (e) {
    e.preventDefault();
    //so that the Yellow page (parent) context menu doesn't appear
    e.stopPropagation();
    createContextMenu4(newplumb,$(this).attr('id'),e);
    });

    //when double-clicking on the function menu open function options
    $("#function-menu"+menucounter).bind("dblclick",function (e) {
    //open options menu only if there is at least one connection to this function menu
    var arr4 = newplumb.select({ target: $(this).find('img').attr('id') });
    if(arr4.length  === 0){
       alert('You must provide a source value to the \'Function menu\' first');
    }
    else{
       createFunctionOptionsPopup(newplumb, $(this).attr('id'));                     
    }
    });

    menucounter++;
}

//function that makes all leaf nodes sources and targets of arrow connectors
function makeLeafNodesSourceTarget (tree, newplumb, JSONData, callback) {
    newplumb.makeSource(tree.find(".jstree-leaf").find( "a" ),commonSource);
    newplumb.makeTarget(tree.find(".jstree-leaf").find( "a" ), {allowLoopback:false, maxConnections: 100} ,commonTarget);

    //bind 'Make Source/Target For Connection' menu to right clicking on each of the leaf nodes
    tree.find(".jstree-leaf").bind("contextmenu",function (e) {
        e.preventDefault();        
        //so that the 'Delete All Connections' context menu doesn't appear
        e.stopPropagation();
        createContextMenu7(newplumb, $(this).attr('id'), e);
    });

    //call function for creating existing connections and join conditions if there are any
    if(JSONData!==null)
        callback(JSONData.connections,JSONData.joins,newplumb);
};

//function that sets the connection colors to their default value
function setConnectionsDefaultColor(newplumb){
    var allConnections = newplumb.getConnections();        
    for(var c=0; c<allConnections.length; c++) {
        if(allConnections[c].getParameter("connection")){ 
            allConnections[c].setPaintStyle({strokeStyle: 'black'});
        } 
        else if (allConnections[c].getParameter("join_condition")){
            allConnections[c].setPaintStyle({strokeStyle: 'grey'});
        }
    } 
}

//function that highlights with a different colour selected connection for Source tree
function highlightSelectedconnectonsSource(newplumb, selectedId){
    newplumb.select({source: selectedId}).each(function(selConnection) {
        selConnection.setPaintStyle({strokeStyle: '#3399FF'});
        if(selConnection.getParameter("toFunction")){
            var spanId = $( "#"+selConnection.targetId).parent(".function-menu").find(".span_shown").attr('id');
            newplumb.select({source: spanId}).each(function(relConnection) {
                relConnection.setPaintStyle({strokeStyle: '#3399CC'});        
            });                                                                    
        } 
    }); 
    newplumb.select({target: selectedId}).each(function(selConnection) {
        if ((selConnection.getParameter("join_condition")) && (!selConnection.getParameter("fk"))){
            selConnection.setPaintStyle({strokeStyle: '#3399FF'});
        }
    });
}

//function that highlights with a different colour selected connection for Target tree
function highlightSelectedconnectonsTarget(newplumb, selectedId){
    newplumb.select({target: selectedId}).each(function(selConnection) { 
        if (!selConnection.getParameter("fk")){
            selConnection.setPaintStyle({strokeStyle: '#3399FF'});
            if(selConnection.getParameter("fromFunction")){
                var functionImgId = $( "#"+selConnection.sourceId).parent(".function-menu").find("img").attr('id');
                newplumb.select({target: functionImgId}).each(function(relConnection) {
                    relConnection.setPaintStyle({strokeStyle: '#3399CC'});        
                });                                                                    
            } 
        }           
    });
    newplumb.select({source: selectedId}).each(function(selConnection) {
        if(selConnection.getParameter("join_condition")){
            selConnection.setPaintStyle({strokeStyle: '#3399FF'});
        }
    });
}

//function that gives the class setNode on all second tree nodes
function makeSelectionConditionAndDuplicationNodes(tree, source, newplumb, JSONData, callback){
    //find all nodes that are second in hierarchy (Set Nodes)
    tree.find(".jstree-children .jstree-children .jstree-anchor:not(.jstree-children .jstree-children .jstree-children .jstree-anchor)").each(function() {
        //add to them the class setNode
        $(this).addClass("setNode");
        //store their original text and image        
        $(this).data('original_text',$(this).text());
        $(this).data('original_img', $(this).find('i').css('background-image').split('/').pop().slice(0, -2));
        var nodeId = $(this).attr('id').substring(0, $(this).attr('id').length-7);
        var expression = tree.jstree(true).get_node(nodeId).data.selectionCondition;
        if(expression!==""){
            if (expression.length > 20)
                //change only child text node (nodeType 3)
                $(this).contents().filter(function() {return this.nodeType === 3;}).replaceWith($(this).data('original_text')+" ["+expression.substring(0,20)+"...]");
            else
                $(this).contents().filter(function() {return this.nodeType === 3;}).replaceWith($(this).data('original_text')+" ["+expression+"]");
        }
    });
    
    $("#"+tree.attr('id')+" .setNode").bind("contextmenu",function (e) {
        e.preventDefault();        
        //so that the 'Delete All Connections' context menu doesn't appear
        e.stopPropagation();
        if (!tree.jstree(true).get_node($(this).attr('id')).data.duplicate) {
            createContextMenu6($(this).attr('id'),e, newplumb, source);
        }
        else{
           createContextMenu6b($(this).attr('id'),e, newplumb, source); 
        }                
    });
    
    //call function for creating existing selection conditions if there are any
    if(source && JSONData!==null){
        if(JSONData.selConditions.length > 0){
          callback(tree, JSONData.selConditions);//,newplumb);
        }
    }
}

//function that loads existing selection conditions
function loadExistingSelectionConditions(tree, selConditions){
    for (var i =0; i < selConditions.length; i++){
        var node_id = selConditions[i].sourceNode+"_anchor";
        //change icon
        tree.jstree(true).set_icon(selConditions[i].sourceNode, 'css/images/selection_condition_set.png');
        //replace whitespaces with the nbsp special character
        var expression = selConditions[i].condition.replace(/\s/g,"&nbsp;");        
        tree.jstree(true).get_node(selConditions[i].sourceNode).data.selectionCondition = expression;
        if (expression.length > 20)
            //change only child text node (nodeType 3)
            $("#"+node_id).contents().filter(function() {return this.nodeType === 3;}).replaceWith($("#"+node_id).data('original_text')+" ["+expression.substring(0,20)+"...]");
        else
            $("#"+node_id).contents().filter(function() {return this.nodeType === 3;}).replaceWith($("#"+node_id).data('original_text')+" ["+expression+"]");
    }
}

//function that removes a mapping task and also deletes the corresponding tabs
function removeMappingTask(scenarioNo){
    $.ajax( {
        url: 'RemoveMappingTask',
        type: 'POST',
		data: {scenarioNo:scenarioNo},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
                var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        }
        else {
            //get mapping task name before removing the tab
            var taskName = $('a[href$="schemaTabs-'+scenarioNo+'"]').text();
            taskName = taskName.substring(taskName.indexOf(scenarioNo+". ")+(scenarioNo+". ").length, taskName.length);

            //remove left tree
            $("#leftdiv"+scenarioNo).remove();
            //remove tabs from main menu
            $('a[href$="schemaTabs-'+scenarioNo+'"]').parent().remove();
            $('#schemaTabs-'+scenarioNo).remove();
            $('a[href$="viewTransformationsTabs-'+scenarioNo+'"]').parent().remove();
            $('#viewTransformationsTabs-'+scenarioNo).remove();
            $('#viewSqlTabs-'+scenarioNo).remove();
            $('a[href$="viewSqlTabs-'+scenarioNo+'"]').parent().remove();
            $('#viewXQueryTabs-'+scenarioNo).remove();
            $('a[href$="viewXQueryTabs-'+scenarioNo+'"]').parent().remove();
            tabs.tabs( "refresh" );
            //also remove TGD tabs
            removeTGDTabs(scenarioNo);
            tabs2.tabs( "refresh" );

            //if none of the TGDs tabs are left remove the TGD tab area
            checkTGDTabArea();
            //if the current scenario is the one removed, set no scenario as the current one
            //(leave it as "==", not "===")
            if (scenarioNo==currentScenario){
                currentScenario = 0;
            }      

            //remove the mapping task from the array of saved ones
            var index = openedTasks.indexOf(taskName);
            if (index > -1) {
                openedTasks.splice(index, 1);
            }
        }
    });   
}

//context menu for join conditions
function createContextMenu1(newplumb,con,event){
    var mandatoryText="Mandatory";
    var fktext="Foreign Key";
    if (con.getParameter("mandatory")){
        mandatoryText="&#10004 "+mandatoryText;
    }
    if (con.getParameter("fk")){
        fktext="&#10004 "+fktext;
    }
    $("#maindiv").append("<ul id='contextmenu"+con.id+"' class='custom-menu'>\
                            <li data-action ='delete'>Delete</li>\
                            <li data-action ='mandatory'>"+mandatoryText+"</li>\
                            <li data-action ='fk'>"+fktext+"</li>\
                         </ul>");
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px");
    if (con.getParameter("mandatory")){
        $(".custom-menu li:nth-child(2)").css("padding", "0 1em 0 1em");
    }
    if (con.getParameter("fk")){
        $(".custom-menu li:nth-child(3)").css("padding", "0 1em 0 1em");
    }
    $( "#contextmenu"+con.id+" li").bind( "click", function() {
        switch($(this).attr("data-action")) {
            case "delete":
                $(".custom-menu").remove();
                con.setPaintStyle({strokeStyle: 'red'});
                var r = confirm("Do you want to delete this connection?");
                if (r===true){
                  newplumb.detach(con);
                }
                else{
                  con.setPaintStyle({strokeStyle: 'grey'});
                }
                break;
            case "mandatory":                     
                if (!con.getParameter("mandatory")){
                    con.setPaintStyle({strokeStyle: 'grey', dashstyle:"2 2", lineWidth: 4});
                    con.setParameter("mandatory",true);
                }
                else{
                    con.setPaintStyle({strokeStyle: 'grey', dashstyle:"2 2", lineWidth: 2});
                    con.setParameter("mandatory",false);
                }
                $.ajax( {
                    url: 'JoinConditionOptions',
                    type: 'POST',
                            data: {sourcePath:con.getParameter("sourcePath"), targetPath:con.getParameter("targetPath"), 
                                    scenarioNo: con.getParameter("scenarioNo"), isSource: con.getParameter("isSource"), changedOption:"mandatory"},
                    beforeSend: function(xhr){
                            xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                    }
                  } ).done(function(responseText) {
                    var obj = $.parseJSON(responseText); 
                    if(obj.hasOwnProperty("exception")){
                        alert(obj.exception);                    
                    }
                });
                $(".custom-menu").remove();
                break;
            case "fk":
                if (!con.getParameter("fk")){
                    con.addOverlay([ "Arrow", { foldback:0, location:1, width:10,length: 12, id:"myArrowLabel"+con.id } ]);
                    con.setParameter("fk",true);
                }
                else{
                    con.removeOverlay("myArrowLabel"+con.id);
                    con.setParameter("fk",false);
                }
                $.ajax( {
                    url: 'JoinConditionOptions',
                    type: 'POST',
                            data: {sourcePath:con.getParameter("sourcePath"), targetPath:con.getParameter("targetPath"), 
                                   scenarioNo: con.getParameter("scenarioNo"), isSource: con.getParameter("isSource"), changedOption:"fk"},
                    beforeSend: function(xhr){
                            xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                    }
                  } ).done(function(responseText) {
                    var obj = $.parseJSON(responseText); 
                    if(obj.hasOwnProperty("exception")){
                        alert(obj.exception);                    
                    }
                });                
                $(".custom-menu").remove();
                break;
            default :
                $(".custom-menu").remove();
                break;
        }
    });
}

//context menu for connections
function createContextMenu2(newplumb,con,event){
    $("#maindiv").append("<ul id='contextmenu"+con.id+"' class='custom-menu'>\
                            <li data-action ='delete'>Delete Connection</li>\
                         </ul>");
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px");
    $( "#contextmenu"+con.id+" li").bind( "click", function() {
        $(".custom-menu").remove();
        con.setPaintStyle({strokeStyle: 'red'}); 
        if (!(con.getParameter("toFunction"))){            
            var r = confirm("Do you want to delete this connection?");
            if (r===true){
              newplumb.detach(con);
            }
            else{
              con.setPaintStyle({strokeStyle: 'black'});
            } 
        }
        else{
            var r = confirm("Do you want to delete this connection?\n\nDeleting this one will also delete all connections\nrelated to its function");
            if (r===true){
                var spanId = $( "#"+con.targetId).parent(".function-menu").find(".span_shown").attr('id');
                var hiddenspanId = $( "#"+con.targetId).parent(".function-menu").find(".span_hidden").attr('id');
        
                newplumb.select({source: spanId}).each(function(relConnection) {
                    newplumb.detach(relConnection);
                });
                newplumb.select({target: con.targetId}).each(function(relConnection) {
                    newplumb.detach(relConnection);
                });
                                
                //also delete the function info
                $("#"+spanId).html("");
                $("#"+hiddenspanId).html("");
            }
            else{
              con.setPaintStyle({strokeStyle: 'black'});
            } 
        }
    });
}

//Yellow area context menu    
function createContextMenu3(newplumb,id,event){
    $("#maindiv").append("<ul id='contextmenu"+id+"' class='custom-menu'>\
                            <li data-action ='constant'>Create Constant</li>\
                            <li data-action ='function'>Create Function</li>\
                         </ul>");
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px");
    $( "#contextmenu"+id+" li").bind( "click", function() {
        switch($(this).attr("data-action")) {
            case "constant":
                $(".custom-menu").remove();
                createConstant(id,relativeX,relativeY,newplumb);
                break;
            case "function":
                $(".custom-menu").remove();
                createFunction(id,relativeX,relativeY,newplumb);
                break;
            case "dependency":
                break;
            default :
                $(".custom-menu").remove();
                break;
        }        
    });
}

//Constant/Function deletion context menu    
function createContextMenu4(newplumb,id,event){
    $("#maindiv").append("<ul id='contextmenu"+id+"' class='custom-menu'>\
                            <li data-action ='delete'>Delete</li>\
                         </ul>");
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px");
    $( "#contextmenu"+id+" li").bind( "click", function() {
        var r = confirm("Are you sure you want to delete this?");
        if (r===true){
            var spanId = $("#"+id).find("span").attr('id');                     
            newplumb.select({source: spanId}).each(function(relConnection) {
                newplumb.detach(relConnection);
            });
            
            if($("#"+id).hasClass("function-menu")){
                var imgId = $("#"+id).find("img").attr('id');                      
                newplumb.select({target: imgId}).each(function(relConnection) {
                    newplumb.detach(relConnection);
                });
            }
            
            $("#"+id).remove();
        }
        $(".custom-menu").remove();      
    });
}

//Delete all connections context menu    
function createContextMenu5(newplumb,idNo,event){
    $("#maindiv").append("<ul id='contextmenu"+idNo+"' class='custom-menu'>\
                            <li data-action ='delete'>Delete All Connections</li>\
                         </ul>");
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px");
    $( "#contextmenu"+idNo+" li").bind( "click", function() {
        var r = confirm("Are you sure you want to delete ALL connections?");
        if (r===true){
            var existingConnections = newplumb.getConnections();
            var connectionsToDeleteSource = new Array();
            var connectionsToDeleteTarget = new Array();
            for(var c=0; c<existingConnections.length; c++) {
                if(existingConnections[c].getParameter("connection")){
                    connectionsToDeleteSource.push(existingConnections[c].getParameter("sourcePath"));
                    connectionsToDeleteTarget.push(existingConnections[c].getParameter("targetPath"));
                    existingConnections[c].setParameter("already_deleted",true);
                    newplumb.detach(existingConnections[c]);
                }
            }
            $.ajax( {
                url: 'DeleteAllConnections',
                type: 'POST',
                data: {'sourcePathArray[]': connectionsToDeleteSource, 'targetPathArray[]': connectionsToDeleteTarget, scenarioNo: idNo},
                beforeSend: function(xhr){
                        xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                }
              } ).done(function(responseText) {
                var obj = $.parseJSON(responseText); 
                if(obj.hasOwnProperty("exception")){
                    alert(obj.exception);                    
                }
              });              
            
            $("#maindivcenter"+idNo+" .constant-menu").remove();
            $("#maindivcenter"+idNo+" .function-menu").remove();
        }
        $(".custom-menu").remove();      
    });
}

//Selection Condition and Duplication context menu
function createContextMenu6(nodeId, event,newplumb, source){
    var contextMenu = "<ul id='contextmenu"+nodeId+"' class='custom-menu'>";
    if (source)
        contextMenu = contextMenu + "<li class='selection'>Edit Selection Condition</li>";
    contextMenu = contextMenu + "<li class='duplication'>Duplicate Node</li>\
                           </ul>";
    $("#maindiv").append(contextMenu);
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px");
    $( "#contextmenu"+nodeId+" li.selection").bind( "click", function() {
        $(".custom-menu").remove();
        createSelectionConditionPopup(nodeId);
    });
    $( "#contextmenu"+nodeId+" li.duplication").bind( "click", function() {
        $(".custom-menu").remove();
        duplicateNode(nodeId, source, newplumb, recreateTree);
    });
}

//Selection Condition and Delete Duplication context menu
function createContextMenu6b(nodeId,event, newplumb, source){
    var contextMenu ="<ul id='contextmenu"+nodeId+"' class='custom-menu'>";
    if (source)
        contextMenu = contextMenu + "<li class='selection'>Edit Selection Condition</li>";
    contextMenu = contextMenu + "<li class='delete_duplication'>Delete Duplicate</li>\
                           </ul>";
    $("#maindiv").append(contextMenu);
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px");
    $( "#contextmenu"+nodeId+" li.selection").bind( "click", function() {
        $(".custom-menu").remove();
        createSelectionConditionPopup(nodeId);
    });
    $( "#contextmenu"+nodeId+" li.delete_duplication").bind( "click", function() {
        $(".custom-menu").remove();
        deleteDuplicateNode(nodeId.substring(0,nodeId.length-7), source, newplumb, recreateTree);
    });   
}

//function that creates a duplicate node
function duplicateNode(item_id, source, newplumb, callback){      
    var parent = $("#"+item_id).parent().parent().parent();
    var idSource = $("#"+item_id).closest('.myJsTree').attr('id');
    var tree = $('#'+idSource);
    var original_image = $("#"+item_id).data('original_img');
    var original_text = $("#"+item_id).data('original_text');
    //copy node
    var duplicateId = tree.jstree().copy_node($("#"+item_id), parent, 'last', null, false, false);
    var sourcePath = tree.jstree(true).get_path('#' + item_id,".");
    if (source)
        var findString ='jstreeSource';
    else
        var findString ='jstreeTarget';
    var scenarioNo = idSource.substring(idSource.length, idSource.indexOf(findString)+findString.length);
    $.ajax( {
        url: 'DuplicateNode',
        type: 'POST',
	data: {sourcePath:sourcePath, scenarioNo: scenarioNo, isSource: source},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText);
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{
            //setting the correct number of duplication
            var no = setDuplicationNo(scenarioNo, original_text);
            tree.jstree(true).get_node(duplicateId).data.selectionCondition="";
            tree.jstree(true).get_node(duplicateId).data.duplicate = true;
            tree.jstree(true).set_text(duplicateId, tree.jstree(true).get_text(item_id)+'_'+no+'_');            
            tree.jstree(true).set_icon(duplicateId, 'css/images/'+original_image);            
            callback(tree, source, newplumb);
            $("#"+item_id).data('original_img',original_image);
            if (source)
                $('#maindivleft'+scenarioNo).height($('#'+idSource).height());
            else
                $('#maindivright'+scenarioNo).height($('#'+idSource).height());
            setMaxHeight(scenarioNo);
        }
    });      
}

//function that deletes a selected duplicate node
function deleteDuplicateNode(node_id, source, newplumb, callback){
    var idSource = $("#"+node_id).closest('.myJsTree').attr('id');
    var tree = $('#'+idSource);
    var original_text = $("#"+node_id+'_anchor').data('original_text');
    original_text = original_text.substring(0, original_text.lastIndexOf('_'));
    original_text = original_text.substring(0, original_text.lastIndexOf('_'));
    var sourcePath = tree.jstree(true).get_path('#' + node_id,".");
    if (source)
        var findString ='jstreeSource';
    else
        var findString ='jstreeTarget';
    var scenarioNo = idSource.substring(idSource.length, idSource.indexOf(findString)+findString.length);  
    $.ajax( {
        url: 'DeleteDuplicateNode',
        type: 'POST',
	data: {sourcePath:sourcePath, scenarioNo: scenarioNo, isSource: source},
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText);
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        } 
        else{
            //first delete all connections from this set node
            $("#"+node_id).find(".jstree-leaf").find( "a" ).each(function() {
                newplumb.select({source: $(this).attr('id')}).each(function(connection) {
                    newplumb.detach(connection);
                });
            });
            tree.jstree().delete_node(node_id);
            callback(tree, source, newplumb);
            
            //reduce the duplication number
            var duplicateForScenario = duplicates[scenarioNo];            
            duplicateForScenario[original_text] = duplicateForScenario[original_text] - 1;               
        } 
    });  
}

//neccessary actions so that the tree will be identical to the previous one when redrawn
function recreateTree(tree, source, newplumb){    
    //open tree nodes
    tree.jstree("open_all");
    tree.find('.jstree-node').children('.jstree-ocl').css( "display", "none" ); 
    //since the tree is redrawn these methods should be re-applied
    makeLeafNodesSourceTarget(tree, newplumb, null, null);
    makeSelectionConditionAndDuplicationNodes(tree, source, newplumb, null, null);
}

//Make Source/target context menu
function createContextMenu7(newplumb, nodeId ,event){
     $("#maindiv").append("<ul id='contextmenu"+nodeId+"' class='custom-menu'>\
                              <li class='makeSourceConn'>Make Source For Connection</li>\
                              <li class='makeTargetConn'>Make Target For Connection</li>\
                           </ul>");
    var offset = $("#maindiv").offset();
    var relativeX = (event.pageX - offset.left);
    var relativeY = (event.pageY - offset.top);
    $(".custom-menu").css("left", relativeX + "px");
    $(".custom-menu").css("top", relativeY + "px"); 
    $( "#contextmenu"+nodeId+" li.makeSourceConn").bind( "click", function() {
        $(".custom-menu").remove();
        //the sourceForConn object is used as a HashSet with the jsplumb container as the key for each scenario
        sourceForConn[$(newplumb.getContainer()).attr('id')] = nodeId;
    }); 
    $( "#contextmenu"+nodeId+" li.makeTargetConn").bind( "click", function() {
        $(".custom-menu").remove();
        var sourceNodeId = sourceForConn[$(newplumb.getContainer()).attr('id')];
        if (sourceNodeId === undefined){
            alert("No source node has been selected for the connection");
        }
        else if(sourceNodeId === nodeId){
            alert("Source and target node of a connection should not be the same");
        }
        else if ($('#' + nodeId).parents(".jstreeSource").length===1
                    && $('#'+sourceNodeId).parents(".jstreeTarget").length===1){
            alert("Connection cannot be created from target to source schemae");
        }
        else{
            //create connection (or join) and unset the source for connection for this scenario 
            newplumb.connect({ source: sourceNodeId+"_anchor", 
                               target: nodeId+"_anchor",
                               overlays: [ [ "Arrow", {width: 10, length: 12, foldback: 0, location: 1, id: "arrow"} ] ]
                             });
            delete sourceForConn[$(newplumb.getContainer()).attr('id')];
        }
    });
}

//adds option items and their values to a listbox if they do not exist already
//and extracts and sends the first line of the selected csv file
function addToList(id, id2){ 
    //either 'Source' or 'Target'
    var type = id.substring(3,9);
    var option_value = $('#'+'dir'+type).val();
    var list_id = type+'FilesListbox';
    //delete the "C:\fakepath\" that some browsers (IE/Chrome) put in front of the file name
    //option_value = option_value.replace(/C:\\fakepath\\/i, '');
    option_value = option_value.split('\\').pop();
    //if there is a file selected on the filechooser
    if (option_value!=='') {
        //..and if it is a csv file
        if(option_value.substr(option_value.lastIndexOf(".") + 1).toLowerCase()==="csv"){
            //...that hasn't been added already
            if($('#'+list_id+' option[value="'+option_value+'"]').length === 0){
                var url='AddTo'+type;               
                var file = $("#"+id2)[0].files[0];
                if (file) {
                    // create reader
                    var reader = new FileReader();
                    reader.readAsText(file);
                    reader.onload = function(e) {
                        // browser completed reading file
                        var allLines = e.target.result.split(/\r\n|\n/);
                        $.ajax( {
                            url: url,
                            type: 'POST',          
                            beforeSend: function(xhr){
                                    xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                            },
                            data: {fileName: file.name, firstLine: allLines[0]}           
                        }).done(function(responseText) {
                            var obj = $.parseJSON(responseText); 
                            if(obj.hasOwnProperty("exception")){
                                alert(obj.exception);                    
                            }                 
                            else {  
                                $('#'+list_id).append('<option value="'+option_value+'">'+option_value+'</option>');
                                //select the option because the field is mandatory to move on
                                $('#'+list_id+' option[value="'+option_value+'"]').prop('selected', true);
                            }
                        });                        
                    };                    
                }
            }
        }
        else{
            alert('Please select a .csv file');
        }
    }
    else{
        alert('Please select a file to add');
    }
}

//removes selected item from a listbox
function removeFromList(id){
    //either 'Source' or 'Target'
    var type = id.substring(6,12);
    var list_id = type+'FilesListbox';
    var url = 'RemoveFrom'+type;  
    if ($("#"+list_id+" option:selected").val()!== undefined){
        $.ajax( {
            url: url,
            type: 'POST',          
            beforeSend: function(xhr){
                    xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
            },
            data: {fileToDelete: $("#"+list_id+" option:selected").val()}         
        } );         
        $("#"+list_id+" option:selected").remove();
        //select at least one option -the first one- if there is one left, because the field is mandatory to move on
        $("#"+list_id+" option:first").prop('selected', true);
    }
}

//uploads xml or sql file to the server
function uploadFile(id){
    var url;
    var fileType = id.substring(0,3);  
    var sourceType = id.substring(9,15);    
    var extension = $("#"+fileType+"Schema"+sourceType).val().substr(($("#"+fileType+"Schema"+sourceType).val().lastIndexOf('.') +1));
    if ((fileType==="xml" && extension.toLowerCase()==="xsd") || (fileType==="sql" && extension.toLowerCase()==="sql")){
        var url = 'UploadTo'+sourceType;         
        //call the appropriate servlet
        $.ajax( {
            url: url,
            type: 'POST',            
            beforeSend: function(xhr){
                    xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
            },
            data: new FormData($("#wizard1")[0]),
            cache: false,
            contentType: false,
            processData: false            
          } ); 
    }
}

//changes the input menu for source/Target according to type (XML/CSV/Relational)
function changeInputMenu(selection){
    //id is either 'typeSource' or 'typeTarget'
    var type = selection.attr('id').substring(4,10);
    //hide the current input menu
    $(".inputMenu"+type).css("display", "none");
    //show the selected menu
    $("#"+selection.val()+type).css("display", "block");
}

//creates the menu of input dialog for new mapping task
function createNewMapTaskDialog(type){
    //type is either 'Source' or 'Target'
    var out = '<h3>'+type+'</h3>\
        <section>\
         <label for="type'+type+'">Choose Datasource Type : </label>\
         <select id="type'+type+'" name="inputType'+type+'" onchange="changeInputMenu($(this))">\
            <option value="csv">CSV</option>\
            <option value="xml">XML</option>\
            <option value="sql">SQL</option>\
            <option value="saved">Saved schemata</option>\
         </select><br\><br\><br\>\
         <div id="csv'+type+'" class="inputMenu'+type+'">\
            <label for="dbName'+type+'">Enter Database Name (*Mandatory)</label>\
            <input id="dbName'+type+'" name="dbName'+type+'" type="text" size="50" class="required" ><br\>\
            <label for="dir'+type+'">Choose '+type+' Files:</label>\
            <input type="file" name="dir'+type+'" id="dir'+type+'" accept=".csv"/><br\>\
            <input type="button" id="add'+type+'" class="addButton" value="Add">\
            <input type="button" id="remove'+type+'" class="removeButton" value="Remove"><br\><br\>\
            <label for="'+type+'FilesListbox">'+type+' Files (*Mandatory)</label>\
            <select id="'+type+'FilesListbox" name="files'+type+'" size="5" class="required"></select>\
         </div>\
         <div id="xml'+type+'" class="inputMenu'+type+'">\
            <label for="xmlSchema'+type+'">Schema File (*Mandatory)</label>\
            <input type="file" name="xmlSchema'+type+'" id="xmlSchema'+type+'" accept=".xsd" class="required fileSchema"/><br\>\
         </div>\
         <div id="sql'+type+'" class="inputMenu'+type+'">\
            <label for="sqlDbName'+type+'">Enter Database Name (*Mandatory)</label>\
            <input id="sqlDbName'+type+'" name="sqlDbName'+type+'" type="text" size="50" class="required" ><br\>\
            <label for="sqlSchema'+type+'">Schema File (*Mandatory)</label>\
            <input type="file" name="sqlSchema'+type+'" id="sqlSchema'+type+'" accept=".sql" class="required fileSchema"/><br\>\
         </div>\
         <div id="saved'+type+'" class="inputMenu'+type+'">\
            <label for="savedDBName'+type+'">Database schema: </label>\
            <select required id="savedDBName'+type+'" name="savedDBName'+type+'">\
                <option value="" class="hidden_text"></option>';
    for(var schema = 0; schema < savedSchemata.length; schema++){
        out +='<option value="'+savedSchemata[schema]+'">'+savedSchemata[schema]+'</option>';
    }
    out += '</select>\
         </div>\
        </section>';
    return out;
}

function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = $.trim(cookies[i]);
            // Does this cookie string begin with the name we want?
            if (cookie.substring(0, name.length + 1) === (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}
    
$(document).ready(function(){
    tabTemplate = "<li><a href='#{href}'>#{label}</a><span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>";
    tabs = $("#maindiv").tabs({
        heightStyle: "fill",
        //when the tab is activated -if it is a View tab- check for changes
        activate: function (event, ui) {
            //ui.newPanel is the selected tab
            if(ui.newPanel.hasClass("sqlTab")){
                checkSql(ui.newPanel.attr("id"));
            }
            else if(ui.newPanel.hasClass("xqueryTab")){
                checkXQuery(ui.newPanel.attr("id"));
            }
            else if (ui.newPanel.hasClass("treeTab")){
                var newplumbInstance = $('#'+ui.newPanel.attr('id')).data("instance");
                if (newplumbInstance !== undefined){
                    newplumbInstance.repaintEverything();
                }                
            }
        }
       
    });
    tabs2 = $("#tgd_div").tabs({
        heightStyle: "fill"
    });

    //sortable tabs
    tabs.find( ".ui-tabs-nav" ).sortable({
        axis: "x",
        stop: function() {
        tabs.tabs( "refresh" );
        }
    });
    
    //tabs: close icon behavior
    tabs.delegate( "span.ui-icon-close", "click", function() {
        //if the close icon on the main tab is clicked
        //close the whole mapping task, after asking for confirmation
        var tabIdentity = $(this).siblings("a").attr("href");
        if(tabIdentity.substr(1, 11) === "schemaTabs-"){
            var tabScenarioNo = tabIdentity.substr(12, tabIdentity.length);
            var r = confirm("Do you want to close mapping task "+tabScenarioNo+"?");
            if (r===true){
                removeMappingTask(tabScenarioNo);
            }
        }
        //deafult behavior: removing the tab on click
        else{
            var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
            $( "#" + panelId ).remove();
        }
        tabs.tabs( "refresh" );
    });
    
    //tabs2: close icon behavior
    tabs2.delegate( "span.ui-icon-close", "click", function() {
        //if the close icon on the main tab is clicked
        //close the whole mapping task, after asking for confirmation
        var tabId = $(this).siblings("a").attr("href");
        var findString1 = "tgdTabs-";
        var findString2 = "no";
        var tabScenarioNo = tabId.substring(tabId.indexOf(findString1)+findString1.length, tabId.indexOf(findString2));
        //if the number of elements that their id starts with 'tgdTabs- plus the tab scenario no is more than one
        var noOfScenarioTabs = $("#tgd_div > div[id^='tgdTabs-"+tabScenarioNo+"']").length + $("#tgd_div > div[id^='constantTgdTabs-"+tabScenarioNo+"']").length;
        if(noOfScenarioTabs > 1){
            var r = confirm("If you close this tab, all TGD tabs for mapping task "+tabScenarioNo+" will be closed too.\nAre you sure?");
            if (r===true){
                removeTGDTabs(tabScenarioNo);
            }
        }
        //deafult bevavior: removing the tab on click
        else{
            var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
            $( "#" + panelId ).remove();
        }
        tabs2.tabs( "refresh" );
        //if none of the TGDs tabs are left remove the TGD tab area
        checkTGDTabArea();
    });  
    //when the document is ready, send initialize request to server
    //and get the saved mapping tasks of the specific user
    $.ajax( {
        url: 'Initialize',
        type: 'POST',
        beforeSend: function(xhr){
                xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
        }
      } ).done(function(responseText) {
        var obj = $.parseJSON(responseText); 
        if(obj.hasOwnProperty("exception")){
            alert(obj.exception);                    
        }                 
        else {   
            savedTasks = obj.savedTasks;
            savedSchemata = obj.savedSchemata;
        }
      });  
         
    //on clicking "New Mapping Task" :
    $( "#newTask" ).click(function() {
        //1.Append the html of the wizard panels
        //that appear as a dialog box
        var mytext = '<div>'+createNewMapTaskDialog('Source')+createNewMapTaskDialog('Target')+'</div>';
        
        $('<form id="wizard1" action="#" title="New Mapping Task - Choose input" enctype="multipart/form-data">'+mytext+'</form>').appendTo('#dialog_container');
        
        var form = $("#wizard1");
        
        //form validation options
        form.validate({
          errorPlacement: function errorPlacement(error, element) {
              if(element.is('select')){
           
            
              element.after(error); }
            else
              element.before(error); 
          }
          ,rules: {
            xmlSchemaSource: {
               extension: "xsd"
            },
            xmlSchemaTarget: {
               extension: "xsd"
            },
            sqlSchemaSource: {
               extension: "sql"
            },
            sqlSchemaTarget: {
               extension: "sql"
            }
          }
        });
        
        //2.Wizard Setup
        form.children("div").steps({
          headerTag: "h3",
          bodyTag: "section",
          transitionEffect: "slideLeft",
          stepsOrientation: "vertical",
          //on changing steps or clicking the finish button validate form
          onStepChanging: function (event, currentIndex, newIndex)
          {
            // Allways allow previous action even if the current form is not valid!
            if (currentIndex > newIndex)
            {
              return true;
            }
            // Needed in some cases if the user went back (clean up)
            if (currentIndex < newIndex)
            {
              // To remove error styles
              form.find(".body:eq(" + newIndex + ") label.error").remove();
              form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
            }
            form.validate().settings.ignore = ":disabled,:hidden";
            return form.valid();
           },
          onFinishing: function (event, currentIndex)
          {
            form.validate().settings.ignore = ":disabled,:hidden";
            return form.valid();
          },
          onFinished: function () {
              var filesSource = [];
              var filesTarget = [];
              //add each option in the source/target listbox to an array that is going to be sent to the server 
              $("#SourceFilesListbox > option").each(function()
                {
                    filesSource.push($(this).val());
                });
               $("#TargetFilesListbox > option").each(function()
                {
                    filesTarget.push($(this).val());
                });    
                
                //delete the "C:\fakepath\" that some browsers (IE/Chrome) put in front of the file name
                var sqlSource = $("#sqlSchemaSource").val();
                sqlSource = sqlSource.split('\\').pop();
                var sqlTarget = $("#sqlSchemaTarget").val();
                sqlTarget = sqlTarget.split('\\').pop();
                
                var xmlSource = $("#xmlSchemaSource").val();
                xmlSource = xmlSource.split('\\').pop();
                var xmlTarget = $("#xmlSchemaTarget").val();
                xmlTarget = xmlTarget.split('\\').pop();
                
                $.ajax( {
                    url: 'NewMappingTask',
                    type: 'POST',
                    data: {scenarioNo: scenarioCounter+1, typeSource : $("#typeSource").val(), typeTarget : $("#typeTarget").val(),
                           dbnameSource : $("#dbNameSource").val(), filesSource: filesSource, xmlSource : xmlSource,
                           sqlDbNameSource: $("#sqlDbNameSource").val(), sqlSource : sqlSource, savedDBNameSource: $("#savedDBNameSource").val(),
                           driverSource : $("#driverSource").val(), uriSource: $("#uriSource").val(), 
                           usernameSource: $("#usernameSource").val(), passwordSource: $("#passwordSource").val(),
                           dbnameTarget : $("#dbNameTarget").val(), filesTarget : filesTarget, xmlTarget: xmlTarget,
                           sqlDbNameTarget: $("#sqlDbNameTarget").val(), sqlTarget : sqlTarget, savedDBNameTarget: $("#savedDBNameTarget").val(),
                           driverTarget : $("#driverTarget").val(), uriTarget: $("#uriTarget").val(), 
                           usernameTarget: $("#usernameTarget").val(), passwordTarget: $("#passwordTarget").val()},
                    beforeSend: function(xhr){
                            xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                    }
                  } ).done(function(responseText) {
                    var JSONTreeData = $.parseJSON(responseText); 
                    if(JSONTreeData.hasOwnProperty("exception")){
                        alert(JSONTreeData.exception);                    
                    } 
                    else{
                      var tempTaskName = "Mapping Task "+(scenarioCounter+1);
                      loadSchemaTrees(tempTaskName, JSONTreeData);
                    }
                });          
               $( "#wizard1" ).dialog("close");
            }
          
        });
        
        //3.Dialog Setup
        $( "#wizard1" ).dialog({
          width : 845,
          height : 680,
          modal: true,
          minWidth: 845,
          minHeight: 400,
          create: function(event, ui) { 
            var widget = $(this).dialog("widget");
          $(".ui-dialog-titlebar-close span", widget).removeClass("ui-icon-closethick").addClass("ui-icon-mine");
          }
          ,close: function(event, ui) { $(this).remove(); }
        });        
    });
    
    //on clicking "Save Task"
    $( "#saveTask" ).click(function() {
        if (currentScenario!==0){
            createSaveMappingTaskPopup();
        }
        else{
            alert('No mapping task has been selected');
        }
    });
    
    //on clicking "Open Task"
    $( "#openTask" ).click(function() {
        createOpenMappingTaskPopup();
    });
    
    //on clicking "Delete Task"
    $( "#deleteTask" ).click(function() {
        createDeleteMappingTaskPopup();
    });
    
    //on clicking "Download Task"
    $( "#downloadTask" ).click(function() {
        createDownloadMappingTaskPopup();
    });
    
    //on clicking Export TGDs
    $( "#export" ).click(function() {
        if (currentScenario!==0){
            window.location = 'ExportTgds';         
        }
        else{
            alert("No mapping task has been selected");
        }
    });  
    
    //on clicking Logout
    /*$( "#logout" ).click(function() {
        $.post('logout.jsp',function(){
            location.reload();
        });  
    }); */
    
    //on clicking Generate Transformations
    $( "#generate" ).click(function() {
       if (currentScenario!==0){
            $.ajax( {
                url: 'Generate',
                type: 'POST',
                beforeSend: function(xhr){
                        xhr.setRequestHeader("X-XSRF-TOKEN", csrftoken);
                }
              } ).done(function(responseText) {
                var obj = $.parseJSON(responseText); 
                if(obj.hasOwnProperty("exception")){
                    alert(obj.exception);                    
                } 
                else{
                    if(obj.tgds.length>0 || obj.constantTgds.length>0){
                        $("#maindiv").css("height","70%");
                        $("#tgd_div").css("display","block");
                        tabs.tabs("refresh");                          
                        addTGDsTabs(obj); 
                        }
                    else{
                        alert('No lines have been drawn'); 
                    }
                }
              });  
        }
        else{
            alert("No mapping task has been selected");
        }
    });   
     
    //Main menu options
    $("#jMenu").jMenu({
      ulWidth : 'auto',
      effects : {
      effectSpeedOpen: 300,
      effectSpeedClose: 200/*,
      effectTypeOpen: "slide",
      effectTypeClose: "slide"  */  
      },
      animatedText : false
    });             

});


// on clicking the add button, upload (part of) the selected csv file to the server
$(document).on('click','.addButton',function() { 
     addToList($(this).attr('id'), $(this).siblings("input[type='file']").attr('id')); 
 }); 
 
// on clicking the remove button, delete the selected csv file from the server
$(document).on('click','.removeButton',function() { 
     removeFromList($(this).attr('id')); 
 }); 
 
 // on changing the file input value, upload the selected xml or sql file to the server
 $(document).on('change','.fileSchema' , function(){ 
     uploadFile($(this).attr('id')); 
 });
 
 // on clicking a constant option
 $(document).on('click','.constantOption' , function(){ 
     if ($(this).attr('id')==="stringOption" || $(this).attr('id')==="numberOption"){
        $('#func_selection').attr('disabled', true); 
        $('#text_field').attr('disabled', false);        
     } 
     else if($(this).attr('id')==="funcOption"){
        $('#func_selection').attr('disabled', false); 
        $('#text_field').attr('disabled', true);
     }
     $('#text_field').removeClass( 'ui-state-error' );
 });

// bind click event to link for Schema Tree Window
$(document).on('click','.schemaProjectTreeNode',function() {
    var ptreeId = $(this).attr('id');
    var findString ='schemaProjectTreeNode';
    var activeTabNo = ptreeId.substring(ptreeId.length, ptreeId.indexOf(findString)+findString.length);
    $( '#maindiv a[href="#schemaTabs-'+activeTabNo+'"]').trigger( "click" );
});

// bind click event to link for View Windows
$(document).on('click','.projectTreeNode',function() { 
    var type = $(this).data("type");
    var activeTabNo =  $(this).closest(".projectTreeRoot").data("scenarioNo");
    //$( "#maindiv" ).tabs({ active: 2*(activeTabNo-1)+1 });
    $( '#maindiv a[href="#'+type+'Tabs-'+activeTabNo+'"]').trigger( "click" );
});

// bind double-click event to link for View Windows
$(document).on('dblclick','.projectTreeNode',function() { 
    var type = $(this).data("type");
    var activeTabNo =  $(this).closest(".projectTreeRoot").data("scenarioNo");
    //if the tab doesn't exist, create it
    var list = $("#maindiv .viewTab");
    var existing = false;
    list.each(function() {
        if($(this).attr('id')=== type+'Tabs-' + activeTabNo){
            existing=true;
        }
    });
    if (!existing){
        switch (type){
            case "viewTransformations":
                addViewTransformationsTab(activeTabNo);
                break;
            case "viewSql":
                addViewSqlTab(activeTabNo);
                break;
            case "viewXQuery":
                addViewXQueryTab(activeTabNo);
                break;
            default:
                break;
        }
    }
});

// If the document is clicked somewhere
//remove the context menu
$(document).on("mousedown", function (e) {
    // If the clicked element is not the menu
    if (!$(e.target).parents(".custom-menu").length > 0) {
        $(".custom-menu").remove();
    }
});

//prevent submit on forms (in context menus for example) when user hits enter
$(document).on("keypress", "form", function(event) { 
    return event.keyCode !== 13;
});

