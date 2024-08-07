package com.example.translationapi;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("translate")
public class TranslationController {

    TranslationService translationService = new TranslationService(); // сервис для перевода текста
    DataBaseRepository dbRepository = new DataBaseRepository(); // репозиторий для сохранения записей в базу данных

    @GetMapping
    public String translate(HttpServletRequest request) throws InterruptedException {

        // сохраняем параметры запроса
        String text = request.getParameter("text"); // текст для перевода
        String from = request.getParameter("from"); // исходный язык
        String to = request.getParameter("to"); // целевой язык

        // сохраняем IP-адрес отправителя запроса
        String ip = request.getRemoteAddr();

        Answer answer = translationService.translateText(text, from, to); // вызываем сервис для перевода текста, получаем ответ
        dbRepository.saveRecord(ip, text, answer.GetMessage()); // сохраняем запись в БД

        return answer.toString();
    }
}
