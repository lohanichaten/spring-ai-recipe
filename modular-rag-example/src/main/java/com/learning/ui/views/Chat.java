package   com.learning.ui.views;


import com.learning.service.ChatService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Chat", order = 2)
@Route("/chat")
public class Chat extends Chatbot {

    public Chat(ChatService chatService) {
        super(chatService);
    }

}
