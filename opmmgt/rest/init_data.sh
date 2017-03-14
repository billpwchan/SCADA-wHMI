#!/bin/sh

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"abbrev":"M","name":"VIEW_ENTITY","description":"MONITOR"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"abbrev":"C","name":"COMMAND_ENTITY","description":"CONTROL"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":3,"abbrev":"D","name":"VIEW_ENTITY_TYPE_ALARM","description":"DISPLAY"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":4,"abbrev":"A","name":"COMMAND_ENTITY_TYPE_ALARM","description":"ALARM"}' http://127.0.0.1:12080/opm/actions

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"family":"A","name":"FCT11","description":"function 11"}' http://127.0.0.1:12080/opm/functions
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"family":"A","name":"FCT12","description":"function 12"}' http://127.0.0.1:12080/opm/functions

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"name":"LOC1","description":"location 1"}' http://127.0.0.1:12080/opm/locations
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"name":"LOC2","description":"location 2"}' http://127.0.0.1:12080/opm/locations

curl -X POST -H "Content-Type: application/json" -d '{"name":"ROOT","description":"ROOT"}' http://127.0.0.1:12080/opm/profiles

curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":1},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":2},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":1},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":2},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
