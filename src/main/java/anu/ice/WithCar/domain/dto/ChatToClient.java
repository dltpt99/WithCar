package anu.ice.WithCar.domain.dto;

import lombok.Data;

@Data
public class ChatToClient {

	private final String content;
	private final String nick;

	public ChatToClient(String content, String nick) {
		this.content = content;
		this.nick = nick;
	}

}
