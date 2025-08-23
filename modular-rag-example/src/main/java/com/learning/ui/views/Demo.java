package  com.learning.ui.views;

import com.learning.service.DemoService;
import com.learning.ui.components.Chatbot;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Menu(title = "Demo", order = 1)
@Route("")
public class Demo extends Chatbot {

    public Demo(DemoService demoService) {
        super(demoService);
    }

}
