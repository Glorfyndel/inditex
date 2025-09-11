Feature: Consult product price

  Scenario Outline: Consult price for product '1 - 35455'
    Given the application is running
    When I request the price for brand <brandId>, product <productId> at "<applicationDate>"
    Then the response should be:
      | brandId   | productId   | priceList   | startDate   | endDate   | price   | currency   |
      | <brandId> | <productId> | <priceList> | <startDate> | <endDate> | <price> | <currency> |

    Examples:
      | brandId | productId | applicationDate     | priceList | startDate           | endDate             | price  | currency |
      | 1       | 35455     | 2020-06-14T10:00:00 | 1         | 2020-06-14T00:00:00 | 2020-12-31T23:59:59 | 35.50  | EUR      |
      | 1       | 35455     | 2020-06-14T16:00:00 | 2         | 2020-06-14T15:00:00 | 2020-06-14T18:30:00 | 25.45  | EUR      |
      | 1       | 35455     | 2020-06-14T21:00:00 | 1         | 2020-06-14T00:00:00 | 2020-12-31T23:59:59 | 35.50  | EUR      |
      | 1       | 35455     | 2020-06-15T10:00:00 | 3         | 2020-06-15T00:00:00 | 2020-06-15T11:00:00 | 30.50  | EUR      |
      | 1       | 35455     | 2020-06-16T21:00:00 | 4         | 2020-06-15T16:00:00 | 2020-12-31T23:59:59 | 38.95  | EUR      |