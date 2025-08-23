package  com.learning.ui.views;

import com.learning.service.ChatToolService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Tools", order = 10)
@Route("/tools")
public class Tools extends Chatbot {

    public Tools(ChatToolService aiService) {
        super(aiService);
    }

}
