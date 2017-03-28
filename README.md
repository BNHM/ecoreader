# ecoreader

A vast store of knowledge on our natural environment is not contained on the web or in digital format. It exists in hand and type-written field notebooks containing critical ecological and biological details in notes, drawings, and photographs.  While there are applications to aid in the digitization of these notebooks or aggregate metadata (e.g. Archivist Toolkit, Archive Space, Biodiversity Heritage Library (BHL)) there is no open source application to directly publish, visualize, and query digitized notebooks from source collections that enable direct control over the input and output process via code.

_EcoReader_ was conceived by the Museum of Vertebrate Zoology to quickly and easily make field notebooks accessible for online viewing.  The EcoReader code library is designed to easily support multiple data input formats, using a common notebook metadata interface, and provide for consistent output using a centralized indexing mechanism.  The indexing mechanism is not yet written but will be either Mysql, SQLite, or a Document Store such as MongoDB.    

An instance of [EcoReader](http://ecoreader.berkeley.edu/) is running on our BNHM server.

The basic workflow in ecoReader consists of:

1. Archivists use Archivist Toolkit (AT) to catalog field notebooks while images of notebook pages are uploaded to web archive.

2. Export <a href='http://www.loc.gov/standards/mods/'>Metadata Object Description Schema (MODS)</a> metadata files from AT

3. Read MODS files that appear in a designated pickup location and store the parsed information in online database (index).

4. Enable online viewing and spatial query of all notebook metadata and images by reading online database.

5. TBD: Publish data to BHL periodically.
     
_EcoReader_ replaces the "BSCIT MVZ Archival Field Notebooks" website. This first iteration is funded through the [MVZ 'Hidden Collections' CLIR grant ](http://www.clir.org/hiddencollections/awards/for-2011).

