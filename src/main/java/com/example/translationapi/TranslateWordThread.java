package com.example.translationapi;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class TranslateWordThread extends Thread {
    private static final String URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";
    private static final String TOKEN = "t1.9euelZrHl46ay5eYyc6akJvNjI7NyO3rnpWakMeLnMmdkpDNnZmYnJmVnJXl8_cNGzdK-e8-UxA__t3z901JNEr57z5TED_-zef1656VmpKNjpKempqWzZiZj5mLmYvK7_zF656VmpKNjpKempqWzZiZj5mLmYvK.8eW5RNmlJ41ozXpxA_UB8nLBUyA7gCi0y_gGwAnxVPPwQNDKWth-voOMJVHvZj66ecOiEV1UwXBkGHhdpDGJBA";
    private static final String FOLDER_ID = "b1gr8vkkgfn16r74nomp";

    private RestTemplate restTemplate = new RestTemplate();
    private Semaphore semaphore;

    private String word;
    private String from;
    private String to;
    private Answer answer;

    private TranslateWordThread preThread;

    TranslateWordThread(String name, String word, String from, String to, Answer answer, Semaphore semaphore) {
        super(name);
        this.word = word;
        this.from = from;
        this.to = to;
        this.answer = answer;
        this.semaphore = semaphore;
    }

    TranslateWordThread(String name, String word, String from, String to, Answer answer, Semaphore semaphore, TranslateWordThread preThread) {
        super(name);
        this.word = word;
        this.from = from;
        this.to = to;
        this.answer = answer;
        this.semaphore = semaphore;
        this.preThread = preThread; // будем хранить предыдущий поток, чтобы проверять, закончился ли он (чтобы слова были по порядку, а не в перемешку)
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // ТЕЛО ЗАПРОСА
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sourceLanguageCode", from);
        requestBody.put("targetLanguageCode", to);
        requestBody.put("texts", word);
        requestBody.put("folderId", FOLDER_ID);

        // ЗАГОЛОВКИ
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(TOKEN);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = null;
        try {
            response = restTemplate.postForEntity(URL, entity, Map.class); // получаем ответ от переводчика
        } catch (HttpClientErrorException.BadRequest ex) { // если код ошибки
            int exCode = ex.getStatusCode().value();
            String message = ex.getMessage().substring(4);
            answer.SetAnswer(exCode, message);
            semaphore.release();
            return;
        }
        // если ответ получен без ошибки

        int code = response.getStatusCode().value();

        String text = response.getBody().get("translations").toString(); // получаем translations - строка типа: [{text=hi}]
        String word = text.substring(7, text.length()-2);

        String trash = ", detectedLanguageCode="; // если переводчик сам определил язык, то он пишет код этого языка после каждого слова, надо это убрать
        if (word.contains(trash)) {
            int index = word.indexOf(trash);
            word = word.substring(0, index);
        }

        if (preThread != null)
        {
            try {
                preThread.join(); // ждём, пока предыдущее слово добавится в ответ
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        answer.SetAnswer(code, word);

        semaphore.release();
    }
}
