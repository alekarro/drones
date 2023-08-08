## drones-api

:scroll: **START**

### Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: **the drone**. 
Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, 
the drone has the potential to leapfrog traditional transportation infrastructure.

Useful drone functions include delivery of small items that are (urgently) needed in locations with difficult access.

We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads.
Our drones carry medications only.

A **Drone** has:
- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has:
- name (allowed only letters, numbers, ‘-‘, ‘_’);
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medication case).

Our service allows:
- registering a drone;
- update a drone;
- check drone battery level for a given drone;
- loading a drone with medication items;
- checking loaded medication items for a given drone;
- checking available drones for loading;
- delivery loading
- the service audits drones every 2 minutes and stores the state of all drones in a database.

Constraints:
- A drone cannot being loaded with more weight that it can carry;
- A drone cannot being in LOADING state if the battery level is **below 25%**;


### build and run tests
Before starting the application a few tools must be installed in your OS:
1. JDK version 11 or later 
2. Maven version 3.8 or later
3. Docker + docker-compose version 4.20 or later 
-------------------

Steps to build:
1. open terminal (cmd and so on)
2. go to folder ../drones-api 
3. run **mvn clean package**

If code is in compilable state and tests passed,folder **target** will be created with drones-api-1.0.jar


### run
from the same folder ../drones-api run command
**docker-compose up --build**

Two containers will be created in Docker - 
1. application with deployed tomcat
2. postgresql database  

The api is ready for using, base URL is **https://localhost:8080**


### API
List of api services:\
_in test and dev mode domainname is localhost_

### Drone
**POST http://domainname:8080/drone** \
Creates or updates the drone and it's current state. If drone with serialNumber already exists, it will be updated.\
_request:_\
JSON with structure:\
{

    "serialNumber": unique code of drone, mandatory
    "model": model of drone, possible values are: LIGHTWEIGHT, MIDDLEWEIGHT, CRUISERWEIGHT, HEAVYWEIGHT, mandatory
    "weightLimit": max loading weight for drone, mandatory
    "batteryLevel": battery level of the drone, default 0
    "state": state of the drone, possible values are: IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING, default LOADING or IDLE(if battery level < 25%)  
}\
_Example:_\
{

    "serialNumber": "number_1",
    "model": "LIGHTWEIGHT",
    "weightLimit": 450
}

_Response:_\
{

    "result": boolean result of operation, true is success or false is fail
    "message" error message in case of fail
}

**GET http://domainname:8080/drone/battery-allowable** \
Check if the drone with the specified serial number has enough battery power level for loading.\
_request:_\
JSON with structure:\
{
    
    "serialNumber": unique code of drone, mandatory
}\
_Example:_\
{

    "serialNumber": "number_1"
}

_Response:_\
{

    "result": boolean result of operation, true is battery power level is enough or false if drone battery is low   
    "message" error message in case of fail
}

**PUT http://domainname:8080/drone/state** \
Update state of drone, like battery level, state, loaded weight\
_request:_\
JSON with structure:\
{

    "serialNumber": unique code of drone, mandatory
    "batteryLevel": battery level of the drone, default 0
    "state": state of the drone, possible values are: IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING, default LOADING or IDLE(if battery level < 25%)  
    "loadedWeight": total weight that drone has loaded, default 0

}\
_Example:_\
{

    "serialNumber": "number_1",
    "batteryLevel": 50,
    "state": "LOADED",
    "loadedWeight": 450
}

_Response:_\
{

    "result": boolean result of operation, true is battery power level is enough or false if drone battery is low  
    "message" error message in case of fail
}


### Medication

**POST http://domainname:8080/medication** \
Creates or updates the medication. If medication with code already exists, it will be updated.\
_request:_\
JSON with structure:\
{

    "code": unique code of medication, mandatory
    "name": name of medication, mandatory
    "weight": weight of medication, mandatory
    "image": bytes, image of the medication  
}\
_Example:_\
{

    "code": "MED_1",
    "name": "drug-1",
    "weight": 100,
    "image": "iVBORw0KGgoAAAANSUhEUgAAAgAAAAIACAYAAAD0eNT6AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALngAAC54BLJl9GAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAACAASURBVHic7N15mFTVmT/w73turb2xI0uzI7sCInQ3myi4xi0xuCUapVuSmMQkZmYSk5gwySRjMpmJmSSTXxRxQRMjGpcoxgVEUVYRUHZRdgFBlga6q6vuvef3R7uw9H5P9a2q+/08zzwTu6vf8+rTXfdb5557DkBERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERD4TvxtojrxR07s6Eecq0ZgIoBuAmN89EeWYhAb2KOANhKwnql+/Z7vfDRFRemRHAJg0IxRL7JgByO2AjvvdDlFApAT4c3VV8gd4e/Yxv5shIrMyPwCUTY3HUPQUNC7wuxWiINLAypCWi48tnbnX716IyBzldwONiaHoz7z4E/lHgJGO6CcwdGrE716IyJyMDgCxMRXnQeMGv/sgIoyLFhR93e8miMicjA4AYunb/e6BiGqJwveBGRn9nkFETZe5f8wXfyuqNc71uw0i+phG13jpttF+t0FEZmRsAIgerikGkOd3H0T0GS0yzO8eiMiMjA0AIehCv3sgohOJqzr53QMRmZGxAUC7dsb2RhRUWnTI7x6IyAxeZImIiAKIAYCIiCiAGACIiIgCiAGAiIgogBgAiIiIAogBgIiIKIAYAIiIiAIop5/pdUaUIjXlCr/bIMoI6v0NiPz9Qb/bIKIMwRkAIiKiAGIAICIiCiAGACIiogBiACAiIgogBgAiIqIAYgAgIiIKIAYAIiKiAGIAICIiCiAGACIiogBiACAiIgogBgAiIqIAYgAgIiIKIAYAIiKiAGIAICIiCiAGACIiogBiACAiIgogBgAiIqIAYgAgIiIKIAYAIiKiAGIAICIiCiAGACIiogBiACAiIgogBgAiIqIA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"
}

_Response:_\
{

    "result": boolean result of operation, true is success or false is fail
    "message" error message in case of fail
}


### Loading

**POST http://domainname:8080/loading** \
Loads specified drone with medications.\
_request:_\
JSON with structure:\
{

    "droneSerialNumber": drone code
    "loadingItems": [
        {
            "medicationCode": medication code
            "quantity": quantity of medication
        }
        ... more items
    ]
}\
_Example:_\
{

    "droneSerialNumber": "number_1",
    "loadingItems": [
        {
            "medicationCode": "MED_1",
            "quantity": 2
        },
        {
            "medicationCode": "MED_2",
            "quantity": 5
        }
    ]
}

_Response:_\
{

    "result": boolean result of operation, true is success or false is fail
    "message" error message in case of fail
}


**GET http://domainname:8080/loading** \
Return Drone state and its loading with medications.\
_request:_\
JSON with structure:\
{

    "code": drone code(serial number)
}\
_Example:_\
{

    "code": "number_1",
}

_Response:_\
{

    "result": {
        "serialNumber": unique code of drone
        "batteryLevel": battery level of the drone
        "state": state of the drone, possible values are: IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING 
        "loadedWeight": total weight that drone has loaded
        "weightLimit": weight limit of the drone 
        "loadedMedications" :  
        [
            {
                "code": medication code
                "loadedQuantity": quantity of loaded mediations
            }
            ... more medications
        ]
    }
    "message" error message in case of fail
}


**GET http://domainname:8080/loading/available-drones** \
Return available drones for loading medications.\
_request:_\
empty

_Response:_\
List of all available drones and their states.\
{

    "result": [
        {
            "serialNumber": unique code of drone
            "batteryLevel": battery level of the drone
            "state": state of the drone, possible values are: IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING 
            "loadedWeight": total weight that drone has loaded
            "weightLimit": weight limit of the drone 
        }
        ... more drones
    ],
    "message" error message in case of fail
}


**POST http://domainname:8080/loading/delivered** \
Delivers drone's loading to destination. Drone gets state 'DELIVERED', and loadedWeight becomes 0 \
_request:_\
JSON with structure:\
{

    "code": drone code(serial number)
}\
_Example:_\
{

    "code": "number_1",
}

_Response:_\
{

    "result": boolean result of operation, true is success or false is fail
    "message" error message in case of fail
}


**Examples:**\
in folder ../drones-api/src/test/resources there are **.rest** files with requests for testing.  


:scroll: **END**
