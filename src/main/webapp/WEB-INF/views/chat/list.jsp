<%--
  Created by IntelliJ IDEA.
  User: Seyoung
  Date: 2023-11-27
  Time: 오전 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Chat List</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="  crossorigin="anonymous"></script>

<script>
  var socket;

  function create() {
    const chatform = {
      type: "CREATE"
    }
    socket.send(JSON.stringify(chatform));
    $("#hidingForm").submit();
  }

  function join(){
    const chatform = {
      type: "JOIN",
      chatRoomId: $('#chatRoomId').val()
    }
    $("#hidingForm").submit();
  }

  $(document).ready(function () {
    socket = new WebSocket("ws://localhost:6080/chat");

    socket.onopen = function (event) {
      // Connection opened
      console.log("WebSocket connection opened ");
    };

    socket.onmessage = function (event) {
      var messageObj = JSON.parse(event.data);
      if(messageObj.type =="OPEN") {$("#chatRoomList").append(messageObj.content)}
      if(messageObj.type =="CREATE") {$("#chatRoomId").val(messageObj.chatRoomId);}
    };
  });
</script>

<header>you can join or create one</header>
<div> Here is the list of chat rooms
<div id="chatRoomList"> </div>
</div>
<button onclick="create()">Create</button>
<button onclick="join()">Join</button>

<form id="hidingForm" action="/chat/chatpage">
  <input name="chatRoomId">
</form>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
