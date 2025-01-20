package org.clinique.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.*;

import java.io.IOException;

public class AiAssistantController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String api_key = "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";

    @FXML
    private void handleSend() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            updateChatArea("You: " + userInput);
            sendToOpenAI(userInput);
            inputField.setText(""); // Clear the input after sending
        }
    }

    private void sendToOpenAI(String query) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String requestBody = "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + query + "\"}]"
                + "}";

        RequestBody body = RequestBody.create(requestBody, JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + api_key)
                .addHeader("Content-Type", "application/json")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() ->
                        updateChatArea("AI: Failed to fetch response")
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string(); // Assuming response JSON has a field like message
                    String aiResponse = parseAIResponse(responseData);
                    javafx.application.Platform.runLater(() ->
                            updateChatArea("AI: " + aiResponse)
                    );
                } else {
                    javafx.application.Platform.runLater(() ->
                            updateChatArea("AI: Error - " + response.message())
                    );
                }
            }
        });
    }



    private String parseAIResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode messageNode = firstChoice.path("message");
                if (!messageNode.isMissingNode()) {
                    return messageNode.path("content").asText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unable to parse AI response";
    }


    private void updateChatArea(String message) {
        chatArea.appendText("\n" + message);
    }
}