package com.roma.apitelegramstore.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.stream.Collectors;

@Service
public class TelegramNotificationService {

    private final RestClient restClient;

    public TelegramNotificationService(RestClient restClient) {
        this.restClient = restClient;
    }

    // Спринг автоматически вытащит токен и чат-id из application.properties с помощью аннотации @Value!
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.chat-id}")
    private String chatId;



    // Метод, который принимает готовый заказ и отправляет красивый текст в Telegram
    public void sendOrderNotification(Order order) {

        // 1. Твой сборщик текста сообщения остается без изменений
        String itemsText = order.getItems().stream()
                .map(item -> "• " + item.getProduct().getTitle() + " — " + item.getQuantity() + " шт.")
                .collect(Collectors.joining("\n"));

        String messageText = String.format(
                "🎉 **Новый заказ в магазине!**\n\n" +
                        "👤 Покупатель: %s\n" +
                        "🆔 Номер заказа: #%d\n\n" +
                        "🛒 Корзина покупок:\n%s",
                order.getCustomerName(), order.getId(), itemsText
        );

        // 2. БЕЗОПАСНАЯ СБОРКА URL через фабрику Спринга (без плюсиков!)
        // Мы жестко фиксируем домен, а токен передаем как безопасный отрезок пути
        // Идеальная сборка URL через UriComponentsBuilder для твоей версии Spring
        java.net.URI uri = org.springframework.web.util.UriComponentsBuilder
                .fromUriString("https://api.telegram.org") // ЗАМЕНИЛИ НА fromUriString!
                .path("/bot{token}/sendMessage")
                .buildAndExpand(botToken.trim())
                .toUri();


        // 3. Отправляем POST-запрос на сервера Телеграма
        restClient.post()
                .uri(uri) // Передаем готовый безопасный объект URI вместо строки
                .body(new TelegramMessagePayload(chatId.trim(), messageText))
                .retrieve()
                .toBodilessEntity();
    }

    // Маленький внутренний DTO-класс, который требует Telegram API (он принимает только json с chat_id и text)
    private record TelegramMessagePayload(String chat_id, String text) {}
}
