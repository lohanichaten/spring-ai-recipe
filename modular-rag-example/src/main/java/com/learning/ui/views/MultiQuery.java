package  com.learning.ui.views;

import com.learning.service.ChatMultiQueryService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Multi-Query", order = 8)
@Route("/multi-query")
public class MultiQuery extends Chatbot {

    public MultiQuery(ChatMultiQueryService chatService) {
        super(chatService);
    }

}
