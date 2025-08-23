package  com.learning.ui.views;

import com.learning.service.ChatTranslationService;
import com.learning.ui.components.StreamingChatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Translation", order = 5)
@Route("/translation")
public class Translation extends StreamingChatbot {

    public Translation(ChatTranslationService chatService) {
        super(chatService);
    }

}
