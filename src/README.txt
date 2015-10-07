This is an implementation of the Routing Information Protocol (a Distance Vector routing protocol)
which can be run on various machines where each machine acts as a router in a network.

Note: This project is still under development.

Once completely developed, it will do the following:
Each computer takes the list of neighbors it has i.e their IP address and subnet mask and the interface via which they are connected to that neighbor.
Also, it takes the subnet mask of the network it is connected to.
It then creates the initial routing table and runs the distance vector algorithm to slowly converge and get the next hop for all destinations in the network.