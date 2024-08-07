# Лабораторная работа по Java для Т-банка

Программа разрабатывалась в IntelliJ IDEA. Точка входа main находится в классе TranslationApiApplication (лежит в папке src/main/java/com/example/translationapi). 

Программа состоит из следующих классов:
- Answer - ответ, которые возвращает программа
- DataBaseRepository - репозиторий базы данных
- TranslateWordThread - класс потока, в котором переводится слово
- TranslationApiApplication - точка входа
- TranslationController - контроллер, где обработывается GET-запроса пользователя
- TranslationService - сервис для перевода текста

В классе TranslationController обрабатывается GET-запрос /translate с параметрами:
- text - строка для перевода
- from - исходный язык
- to - целевой язык

Пример: http://localhost:8080/translate?text=hello&from=en&to=ru

В качестве базы данных использовалась PostgreSQL. Для корректной работы программы необходимо настроить подключение к БД. Программа подключается к локальной базе данных translation_db через URL jdbc:postgresql://localhost:5432/translation_db. Имя пользователя: postgres. Пароль: aircraft. Эти данные необходимо изменить под себя. 

В базе данных находится таблица records, которая имеет следующие атрибуты:
- id - уникальный идентификатор (автоматически инкрементируется)
- ip_address - ip-адрес пользователя, сделавшего запрос
- input_text - исходная строка
- translated_text - переведённая строка (или сообщение об ошибке, если перевести не удалось)

CREATE TABLE records (
	id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	ip_address varchar NOT NULL,
	input_text varchar NOT NULL,
	translated_text varchar NULL,
	CONSTRAINT record_pk PRIMARY KEY (id)
);

Для подключения к Translate API Yandex использована моя учётная запись. В POST-запросе используются мой токен авторизации (TOKEN) и идентификатор каталога (FOLDER_ID). Возможно, что во время проверки работы мой токен уже будет не действителен, поэтому эти данные стоит тоже изменить под себя (находятся в классе TranslateWordThread).




