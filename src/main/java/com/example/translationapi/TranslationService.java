package com.example.translationapi;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

@Service
public class TranslationService {
    public Answer translateText(String text, String from, String to) throws InterruptedException {

        Answer answer = new Answer(); // ответ, в котором будет либо переведённая строка, либо сообщение об ошибке (объект answer является общим ресурсом для потоков)

        String[] words = text.split(" "); // делим текст на слова

        Semaphore semaphore = new Semaphore(10, true); // семафор на 10 потоков

        ArrayList<TranslateWordThread> threads = new ArrayList<TranslateWordThread>(); // пул потоков

        for (int i = 0; i < words.length; i++) // проходим по словам
        {
            TranslateWordThread thread;

            if (i == 0)
                thread = new TranslateWordThread(String.valueOf(i), words[i], from, to, answer, semaphore);
            else
                thread = new TranslateWordThread(String.valueOf(i), words[i], from, to, answer, semaphore, threads.get(threads.size()-1)); // если поток не первый, то в конструктор передаём предыдущий поток, чтобы отслеживать его завершение

            threads.add(thread); // добавляем в пул потоков
            thread.start(); // запускаем поток
        }

        for (TranslateWordThread thread:threads)
            thread.join(); // ждём завершения всех потоков

        return answer;
    }
}
