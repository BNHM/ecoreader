<%@ include file="header-home.jsp" %>

<div id="validation" class="section">
    <div class="row" id="query">
        <p>
            Search and read scanned historical field notebooks from the Museum of Vertebrate Zoology
        </p>

        <form class="form-horizontal" >
          <div class="form-group form-group-sm">
            <label for="authors" class="col-md-2 control-label">Author Name</label>
            <div class="col-md-8">
              <select class="combobox form-control" width=20 name="authors" id="authors">
                        <option value=0>Loading authors ...</option>
              </select>
            </div>
          </div>
          <div class="form-group form-group-sm">
            <label for="begin_date" class="col-md-2 control-label">Year Between</label>
              <div class="col-md-2">
                  <input type="text" class="form-control" id="begin_date">
              </div>
              <div class="col-md-1" style="height:30px;line-height:30px;text-align:center;">
                  <span style="display:inline-block;vertical-align:bottom;line-height:normal;">and</span>
              </div>
              <div class="col-md-2">
                  <input type="text" class="form-control" id="end_date">
              </div>
          </div>
          <div class="form-group form-group-sm">
            <label for="section_title" class="col-md-2 control-label">Section Title</label>
            <div class="col-md-8">
              <input type="text" class="form-control" id="section_title">
            </div>
          </div>
          <div class="form-group form-group-sm">
            <label for="volume_id" class="col-md-2 control-label">Volume Id</label>
            <div class="col-md-2">
              <input type="text" class="form-control" id="volume_id">
            </div>
          </div>
          <div class="form-group form-group-sm">
            <label for="type" class="col-md-2 control-label">Type</label>
            <div class="col-md-8">
              <select class="form-control" width=20 name="type" id="type">
                        <option value="any">Any<option>
              </select>
            </div>
          </div>
          <div class="form-group form-group-sm">
            <div class="col-md-offset-2 col-md-8">
              <div class="checkbox">
                <label>
                  <input type="checkbox"> Scanned sections only
                </label>
              </div>
            </div>
          </div>
          <div class="form-group form-group-sm">
            <div class="col-md-offset-2 col-md-8">
              <input type="button" value="Submit" class="btn btn-default">
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
        $("input[type=button]").click(function() {
             populateVolumes();
        });
    });
</script>

<%@ include file="footer.jsp" %>