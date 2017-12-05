# lift-emulation

Author: Sergey Kosarev  

Summary: Lift emulation

Source:  https://github.com/macrergate/lift-emulation

## What is it?

Lift emualtion:)

## System Requirements

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.x.

## Build the App

Type this command to build the archive:

        mvn clean package


## Launch the App
Type this command to run the application:

        java -jar target/lift-emulation.jar [arguments]

### arguments
The following numbers delimited by space(s)
1. integer - count of the floors in the building (min:5, max: 20, default: 20) 
2. floorHeight - double - height of the each floor in meters (default: 3)
3. speed - double - speed of the lift in meters per second (defult: 1)
4. doorsTime double - time in seconds between openeing and closing doors (default: 5)   

Example:
    
        java -jar target/lift-emulation.jar 10 2.5
    
    
in this example 3rd and 4rd arguments will have default values.    


## Interacting with the Application
You can type following commands in the application console
- i\<FLOORNUMBER> - push button to the specified floor inside the lift, i.e i5 
- d\<FLOORNUMBER> - push down button on the specified floor outside the lift, i.e o19
- u\<FLOORNUMBER> - push up button on the specified floor outside the lift, i.e o19
- q - exit 

