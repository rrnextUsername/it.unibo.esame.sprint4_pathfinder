%====================================================================================
% butler description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxbutler, "localhost",  "MQTT", "0" ).
context(ctxrobotmind, "localhost",  "MQTT", "0" ).
 qactor( resourcemodel, ctxrobotmind, "external").
  qactor( onestepahead, ctxrobotmind, "external").
  qactor( butler, ctxbutler, "it.unibo.butler.Butler").
  qactor( pathfinder, ctxbutler, "it.unibo.pathfinder.Pathfinder").
  qactor( dummy_obstacle, ctxbutler, "it.unibo.dummy_obstacle.Dummy_obstacle").
