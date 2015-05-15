# ecoreader

A vast store of knowledge on our natural environment is not contained on the web or in digital format. It exists in hand and type-written field notebooks containing useful detailed notes, drawings, and photographs.  While there are applications to aid in the digitization of these notebooks or aggregate metadata (e.g. Archivist Toolkit, Archive Space, Biodiversity Heritage Library (BHL)) there is no open source application to directly publish, visualize, and query digitized notebooks from source collections that enable direct control over the input and output process via code.

ecoReader was conceived by the Museum of Vertebrate Zoology to quickly and easily make field notebooks accessible for online viewing.  The ecoReader code library is designed to easily support multiple data input formats, using a common notebook metadata interface, and provide for consistent output using a centralized indexing mechanism.  The indexing mechanism is not yet written but will be either Mysql, SQLite, or a Document Store such as MongoDB.

A DRAFT instance of <a href='ecoreader.berkeley.edu'>ecoReader is running on our BNHM server</a>.

The basic workflow in ecoReader consists of:

1. Use Archivist Toolkit (AT) to collect images and metadata about field notebooks.

2. Export <a href='http://www.loc.gov/standards/mods/'>Metadata Object Description Schema (MODS)</a> data files from AT

3. Read MODS data files that appear in a pickup location and store the parsed information in online database (index).

4. Enable online viewing and spatial query of all notebook metadata and images by reading online database.

5. Publish data to BHL periodically.
 
