package com.skripsi.atma.chat;

import com.quickblox.module.chat.QBChat;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

public class ChatController {

    // ================= QuickBlox ===== Step 8 =================
    // Get QuickBlox chat server domain.
    // There will be created connection with chat server below.
    public static final String CHAT_SERVER = QBChat.getChatServerDomain();

    private XMPPConnection connection;

    private ConnectionConfiguration config;
    private Chat chat;

    private String chatLogin;
    private String password;
    private String friendLogin;

    private ChatManager chatManager;

    public ChatController(String chatLogin, String password) {
        this.chatLogin = chatLogin;
        this.password = password;
    }

    public void startChat(String buddyLogin) {
        this.friendLogin = buddyLogin;

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Chat action 1 -- create connection.
            	Connection.DEBUG_ENABLED = true;
                config = new ConnectionConfiguration(CHAT_SERVER);
                connection = new XMPPConnection(config);

                try {
                    connection.connect();
                    connection.login(chatLogin, password);

                    // Chat action 2 -- create chat manager.
                    chatManager = connection.getChatManager();
                    
                    // Chat action 3 -- create chat.
                    chat = chatManager.createChat(friendLogin, messageListener);

                    // Set listener for outcoming messages.
                    chatManager.addChatListener(chatManagerListener);

                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private ChatManagerListener chatManagerListener = new ChatManagerListener() {
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            // Set listener for incoming messages.
            chat.addMessageListener(messageListener);
        }
    };

    public void sendMessage(String message) {
        try {
            if (chat != null) {
                chat.sendMessage(message);
            }
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    private MessageListener messageListener = new MessageListener() {
        @Override
        public void processMessage(Chat chat, Message message) {
            // 'from' and 'to' fields contains senders ids, e.g.
            // 17792-1028@chat.quickblox.com/mac-167
            // 17744-1028@chat.quickblox.com/Smack
            String from = message.getFrom().split("@")[0];
            String to = message.getTo().split("@")[0];

            System.out.println(String.format(">>> Message received (from=%s, to=%s): %s",
                    from, to, message.getBody()));

            if (onMessageReceivedListener != null) {
                onMessageReceivedListener.onMessageReceived(message);
            }
        }
    };


    public static interface OnMessageReceivedListener {
        void onMessageReceived(Message message);
    }

    // Callback that performs when device retrieves incoming message.
    private OnMessageReceivedListener onMessageReceivedListener;

    public OnMessageReceivedListener getOnMessageReceivedListener() {
        return onMessageReceivedListener;
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener onMessageReceivedListener) {
        this.onMessageReceivedListener = onMessageReceivedListener;
    }
}