# Currency Converter App

This project applies discount and then uses an exchange rate from a third party api to convert the amount to the given currency.

### Technologies:
* Java
* Spring-boot
* Spring-security
* Maven
* Mockito
* JaCoCo
* Sonar

### Installation:

* Clone the repository
* Install dependencies:
```bash
mvn install
```

* Run the application:
```bash
mvn spring-boot:run
```

* Run the unit tests:
```bash
mvn test
```

* Run the jacoco report:
```bash
mvn clean verify jacoco:report
```

* Run the lint:
```bash
mvn mvn checkstyle:check
```


Application will start running on port: 8000

### API Used:
https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + source

**POST** /api/calculate: Authenticated endpoint that converts the bill amount in the given currency.
* CalculationRequestDto:
  *  items; 
  * totalAmount; 
  * originalCurrency; 
  * targetCurrency; 
  * userType; 
  * customerTenureInYears;
* Returns :
  * CalculationResponseDto
      * originalAmount;
      * discountedAmount; 
      * finalAmount; 
      * targetCurrency;: 


