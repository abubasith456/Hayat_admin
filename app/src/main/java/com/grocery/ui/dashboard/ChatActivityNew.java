package com.grocery.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.grocery.BaseActivity;
import com.grocery.R;
import com.grocery.dialog.CustomDialogWithTwoButtons;
import com.grocery.model.Message;
import com.grocery.utils.Const;
import com.grocery.utils.SocketInstance;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivityNew extends BaseActivity {

    @BindView(R.id.deleteButton)
    ImageView deleteButton;

    @BindView(R.id.messages)
    RecyclerView mMessagesView;

    @BindView(R.id.message_input)
    EditText mInputMessageView;

    @BindView(R.id.send_button)
    ImageButton sendButton;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private List<Message> mMessages = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private String mSendername, mReceiverName, mSenderId, mReceiverId;
    private Socket mSocket;
    private Boolean isConnected = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        try{
            editor = pref.edit();
            mSendername = pref.getString("user_name", "");
            mReceiverName = Const.receiverName;
            mSenderId = pref.getString("user_id", "");
            mReceiverId = Const.receiverId;
            SocketInstance app = (SocketInstance) getApplication();
            mSocket = app.getSocket();

            mMessagesView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new MessageAdapter(this, mMessages);
            mMessagesView.setAdapter(mAdapter);
            scrollToBottom();

            mSocket.emit("register_userid",mSenderId); /* Global One */
//            mSocket.emit("register_userid","32492"); /* For my Mobile */
//            mSocket.emit("register_userid","32517"); /* For Friend Mobile */

            mSocket.emit("chat_history", mSenderId, mReceiverId); /* Global One */
//            mSocket.emit("chat_history","32492","32517"); /* For my Mobile */
//            mSocket.emit("chat_history","32517","32492"); /* For Friend Mobile */
            showProgress();

            mSocket.on("mysid", onMySid);

            mSocket.on("chat_history_response", onNewMessage);

            mSocket.on("chat_to_client_response", onMessageStatus);
            mSocket.on("chat_from_client_response", onMessageStatus);

            mSocket.connect();


            mInputMessageView.setOnEditorActionListener((v, id, event) -> {
                if (id == EditorInfo.IME_ACTION_SEND || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            });

        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.send_button)
    void onSendMessage() {
        try {
            attemptSend();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("mysid", onMySid);
        mSocket.off("chat_history_response", onNewMessage);
        mSocket.off("chat_to_client_response", onMessageStatus);
        mSocket.off("chat_from_client_response", onMessageStatus);
    }

    private void addMessage(String username, String message, int type) {
        mMessages.add(new Message.Builder(type)
                .username(username).message(message).isSelected(false).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
        showOrHideDeleteButton();
    }

    private void attemptSend() {
        try {
            if (null == mSendername) return;
            if (!mSocket.connected()) return;

            String message = mInputMessageView.getText().toString().trim();
            if (TextUtils.isEmpty(message)) {
                mInputMessageView.requestFocus();
                return;
            }
            int type = Message.TYPE_SENDER;
//        addMessage("You", message, type);
            mInputMessageView.setText("");
            mSocket.emit("chat_to_client", mSenderId, mReceiverId, message); /* Global One */
//        mSocket.emit("chat_to_client", "32492","32517", message); /* For my Mobile */
//        mSocket.emit("chat_to_client", "32517","32492", message); /* For Friend Mobile */
            Thread.sleep(200);
            mSocket.emit("chat_history", mSenderId, mReceiverId);
            showProgress();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }

    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onMySid = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String sid;
                    String userid;
                    try {
                        sid = data.getString("sid");
                        userid = data.getString("userid");
                        isConnected = true;
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        hideProgress();
                        org.json.JSONArray data = (org.json.JSONArray) args[0];
                        int count=0;
                        mMessages = new ArrayList<>();
                        while (count<data.length()){
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = data.getJSONObject(count);
                                int type = Message.TYPE_SENDER;
                                String userName = "You";
                                if(jsonObject.getString("type").equalsIgnoreCase("s")){
                                    type = Message.TYPE_SENDER;
                                    userName = "You";
                                }
                                else{
                                    type = Message.TYPE_RECEIVER;
                                    userName = mReceiverName;
                                }
                                mMessages.add(new Message.Builder(type).chatId(jsonObject.getInt("chat_id"))
                                        .username(userName).message(jsonObject.getString("msg")).isSelected(false).build());
                            } catch (Exception exception){
                                Log.e("Error ==> ", "" + exception);
                            }
                            count++;
                        }
                        mAdapter = new MessageAdapter(ChatActivityNew.this, mMessages);
                        mMessagesView.setAdapter(mAdapter);
                        scrollToBottom();
                    } catch (Exception exception){
                        Log.e("Error ==> ", "" + exception);
                    }
                }
            });
        }
    };

    private Emitter.Listener onMessageStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String message;
                    String sender_id;
                    try {
                        if (jsonObject.has("msg") && jsonObject.has("sender_id")  ) {
                            message = jsonObject.getString("msg");
                            sender_id = jsonObject.getString("sender_id");
                            int type = Message.TYPE_RECEIVER;
//                            addMessage(mReceiverName, message, type);
                            mSocket.emit("chat_history", mSenderId, mReceiverId);
//                            showProgress();
                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    void showOrHideDeleteButton(){
        if(mMessages.size() > 0)
            deleteButton.setVisibility(View.VISIBLE);
        else
            deleteButton.setVisibility(View.GONE);
    }

    public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

        private List<Message> mMessages;

        public MessageAdapter(Context context, List<Message> messages) {
            mMessages = messages;
            showOrHideDeleteButton();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout = -1;
            switch (viewType) {
                case Message.TYPE_SENDER:
                    layout = R.layout.row_item_sender_message;
                    break;
                case Message.TYPE_RECEIVER:
                    layout = R.layout.row_item_receiver_message;
                    break;
            }
            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Message message = mMessages.get(position);
            viewHolder.setIsSelected(message.getIsSelected());
            viewHolder.setMessage(message.getMessage());
            viewHolder.setUsername(message.getUsername());
            viewHolder.selectedRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message.setIsSelected(!message.getIsSelected());
                    int itemPosition = mMessagesView.getChildLayoutPosition(view);
                    viewHolder.selectedRow.setBackgroundColor(message.getIsSelected() ? Color.CYAN : Color.WHITE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMessages.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mMessages.get(position).getType();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout selectedRow;
            private TextView mUsernameView;
            private TextView mMessageView;

            public ViewHolder(View itemView) {
                super(itemView);
                selectedRow = (LinearLayout) itemView.findViewById(R.id.selectedRow);
                mUsernameView = (TextView) itemView.findViewById(R.id.username);
                mMessageView = (TextView) itemView.findViewById(R.id.message);


            }

            public void setIsSelected(boolean isSelected) {
                if (null == mUsernameView) return;
                selectedRow.setBackgroundColor(isSelected ? Color.CYAN : Color.WHITE);
            }

            public void setUsername(String username) {
                if (null == mUsernameView) return;
                mUsernameView.setText(username);
            }

            public void setMessage(String message) {
                if (null == mMessageView) return;
                mMessageView.setText(message);
            }


        }
    }

    @OnClick(R.id.backButton)
    void backButtonClick(){
        try {
            finish();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    CustomDialogWithTwoButtons.OnDismissListener onDismissListener = () -> {
        ArrayList<Integer> selectedRows = new ArrayList<>();
        for(Message message : mMessages){
            if(message.getIsSelected())
                selectedRows.add(message.getChatId());
        }
        if(selectedRows.size() > 0)
            Log.d("", "Selected Message Rows Counts is > 0");
//            deleteSelectedChatMessagesFromHistory(selectedRows);
        else
            Log.d("", "Selected Message Rows Counts is 0");
//            deleteReceiverEntireChatHistory();
    };


    @OnClick(R.id.deleteButton)
    void deleteButtonClick(){
        try {
            showCustomDialogWithTwoButtons("", "Do you want to delete your chat message?", getResources().getString(R.string.yes), getResources().getString(R.string.confirm), onDismissListener);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }
}
