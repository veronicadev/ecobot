package veronicadev.ecobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;


public class EcoBot extends TelegramLongPollingBot {
    private Logger logger = LoggerFactory.getLogger(EcoBot.class);
    private static final String BOT_TOKEN = System.getProperty("BOT_TOKEN");
    private static final String botUsername = "astiecobot";

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();

            if(message.hasText()){
                String input = message.getText();
                String chatId = message.getChatId().toString();
                logger.info(input);
                switch(input){
                    case "/start":
                        performStart(chatId);
                    break;
                    case "/info":
                        performInfo(chatId);
                    break;
                    case "/tomorrow":
                        performGetAreaMenu(chatId, "tomorrow");
                    break;
                    case "/getarea":
                        performGetAreaMenu(chatId, "getarea");
                    break;
                    case "/ecocentro":
                        performEcocentro(chatId);
                    break;
                    case "/calendar":
                        performGetAreaMenu(chatId, "calendar");
                    break;
                }
            }
        }else if(update.hasCallbackQuery()){
            CallbackQuery callbackquery = update.getCallbackQuery();
            String[] data = callbackquery.getData().split(":");

            if(data[0].equals("tomorrow")){
                this.getTomorrow(data[1], callbackquery.getMessage().getChatId().toString());
            }else if(data[0].equals("getarea")){
                this.getAreaData(data[1], callbackquery.getMessage().getChatId().toString());
            }else if(data[0].equals("calendar")){
                this.getCalendar(data[1], callbackquery.getMessage().getChatId().toString());
            }else{
                try {
                    this.sendAnswerCallbackQuery("Usa uno dei comandi elencati sopra!", false, callbackquery);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Message performEcocentro(String chatId){
        StringBuilder stringBuilder = new StringBuilder();
        DataManager dataManager = DataManager.getInstance();
        String cityName = dataManager.getMunicipalityName();
        RecyclingDepot rd = dataManager.getRecyclingDepot();
        stringBuilder.append("\uD83C\uDFE1 Ecocentro di ").append(cityName).append("\uD83C\uDFE1").append("\n\n");
        stringBuilder.append("\uD83D\uDD50 Orario \uD83D\uDD50 \n").append(rd.getTime()).append("\n\n");
        stringBuilder.append("\uD83D\uDCED Indirizzo \uD83D\uDCED \n").append(rd.getAddress()).append("\n\n");
        stringBuilder.append("☎️ Telefono  ☎️ \n").append(rd.getTelephone()).append("\n\n");
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText(stringBuilder.toString());
        sendMessagerequest.enableMarkdown(true);
        Message response = null;
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Message performStart(String chatId){
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText("Ciao, sono EcoBot \uD83D\uDE9B ! Usa i comandi per chiedermi informazioni riguardo il calendario della raccolta differenziata della tua città! ♻️Usa `/info` per sapere quali comandi puoi usare");
        sendMessagerequest.enableMarkdown(true);
        Message response = null;
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Message performInfo(String chatId){
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText("Questa è la lista di comandi che puoi usare...TODO");
        sendMessagerequest.enableMarkdown(true);
        Message response = null;
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Message performGetAreaMenu(String chatId, String callbackName){
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText("Scegli l'area interessata");
        sendMessagerequest.enableMarkdown(true);
        sendMessagerequest.setReplyMarkup(this.getAreasMenu(callbackName));
        Message response = null;
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Message getAreaData(String areaName, String chatId){
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        Message response = null;
        List<Area> areasFiltered = DataManager.getInstance().getAreas().stream().filter(a -> a.getName().equals(areaName)).collect(Collectors.toList());

        if(!areasFiltered.isEmpty()){
            if(areasFiltered.get(0)!=null){
                Area area = areasFiltered.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("♻️").append(areaName).append("♻️\n\n");
                stringBuilder.append(area.getAddressedTo()).append("\n\n");
                stringBuilder.append("*CALENDARIO*\n\n");
                if(area.getWeekCalendar().size()>0){
                    for(TrashContainer t: area.getWeekCalendar()) {
                        String dayName =  DateUtils.getDayName(Integer.valueOf(t.getDay()), Locale.ITALY);
                        stringBuilder.append("\uD83D\uDDD3️").append(" *").append(dayName).append("*: \n");
                        stringBuilder.append(TrashType.valueOf(t.getType()).getName()).append("\n");
                        stringBuilder.append("_").append(t.getHoursRange()).append("_ \n\n");
                    }
                }
                sendMessagerequest.setText(stringBuilder.toString());
                sendMessagerequest.setParseMode("Markdown");
            }
        }else{
            sendMessagerequest.setText("Area non disponibile");
        }
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return response;
    }


    public Message getCalendar(String areaName, String chatId){
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        Message response = null;
        List<Area> areasFiltered = DataManager.getInstance().getAreas().stream().filter(a -> a.getName().equals(areaName)).collect(Collectors.toList());

        if(!areasFiltered.isEmpty()){
            if(areasFiltered.get(0)!=null){
                Area area = areasFiltered.get(0);
                Date today = new Date();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("♻️").append(areaName).append("♻️\n\n");
                stringBuilder.append(area.getAddressedTo()).append("\n\n");
                stringBuilder.append("*CALENDARIO PER PROSSIMI 7 GIORNI*\n\n");

                if(area.getWeekCalendar().size()>0){
                    for(int day=1; day<=7; day++){

                        int dayOfTheWeek = DateUtils.addDay(day, today).get(Calendar.DAY_OF_WEEK);
                        int dayOfMonth = DateUtils.addDay(day, today).get(Calendar.DAY_OF_MONTH);
                        String monthName = DateUtils.getMonthName(dayOfMonth, Locale.ITALY);
                        TrashContainer t = DataManager.getInstance().findContainer(String.valueOf(dayOfTheWeek), areasFiltered.get(0));

                        stringBuilder.append("\uD83D\uDDD3️").append(" *").append(dayOfMonth).append(" ").append(monthName).append("*: \n");


                        if(t==null){
                            stringBuilder.append("Nessun ritiro\n\n");
                        }else{
                            stringBuilder.append(TrashType.valueOf(t.getType()).getName()).append("\n");
                            stringBuilder.append("_").append(t.getHoursRange()).append("_ \n\n");
                        }
                    }
                }
                sendMessagerequest.setText(stringBuilder.toString());
                sendMessagerequest.setParseMode("Markdown");
            }
        }else{
            sendMessagerequest.setText("Area non disponibile");
        }
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return response;
    }


    private void getTomorrow(String areaName, String chatId){
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        List<Area> areasFiltered = DataManager.getInstance().getAreas().stream().filter(a -> a.getName().equals(areaName)).collect(Collectors.toList());
        if(!areasFiltered.isEmpty()){

            int dayOfTheWeek = DateUtils.addDay(1, new Date()).get(Calendar.DAY_OF_WEEK);
            String dayName = DateUtils.getDayName(dayOfTheWeek, Locale.ITALY);

            if(areasFiltered.get(0)!=null){
                TrashContainer t = DataManager.getInstance().findContainer(String.valueOf(dayOfTheWeek), areasFiltered.get(0));
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("♻️").append(areaName).append("♻️\n\n Domani ").append(dayName).append(": \n");
                if(t!=null){
                    stringBuilder.append(TrashType.valueOf(t.getType()).getName()).append("\n\n").append("\uD83D\uDD51 ").append(t.getHoursRange());
                }else{
                    stringBuilder.append("Nessun ritiro");
                }
                sendMessagerequest.setText(stringBuilder.toString());
            }
        }else{
            sendMessagerequest.setText("Area non disponibile");
        }
        try {
            execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) throws TelegramApiException{
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        execute(answerCallbackQuery);
    }

    private InlineKeyboardMarkup getAreasMenu(String callbackName){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (Area a: DataManager.getInstance().getAreas()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(a.getName());
            button.setCallbackData(callbackName.concat(":").concat(a.getName()));
            rowInline.add(button);
        }

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


}
