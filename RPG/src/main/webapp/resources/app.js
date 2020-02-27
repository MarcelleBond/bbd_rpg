let stompClient = null;
let playerConnected = false;
function setConnected(connected) {
    playerConnected = connected;
    $('#connect').prop('disabled', (playerConnected || $('#name').val() === ""));
    $('#disconnect').prop('disabled', !playerConnected);
    if (!connected)
        $("#statusList").html("Not connected");
    else
        $("#statusList").html("");
}

function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, (frame) => {
        setConnected(true);
        stompClient.subscribe('/player/active', (message) => {
            let players = JSON.parse(message.body);
            showPlayers(players);
        });
        stompClient.send('/app/join', {}, $("#name").val());
    });
}

function disconnect() {
    if (stompClient !== null)
        stompClient.send('/app/leave', {}, $("#name").val());
    stompClient.disconnect();
    setConnected(false);
}
function showPlayers(players) {
    let statusList = $('#statusList');
    statusList.html('');
    players.forEach((player) => {
        statusList.append(`<li>${player}</li>`);
    });
}

$(() => {
    setConnected(false);
    let connectButton = $("#connect");
    let disconnectButton = $("#disconnect");
    let nameInput = $("#name");
    nameInput.on('input', () => {
        connectButton.prop('disabled', (playerConnected || nameInput.val() === ""));
    });
    connectButton.prop('disabled', (playerConnected || nameInput.val() === ""));
    disconnectButton.prop('disabled', !playerConnected);
    connectButton.click(() => connect());
    disconnectButton.click(() => disconnect());
});









