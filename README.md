# orders-service (upgrade POC)

A small Spring Boot service. It is the baseline for the upgrade check:

- `main` (the baseline, `master` in the check): Spring Boot 3.2.5 on Java 17
- `change_branch`: Spring Boot 4.2.0 on Java 25, with JaCoCo 0.8.15 and the Spring Boot 4 test changes (Jackson 3 `ObjectMapper`, `spring-boot-starter-webmvc`, `spring-boot-starter-webmvc-test`)

## Endpoints

| Method | Path | What it does |
|---|---|---|
| POST | `/api/orders` | Create an order (`customerId`, `amountCents`, `currency` as 3 capital letters) |
| GET | `/api/orders` | List orders, sorted by id |
| GET | `/api/orders/{id}` | One order, or 404 |
| GET | `/api/orders/totals` | Total amount in cents per currency |
| GET | `/api/orders/report.csv` | All orders as CSV |
| POST | `/api/orders/{id}/cancel` | Cancel an order. **Deliberately has no test** |
| GET | `/actuator/health` | Health |

## Run the tests and the coverage check

```powershell
mvn verify
```

The build fails when line coverage is below 60%. The JaCoCo report is in `target\site\jacoco\index.html`.

## Notes for the POC

- `OrderService.cancel` has no test on purpose. The impact check should report it as untraced code.
- The repository is in memory. Tests share it, so they assert on the ids they create.
- The change_branch needs JDK 25 (Spring Boot 4.2 supports Java 17 to 26). Set `JAVA_HOME` to a JDK 25 folder before running Maven, or use the JDK path in `config\upgrade.json`. The main branch needs JDK 17.
