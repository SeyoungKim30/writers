<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WebSocket Chat</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <link rel="stylesheet" href="/resources/chatpage.css">
    <script>
        var socket;

        $(document).ready(function () {
            socket = new WebSocket("ws://localhost:6080/${path}/chat");

            socket.onopen = function (event) {
                // Connection opened
                console.log("WebSocket connection opened");
            };

            socket.onmessage = function (event) {
                // Received a message
                var messageObj = JSON.parse(event.data);
                showMessage(messageObj);
            };
        });

        function sendMessage() {
            var messageInput = $("#message-input");
            var sender = $("#message-sender").val();
            var messageObj = {
                username:sender,
                message : messageInput.val()
            };
            if (messageObj.message.trim() !== "") {
                socket.send(JSON.stringify(messageObj));
                messageInput.val("").focus();   //보내고 입력칸 지움
            }
        }

        function showMessage(messageObj) {
            var username =messageObj.username;
            var message = messageObj.message;

            var alignClass = "left";
            if (username == $("#message-sender").val()) {
                alignClass = "right";
            }

            var messageDiv = $("<div></div>")
                .addClass("message")
                .addClass(alignClass)
                .text(message);

            $("#chat-messages").append(messageDiv);

            // Scroll to the bottom
            var chatMessages = document.getElementById("chat-messages");
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    </script>
</head>
<body>

<div id="chat-container">
    <div id="chat-messages"></div>
    <form>
        <input id="message-sender" value="userA">
    <input type="text" id="message-input" placeholder="Type your message"/>
    <button type="button" id="send-btn" onclick="sendMessage()">Send</button>
    </form>
</div>

</body>
</html>
