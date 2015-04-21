# nature-reader

A vast store of knowledge concerning the natural environment is not contained on the web and not even in digital format. It exists in hand and type-written field notebooks containing useful detailed notes and descriptions written.  While there are applications to aid in the digitization of these notebooks or aggregate metadata (e.g. Archivist Toolkit, Archive Space, Biodiversity Heritage Library (BHL)) there is no open source application to directly publish, visualize, and query digitized notebooks from source collections.  

Nature Reader was conceived by the Museum of Vertebrate Zoology to quickly and easily make field notebooks accessible for online viewing and to later interface with BHL.

The basic workflow in Nature Reader consists of:

1. Use Archivist Toolkit (AT) to collect images and metadata about field notebooks.
2. Export <a href='http://www.loc.gov/standards/mods/'>Metadata Object Description Schema (MODS)</a> data files from AT
3. Read MODS data files and store parsed information in an online index
4. Enable online viewing and spatial query of all notebook metadata and images by reading the online index.
 
