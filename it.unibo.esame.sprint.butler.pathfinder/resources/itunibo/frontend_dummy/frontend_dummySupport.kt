package itunibo.frontend_dummy

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import itunibo.coap.modelResourceCoap
import it.unibo.kactor.ApplMessage

object frontend_dummySupport{
lateinit var resourcecoap : modelResourceCoap
	
	fun setCoapResource( rescoap : modelResourceCoap ){
		resourcecoap = rescoap
	}
	
	fun persist( actor: ActorBasic, currentMsg: ApplMessage){
			actor.scope.launch{
				
				val fridge_inventory= currentMsg.msgContent()				
				actor.solve("assert(result( frigo , $fridge_inventory ))")
  			}	
	}	
}

