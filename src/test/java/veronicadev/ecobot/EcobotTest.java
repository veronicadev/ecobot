package veronicadev.ecobot;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.*;

public class EcobotTest {

    private EcoBot bot;
    private SilentSender silent;

    @Before
    public void setUp() {
        //bot = Mockito.mock(EcoBot.class);
    }

    @Test
    public void test_1(){
        Assert.assertNotNull(new EcoBot());
    }

    @Test
    public void test_2(){
        EcoBot ecoBot = new EcoBot();
        Assert.assertTrue(ecoBot.getBotToken().equals(System.getProperty("BOT_TOKEN")));
    }

    @Test
    public void test_3(){
        EcoBot ecoBot = new EcoBot();
        Assert.assertNotNull(ecoBot.getBotUsername());
    }

    /*@Test
    public void test_bo() {
        Mockito.doCallRealMethod().when(bot).onUpdatesReceived(any());
        Update update = new Update();
        Message message = new Message();
        message.setText("/start");
        update.setMessage(message);
        bot.onUpdateReceived(update);
        Mockito.verify(bot).onUpdateReceived(update);
    }*/
}