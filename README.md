# checkout-test
This application is a basic checkout application.

It has a product list and supports an add item function along with a calculate total.

Before processing 'start' must be called, which loads a default product list.

The product list can be overwritten with a new list of items

Once start is called items (from the product list) can be added, and the total of all items in the basket can be calculated. Items are added with a quantity. If items are added that are already in the basket, the quantity is simply summed.

At the end, finish() must be called and the checkout resets.
