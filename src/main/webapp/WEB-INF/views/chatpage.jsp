<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WebSocket Chat</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="  crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/resources/chatpage.css">
    <script>

        var socket;
 //       var chatRoomId = generateRandomUUID();

        $(document).ready(function () {
            socket = new WebSocket("ws://localhost:6080/${path}/chat");

            socket.onopen = function (event) {
                // Connection opened
                console.log("WebSocket connection opened ");
            };

            socket.onmessage = function (event) {
                // Received a message
                var messageObj = JSON.parse(event.data);
                console.log("chat room id" + messageObj.chatRoomId);
                $("#chatRoomId").val(messageObj.chatRoomId);
                $("#session").val(messageObj.session);
                showMessage(messageObj);
            };
        });

        function sendMessage() {
            var content = $("#content");
            const chatform = {chatRoomId: $("#chatRoomId").val().trim(),
                type: $("#type").val(),
                sender: $("#sender").val().trim(),
                content: $("#content").val().trim()
            }

            if (chatform.content.trim() !== "") {
                socket.send(JSON.stringify(chatform));
                content.val("").focus();   //보내고 입력칸 지움
            }
        }

        function showMessage(messageObj) {
            var sender =messageObj.sender;
            var content = messageObj.content;
            var chatRoomId =messageObj.chatRoomId;

            var alignClass = "left";
            if (sender == $("#sender").val()) {
                alignClass = "right";
            }

            var messageDiv = $("<div></div>")
                .addClass("message")
                .addClass(alignClass)
                .text(sender + content);

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
    <form id="chatform">
        session : <input id="session">
        chatRoomId : <input id="chatRoomId" name="chatRoomId" placeholder="chatRoomId">
        Type :<select id="type" name="type" ><option>MESSAGE</option><option>CREATE</option><option>JOIN</option></select>
        Sender : <input id="sender" name="sender" value="userA">
        Content : <input name="content" id="content" placeholder="Type your message"/>
    <button type="button" id="send-btn" onclick="sendMessage()">Send</button>
    </form>
</div>

</body>
</html>
