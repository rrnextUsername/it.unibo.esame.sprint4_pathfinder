package tests

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.GlobalScope
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch
import it.unibo.kactor.sysUtil
import org.junit.jupiter.api.AfterEach
import it.unibo.kactor.MsgUtil

class TestDestinationReachedSuddenObstacle {
  var butler : ActorBasic? = null
  var pathfinder : ActorBasic? = null
  var obstacle : ActorBasic? = null
	
	@BeforeEach
	fun systemSetUp() {
  	 		GlobalScope.launch{ //activate an observer ...
 				itunibo.coap.observer.main()		//blocking
 			}	
  	 		GlobalScope.launch{
 			    println(" %%%%%%% TestPathfinder starts fridge mind ")
				it.unibo.ctxButler.main()
 			}
			delay(5000)		//give the time to start
			butler = sysUtil.getActor("butler")	
			pathfinder = sysUtil.getActor("pathfinder")	
			obstacle = sysUtil.getActor("dummy_obstacle")
			materializeObstacle(obstacle!!)
		    println(" %%%%%%% TestPathfinder getActors resource=${butler}")
 	}
 
	@AfterEach
	fun terminate() {
		println(" %%%%%%% TestPathfinder terminate ")
	}
 
	@Test
	fun destinationReachedTest() {
		println(" %%%%%%% TestPathfinder  test destinazione ")
		sendCmdMessage(butler!!,5000)
		solveCheckGoal(pathfinder!!,"curPos(3,0)")
		solveCheckGoal(obstacle!!,"done( obstacle, 3, 0)")
		delay(5000)
		removeObstacle(obstacle!!)
		delay(5000)
		solveCheckGoal(obstacle!!,"done( removeObstacle, 3, 0)")
		solveCheckGoal(butler!!,"done(movimento, location1)")
		solveCheckGoal(pathfinder!!,"curPos(5,0)")
 	}
//----------------------------------------
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
		actor.scope.launch{
			println("--- sendCmdMessage cmd(testDestination, null)")
  			MsgUtil.sendMsg("cmd","cmd(testDestination,null)",actor)
 		}
		delay(time) //give time to do the move
  	}
	
	fun materializeObstacle(actor : ActorBasic){
		actor.scope.launch{
			println("--- sendMsg initObstacle")
  			MsgUtil.sendMsg("initObstacle","initObstacle",actor)
 		}
	}
	
	fun removeObstacle(actor : ActorBasic){
		actor.scope.launch{
			println("--- sendMsg removeObstacle")
  			MsgUtil.sendMsg("removeObstacle","removeObstacle",actor)
 		}
	}

 	
	fun solveCheckGoal( actor : ActorBasic, goal : String ){
		actor.solve( goal  )
		var result =  actor.resVar
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestPathfinder expected 'success' found $result")
	}

	fun delay( time : Long ){
		Thread.sleep( time )
	}


}
