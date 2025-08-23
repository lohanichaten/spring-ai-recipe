package  com.learning.ui.views;

import com.learning.service.ChatPostProcessService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Post-Process", order = 7)
@Route("/post-process")
public class PostProcess extends Chatbot {

    public PostProcess(ChatPostProcessService chatService) {
        super(chatService);
    }

}
