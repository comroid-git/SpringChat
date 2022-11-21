var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('from').disabled = connected;
    document.getElementById('conversationDiv').style.visibility
        = connected ? 'visible' : 'hidden';
    document.getElementById('login').style.visibility
        = !connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

function connect() {
    if (document.getElementById('from').value === '') {
        console.error('Please enter a user name');
        return;
    }

    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/handshake', function(valid){
            if (!valid) {
                console.error('Failed to connect: Username is already in use')
                return;
            }
            stompClient.subscribe('/topic/messages', function(messageOutput) {
                showMessageOutput(JSON.parse(messageOutput.body));
            });
            stompClient.subscribe('/topic/users', function(messageOutput) {
                var data = JSON.parse(messageOutput.body);
                document.getElementById('userlist').innerHTML = '';
                data.forEach(addUserToUI);
            });
            stompClient.send("/app/users/join", {}, document.getElementById('from').value);
        });
        stompClient.send('/app/users/handshake', {}, document.getElementById('from').value);
    });
}

function addUserToUI(user) {
    var userEntry = document.createElement('p'); // <p></p>
    userEntry.textContent = user; // <p>user</p>
    userEntry.style.color = 'white';
    document.getElementById('userlist').appendChild(userEntry);
}

function disconnect() {
    if(stompClient != null) {
        stompClient.send("/app/users/leave", {}, document.getElementById('from').value);
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var from = document.getElementById('from').value;
    var text = document.getElementById('text').value;
    if (text === "")
        return;
    stompClient.send("/app/msg", {}, JSON.stringify({'from':from, 'text':text}));
    document.getElementById('text').value = "";
    document.getElementById('response').scrollTo(0, document.getElementById('response').scrollHeight)
}

function showMessageOutput(msg) {
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
    document.getElementById('text')
        .addEventListener('keyup', function(event) {
            event.preventDefault();
            if (event.keyCode === 13) {
                document.getElementById("sendMessage").click();
            }
        });
}
