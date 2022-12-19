package com.bot;

public class Main {

    private static final String BOT_TOKEN = "5400052634:AAGdS_xgsLzQXTET7s5WtKX2tTJgahqmYgc";

    public static void main(String[] args) {

        TelegramBotApplication bot = TelegramBotApplication.builder().botToken(BOT_TOKEN).build();
        bot.run();
    }
}
