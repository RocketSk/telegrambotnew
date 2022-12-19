package com.bot;

import com.bot.service.impl.CourseServiceImpl;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelegramBotApplication extends TelegramBot {

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @lombok.Builder
    public TelegramBotApplication(String botToken) {
        super(botToken);
    }

    public void run() {
        this.setUpdatesListener(updates -> {
            updates.stream()
                    .<Runnable>map(update -> () -> process(update))
                    .forEach(executorService::submit);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        if (message != null) {
            String text = message.text();
            Optional.ofNullable(text).ifPresent(commandName -> this.serveCommand(commandName, message.chat().id()));
        }
    }

    private void serveCommand(String commandName, long chatId) {
        switch (commandName) {
            case "/start": {
                SendMessage resp = new SendMessage(chatId, "Добро пожаловать")
                        .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                                {"Курсы", "Отзывы"},
                                {"Поддержка"}
                        }).resizeKeyboard(true));
                this.execute(resp);
                break;
            }
            case "/help": {
                SendMessage resp = new SendMessage(chatId, "Вы активировали команду /help");
                this.execute(resp);
                break;
            }
            case "/menu": {

            }
            case "Курсы": {
                CourseServiceImpl courseService = new CourseServiceImpl();
                courseService.getAllCourses().stream()
                        .map(phone -> new SendPhoto(chatId, phone.getPhoto_url())
                                .caption(String.format("%s - %s", phone.getTitle(), phone.getDescription())))
                        .forEach(this::execute);
                break;
            }
            default: {
                SendMessage resp = new SendMessage(chatId, "Команда не найдена");
                this.execute(resp);
                break;
            }
        }
    }
}
