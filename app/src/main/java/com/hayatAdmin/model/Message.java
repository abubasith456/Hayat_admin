package com.hayatAdmin.model;

public class Message {

    public static final int TYPE_SENDER = 11;
    public static final int TYPE_RECEIVER = 12;

    private int mType;
    private int mChatId;
    private String mMessage;
    private String mUsername;
    private boolean isSelected;

    private Message() {}

    public int getType() {
        return mType;
    };

    public int getChatId() {
        return mChatId;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public boolean getIsSelected () {
        return isSelected;
    }

    public void setIsSelected (boolean misSelected) {
        isSelected = misSelected;
    }


    public static class Builder {
        private final int mType;
        private int mChatId;
        private String mUsername;
        private String mMessage;
        private boolean isSelected;

        public Builder(int type) {
            mType = type;
        }

        public Builder chatId(int chatId) {
            mChatId = chatId;
            return this;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder isSelected(boolean mIsSelected) {
            isSelected = mIsSelected;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mChatId = mChatId;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.isSelected = isSelected;
            return message;
        }
    }
}
