# distributed-systems

## Задание 1

### Запуск

```bash
gradlew run --args="-f <path_to_RU-NVS.osm.bz2>"
```

### Вывод

```
katpatuka 17930
rockcreek 14327
yohanson 10908
Miroff 4637
siberiano 4481
vanomel 3189
Upliner 2934
...
```

## Задание 2

### Запуск

```bash
gradlew run --args="-f <path_to_RU-NVS.osm.bz2> -j -l 100000"
```
Опция `-l` или `--limit` определяет количество обрабатываемых записей. Для корретного функционирования необходимо также иметь PostgreSQL-базу `distributed`.

### Вывод

```
SQL: 8385 records/second
Prepared statement: 8103 records/second
Batch: 36954 records/second
```