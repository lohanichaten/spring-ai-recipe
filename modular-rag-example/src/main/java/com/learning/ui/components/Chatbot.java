package com.learning.ui.components;

import org.vaadin.firitin.components.messagelist.MarkdownMessage;

import com.learning.service.AiService;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Chatbot extends VerticalLayout {

    private static final String USER_LABEL = "You";
    private static final String ASSISTANT_LABEL = "AI";

    public Chatbot(AiService aiService) {
        setSizeFull();

        var chatWindow = new VerticalLayout();
        var inputForm = new MessageInput();
        inputForm.setWidthFull();

        inputForm.addSubmitListener(event -> {
            var userMessage = event.getValue();
            chatWindow.add(new MarkdownMessage(userMessage, USER_LABEL));

            var assistantMessage = new MarkdownMessage(ASSISTANT_LABEL);
            var result = aiService.chat(userMessage);
            assistantMessage.setMarkdown(result);
            chatWindow.add(assistantMessage);
        });

        addAndExpand(new Scroller(chatWindow));
        add(inputForm);
    }

}
