#!/bin/bash
META='info.txt'
nodefile="nodes-list.txt"
if [ -f "$META" ]; then
    export server_private_ip=$(echo `grep -i "Server-private-ip" $META` | cut -d ":" -f 2)
    export client_private_ip=$(echo `grep -i "Client-private-ip" $META` | cut -d ":" -f 2)
    export number_of_nodes=$(echo `grep -i "Number-of-nodes" $META` | cut -d ":" -f 2)
    export first_port=$(echo `grep -i "First-port" $META` | cut -d ":" -f 2)
else
    echo "$META does not exist. Abort!"
    exit
fi

> $nodefile

if [[ $1 == one-server ]]; then

  echo "$client_private_ip:43100" >> $nodefile
  echo "assuming the one server is running on the client machine"
  echo "generating nodes-list.txt file with $client_private_ip:43100"

elif [[ $1 == server ]]; then

  if [ -z "$number_of_nodes" ] || [ -z "$first_port" ]
  then
    echo "No port number and/ or number of nodes are set. Abort!"
    exit
  fi

  last_port=$(expr $first_port + $number_of_nodes)
  last_port=$(expr $last_port - 1)
  for port in `seq $first_port $last_port`
  do
    echo "$server_private_ip:$port" >> $nodefile
  done
  echo "generating nodes-list.txt for server nodes $server_private_ip from port $first_port to $last_port"

fi
