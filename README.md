# Ticker List 
This application provides Organization Details. Refresh operation refreshes the details. Data returned includes following fields
 - Company Name
 - Form Name
 - CIK
 - Filing Date
 - URL to filing document
 
 Information returned by the application can be viewed using single page application (SPA) or REST API. Below screenshot shows content displayed in SPA
 ![SinglePageApplicationScreenshot](/src/main/resources/miscellaneous/spa_image.png)
 
 REST api sample request and response is shown below:
 Sample Request:
 ```
 curl --request GET \
  --url 'http://localhost:8443/sec?cik=0000789019&formname=10-Q&startfilingdate=2019-01-01&endfilingdate=2020-12-01&offset=0&length=25' \
  --header 'accept: application/json'
 ```

 Sample Response:
 ```
 [
   {
     "organizationName": "MICROSOFT CORP",
     "formName": "10-Q",
     "cik": "0000789019",
     "filingDate": "2020-10-27",
     "secUrl": "https://www.sec.gov/Archives/edgar/data/789019/0001564590-20-047996.txt"
   },
   {
     "organizationName": "MICROSOFT CORP",
     "formName": "10-Q",
     "cik": "0000789019",
     "filingDate": "2020-04-29",
     "secUrl": "https://www.sec.gov/Archives/edgar/data/789019/0001564590-20-019706.txt"
   },
   {
     "organizationName": "MICROSOFT CORP",
     "formName": "10-Q",
     "cik": "0000789019",
     "filingDate": "2020-01-29",
     "secUrl": "https://www.sec.gov/Archives/edgar/data/789019/0001564590-20-002450.txt"
   },
   {
     "organizationName": "MICROSOFT CORP",
     "formName": "10-Q",
     "cik": "0000789019",
     "filingDate": "2019-10-23",
     "secUrl": "https://www.sec.gov/Archives/edgar/data/789019/0001564590-19-037549.txt"
   },
   {
     "organizationName": "MICROSOFT CORP",
     "formName": "10-Q",
     "cik": "0000789019",
     "filingDate": "2019-04-24",
     "secUrl": "https://www.sec.gov/Archives/edgar/data/789019/0001564590-19-012709.txt"
   },
   {
     "organizationName": "MICROSOFT CORP",
     "formName": "10-Q",
     "cik": "0000789019",
     "filingDate": "2019-01-30",
     "secUrl": "https://www.sec.gov/Archives/edgar/data/789019/0001564590-19-001392.txt"
   }
 ]
 ```

 Swagger Document: {base-url}/swagger-ui.html

## Installation
This a Spring Boot project and has Maven Structure. To start the application, maven commands such as `mvn -U clean install` can be used. Prerequisites:
 - Java 8+
 - Maven
 - PostgreSQL
 
## License
[GNU General Public License](https://www.gnu.org/licenses/#GPL)