<%@ include file="header-home.jsp" %>

<div id="validation" class="section">
    <div class="row" id="query">
        <p>
            BETA Version! Search and read scanned historical field notebooks from the Museum of Vertebrate Zoology.
            <br>
            This service replaces the MVZ Archival Field Notebooks Query Page.  We are actively updating data,
            and should be complete November 1st.  Meanwhile, a more limited selection of archives are searchable
             here.  For any inquiries or issues contact <b>mvzarchives@berkeley.edu</b>.
        </p>


        <div class="form-horizontal my-form">
          <div class="form-group form-group-sm" id="author_row">
            <label for="author" class="col-md-2 control-label">Author Name</label>
            <div class="col-md-3" id="author_combobox">
              <select class="combobox form-control" width=20 id="authors">
                        <option value=''>Loading authors ...</option>
              </select>
            </div>
          <div class="col-md-1" id="toggle_query">
            <button class="btn btn-default btn-sm" type="button" onclick="toggleQuery();">+</button>
          </div>
          <div class="col-md-1"><input type="button" value="Submit" class="btn btn-default btn-sm btn-block"></div>
          <div class="col-md-1"><input type="button" value="Reset" class="btn btn-default btn-sm btn-block"></div>

          </div>
        </div>

        <form class="form-inline my-form" >

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

</div>


<script>
    $(document).ready(function() {
        populateAuthors();
        $('input[type=button][value="Submit"]').click(function() {
             populateVolumes();
        });
        $('input[type=button][value="Reset"]').click(function() {
            location.reload();
        });
    });
</script>

<%@ include file="footer.jsp" %>