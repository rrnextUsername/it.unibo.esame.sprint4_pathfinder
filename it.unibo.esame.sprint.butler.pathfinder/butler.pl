%====================================================================================
% butler description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxbutler, "localhost",  "MQTT", "0" ).
 qactor( butler, ctxbutler, "it.unibo.butler.Butler").
  qactor( pathfinder, ctxbutler, "it.unibo.pathfinder.Pathfinder").
  qactor( dummy_obstacle, ctxbutler, "it.unibo.dummy_obstacle.Dummy_obstacle").
