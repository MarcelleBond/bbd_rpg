let stompClient = null;
let playerConnected = false;
let map;
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

function getMap(type){
    let requestPromise;
    switch (type) {
        case "rooms":
            requestPromise = axios.get('http://localhost:8080/api/map/rooms');
            break;
        case "maze":
            requestPromise = axios.get('http://localhost:8080/api/map');
    }
    requestPromise
        .then((result) => {
            console.log(result);
            map = result.data;
            drawMap();
        });
}

function drawMap(){
    console.log(map);
    let canvas = document.getElementById('canvas');
    let ctx = canvas.getContext('2d');
    let tileset = new Image();
    tileset.src = '/resources/tiles.png';
    tileset.onload = () => {
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
                //draw any overlaying elements (player, monsters, items, etc.)
                if (y == 9 && x == 9){
                    tile = {x: 0, y: 8};
                }
                    ctx.drawImage(tileset, 16*tile.x, 16*tile.y, 16, 16, 16*x, 16*y, 16, 16);
            }
        }
    };
}

$(() => {
    getMap("maze");
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









