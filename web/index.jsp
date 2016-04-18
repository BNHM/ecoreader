<%@ include file="header-home.jsp" %>

	<div id="site_content">
	  <div id="content">
		<div class="content_item">

            <div id="validation" class="section">
                <div class="row" id="query">
                   <!-- <h1>Welcome to the EcoReader</h1>
      	            <p>This portal provides access to digitized field notes from the Museum of Vertebrate Zoology Archives. Field notes that have not been scanned are also listed for reference. We will continue to add more volumes as they are cataloged. Please contact the Archives at mvzarchives@berkeley.edu.com for more information.</p>
      	            -->

        <div class="form-horizontal my-form">
          <div class="form-group form-group-sm" id="author_row">
            <label for="author" class="control-label col-md-2" style="font-size: 14px">Author Name</label>
            <div class="col-md-3" id="author_combobox">
              <select class="combobox form-control" width=20 id="authors" name="authors">
                        <option value=''>Loading authors ...</option>
              </select>
            </div>
          <!--<div class="col-md-1" id="toggle_query">
            <button class="btn btn-default btn-sm" type="button" onclick="toggleQuery();">+</button>
          </div>-->
          <div class="col-md-1"><input type="button" value="Submit" class="btn btn-default btn-sm btn-block"></div>
          <div class="col-md-1"><input type="button" value="Reset" class="btn btn-default btn-sm btn-block"></div>

          </div>
        </div>

        <form class="form-inline my-form" action=GET >

        <div class="toggle-content" id="query_toggle">
          <div class="form-group form-group-sm">
            <label for="begin_date" class="control-label">Year Between</label>
              <input type="text" class="form-control int_input" name="begin_date">
              <span id="and" style="display:inline-block;vertical-align:bottom;line-height:normal;">and</span>
              <input type="text" class="form-control int_input" name="end_date">
          </div>
          <div class="form-group form-group-sm" id="section_title">
            <label for="section_title" class="control-label">Section Title</label>
              <input type="text" class="form-control" name="section_title">
          </div>
          <div class="form-group form-group-sm">
            <label for="volume_id" class="control-label">Volume Id</label>
              <input type="text" class="form-control int_input" name="volume_id">
          </div>
          <div class="form-group form-group-sm">
              <div class="checkbox">
                <label>
                  <input type="checkbox" name="scanned_only" value="true"> Scanned sections only
                </label>
              </div>
          </div>
          </div>
        </form>
    </div>

        <div class="row" id="results">
        </div>

</div><!--close the validation div-->

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
      if (this.value && $(this).attr('name'))
         url += $(this).attr('name') + "=" + encodeURIComponent(this.value) + "&";  //appends the name and value
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