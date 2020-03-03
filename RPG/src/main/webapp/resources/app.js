let stompClient = null;
let playerConnected = false;
let map;
let player = {name: null, pos: null};
let tileset = new Image();
tileset.src = '/resources/tiles.png';
let canvas = document.getElementById('canvas');
    let ctx = canvas.getContext('2d');
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
        player.name = $("#name").val();
        setConnected(true);
        stompClient.subscribe('/player/active', (message) => {
            let players = JSON.parse(message.body);
            showPlayers(players);
        });
        stompClient.subscribe(`/map/initialMap/${player.name}`, (message) => {
            message = JSON.parse(message.body);
            map = message.map;
            player.pos = message.player;
            rerender();
            $("body").keypress((e) => {
                console.log(e.originalEvent.key);
            });
        });
        stompClient.send('/app/join', {}, player.name);
        stompClient.send(`/app/initializeMap/${player.name}`, {}, '{"x": 40, "y": 20}');
    });
}

function disconnect() {
    if (stompClient !== null)
        stompClient.send('/app/leave', {}, $("#name").val());
    stompClient.disconnect();
    $("body").unbind("keypress");
    setConnected(false);
}
function showPlayers(players) {
    let statusList = $('#statusList');
    statusList.html('');
    players.forEach((player) => {
        statusList.append(`<li>${player}</li>`);
    });
}
function drawPlayer() {
    ctx.drawImage(tileset, 16*1, 16*21, 16, 16, 16*player.pos.x, 16*player.pos.y, 16, 16);
}
function drawMap(){
    for (let y = 0; y < map.length; y++){
        for (let x = 0; x < map[0].length; x++){
            let tile = {x: 0, y: 0};
            if (map[y][x] == '1'){
                tile = {x: 7, y: 5};
            }
            if (map[y][x] == '0'){
                tile = {x: 0, y: 5};
            }
            if (map[y][x] == '$'){
                tile = {x: 3, y: 1};
            }
            ctx.drawImage(tileset, 16*tile.x, 16*tile.y, 16, 16, 16*x, 16*y, 16, 16);
        }
    }
}

function rerender(){
    drawMap();
    drawPlayer();
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









