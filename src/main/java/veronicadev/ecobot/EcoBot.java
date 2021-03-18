package veronicadev.ecobot;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class EcoBot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = System.getProperty("BOT_TOKEN");
    private static final String botUsername = "astiecobot";

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();

            //check if the message contains a text
            if(message.hasText()){
                String input = message.getText();
                String chatId = message.getChatId().toString();
                System.out.println("** ".concat(input).concat(" **"));
                switch(input){
                    case "/start":
                        performStart(chatId);
                    break;
                    case "/info":
                        performInfo(chatId);
                    break;
                    case "/tomorrow":
                        performTomorrow(chatId);
                    break;
                }
            }
        }else if(update.hasCallbackQuery()){
            CallbackQuery callbackquery = update.getCallbackQuery();
            String[] data = callbackquery.getData().split(":");
            if(data[0].equals("tomorrow")){
                this.getTomorrow(data[1], callbackquery.getMessage().getChatId().toString());
            }else{
                try {
                    this.sendAnswerCallbackQuery("Usa uno dei comandi elencati sopra!", false, callbackquery);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public Message performTomorrow(String chatId){
        SendMessage sendMessagerequest = new SendMessage();
        sendMessagerequest.setChatId(chatId);
        sendMessagerequest.setText("Scegli l'area interessata");
        sendMessagerequest.enableMarkdown(true);
        sendMessagerequest.setReplyMarkup(this.getAreasMenu());
        Message response = null;
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
        System.out.println(areasFiltered);

        if(!areasFiltered.isEmpty()){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 1);
            int dayOfTheWeek = c.get(Calendar.DAY_OF_WEEK);
            if(areasFiltered.get(0)!=null){
                System.out.println(dayOfTheWeek);
                System.out.println(DataManager.getInstance().findByDay(String.valueOf(dayOfTheWeek), areasFiltered.get(0)));
                sendMessagerequest.setText("♻️"+areaName+"  ♻️Domani: "+DataManager.getInstance().findByDay(String.valueOf(dayOfTheWeek), areasFiltered.get(0)));
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

    private InlineKeyboardMarkup getAreasMenu(){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (Area a: DataManager.getInstance().getAreas()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(a.getName());
            button.setCallbackData("tomorrow:".concat(a.getName()));
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
