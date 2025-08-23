package  com.learning.ui.views;

import com.learning.service.ChatVectorStoreService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Vector Store", order = 4)
@Route("/vector-store")
public class VectorStore extends Chatbot {

    public VectorStore(ChatVectorStoreService chatService) {
        super(chatService);
    }

}
