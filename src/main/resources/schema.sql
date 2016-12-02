create table if not exists currency_conversion (
  id int auto_increment,
  source_currency char(3),
  target_currency char(3),
  amount  decimal(20,7),
  rate  decimal(20,7)
);