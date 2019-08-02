/* Generated by AN DISI Unibo */ 
package it.unibo.pathfinder

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pathfinder ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
		var mapEmpty    = false
		val mapname     = "roomMapWithTable" 
		
		var Curmove     = "" 
		var curmoveIsForward = false 
		
		//REAL ROBOT
		//var StepTime   = 1000 	 
		//var PauseTime  = 500 
		
		//VIRTUAL ROBOT
		var StepTime   = 330	//for virtual
		var PauseTime  = 500
		
		var PauseTimeL  = PauseTime.toLong()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('moves.pl')","") //set resVar	
						itunibo.planner.plannerUtil.initAI(  )
						itunibo.planner.moveUtils.loadRoomMap(myself ,mapname )
						itunibo.planner.moveUtils.showCurrentRobotState(  )
							val MapStr =  itunibo.planner.plannerUtil.getMapOneLine()  
						println("MapStr: $MapStr")
						println("&&&  workerinroom STARTED")
					}
					 transition( edgeName="goto",targetState="waitGoal", cond=doswitch() )
				}	 
				state("waitGoal") { //this:State
					action { //it:State
					}
					 transition(edgeName="t09",targetState="setGoalAndDo",cond=whenDispatch("setGoal"))
				}	 
				state("setGoalAndDo") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("setGoal(X,Y)"), Term.createTerm("setGoal(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								storeCurrentMessageForReply()
								itunibo.planner.plannerUtil.setGoal( payloadArg(0), payloadArg(1)  )
								itunibo.planner.moveUtils.doPlan(myself)
						}
					}
					 transition( edgeName="goto",targetState="executePlannedActions", cond=doswitch() )
				}	 
				state("executePlannedActions") { //this:State
					action { //it:State
						solve("move(M)","") //set resVar	
						if(currentSolution.isSuccess()) { Curmove = getCurSol("M").toString()
						curmoveIsForward=(Curmove == "w")
						 }
						else
						{ Curmove = ""
						curmoveIsForward=false
						 }
					}
					 transition( edgeName="goto",targetState="checkAndDoAction", cond=doswitchGuarded({(Curmove.length>0) }) )
					transition( edgeName="goto",targetState="goalOk", cond=doswitchGuarded({! (Curmove.length>0) }) )
				}	 
				state("goalOk") { //this:State
					action { //it:State
						itunibo.planner.moveUtils.showCurrentRobotState(  )
							val MapStr =  itunibo.planner.plannerUtil.getMapOneLine()  
						forward("modelUpdate", "modelUpdate(roomMap,$MapStr)" ,"resourcemodel" ) 
						replyToCaller("goalReached", "goalReached(ok)")
					}
					 transition( edgeName="goto",targetState="waitGoal", cond=doswitch() )
				}	 
				state("checkAndDoAction") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="doForwardMove", cond=doswitchGuarded({curmoveIsForward}) )
					transition( edgeName="goto",targetState="doTheMove", cond=doswitchGuarded({! curmoveIsForward}) )
				}	 
				state("doTheMove") { //this:State
					action { //it:State
						solve("retract(move(M))","") //set resVar	
						itunibo.planner.moveUtils.rotate(myself ,Curmove, PauseTime )
					}
					 transition( edgeName="goto",targetState="executePlannedActions", cond=doswitch() )
				}	 
				state("doForwardMove") { //this:State
					action { //it:State
						delay(PauseTimeL)
						itunibo.planner.moveUtils.attemptTomoveAhead(myself ,StepTime )
						solve("curPos(X,Y)","") //set resVar	
						val X = getCurSol("X").toString()
						val Y = getCurSol("Y").toString()
						println("curPos:	$X, $Y")
						emit("makingStep", "makingStep($X,$Y)" ) 
					}
					 transition(edgeName="t010",targetState="handleStopAppl",cond=whenEvent("stopAppl"))
					transition(edgeName="t011",targetState="handleStepOk",cond=whenDispatch("stepOk"))
					transition(edgeName="t012",targetState="hadleStepFail",cond=whenDispatch("stepFail"))
				}	 
				state("handleStopAppl") { //this:State
					action { //it:State
						println("APPLICATION STOPPED. Waiting for a reactivate")
						solve("assert(done(stop))","") //set resVar	
					}
					 transition(edgeName="t013",targetState="handleReactivateAppl",cond=whenEvent("reactivateAppl"))
				}	 
				state("handleReactivateAppl") { //this:State
					action { //it:State
						println("APPLICATION RESUMED")
						solve("assert(done(restart))","") //set resVar	
					}
					 transition(edgeName="t014",targetState="handleStepOk",cond=whenDispatch("stepOk"))
					transition(edgeName="t015",targetState="hadleStepFail",cond=whenDispatch("stepFail"))
				}	 
				state("handleStepOk") { //this:State
					action { //it:State
						itunibo.planner.moveUtils.updateMapAfterAheadOk(myself)
						solve("retract(move(M))","") //set resVar	
					}
					 transition( edgeName="goto",targetState="executePlannedActions", cond=doswitch() )
				}	 
				state("hadleStepFail") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("stepFail(R,T)"), Term.createTerm("stepFail(RESULT,DURATION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								solve("curPos(X,Y)","") //set resVar	
								val X = getCurSol("X").toString()
								val Y = getCurSol("Y").toString()
								solve("assert(stopped,$X,$Y)","") //set resVar	
								val Duration= payloadArg(1).toInt()
								itunibo.planner.moveUtils.backToCompensate(myself ,Duration, PauseTime )
						}
						stateTimer = TimerActor("timer_hadleStepFail", 
							scope, context!!, "local_tout_pathfinder_hadleStepFail", 2000.toLong() )
					}
					 transition(edgeName="t016",targetState="executePlannedActions",cond=whenTimeout("local_tout_pathfinder_hadleStepFail"))   
				}	 
			}
		}
}
