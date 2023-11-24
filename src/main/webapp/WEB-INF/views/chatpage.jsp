<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WebSocket Chat</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <link rel="stylesheet" href="/resources/chatpage.css">
    <script>

        var socket;
 //       var chatRoomId = generateRandomUUID();

        $(document).ready(function () {
            socket = new WebSocket("ws://localhost:6080/${path}/chat");

            socket.onopen = function (event) {
                // Connection opened
                console.log("WebSocket connection opened :" + chatRoomId);
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
            var chatRoom = $("#chatRoomId").val()
            var messageObj = {
                chatRoomId: chatRoom,
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

        function generateRandomUUID() {
            // 실제로는 더 안전한 방법으로 UUID를 생성하는 라이브러리를 사용하는 것이 좋습니다.
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                const r = Math.random() * 16 | 0,
                    v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }
    </script>
</head>
<body>

<div id="chat-container">
    <div id="chat-messages"></div>
    <form>
        <input id="chatRoomId" value="123">
        <input id="message-sender" value="userA">
    <input type="text" id="message-input" placeholder="Type your message"/>
    <button type="button" id="send-btn" onclick="sendMessage()">Send</button>
    </form>
</div>

</body>
</html>
