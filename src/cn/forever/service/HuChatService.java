package cn.forever.service;

import java.util.List;

import cn.forever.webSocket.Message;

public interface HuChatService {
	/**
	 * 在持久化到本地的时候保存
	 * @param messages 多条信息
	 */
	public void saveMessageToDB(List<Message> messages);
	/**
	 * 在持久化到本地的时候保存
	 * @param message 单条信息
	 */
	public void saveMessageToDB(Message message);
}
