#!/bin/bash

curl "http://localhost:8090/stack" \
-H "Content-Type:application/json" \
--data @<(cat <<EOF
{
  "svn_url": "http://svn-server/app1/",
  "stack_name":"2048",
  "images": [
    {
      "name":"2048",
      "tag":"latest",
      "role":"service" 
    }
  ]
}		
EOF
)



