package anu.ice.WithCar.domain.dto;

import lombok.Data;

@Data
public class ChatToClient {

	private final String msgID;
	private final String content;
	private final String nick;
	private final String time;

	public ChatToClient(String msgID, String content, String nick, String time) {
		this.content = content;
		this.nick = nick;
		this.msgID = msgID;
		this.time = time;
	}
}
