package lk.poools.top.springbot.test;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
public class StringDateFormats {

    final String txt;
    Calendar calendar;

    public StringDateFormats(String txt) throws ParseException {
        this.txt = txt;
        extractDate();
        log.info("New String Format Created");
    }

    //Добавляет дату из текста, иначе дефолтную
    private void extractDate() throws ParseException {
        Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{2}");
        Matcher dateMatcher = datePattern.matcher(txt);
        this.calendar = new GregorianCalendar();
        if (dateMatcher.find()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            this.calendar.setTime(dateFormat.parse(dateMatcher.group()));
        }
        log.info("extractDate - Done, Date is " + calendar);
    }

    //Проверяет наличие ключевых слов
    public boolean isContaining() {
        return this.txt.contains("Доходность BTC");
    }

    //Форматирования в удобный вид
    public String getResult() {
        StringBuilder stringBuilder = new StringBuilder();
        String[] result = txt.trim().replaceAll("\n", "").split(" ");

        Pattern poolNamePattern = Pattern.compile("^[A-Za-z0-9]+");

        for (int i = 0; i < result.length; i++) {
            if (result[i].contains("[") && i + 2 < result.length && result[i + 1].equals("-")) {
                Matcher matcher = poolNamePattern.matcher(result[i - 1]);
                if (matcher.find()) {
                    if (!result[i + 2].isEmpty()) {
                        stringBuilder.append(result[i - 1])
                                .append(" ");
                        stringBuilder.append(result[i])
                                .append(" ")
                                .append(result[i + 1])
                                .append(" ")
                                .append(result[i + 2], 0, result[i + 2].length() - 2);
                    }
                    if (i + 2 < result.length - 1) {
                        stringBuilder.append(", ");
                    }
                }
            }
        }
        log.info("New data: " + stringBuilder);
        log.info("Date is: " + calendar);
        return stringBuilder.toString();
    }
}
