-- TODO: insert users (1 admin, 1 user)
-- TODO: add `user_id` to activities

ALTER SEQUENCE activities_id_seq RESTART WITH 100;

INSERT INTO activities (id, title, note, paid, price, category, this_type, handled_at, created_at, updated_at) VALUES
  (nextval('activities_id_seq'), 'Pizza', NULL, true, 25.0, 'food', 'EXPENSE', now(), now(), now()),
  (nextval('activities_id_seq'), 'Hamburger', NULL, true, 10.0, 'food', 'EXPENSE', now(), now(), now()),
  (nextval('activities_id_seq'), 'iPhone', NULL, true, 799.0, 'shopping', 'EXPENSE', now() - INTERVAL '1 DAY', now(), now()),
  (nextval('activities_id_seq'), 'Mouse', NULL, true, 99.0, 'shopping', 'EXPENSE', now() - INTERVAL '10 HOUR', now(), now()),
  (nextval('activities_id_seq'), 'Netflix', 'Monthly subscription', true, 17.0, 'streaming', 'EXPENSE', now() - INTERVAL '2 DAY', now(), now()),
  (nextval('activities_id_seq'), 'Gasoline', NULL, false, 120.0, 'transportation', 'EXPENSE', now() + INTERVAL '2 DAY', now(), now()),
  (nextval('activities_id_seq'), '2 Tyres', 'Replace front tyres', false, 520.9, 'transportation', 'EXPENSE', now() + INTERVAL '1 WEEK', now(), now()),
  (nextval('activities_id_seq'), 'Salary', NULL, true, 3500.0, 'salary', 'INCOME', now() - INTERVAL '1 WEEK', now(), now()),
  (nextval('activities_id_seq'), 'Gift card', NULL, true, 25.0, 'gift', 'INCOME', now(), now(), now()),
  (nextval('activities_id_seq'), '3 Pizzas', 'Eating out with family', false, 75.0, 'food', 'EXPENSE', now() - INTERVAL '2 WEEK', now(), now());
