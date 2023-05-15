package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.dto.ChatToClient;
import anu.ice.WithCar.domain.dto.ChatFromClient;
import anu.ice.WithCar.security.JwtTokenProvider;
import anu.ice.WithCar.service.ChatServiceForRecruit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {

	private final ChatServiceForRecruit chatServiceForRecruit;

	@Autowired
	public ChatController(ChatServiceForRecruit chatServiceForRecruit) {
		this.chatServiceForRecruit = chatServiceForRecruit;
	}

	@MessageMapping("/recruit/{recruitID}")
	@SendTo("/topic/recruit/{recruitID}")
	public void greeting(@DestinationVariable("recruitID") long no,
								 ChatFromClient message) {
//		Thread.sleep(1000); // simulated delay
//		System.out.println("MESSAGE ARRIVED : " + message.getToken());

//		return new ChatToClient(HtmlUtils.htmlEscape(message.getMessage()), provider.getUserId(message.getToken()));
		chatServiceForRecruit.sendMessage(message, no);
	}

}
