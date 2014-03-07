**Team Name**: The Blank Slate (blankslate)

**Team Members**

Name | CS ID
---- | -----
Dhananjay Bhaskar | w7d7
Alex Tan | h5m8
Scott Mastromatteo | i7z7
Ben Liang | w0y7

**Project Title**: Healthy BC - Walk-In Clinics in our province

**Project Description**

This application leverages open data on walk-in clinics and health programs around BC provided by the province on their website: http://www.data.gov.bc.ca/ 
It is designed to provide support to patients that do not have family physicians or who need medical treatment and/or diagnosis at times when their family physician is not available.

After authentication, the users of this application can browse and query important information including name, location, hours, address and contact information for each walk-in clinic. Clinic locations will be plotted on a map to allow easy visualization of location data. In addition to providing a wealth of information the application will feature social integration with Facebook and Twitter, allowing users to review their experience and provide feedback for others (by clicking on ‘Like’ or posting a comment).

**Project Contents**:

1. data/walkinclinics.csv : This CSV file (containing the following fields separated by semicolons) is generated from the dataset provided by DataBC. This file will be parsed by our application to store data persistently in the Google App Engine.
  * NAME 
  * REFERENCE (unique identifier string)
  * PHONE
  * WEBSITE
  * EMAIL_ADDRESS
  * WC_ACCESS (wheelchair access - boolean value)
  * LANGUAGE (comma separated)
  * STREET_NO
  * STREET_NAME
  * STREET_TYPE
  * CITY
  * POSTAL_CODE
  * LATITUDE
  * LONGITUDE
  * DESCRIPTION
  * HOURS
2. src/ : Contains the JAVA source files
