# Drone-Pathfinding-Delivery-System
This application functions by connecting to different endpoints from a REST server. Unfortunately, these servers are no longer available.

Instead, some result files have been produced from previous runs of the system to prove it's functionality.

This system takes a date, and a base address of a REST server as parameters, and uses different endpoints to retrieve dynamic data for that program run. Data on the REST server is in JSON format, so the system has to make a connection to the server to retrieve the data, and then deserialise it.

The objective of the system is to deliver orders via drone for each day, by retrieveing information on orders from each day, and participating restaurants. There is also data for no-fly zones, which the drone cannot fly through, and a central area which the drone cannot leave once it enters it. All deliveries are made to a constant location, and the drone has a limited battery so it needs to maximise the deliveries done for that day. This is discussed in more details in the project specification.

A star patfinding
