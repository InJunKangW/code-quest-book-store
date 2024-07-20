INSERT INTO product (product_id, product_name, product_description, product_view_count, product_state, product_inventory)
VALUES (1, 'Test Product1', 'Description of test product1', 10, 0, 10);
INSERT INTO book (book_id, book_title, book_publisher, book_author, book_isbn_10, book_isbn_13, book_pubdate, product_id)
VALUES (1, 'New Test Book1', 'Test Publisher1', 'Test Author1', '1234567890', '0987654321', '2024-07-20', 1);

INSERT INTO product (product_id, product_name, product_description, product_state, product_inventory)
VALUES (2, 'Test Product2', 'Description of test product2', 1, 20);
INSERT INTO book (book_id, book_title, book_publisher, book_author, book_isbn_10, book_isbn_13, book_pubdate, product_id)
VALUES (2, 'Test Book2', 'Test Publisher2', 'Test Author2', '1234567891', '0987654322', '2024-07-20', 2);


INSERT INTO product (product_id, product_name, product_description, product_state, product_inventory)
VALUES (3, 'Test Product3', 'Description of test product3', 0, 30);
INSERT INTO book (book_id, book_title, book_publisher, book_author, book_isbn_10, book_isbn_13, book_pubdate, product_id)
VALUES (3, 'New Test Book3', 'Test Publisher3', 'Test Author3', '1234567892', '0987654323', '2024-07-20', 3);

INSERT INTO product (product_id, product_name, product_description, product_state, product_inventory)
VALUES (4, 'Test Product4', 'Description of test product4', 0, 40);
INSERT INTO book (book_id, book_title, book_publisher, book_author, book_isbn_10, book_isbn_13, book_pubdate, product_id)
VALUES (4, 'Test Book4', 'Test Publisher4', 'Test Author4', '1234567893', '0987654324', '2024-07-20', 4);

INSERT INTO product (product_id, product_name, product_description, product_state, product_inventory)
VALUES (5, 'Test Product5', 'Description of test product5', 1, 0);
INSERT INTO book (book_id, book_title, book_publisher, book_author, book_isbn_10, book_isbn_13, book_pubdate, product_id)
VALUES (5, 'New Test Book5', 'Test Publisher5', 'Test Author5', '1234567894', '0987654325', '2024-07-20', 5);

INSERT INTO product (product_id, product_name, product_description, product_state, product_inventory)
VALUES (6, 'Test Product6', 'Description of test product6', 0, 0);
INSERT INTO book (book_id, book_title, book_publisher, book_author, book_isbn_10, book_isbn_13, book_pubdate, product_id)
VALUES (6, 'Test Book6', 'Test Publisher6', 'Test Author6', '1234567895', '0987654326', '2024-07-20', 6);

insert into tag (tag_id, tag_name) values (1, 'test tag1');
insert into tag (tag_id, tag_name) values (2, 'test tag2');

insert into product_category (product_category_id, category_name) values (1,'test category1');
insert into product_category (product_category_id, category_name) values (2,'test category2');

insert into product_tag (product_id, tag_id) values (1, 1);
insert into product_tag (product_id, tag_id) values (1, 2);
insert into product_tag (product_id, tag_id) values (2, 1);
insert into product_tag (product_id, tag_id) values (3, 1);
insert into product_tag (product_id, tag_id) values (4, 1);
insert into product_tag (product_id, tag_id) values (5, 1);
insert into product_tag (product_id, tag_id) values (6, 2);

insert into product_category_relation (product_id, product_category_id) values (1, 1);
insert into product_category_relation (product_id, product_category_id) values (1, 2);
insert into product_category_relation (product_id, product_category_id) values (3, 1);
insert into product_category_relation (product_id, product_category_id) values (5, 1);


INSERT INTO product_like (client_id, product_id) VALUES (1,1);
INSERT INTO product_like (client_id, product_id) VALUES (1,3);
INSERT INTO product_like (client_id, product_id) VALUES (1,5);

INSERT INTO cart_remove_type (cart_remove_type_name)
VALUES ('직접_삭제');
INSERT INTO cart_remove_type (cart_remove_type_name)
VALUES ('구매');