var stompClient = null;
var subscriptionHandshake;
var subscriptionStatus;
var subscriptionMessages;
var subscriptionUsers;
var username;
var soundNotify = new Audio('sound/notification.mp3');

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('from').disabled = connected;
    document.getElementById('conversation').style.visibility
        = connected ? 'visible' : 'hidden';
    document.getElementById('login').style.visibility
        = !connected ? 'visible' : 'hidden';
}

function connect() {
    if (document.getElementById('from').value === '') {
        console.error('Please enter a user name');
        return;
    }

    var socket = new SockJS('/app');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        subscriptionHandshake = stompClient.subscribe('/topic/handshake', function(handshake){
            handleHandshake(JSON.parse(handshake.body));
        });
        stompClient.send('/app/users/handshake', {}, document.getElementById('from').value);
    });
}

function handleBacklog(backlogMsg) {
    switch (backlogMsg.backlogType) {
        case 'Message':
            return appendMessage(backlogMsg);
        case 'StatusUpdate':
            return appendStatusMessage(backlogMsg);
        default:
            console.error('Invalid log type: ' + backlogMsg.backlogType);
            return;
    }
}

function handleHandshake(handshake) {
    username = handshake.username;
    document.getElementById('response').innerHTML = '';
    handshake.backlog.forEach(handleBacklog);
    setConnected(true);
    subscriptionHandshake.unsubscribe();
    subscriptionStatus = stompClient.subscribe('/topic/status', function(status){
        appendStatusMessage(JSON.parse(status.body));
    });
    subscriptionMessages = stompClient.subscribe('/topic/messages', function(messageOutput) {
        var msg = JSON.parse(messageOutput.body);
        if (msg.from !== username)
            soundNotify.play();
        appendMessage(msg);
    });
    subscriptionUsers = stompClient.subscribe('/topic/users', function(messageOutput) {
        var data = JSON.parse(messageOutput.body);
        document.getElementById('userlist').innerHTML = '';
        data.forEach(addUserToUI);
    });
    stompClient.send("/app/users/join", {}, username);
}

function addUserToUI(user) {
    var userEntry = document.createElement('p'); // <p></p>
    userEntry.textContent = user; // <p>user</p>
    userEntry.style.color = 'white';
    document.getElementById('userlist').appendChild(userEntry);
}

function disconnect() {
    if(stompClient != null) {
        stompClient.send("/app/users/leave", {}, username);
        subscriptionMessages.unsubscribe();
        subscriptionUsers.unsubscribe();
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var text = document.getElementById('text').value;
    if (text === "")
        return;
    stompClient.send("/app/msg", {}, JSON.stringify({'from':username, 'text':text}));
    document.getElementById('text').value = "";
    document.getElementById('response').scrollTo(0, document.getElementById('response').scrollHeight)
}

function appendStatusMessage(status) {
    var msg = "";
    switch (status.type) {
        case 'USER_JOIN':
            msg = 'User ' + status.detail + ' joined'
            break;
        case 'USER_LEAVE':
            msg = 'User ' + status.detail + ' left'
            break;
        default:
            console.error('Invalid status update: ' + JSON.stringify(status))
            return;
    }
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    var span = document.createElement('span');
    span.style.color = 'lightgoldenrodyellow';
    span.textContent = msg;
    p.appendChild(span);
    response.appendChild(p);
}

function appendMessage(msg) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    var span = document.createElement('span');
    span.style.color = msg.color;
    span.textContent = '[' + msg.time + '] <' + msg.from + '> ' + msg.text;
    p.appendChild(span);
    response.appendChild(p);
}

function init() {
    disconnect();

    // Enables pressing the Enter Key in the Send Message Prompt
    document.getElementById('text')
        .addEventListener('keyup', function(event) {
            event.preventDefault();
            if (event.keyCode === 13) {
                document.getElementById("sendMessage").click();
            }
        });

    // Enables pressing the Enter Key in the Login Prompt
    document.getElementById('login')
        .addEventListener('keyup', function(event) {
            event.preventDefault();
            if (event.keyCode === 13) {
                connect();
            }
        });
}
