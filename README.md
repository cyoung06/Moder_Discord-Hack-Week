# MODER
A bot for moderation, made for discord hack week

# To Run

1. make config.properties file
2. put following in that file
```
token=${Your Bot Token}
```

ex)
```
token=ADJ@.d@JFAJKWE@J
```

3. make hikari.properties file
4. put following in that file

```
jdbcUrl=jdbc:mysql://${DB_IP}:${DB_PORT}/${DB_SCHEMA}?useSSL=false&serverTimezone=${TIME_ZONE}&CharacterEncoding=${ENCODING}&useUnicode=true
driverClassName=com.mysql.jdbc.Driver
dataSource.user=${Username}
dataSource.password=${Password}
dataSource.databaseName=${DB_SCHEMA}
dataSource.cachePrepStmts=true
dataSource.prepStmtCacheSize=250
dataSource.prepStmtCacheSqlLimit=2048
```

5. Run the bot!
