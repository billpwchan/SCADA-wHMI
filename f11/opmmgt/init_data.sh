#!/bin/sh

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"abbrev":"M","name":"VIEW_ENTITY","description":"MONITOR"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"abbrev":"C","name":"COMMAND_ENTITY","description":"CONTROL"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":3,"abbrev":"D","name":"VIEW_ENTITY_TYPE_ALARM","description":"DISPLAY"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":4,"abbrev":"A","name":"COMMAND_ENTITY_TYPE_ALARM","description":"ALARM"}' http://127.0.0.1:12080/opm/actions

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"family":"A","name":"FCT11","description":"function 11"}' http://127.0.0.1:12080/opm/functions
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"family":"A","name":"FCT12","description":"function 12"}' http://127.0.0.1:12080/opm/functions
curl -X POST -H "Content-Type: application/json" -d '{"category":3,"family":"A","name":"FCT13","description":"function 13"}' http://127.0.0.1:12080/opm/functions
curl -X POST -H "Content-Type: application/json" -d '{"category":4,"family":"B","name":"FCT21","description":"function 21"}' http://127.0.0.1:12080/opm/functions
curl -X POST -H "Content-Type: application/json" -d '{"category":5,"family":"B","name":"FCT22","description":"function 22"}' http://127.0.0.1:12080/opm/functions

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"name":"LOC1","description":"location 1"}' http://127.0.0.1:12080/opm/locations
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"name":"LOC2","description":"location 2"}' http://127.0.0.1:12080/opm/locations
curl -X POST -H "Content-Type: application/json" -d '{"category":3,"name":"LOC3","description":"location 3"}' http://127.0.0.1:12080/opm/locations
curl -X POST -H "Content-Type: application/json" -d '{"category":4,"name":"LOC4","description":"location 4"}' http://127.0.0.1:12080/opm/locations
curl -X POST -H "Content-Type: application/json" -d '{"category":5,"name":"LOC5","description":"location 5"}' http://127.0.0.1:12080/opm/locations

curl -X POST -H "Content-Type: application/json" -d '{"name":"PROF1","description":"profile 1"}' http://127.0.0.1:12080/opm/profiles
curl -X POST -H "Content-Type: application/json" -d '{"name":"PROF2","description":"profile 2"}' http://127.0.0.1:12080/opm/profiles
curl -X POST -H "Content-Type: application/json" -d '{"name":"PROF3","description":"profile 3"}' http://127.0.0.1:12080/opm/profiles

curl -X POST -H "Content-Type: application/json" -d '{"mask1":"MCD","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":1},"profile":{"name":"PROF1"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"MC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":2},"profile":{"name":"PROF1"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"MD","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":1},"profile":{"name":"PROF1"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"MD","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":2},"profile":{"name":"PROF1"}}' http://127.0.0.1:12080/opm/masks

curl -X POST -H "Content-Type: application/json" -d '{"mask1":"1","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":1},"profile":{"name":"PROF2"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"1","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":2},"profile":{"name":"PROF2"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"0","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":1},"profile":{"name":"PROF2"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"0","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":2},"profile":{"name":"PROF2"}}' http://127.0.0.1:12080/opm/masks
