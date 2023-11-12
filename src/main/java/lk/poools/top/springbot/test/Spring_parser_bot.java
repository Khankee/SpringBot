package lk.poools.top.springbot.test;

import lk.poools.top.springbot.config.BotConfig;
import lk.poools.top.springbot.services.PoolService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.ParseException;
import java.util.Calendar;

@Slf4j
@Component
@AllArgsConstructor
public class Spring_parser_bot extends TelegramLongPollingBot {


    final BotConfig botConfig;
    PoolService service;

    @Override
    public void onUpdateReceived(Update update) {
        //Чтобы боту лично не писали и не выдавал ошибку
        if (update.getChannelPost() == null) return;
        if (update.getChannelPost().getText() == null) return;

        //Объект хранит текст весь
        StringDateFormats format;
        try {
            format = new StringDateFormats(update.getChannelPost().getText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Если в публикации нет слов "Доходность BTC", дальше не продолжает
        if (!format.isContaining()) {
            log.info("Эта публикация не содержит информацию о доходности BTC");
            return;
        }

        String data = format.getResult();
        Calendar calendar = format.getCalendar();

        if (!service.isDayBusy(calendar)) {
            service.createAndSavePool(data, calendar);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
