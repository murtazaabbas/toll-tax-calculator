# Toll Tax calculator
This application will calculate the toll tax according to the daily, hourly rules and vehicle types.
## Rules
* The daily fee limit is 60 SEK.
* Toll taxes are waived on holidays and weekends.
* When there are multiple dates and times, a maximum of 60 SEK will apply to each date.
* The tax rules for multiple cities are supported.
* A vehicle can only be charged once per hour.
* If there are multiple fees within the same hour, the highest fee is applied.
* Certain vehicle types are exempt from fees.

## Project Specs
* Java 17
* Maven
* Junit
* Springboot 3

## Postman API
Post API will be exposed over the below URL on application run. please change the base url as per need.
````http://localhost:8080/v1/toll-tax/calculate-fee````

**Post Request:**
Below-mentioned request is add for the sample and will be changed as per requirement.
````
{
    "city": "gothenburg",
    "vehicle": "car",
    "tollDateTime": [
        "2013-02-07 06:23:27",
        "2013-02-07 06:35:27",
        "2013-02-07 06:50:27",
        "2013-02-07 07:10:27",
        "2013-02-07 07:23:00",
        "2013-02-08 07:10:27"
    ]
}
````

**Failure Response:**

````
{
    "status": "error",
    "message": "Vehicle in parameter is invalid"
}
````
"message" text will vary depends on the request body

**Success Response:**

````
{
    "fee": 36
}
````

## Testcase
Junit is being used for Service level testing.

## What can be more optimized!
* ControllerAdvice for better exception handling and error handling
* API request data validation. Like we can use Jarkarta validations in the request model
* Using H2 DB OR config server for better handling of application configurations
* For logging we can add Slf4j
* We can protect our API by using any common token base authentication
* API response that can contains date wise total fee. Didn't add as it was not requirement for now
