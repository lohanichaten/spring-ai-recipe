package  com.learning.ui.views;

import com.learning.service.ChatRewriteService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Rewrite", order = 6)
@Route("/rewrite")
public class Rewrite extends Chatbot {

    public Rewrite(ChatRewriteService chatService) {
        super(chatService);
    }

}
