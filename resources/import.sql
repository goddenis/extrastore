INSERT INTO Company VALUES(1, 'Extra')
INSERT INTO Company VALUES(2, 'SHSD')
INSERT INTO Company VALUES(3, 'Client Company')



INSERT INTO Store VALUES (1, 'sozvezdie-store.ru', 'Sozvezdie Delivery', null, 1)
INSERT INTO Store VALUES (2, 'avtoraketa2.ru', 'AvtoRaketa', null, 1)
INSERT INTO Store VALUES (3, 'firstshop.ru', 'First Shop', 'http://extrapost.ru/templates/firstshop', 1)
INSERT INTO Store VALUES (5, 'shsd-books.ru', 'SHSD Books', null, 2)
INSERT INTO Store VALUES (6, 'nextshop.ru', 'Next Shop', null, 3)


INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (1, 'wifishirt', 'Wi-Fi T-Shirt', 1500, 'http://extrapost.ru/img/product1.jpg')
INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (2, 'microfibrus', 'Microfibrus', 200, 'http://extrapost.ru/img/product2.jpg')
INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (3, 'dispenser', 'Scotch dispenser', 500, 'http://extrapost.ru/img/product3.jpg')
INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (4, 'magnets', 'Refrigerator magnets', 250, 'http://extrapost.ru/img/product4.jpg')
INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (5, 'chocolate', 'Chocolate', 200, 'http://extrapost.ru/img/product5.jpg')
INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (6, 'leonardo', 'Leonardo da Vinchi', 1500, 'http://extrapost.ru/img/product6.jpg')
INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (7, 'mama', 'Mama Hurries Home', 250, 'http://extrapost.ru/img/product7.jpg')
INSERT INTO Product (id, urlAlias, name, price, urlImageSmall) VALUES (8, 'keystopper', 'The Key Stopper', 400, 'http://extrapost.ru/img/product8.jpg')


INSERT INTO Product2Store(productId, storeId) VALUES(1, 6)
INSERT INTO Product2Store(productId, storeId) VALUES(2, 6)
INSERT INTO Product2Store(productId, storeId) VALUES(3, 6)
INSERT INTO Product2Store(productId, storeId) VALUES(4, 6)
INSERT INTO Product2Store(productId, storeId) VALUES(5, 6)
INSERT INTO Product2Store(productId, storeId) VALUES(6, 6)
INSERT INTO Product2Store(productId, storeId) VALUES(7, 6)
INSERT INTO Product2Store(productId, storeId) VALUES(8, 6)

INSERT INTO Product2Store(productId, storeId) VALUES(2, 3)
INSERT INTO Product2Store(productId, storeId) VALUES(2, 5)
INSERT INTO Product2Store(productId, storeId) VALUES(6, 5)
INSERT INTO Product2Store(productId, storeId) VALUES(7, 5)

