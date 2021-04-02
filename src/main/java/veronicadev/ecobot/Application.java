package veronicadev.ecobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import veronicadev.ecobot.utils.DataManager;


public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            logger.info("** Application init **");
            DataManager.getInstance().readJSON("data.json");
            EcoBot ecoBot = new EcoBot();
            api.registerBot(ecoBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            logger.error("** Catched Error in Application **", e);
        }

    }

}
