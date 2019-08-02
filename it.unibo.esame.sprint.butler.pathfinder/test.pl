comando( testDestination, null ):-
	assert( azione( movimento, location1, null, null ) ).
	
comando( testOrder, null ):-
	assert( azione( movimento, location1, null, null ) ),
	assert( azione( check, check1, null, null ) ),
	assert( azione( movimento, location2, null, null ) ),
	assert( azione( check, check2, null, null ) ),
	assert( azione( movimento, home, null, null ) ).
	
comando( goHome, null):-
	assert( azione( movimento, home, null, null ) ).
	
	

	