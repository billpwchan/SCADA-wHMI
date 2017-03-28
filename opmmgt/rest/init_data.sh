#!/bin/sh

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"abbrev":"M","name":"M","description":"MONITOR"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"abbrev":"C","name":"C","description":"CONTROL"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":3,"abbrev":"D","name":"D","description":"DISPLAY ALARM"}' http://127.0.0.1:12080/opm/actions
curl -X POST -H "Content-Type: application/json" -d '{"category":4,"abbrev":"A","name":"A","description":"ALARM ACK"}' http://127.0.0.1:12080/opm/actions

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"family":"A","name":"11","description":"function 11"}' http://127.0.0.1:12080/opm/functions
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"family":"A","name":"12","description":"function 12"}' http://127.0.0.1:12080/opm/functions

curl -X POST -H "Content-Type: application/json" -d '{"category":1,"name":"1","description":"location 1"}' http://127.0.0.1:12080/opm/locations
curl -X POST -H "Content-Type: application/json" -d '{"category":2,"name":"2","description":"location 2"}' http://127.0.0.1:12080/opm/locations

curl -X POST -H "Content-Type: application/json" -d '{"name":"ROOT","description":"ROOT"}' http://127.0.0.1:12080/opm/profiles

curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":1},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":1},"location":{"category":2},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":1},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
curl -X POST -H "Content-Type: application/json" -d '{"mask1":"DMAC","mask2":"MCD","mask3":"MCD","mask4":"MCD","function":{"category":2},"location":{"category":2},"profile":{"name":"ROOT"}}' http://127.0.0.1:12080/opm/masks
