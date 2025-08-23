package  com.learning.ui.views;

import com.learning.service.ChatRoutingService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Routing", order = 9)
@Route("/routing")
public class Routing extends Chatbot {

    public Routing(ChatRoutingService aiService) {
        super(aiService);
    }

}
