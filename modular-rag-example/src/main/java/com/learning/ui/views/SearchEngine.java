package  com.learning.ui.views;

import com.learning.service.ChatSearchEngineService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Search Engine", order = 3)
@Route("/search-engine")
public class SearchEngine extends Chatbot {

    public SearchEngine(ChatSearchEngineService chatService) {
        super(chatService);
    }

}
