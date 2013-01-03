miilayer
========

Miicraft printer use multiple layers to print a 3d model from resin by curing the resin with UV light
each layer/slice of the model is a black and white image that can be edited manually.

each layer is approximately 100 micron and requries 500 layer for a 5cm model.

in some cases it is required to edit the layers for minor corrections and to add supporting structures

This simple software offers the possibility to add supporting structures and to edit simgle images rapidly

features:
- manualy position supporting points
- menu functions 'generate structures' automatically generates the structures in all layers downward
- paint function to correct layers manually
- move up and down through layers
- (experimental) 3d model reconstruction according to layer images

how to use:

1) choose the directory where the image layers of your model are located
2) in the first tab 'layer' use travel up and down to view any layer (starts from the bottom)
3) on any layer place the required supporting point by klicking into the image (the size of the point was tested to be optimal)
note: only place a point on the layer where the structure should start
4) continue adding supports until you feel ready
5) choose from the files menue 'Generate Structure' this will automatically paint the points in all layers to the very bottom layer

note that 'Generate Structure' cannot be undone!

if you have tall structures then it is recommended to connect some of the supporting dots on every 10th layer to make the supportin
structure more stable.
Note: you can paint a stroke at the top layer where your structure starts and choose the GenerateStructure Step, the stroke you just 
painted will be reproduced on every 10th layer automaticall. Caution to clear the stroke after this is done ... or clear all strokes
before painting new ones :-)

6) browse through all the layers and correct them manually by using the black or white color to paint on the layers
note that after you edit a layer you need to save it by pressing save on the bottom
also don't forget to clear strokes before generation of structures

How to Start MiiLayer
=====================

download the MiiLayer.jar and doublecklick. in most cases this should start the layer editor
if the editor does not start, type

java -jar MiiLayer.jar MiiLayer 

this will start the editor for sure :-)

Software Update 1.1.2013
=========================
- Paint Strokes are now remembered and can be propagated either on each layer or every 10th layer to support structure painting
- add clearPoints and clearStrokes to clear all Points or Strokes
Note: allways make sure you clear Strokes before you hit the Generate Structure or Genereate Structure Step, these cannot be undone
and will scribbel in all your layers

Future Roadmap: (things I might add if MiiCraft is not enhancing there software)
================================================================================
- add OnionSkin to see lower and upper layers
- add validation to discover floating layers or layer parts
- generation of automatic more complex supporting structures
- 3D preview mode to validate structrues visually (already experimental)
- analizing model for fully automatic structure generation (no idea so far how this should work but it would be cool)
- undo Generate Structure Function ... (this would actually just require to copy image to a tmp directory)
- Resolution independent (available for other resin printers)
