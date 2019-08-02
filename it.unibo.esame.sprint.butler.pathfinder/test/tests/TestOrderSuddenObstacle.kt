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

class TestOrderSuddenObstacle {
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
	fun orderTest() {
		println(" %%%%%%% TestPathfinder  test destinazione ")
		sendCmdMessage(butler!!,5000)
		solveCheckGoal(pathfinder!!,"curPos(3,0)")
		solveCheckGoal(obstacle!!,"done( obstacle, 3, 0)")
		delay(3000)
		removeObstacle(obstacle!!)
		delay(20000)
		solveCheckGoal(obstacle!!,"done( removeObstacle, 3, 0)")
		solveCheckGoal(pathfinder!!,"curPos(0,0)")
		solveCheckGoalOrder(butler!!,"done(movimento, RES)","location1")
		solveCheckGoalOrder(butler!!,"done(movimento, RES)","location2")
		solveCheckGoalOrder(butler!!,"done(movimento, RES)","home")
		solveCheckGoalOrder(butler!!,"done(check, RES)","check1")
		solveCheckGoalOrder(butler!!,"done(check, RES)","check2")
 	}
//----------------------------------------
	
	fun sendCmdMessage( actor : ActorBasic, time : Long ){
		actor.scope.launch{
			println("--- sendCmdMessage cmd(testOrder, null)")
  			MsgUtil.sendMsg("cmd","cmd(testOrder,null)",actor)
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

 	fun solveCheckGoalOrder( actor : ActorBasic, goal : String, expectedSolveResult: String){
		actor.solve( "retract($goal)" )
		var result =  actor.resVar
		var solveResult = actor.getCurSol("RES").toString()
		println(" %%%%%%%  actor={$actor.name} goal= $goal  result = $result")
		assertTrue(result=="success","%%%%%%% TestPathfinder expected 'success' found $result")
		assertTrue(solveResult==expectedSolveResult,"%%%%%%% TestPathfinder expected $expectedSolveResult found $solveResult")
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
