package veronicadev.ecobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import veronicadev.ecobot.models.Area;
import veronicadev.ecobot.models.RecyclingDepot;
import veronicadev.ecobot.models.TrashContainer;
import veronicadev.ecobot.utils.DataManager;
import veronicadev.ecobot.utils.DateUtils;
import veronicadev.ecobot.utils.Messages;
import veronicadev.ecobot.utils.TrashType;

import java.util.*;
import java.util.stream.Collectors;


public class EcoBot extends TelegramLongPollingBot {
    private Logger logger = LoggerFactory.getLogger(EcoBot.class);
    private static final String BOT_TOKEN = System.getProperty("BOT_TOKEN");
    private static final String botUsername = "astiecobot";

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("** EcoBot onUpdateReceived init **");
        if(update.hasMessage()){
            Message message = update.getMessage();
            if(message.hasText()){
                String input = message.getText();
                String chatId = message.getChatId().toString();
                logger.info("** EcoBot getText: " + input +  " **");
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
            logger.info("** EcoBot getCallbackQuery: " + callbackquery.getData() +  " **");
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
        logger.info("** EcoBot onUpdateReceived end **");
    }

    public Message performEcocentro(String chatId){
        StringBuilder stringBuilder = new StringBuilder();
        DataManager dataManager = DataManager.getInstance();
        String cityName = dataManager.getMunicipalityName();
        RecyclingDepot rd = dataManager.getRecyclingDepot();
        stringBuilder.append("\uD83C\uDFE1 Ecocentro di ").append(cityName).append("\uD83C\uDFE1").append("\n\n");
        stringBuilder.append("\uD83D\uDD50 Orario \uD83D\uDD50 \n").append(rd.getTime()).append("\n\n");
        stringBuilder.append("\uD83D\uDCED Indirizzo \uD83D\uDCED \n").append(rd.getAddress()).append("\n\n");
        stringBuilder.append("?????? Telefono  ?????? \n").append(rd.getTelephone()).append("\n\n");
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
        logger.info("** EcoBot performStart init **");
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText("Ciao, sono EcoBot \uD83D\uDE9B ! Usa i comandi per chiedermi informazioni riguardo il calendario della raccolta differenziata della tua citt??! ??????Usa `/info` per sapere quali comandi puoi usare");
        sendMessagerequest.enableMarkdown(true);
        Message response = null;
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        logger.info("** EcoBot performStart end **");
        return response;
    }

    public Message performInfo(String chatId){
        logger.info("** EcoBot performInfo init **");
        SendMessage sendMessagerequest = new SendMessage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ciao, sono EcoBot qui sotto sono elencati i comandi che puoi usare \n\n");
        stringBuilder.append("/ecocentro - Informazioni riguardo l'ecocentro (orario, indirizzo e telefono) \n");
        stringBuilder.append("/getarea - Informazioni e calendario riguardo una specifica zona selezionata dall'utente \n");
        stringBuilder.append("/tomorrow - Indica che tipo di rifiuto viene raccolto domani \n");
        stringBuilder.append("/calendar - Mostra il calendario dei prossimi 7 giorni di una specifica zona \n");
        stringBuilder.append("/info - Informazioni di Ecobot \n\n");
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText(stringBuilder.toString());
        sendMessagerequest.enableMarkdown(true);
        Message response = null;
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        logger.info("** EcoBot performInfo end **");
        return response;
    }

    public Message performGetAreaMenu(String chatId, String callbackName){
        logger.info("** EcoBot performGetAreaMenu init **");
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText(Messages.CHOOSE_AREA);
        sendMessagerequest.enableMarkdown(true);
        sendMessagerequest.setReplyMarkup(this.getAreasMenu(callbackName));
        Message response = null;
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        logger.info("** EcoBot performGetAreaMenu end **");
        return response;
    }

    public Message getAreaData(String areaName, String chatId){
        logger.info("** EcoBot getAreaData init **");
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        Message response = null;
        List<Area> areasFiltered = DataManager.getInstance().getAreas().stream().filter(a -> a.getName().equals(areaName)).collect(Collectors.toList());

        if(!areasFiltered.isEmpty()){
            if(areasFiltered.get(0)!=null){
                Area area = areasFiltered.get(0);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("??????").append(areaName).append("??????\n\n");
                stringBuilder.append(area.getAddressedTo()).append("\n\n");
                stringBuilder.append(Messages.CALENDAR);
                if(area.getWeekCalendar().size()>0){
                    for (int i = 1; i <=7 ; i++) {
                        List<TrashContainer> trashContainerList = DataManager.getInstance().findContainers(String.valueOf(i), areasFiltered.get(0));
                        String dayName = DateUtils.getDayName(i, Locale.ITALY);
                        stringBuilder.append("\n\n").append("\uD83D\uDDD3???").append(" *").append(dayName).append("*: \n");
                        if(!trashContainerList.isEmpty()){
                            for (TrashContainer t: trashContainerList) {
                                stringBuilder.append("\uD83D\uDDD1???").append(TrashType.valueOf(t.getType()).getName()).append("\n");
                                stringBuilder.append("\uD83D\uDD51 ").append("_").append(t.getHoursRange()).append("_\n");
                            }
                        }else{
                            stringBuilder.append(Messages.NO_TAKING);
                        }
                    }
                }
                sendMessagerequest.setText(stringBuilder.toString());
                sendMessagerequest.setParseMode("Markdown");
            }
        }else{
            sendMessagerequest.setText(Messages.AREA_NOT_AVAILABLE);
        }
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        logger.info("** EcoBot getAreaData end **");
        return response;
    }

    public Message getCalendar(String areaName, String chatId){
        logger.info("** EcoBot getCalendar init **");
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        Message response = null;
        List<Area> areasFiltered = DataManager.getInstance().getAreas().stream().filter(a -> a.getName().equals(areaName)).collect(Collectors.toList());

        if(!areasFiltered.isEmpty()){
            if(areasFiltered.get(0)!=null){
                Area area = areasFiltered.get(0);
                Date today = new Date();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("??????").append(areaName).append("??????\n\n");
                stringBuilder.append(area.getAddressedTo()).append("\n\n");
                stringBuilder.append(Messages.CALENDAR_NEXT_7_DAYS);

                if(area.getWeekCalendar().size()>0){
                    for(int day=1; day<=7; day++){
                        Calendar calendar = DateUtils.addDay(day, today);
                        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        int month =calendar.get(Calendar.MONTH);
                        String monthName = DateUtils.getMonthName(month, Locale.ITALY);
                        stringBuilder.append("\n\n").append("\uD83D\uDDD3???").append(" *").append(dayOfMonth).append(" ").append(monthName).append("*: \n");

                        List<TrashContainer> trashContainerList = DataManager.getInstance().findContainers(String.valueOf(dayOfTheWeek), areasFiltered.get(0));
                        if(!trashContainerList.isEmpty()){
                            for (TrashContainer t: trashContainerList) {
                                stringBuilder.append("\uD83D\uDDD1???").append(TrashType.valueOf(t.getType()).getName()).append("\n");
                                stringBuilder.append("\uD83D\uDD51 ").append("_").append(t.getHoursRange()).append("_\n");
                            }
                        }else{
                            stringBuilder.append(Messages.NO_TAKING);
                        }
                    }
                }
                sendMessagerequest.setText(stringBuilder.toString());
                sendMessagerequest.setParseMode("Markdown");
            }
        }else{
            sendMessagerequest.setText(Messages.AREA_NOT_AVAILABLE);
        }
        try {
            response = execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        logger.info("** EcoBot getCalendar end **");
        return response;
    }

    private void getTomorrow(String areaName, String chatId){
        logger.info("** EcoBot getTomorrow init **");
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        List<Area> areasFiltered = DataManager.getInstance().getAreas().stream().filter(a -> a.getName().equals(areaName)).collect(Collectors.toList());
        if(!areasFiltered.isEmpty()){

            int dayOfTheWeek = DateUtils.addDay(1, new Date()).get(Calendar.DAY_OF_WEEK);
            String dayName = DateUtils.getDayName(dayOfTheWeek, Locale.ITALY);

            if(areasFiltered.get(0)!=null){
                List<TrashContainer> trashContainerList = DataManager.getInstance().findContainers(String.valueOf(dayOfTheWeek), areasFiltered.get(0));

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("??????").append(areaName).append("??????\n\n Domani ").append(dayName).append(":").append("\n");
                if(!trashContainerList.isEmpty()){
                    for (TrashContainer t: trashContainerList) {
                        stringBuilder.append("\uD83D\uDDD1???").append(TrashType.valueOf(t.getType()).getName()).append("\n").append("\uD83D\uDD51 ").append(t.getHoursRange()).append("\n\n");
                    }
                }else{
                    stringBuilder.append(Messages.NO_TAKING);
                }
                sendMessagerequest.setText(stringBuilder.toString());
            }
        }else{
            sendMessagerequest.setText(Messages.AREA_NOT_AVAILABLE);
        }
        try {
            execute(sendMessagerequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        logger.info("** EcoBot getTomorrow end **");
    }

    private void sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) throws TelegramApiException{
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        execute(answerCallbackQuery);
    }

    public static InlineKeyboardMarkup getAreasMenu(String callbackName){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<Area> areas = DataManager.getInstance().getAreas();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        if(areas.size()>0) {
            for (Area area : areas) {
                List<InlineKeyboardButton> riga = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                String name = area.getName();
                button.setText(name);
                button.setCallbackData(callbackName.concat(":").concat(name));
                riga.add(button);
                rowsInline.add(riga);
            }
        }

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
