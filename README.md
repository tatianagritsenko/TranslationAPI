## TranslationAPI
#Лабораторная работа по Java для Т-банка

Файлы с кодом программы лежат в папке src/main/java/com/example/translationapi

Программа разрабатывалась в IntelliJ IDEA.Точка входа main находится в классе TranslationApiApplication. 

В классе TranslationController обрабатывается GET-запрос /translate с параметрами:
- text - строка, которую надо перевести
- from - исходный язык
- to - целевой язык

Пример: http://localhost:8080/translate?text=hello world&from=en&to=ru

