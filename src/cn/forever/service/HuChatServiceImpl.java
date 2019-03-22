package cn.forever.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.forever.dao.ObjectDao;
import cn.forever.model.HuChatInfo;
import cn.forever.webSocket.Message;
@Component("huChatService")
@Transactional
public class HuChatServiceImpl implements HuChatService {
	@Resource(name="objectDao")
	private ObjectDao objectDao;
	@Override
	public void saveMessageToDB(List<Message> messages) {
		for (Message message : messages) {
			System.out.println("message:"+message.toString());
			HuChatInfo huChatInfo =new HuChatInfo();
			huChatInfo.setContent(message.getContent());
			huChatInfo.setCreate_datetime(message.getCreate_datetime());
			huChatInfo.setMsgType(message.getMsgType());
			huChatInfo.setFileType(message.getFileType());
			huChatInfo.setPath(message.getPath());
			huChatInfo.setUserId(message.getUserId());
			huChatInfo.setUsername(message.getUsername());
			objectDao.saveOrUpdate(huChatInfo);
		}

	}
	@Override
	public void saveMessageToDB(Message message) {
		// TODO Auto-generated method stub
		HuChatInfo huChatInfo =new HuChatInfo();
		huChatInfo.setContent(message.getContent());
		huChatInfo.setCreate_datetime(message.getCreate_datetime());
		huChatInfo.setMsgType(message.getMsgType());
		huChatInfo.setFileType(message.getFileType());
		huChatInfo.setPath(message.getPath());
		huChatInfo.setImgUrl(message.getImgUrl());
		huChatInfo.setUserId(message.getUserId());
		huChatInfo.setUsername(message.getUsername());
		huChatInfo.setVisible(message.getVisible());//是否显示
		objectDao.saveOrUpdate(huChatInfo);
	}

}
