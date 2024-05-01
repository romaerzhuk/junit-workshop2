## JUnit workshop 2

1. Пирамида тестирования
    * [Хабр > Всё, что вы хотели знать про пирамиду тестирования, но не знали как спросить](https://habr.com/ru/articles/788212/)
    * [Хекслет > Пирамида тестирования](https://ru.hexlet.io/courses/testing-phase/lessons/testing-pyramid/theory_unit)
    * [JavaRush > Java Unit Testing: методики, понятия, практика](https://javarush.com/groups/posts/2500-vse-o-unit-testing-metodiki-ponjatija-praktika)
    * [QA_Bible > Пирамида / уровни тестирования](https://vladislaveremeev.gitbook.io/qa_bible/vidy-metody-urovni-testirovaniya/piramida-urovni-testirovaniya-test-pyramid-testing-levels)
    * [Test Engineer > Пирамида тестирования](https://testengineer.ru/testing-pyramid/)
2. Методы тестирования:
    * [QA_Bible > Методы тестирования (White/Black/Grey Box)](https://vladislaveremeev.gitbook.io/qa_bible/vidy-metody-urovni-testirovaniya/metody-testirovaniya-white-black-grey-box)
    * [Test Engineer > Тестирование белого ящика vs тестирование черного ящика](https://testengineer.ru/testirovanie-belogo-chernogo-yashchika/)
3. Unit-тесты:
    * быстрый запуск и устранение ошибок;
    * уверенность: проверка всех деталей алгоритмов;
    * рефакторинг: удаление избыточного и грязного кода.
4. Фреймворки для Unit-тестирования:
    1. Базовые фреймворки Unit-тестирования:
        * JUnit;
        * TestNG.
    2. AssertJ;
    3. Hamcrest;
    4. SpringBootTest.
5. Mockito, лёгкое тестирование на моках:
    * Mockito;
    * PowerMock;
    * Easymock;
    * JMockit.
6. Собственный набор утилит: test-util
    * `UidExtension`: `uid()`, `uidL()`, `uidS()`, `uidDec()`, `newLocalDate()`,... — уникальные значения для тестов.
    * `PropertiesMatcher`, сравнивает JavaBean с ожидаемыми значениями по его свойствам. Совместим с Hamcrest и AssertJ.
    * `MethodSourceHelper`, предоставляет вспомогательные методы для параметризации.

### Пример модульного тестирования SpringBoot-приложения.

См. репозиторий Git https://github.com/romaerzhuk/junit-workshop2

1. Пустой каталог.
2. Импортировано SpringBoot-приложение junit-workshop2.
3. Добавлен docker-compose. `APPLICATION FAILED TO START`.
   DataSource не настроен: `DataSourceBeanCreationException: Failed to determine a suitable driver class`.
4. `JunitWorkshop2ApplicationTests` успешно проверяет запуск контекста.
5. `JunitWorkshop2ApplicationTests` переименован в `JunitWorkshop2ApplicationIntegrationTest`.
   Суффикс `Test` для лёгких Unit-тестов, `IntegrationTest` — для тяжёлых, интеграционных.
   Тяжёлое должно и выглядеть тяжелее лёгкого.
6. `EmployeeControllerIntegrationTest` падает: 400 No static resource values. Контекст поднимается дважды. БД тоже недоступна.
7. `EmployeeControllerIntegrationTest` успешно отработал, но контекст всё ещё запускается дважды, и в логах БД всё ещё падает.
8. `@IntegrationTest` — единый контекст на все интегротесты.
9. Ненадёжный тест проверки запроса данных из `EmployeeService`.
10. `EmployeeControllerIntegrationTest` надёжнее проверяет, что ответ получается из `EmployeeService.find`, а не константа.
    Использован `UidExtension`. Тест упал.
11. Подправлен `EmployeeController`. Тест работает. Контекст поднимается дважды.
12. `@SpyBean` вынесен в единую конфигурацию тестов: `TestConfiguration`.
13. Добавлен падающий `EmployeeServiceTest`, без реализации: `throw new UnsupportedOperationException()`.
14. `EmployeeServiceTest.find` заработал. Подклюена Guava, `@VisibleForTesting`. Тест `mapToDto` падает: нет реализации.
15. `EmployeeServiceTest.mapToDto` падает, чтоб показать, как ведёт себя `PropertiesMatcher` при падении.
16. `EmployeeServiceTest` заработал.
17. `EmployeeSpecificationsTest.getByFilter` тестирует на моках спецификацию поиска сотрудника по фильтру.
    Добавлены отдельные методы `@VisibleForTesting`: `getById`, `getByName` и падающие тесты-пустышки в `EmployeeSpecificationIntegrationTest`.
18. `EmployeeSpecificationsIntegrationTest` добавлены тесты: `getById`, `getByName` падают. Не подхвачен spring test profile.
19. Добавлен `@ActiveProfiles("test")`. `EmployeeSpecificationsIntegrationTest` всё ещё падает: `employee.id=null`.
20. Добавлен `employee_id_seq` с шагом 100. `EmployeeSpecificationsIntegrationTest` всё ещё падает: нет реализации.
21. `EmployeeSpecificationsIntegrationTest` частично заработал.
    На `null` ищет всё, как и задумано, а не возвращает пустой список, как в тесте. Опечатка!
22. `EmployeeSpecificationsIntegrationTest` заработал. Исправлена опечатка.
23. Усложнён фильтр: `Set ids`, вместо одного значения.
    В реализацию изменяемых методов встроены `throw new UnsupportedOperationException()`, что б не забыть вернуться к ним.
    `EmployeeSpecificationsIntegrationTest` падает: нет реализации.
24. `EmployeeSpecificationsIntegrationTest.getByIdIn` упал из-за беспорядочной выборки из БД.
25. `EmployeeSpecificationsIntegrationTest.getByIdIn` исправлен.
    Нужно было использовать `containsExactlyInAnyOrderElementsOf` вместо `containsExactlyElementsOf`.
26. `EmployeeSpecificationsIntegrationTest` усложнил `getByName`: теперь возвращается минимум две записи.
    Чтоб не падал из-за нарушения порядка сортировки используется `containsExactlyInAnyOrderElementsOf`.
27. `EmployeeSpecifications.getByFilter` исправлена реализация. Тест `EmployeeSpecificationsTest.getByFilter` заработал.
28. В `EmployeeFilter` добавлена пара полей: `minStartDate`, `maxStartDate`.
    Требуется усложнить поиск по спецификации. Добавлены методы `EmployeeSpecifications.getByMinStartDate`, `getByMaxStartDate` без реализации.
    Доработан тест `EmployeeSpecificationsTest.getByFilter`: падает, нет реализации.
    Добавлены заглушки тестов `EmployeeSpecificationsIntegrationTest.getByMinStartDate`, `getByMaxStartDate`.
29. `EmployeeSpecificationsTest.getByFilter` успешно выполняется: доработана реализация. Уже громоздко.
30. Добавлен `SpecificationHelper` без реализации. `SpecificationHelperIntegrationTest` падает.
31. `SpecificationHelperIntegrationTest` успешно отрабатывает. `EmployeeSpecificationsTest` использует `SpecificationHelper` без изменения реализации.
    Тест `getByFilter` падает. Удалось упростить тест: сэкономлены два and-а.
32. `EmployeeSpecificationsTest.getByFilter` исправлен.
33. `SpecificationHelperIntegrationTest implements MethodSourceHelper`: позволяет повторно использовать готовые методы для параметризации тестов.
34. `EmployeeSpecificationsIntegrationTest` доработаны тесты `getByMinStartDate`, `getByMaxStartDate`. Тесты падают: нет реализации.
35. `EmployeeSpecifications` добавлена реализация `getByMinStartDate`, `getByMaxStartDate`.
36. `SpecificationHelper`: `varargs` вместо `Iterable`. Mockito мокает и `final` методы.