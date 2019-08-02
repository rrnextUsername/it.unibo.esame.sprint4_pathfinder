stato( spento ).


inventario( table , [] ).
inventario( robot , [] ).
inventario( pantry , [] ).
inventario( dishwasher , [] ).

showResourceModel :- 
	output("RESOURCE MODEL ---------- "),
	showResources,
	output("--------------------------").
		
showResources :- 
	inventario( table , C ),
 	output( inventario( table , C ) ),
	inventario( robot , C ),
 	output( inventario( robot , C ) ),
	inventario( pantry , C ),
 	output( inventario( pantry , C ) ),
	inventario( dishwasher , C ),
 	output( inventario( dishwasher , C ) ),
	stato( S ),
 	output( stato( S ) ),
	fail.
showResources.			

output( M ) :- stdout <- println( M ).

initResourceTheory :- output("resourceModel loaded").
:- initialization(initResourceTheory).