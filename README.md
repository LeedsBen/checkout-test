# checkout-test
This application is a basic checkout application.

It has a product list and supports an add item function along with a calculate total.

Before processing 'start' must be called, which loads a default product list.

The product list can be overwritten with a new list of items

Once start is called items (from the product list) can be added, and the total of all items in the basket can be calculated. Items are added with a quantity. If items are added that are already in the basket, the quantity is simply summed.

At the end, finish() must be called and the checkout resets.

Start the application by running mvn clean install and then java -jar target/checkout-test-1.0-SNAPSHOT.jar

The application is then available at localhost:8080/checkout/start

See JavaDoc for more detail.
