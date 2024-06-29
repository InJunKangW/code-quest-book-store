select distinct *
from book b
inner join product p on b.product_id = p.product_id
inner join product_tag pt on p.product_id = pt.product_id
inner join tag t on pt.tag_id = t.tag_id
inner join product_category_relation pcr on p.product_id = pcr.product_id
inner join product_category pc on pcr.product_category_id = pc.product_category_id
where t.tag_name in ('tag1', 'tag2')
and pc.category_name in ('category1', 'category2')
having count(t.tag_id) = 2;
