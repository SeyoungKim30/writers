<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WebSocket Chat</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <style>
        body {
            font-family: 'Helvetica', 'Arial', sans-serif;
            margin: 0;
            padding: 0;
        }

        #chat-container {
            max-width: 400px;
            margin: 20px auto;
            background-color: #f5f5f5;
            border: 1px solid #ccc;
            border-radius: 10px;
            overflow: hidden;
        }

        #chat-messages {
            max-height: 400px;
            overflow-y: auto;
            padding: 10px;
        }

        .message {
            background-color: #007bff;
            color: #fff;
            padding: 10px;
            margin-bottom: 5px;
            border-radius: 5px;
        }

        .message.right {
            background-color: #28a745;
            color: #fff;
            align-self: flex-end;
        }

        .message.left {
            background-color: #f1f1f1;
            color: #333;
        }

        #message-input {
            width: 80%;
            padding: 8px;
            border: none;
            border-top: 1px solid #ccc;
            box-sizing: border-box;
            float: left;
        }

        #send-btn {
            width: 20%;
            padding: 8px;
            border: none;
            border-top: 1px solid #ccc;
            box-sizing: border-box;
            background-color: #007bff;
            color: #fff;
            cursor: pointer;
            float: left;
        }
    </style>
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
                var message = event.data;
                showMessage(message);
            };
        });

        function sendMessage() {
            var messageInput = $("#message-input");
            var message = messageInput.val();
            if (message.trim() !== "") {
                socket.send(message);
                messageInput.val("").focus();
            }
        }

        function showMessage(message) {
            var alignClass = "left";
            if (message.startsWith("You:")) {
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
    <input type="text" id="message-input" placeholder="Type your message"/>
    <button id="send-btn" onclick="sendMessage()">Send</button>
</div>

</body>
</html>
