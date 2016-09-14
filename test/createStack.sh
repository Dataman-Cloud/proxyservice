#!/bin/bash

curl "http://localhost:8090/stack" \
-H "Content-Type:application/json" \
--data @<(cat <<EOF
{
  "svn_url": "http://svn-server/app1/",
  "stack_name":"stack_test_001",
  "images": [
    {
      "name":"sampleeureka",
      "tag":"1.0.1-SNAPSHOT",
      "role":"eureka" 
    },
    {
      "name":"sampleconfig",
      "tag":"1.0.1-SNAPSHOT",
      "role":"config" 
    },
    {
      "name":"sampleservice",
      "tag":"1.0.1-SNAPSHOT",
      "role":"service" 
    },
    {
      "name":"sampleaggregate1",
      "tag":"1.0.1-SNAPSHOT",
      "role":"service" 
    },
    {
      "name":"sampleaggregate2",
      "tag":"1.0.1-SNAPSHOT",
      "role":"service" 
    },
    {
      "name":"samplegateway",
      "tag":"1.0.1-SNAPSHOT",
      "role":"apigateway" 
    }
  ]
}		
EOF
)



