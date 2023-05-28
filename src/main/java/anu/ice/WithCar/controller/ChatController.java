package anu.ice.WithCar.controller;

import anu.ice.WithCar.domain.dto.ChatFromClient;
import anu.ice.WithCar.domain.dto.ChatToClient;
import anu.ice.WithCar.service.ChatForRecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

	@PostMapping("/recruit/init/{recruitID}")
	@ResponseBody
	public List<ChatToClient> loadBeforeMessages(@PathVariable("recruitID") long no) {
		return chatForRecruitService.getChatMessagesForRecruit(no);
	}


}
