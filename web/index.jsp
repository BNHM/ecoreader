<%@ include file="header-home.jsp" %>

<div id="validation" class="section">
    <div class="sectioncontent">
        Search and read scanned historical field notebooks from the Museum of Vertebrate Zoology

        <p>

        <table>
            <tr>
                <td align="right">Author Name&nbsp;&nbsp;</td>
                <td>
                    <select class="combobox" width=20 name="authors" id="authors">
                        <option value=0>Loading authors ...</option>
                    </select>
                </td>
            </tr>

            <tr><td></td><td></td></tr>

            <tr>
                <td></td>
                <td><input type="button" value="Submit" class="btn btn-default btn-xs"></td>
            </tr>
        </table>

        <div id="results">
        </div>

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