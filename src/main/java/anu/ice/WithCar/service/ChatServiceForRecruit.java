package anu.ice.WithCar.service;

import anu.ice.WithCar.domain.dto.ChatFromClient;
import anu.ice.WithCar.domain.entity.ChatMessageForRecruit;
import anu.ice.WithCar.domain.entity.ChatRoomForRecruit;
import anu.ice.WithCar.domain.entity.Member;
import anu.ice.WithCar.domain.entity.RecruitCarfull;
import anu.ice.WithCar.exception.CarfullRecruit.CarfullRecruitNotFoundException;
import anu.ice.WithCar.exception.Chat.ChatRoomNotFoundExcpetion;
import anu.ice.WithCar.repository.CarfullRecruitRepository;
import anu.ice.WithCar.repository.ChatMessageForRecruitRepository;
import anu.ice.WithCar.repository.ChatRoomForRecruitRepository;
import anu.ice.WithCar.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceForRecruit {
    private final ChatRoomForRecruitRepository roomForRecruitRepository;
    private final ChatMessageForRecruitRepository messageForRecruitRepository;
    private final CarfullRecruitRepository recruitRepository;
    private final JwtTokenProvider provider;

    private final SimpMessagingTemplate template;

    @Autowired
    public ChatServiceForRecruit(ChatRoomForRecruitRepository roomForRecruitRepository, ChatMessageForRecruitRepository messageForRecruitRepository, CarfullRecruitRepository recruitRepository, JwtTokenProvider provider, SimpMessagingTemplate template) {
        this.roomForRecruitRepository = roomForRecruitRepository;
        this.messageForRecruitRepository = messageForRecruitRepository;
        this.recruitRepository = recruitRepository;
        this.provider = provider;
        this.template = template;
    }

    public void createNewChatRoomForRecruit(RecruitCarfull recruitCarfull) {
        ChatRoomForRecruit room = new ChatRoomForRecruit(recruitCarfull);

        // ChatRoomForRecruit의 ID는 RecruitCarfull의 ID와 같다
        room.setId(recruitCarfull.getRecruitCarfullID());

        roomForRecruitRepository.save(room);
    }

    //apply 할때 수행됨
    public void addNewMemberToChatRoom(RecruitCarfull recruitCarfull, Member member) {
        ChatRoomForRecruit room = getRoomById(recruitCarfull);
        List<Member> users = room.getUsers();

        users.add(member);
        room.setUsers(users);

        //채팅방에 입장한 메시지 생성 및 전송
        ChatMessageForRecruit message =  createMessage(recruitCarfull.getRecruitCarfullID(), member, ChatMessageForRecruit.MessageType.ENTER);
        sendMessageToSubcriber(room, message);

        roomForRecruitRepository.save(room);
    }

    //cancel 할때 수행됨
    public void removeMemberFromChatRoom(RecruitCarfull recruitCarfull, Member member) {
        ChatRoomForRecruit room = getRoomById(recruitCarfull);
        List<Member> users = room.getUsers();

        users.remove(member);
        room.setUsers(users);

        //채팅방에서 나간 메시지 생성
        ChatMessageForRecruit message = createMessage(recruitCarfull.getRecruitCarfullID(), member, ChatMessageForRecruit.MessageType.LEAVE);
        sendMessageToSubcriber(room, message);

        roomForRecruitRepository.save(room);
    }

    public void sendMessage(ChatFromClient client, long recruitPostID) {
        Member member = (Member) provider.getAuthentication(client.getToken()).getPrincipal();

        ChatMessageForRecruit message =  createMessage(recruitPostID, member, ChatMessageForRecruit.MessageType.TEXT);

//        ChatToClient chat = new ChatToClient(client.getMessage(), member.getNick());
        sendMessageToSubcriber(recruitPostID, message);
//        return message;
    }

    public List<ChatMessageForRecruit> getChatMessagesForRecruit(long recruitPostID) {
        ChatRoomForRecruit room = getRoomById(recruitPostID);
        return messageForRecruitRepository.findAllByRoom(room);
    }

    private void sendMessageToSubcriber(ChatRoomForRecruit room, ChatMessageForRecruit message) {
        template.convertAndSend("/topic/recruit/"+ room.getId(), message);
    }

    private void sendMessageToSubcriber(long roomID, ChatMessageForRecruit message) {
        template.convertAndSend("/topic/recruit/"+ roomID, message);
    }

    private ChatMessageForRecruit createMessage(long recruitPostID, Member member, ChatMessageForRecruit.MessageType type) {
        ChatMessageForRecruit message = new ChatMessageForRecruit();
        message.setSender(member);
        message.setSendTime(LocalDateTime.now());
        message.setType(type);
        message.setRoom(getRoomById(recruitPostID));

        return messageForRecruitRepository.save(message);
    }

    private ChatRoomForRecruit getRoomById(long recruitPostID) {
        RecruitCarfull recruitCarfull = recruitRepository.findById(recruitPostID).orElseThrow(CarfullRecruitNotFoundException::new);
        return roomForRecruitRepository.findByRecruitCarfull(recruitCarfull)
                .orElseThrow(() -> {
                    createNewChatRoomForRecruit(recruitCarfull);
                    throw new ChatRoomNotFoundExcpetion();
                });

    }

    private ChatRoomForRecruit getRoomById(RecruitCarfull recruitCarfull) {
        return roomForRecruitRepository.findByRecruitCarfull(recruitCarfull).orElseThrow(() -> {
            createNewChatRoomForRecruit(recruitCarfull);
            throw new ChatRoomNotFoundExcpetion();
        });
    }
}
