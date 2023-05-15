package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.dto.ChatFromClient;
import anu.ice.WithCar.service.ChatForRecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	private final ChatForRecruitService chatForRecruitService;

	@Autowired
	public ChatController(ChatForRecruitService chatForRecruitService) {
		this.chatForRecruitService = chatForRecruitService;
	}

	@MessageMapping("/recruit/{recruitID}")
	public void sendMessage(@DestinationVariable("recruitID") long no,
								 ChatFromClient message) {
//		Thread.sleep(1000); // simulated delay
//		System.out.println("MESSAGE ARRIVED : " + message.getToken());

//		return new ChatToClient(HtmlUtils.htmlEscape(message.getMessage()), provider.getUserId(message.getToken()));
		chatForRecruitService.sendMessage(message, no);
	}

	@MessageMapping("/recruit/init/{recruitID}")
	public void loadBeforeMessages(@DestinationVariable("recruitID") long no) {
		chatForRecruitService.getChatMessagesForRecruit(no);
	}


}
