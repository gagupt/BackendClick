package com.example.demo.data;

public class MessageObj {
	private String messageId;
	private String text;
	private long sendAt;

	public MessageObj(String messageId, String text, long sendAt) {
		super();
		this.messageId = messageId;
		this.text = text;
		this.sendAt = sendAt;
	}

	public long getSendAt() {
		return sendAt;
	}

	public void setSendAt(long sendAt) {
		this.sendAt = sendAt;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MessageObj() {
		super();
	}

	@Override
	public String toString() {
		return "MessageObj [messageId=" + messageId + ", text=" + text + ", sendAt=" + sendAt + "]";
	}

}
