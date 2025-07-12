package com.cdut.playtask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cdut.playtask.network.ChatRequest;
import com.cdut.playtask.network.ChatResponse;
import com.cdut.playtask.network.GLMService;
import com.cdut.playtask.network.Message;
import com.cdut.playtask.ui.ChatAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIChatActivity extends AppCompatActivity {

    private EditText etQuestion;
    private Button btnAsk;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    /** 聊天记录列表（内存） */
    private List<Message> chatHistory = new ArrayList<>();

    /** Gson 用于序列化 / 反序列化 */
    private final Gson gson = new Gson();

    /** SP 文件名 */
    private static final String SP_NAME = "chat";
    private static final String KEY_HISTORY = "history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        // 1. 视图初始化
        etQuestion   = findViewById(R.id.et_question);
        btnAsk       = findViewById(R.id.btn_ask);
        recyclerView = findViewById(R.id.recycler_chat);

        // 2. 读取本地历史
        loadChatHistory();

        // 3. 设置 RecyclerView
        adapter = new ChatAdapter(chatHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 4. 发送按钮监听
        btnAsk.setOnClickListener(v -> askAI());
    }

    /** 处理发送请求 & AI 回复 */
    private void askAI() {
        String question = etQuestion.getText().toString().trim();
        if (question.isEmpty()) return;

        // 1) 添加用户消息
        addMessage(new Message("user", question));
        etQuestion.setText("");

        // 2) 构造请求
        ChatRequest request = new ChatRequest(new ArrayList<>(chatHistory));

        // 3) 调用 GLM API
        GLMService.getInstance().chat(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> res) {
                if (res.isSuccessful() && res.body() != null &&
                        res.body().choices != null && !res.body().choices.isEmpty()) {

                    String reply = res.body().choices.get(0).message.content;
                    addMessage(new Message("assistant", reply));

                } else {
                    addMessage(new Message("assistant", "AI 无响应"));
                }
            }
            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                addMessage(new Message("assistant", "请求失败：" + t.getMessage()));
            }
        });
    }

    /** 添加一条消息并刷新 UI + 存储 */
    private void addMessage(Message msg) {
        chatHistory.add(msg);
        adapter.notifyItemInserted(chatHistory.size() - 1);
        recyclerView.scrollToPosition(chatHistory.size() - 1);
        saveChatHistory(); // 持久化
    }

    /** === 本地持久化（SharedPreferences） === */

    private void saveChatHistory() {
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        sp.edit().putString(KEY_HISTORY, gson.toJson(chatHistory)).apply();
    }

    private void loadChatHistory() {
        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String json = sp.getString(KEY_HISTORY, null);
        if (json != null) {
            Type type = new TypeToken<List<Message>>() {}.getType();
            chatHistory = gson.fromJson(json, type);
        }
    }
}
