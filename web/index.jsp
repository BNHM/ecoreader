<%@ include file="header-home.jsp" %>

	<div id="site_content">
	    <div id="content">
            <div class="container"  style="width:960px" id="query">

                <form role="form" class="form-horizontal" action=GET >

                <!-- AUTHORS -->
                <div class="form-group" id="author_row">
                    <label class="col-sm-2 control-label" style="width:150px">Author Name</label>
                    <div class="col-sm-4" id="author_combobox">
                        <select class="combobox form-control" width=20 id="authors" name="authors">
                             <option value=''>Loading authors ...</option>
                        </select>
                    </div>
                    <div class="col-sm-3">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="scanned_only" value="true"> Scanned sections only
                            </label>
                        </div>
                    </div>
                </div>

                <!-- DATES -->
                <div class="form-group">
                    <label class="col-sm-1 control-label" style="width:150px">Year From</label>
                    <div class="col-sm-2">
                        <input type="text" class="form-control int_input"  name="begin_date" placeholder="1900">
                    </div>
                    <label class="col-sm-1 control-label">Year To</label>
                    <div class="col-sm-2">
                        <!-- <span id="and" style="display:inline-block;vertical-align:bottom;line-height:normal;">and</span>-->
                        <input type="text" class="form-control int_input" name="end_date" placeholder="2000">
                    </div>
                    <label for="volume_id" class="col-sm-1 control-label">Volume Id</label>
                    <div class="col-sm-2">
                        <input type="text" class="form-control int_input" width=20 name="volume_id" placeholder="500">
                     </div>
                </div>

                <!-- SECTION TITLE -->
               <div class="form-group" id="section_title">
                    <label for="section_title" class="col-sm-2 control-label" style="width:150px">Section Title</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" name="section_title" width=40 placeholder="Inyo">
                    </div>
                </div>

                <!-- SUBMIT/RESET -->
                <div class="form-group">
                    <label class="col-sm-2" style="width:150px">&nbsp;</label>
                    <div class="col-sm-2"><input type="button" value="Submit" class="btn btn-default btn-sm btn-block"></div>
                    <div class="col-sm-2"><input type="button" value="Reset" class="btn btn-default btn-sm btn-block"></div>
                </div>

               </form>
            </div>

        <div class="container" style="width:960px;" id="results"></div>


        </div><!--close content_container-->
	</div><!--close site_content-->
</div><!--close main-->


<script>
    $(document).ready(function() {

        populateAuthors();

        handleIncomingParameters();

        $('input[type=button][value="Submit"]').click(function() {
            handleSubmit();
        });
        $('input[type=button][value="Reset"]').click(function() {
            window.location = window.location.pathname;
        });


    });

// Get URL Parameters from window.location.href
function getUrlParams() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    // Detect if there are no parameters
    if (window.location.href == hashes[0]) {
       return false;
    }
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

// build URL from selected form elements
function buildURLFromSelected() {
    var url = window.location.pathname + "?";
    $(":input, #authors select").each(function(){
      if (this.value && $(this).attr('name')) {
        if ($(this).attr('name') == "scanned_only") {
            if ($(this).is(':checked')) {
                url += "scanned_only=true&";
            } else {
                url += "scanned_only=false&";
            }
        } else {
            url += $(this).attr('name') + "=" + encodeURIComponent(this.value) + "&";  //appends the name and value
        }
      }
    });
    return url;
}

// Handle incoming URL parameters and assign to proper fields
function handleIncomingParameters() {
    var query=getUrlParams();
    var foundValue = false;
    $.each(query, function(index, key){
       value = erDecode(query[key]);
       foundValue = true;
       if (key == "authors"){
            $('#authors').val(value);
            // boostrap-combobox.js requires a refresh here
            $('#authors').data('combobox').refresh();
        } else if (key == "scanned_only")  {
            if (value == "true")
                $("input[name="+ key +"]").prop('checked',true);
            else 
                $("input[name="+ key +"]").prop('checked',false);
        } else {
            $("input[name="+ key +"]").attr("value",value);
        }
    });
    // if there is some value then send it to populateVolumes() function (like pressing submit)
    if (foundValue) {
        var url = window.location.pathname + "?";
        $.each(query, function(index, key){
            url += key +"=" +query[key] + "&";
        });
        history.replaceState("history", "", url.substring(0, url.length - 1));
        populateVolumes();
    }
}

// Handle spaces in value names
function erDecode(a) {
    return decodeURIComponent((a+'').replace(/\+/g, '%20'));
}

// Handles submit function
function handleSubmit() {
    var url = buildURLFromSelected();
    //changes the url without reloading and removes last &
    history.replaceState("history", "", url.substring(0, url.length - 1));

    populateVolumes();
}

</script>

<%@ include file="footer.jsp" %>
