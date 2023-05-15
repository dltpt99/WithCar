package anu.ice.WithCar.domain.dto;

public class ChatToClient {

	private final String content;
	private final String nick;

	public ChatToClient(String content, String nick) {
		this.content = content;
		this.nick = nick;
	}

	public String getContent() {
		return content;
	}

}
