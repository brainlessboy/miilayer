miilayer
========

Miicraft printer use multiple layers to print a 3d model from resin by curing the resin with UV light
each layer/slice of the model is a black and white image that can be edited manually.

each layer is approximately 100 micron and requries 500 layer for a 5cm model.

in some cases it is required to edit the layers for minor corrections and to add supporting structures

This simple software offers the possibility to add supporting structures and to edit simgle images rapidly

features:
- manualy position supporting points
- menu functions 'generate structures' automatically generates the structures in all layers downard
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

6) browse through all the layers and correct them manually by using the black or white collor to paint on the layers
note that after you edit a layer you need to save it by pressing save on the bottom

Usage:

download the MiiLayer.jar and doublecklick. in most cases this should start the layer editor
if the editor does not start, type

java -jar MiiLayer.jar mii 

this will start the editor for sure :-)


Future Possible Roadmap:
- add OnionSkin to see lower and upper layers
- add validation to discover floating layers or layer parts
- generation of automatic more complex supporting structures
- 3D preview mode to validate structrues visually (already experimental)
