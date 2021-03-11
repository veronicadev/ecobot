package veronicadev.ecobot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class EcoBot extends AbilityBot {
    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");
    private static final String botUsername = "astiecobot";
    public EcoBot() {
        super(BOT_TOKEN, botUsername);
    }

    @Override
    public int creatorId() {
        return Integer.parseInt(System.getenv("DEV_ID"));
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message message = update.getMessage();

            //check if the message contains a text
            if(message.hasText()){
                String input = message.getText();
                if(input.equals("/start")){
                    SendMessage sendMessagerequest = new SendMessage();
                    sendMessagerequest.setChatId(message.getChatId().toString());
                    sendMessagerequest.setText("Ciao, sono EcoBot \uD83D\uDE9B ! Usa i comandi per chiedermi informazioni riguardo il calendario della raccolta differenziata della tua città! ♻️Usa `/info` per sapere quali comandi puoi usare");
                    sendMessagerequest.enableMarkdown(true);
                    sendMessagerequest.setReplyMarkup(this.getAreasMenu());


                    try {
                        execute(sendMessagerequest);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private InlineKeyboardMarkup getAreasMenu(){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        for (Area a: DataManager.getInstance().getAreas()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(a.getName());
            button.setCallbackData("gallery:back:1");
            rowInline.add(button);
        }

        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }


}
