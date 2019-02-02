# Base Kotlin - WebFlux service

This project contains a sample service using Spring WebFlux as the web framework, Arrow to add some functional
 components and uses a reactive mongoDB driver, to make the complete flow reactive. This is a simplified version
 based on one we've started using at my last project.
 
# What's in here 

 - Use webflux to provide a reactive web framework
 - Use Arrow for handling nullable values, and to make working with Mono and Flux easier
 - MongoDB with reactive driver for storage
 - Typesafe config for central configuration handling
 - Customizable error handling
 - Simple validation of incoming parameters
 - CORS filter enabled
 - Work without annotations, and just use the kotlin DSL to define the beans. 
 
# Not in here

There are couple of things I've left out, which don't really have that much to do with the whole flux/kotlin/arrow stuff, 
 but which we needed:
 
 - Customize JSON serialization and deserialization for the HTTP messages.
 - Customize serialization and deserialization for the BSON messages.
 
Should you have questions specifically for these subjects just let me know.  
  