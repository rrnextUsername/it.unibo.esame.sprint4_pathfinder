package itunibo.butler

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import itunibo.coap.modelResourceCoap

object butlerResourceModelSupport{
lateinit var resourcecoap : modelResourceCoap
	
	fun setCoapResource( rescoap : modelResourceCoap ){
		resourcecoap = rescoap
	}
	
	fun updateModelState(actor: ActorBasic, state: String){
		actor.solve("retract( stato( _ ) )")
		actor.solve("assert( stato($state) )")
		
	}	
}

