let tileset = 'retro';
let sz = 16;
let tiles = {
    player: {},
    space: {},
    wall: {},
    entrance: {},
    exit: {}
}
let stompClient = null;
let playerConnected = false;
let map;
let mapSize = {x: 5, y: 5};
let player = {name: null, pos: null};
let tilemap = new Image();
switch(tileset){
    default:
        tilemap.src = '/resources/tiles.png';
        tiles.player = {x: 2, y: 19};
        tiles.space = {x: 0, y: 5};
        tiles.wall = {x: 7, y: 5};
        tiles.entrance = {x: 3, y: 1};
        tiles.exit = {x: 3, y: 1};
        sz = 16;
}
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
            ctx.canvas.width = mapSize.x * sz * 2 + sz;
            ctx.canvas.height = mapSize.y * sz * 2 + sz;
            message = JSON.parse(message.body);
            map = message.map;
            player.pos = message.player;
            rerender();
            let canvasElem = $("canvas");
            canvasElem.attr('tabindex', '0');
            canvasElem.focus();
            $("canvas").unbind('keypress');
            $("canvas").keypress((e) => {
                console.log(e.originalEvent.key);
                let oldPos = {x: player.pos.x, y: player.pos.y};
                switch (e.originalEvent.key){
                    case 'w': player.pos.y--; break;
                    case 's': player.pos.y++; break;
                    case 'a': player.pos.x--; break;
                    case 'd': player.pos.x++; break;
                    case 'q': player.pos.y--; player.pos.x--; break;
                    case 'z': player.pos.y++; player.pos.x--; break;
                    case 'e': player.pos.y--; player.pos.x++; break;
                    case 'c': player.pos.y++; player.pos.x++; break;
                }
                switch (map[player.pos.y][player.pos.x]){
                    case '0': rerender(); break;
                    case '$': stompClient.send(`/app/initializeMap/${player.name}`, {}, JSON.stringify(mapSize)); break;
                    default: player.pos = oldPos;
                }
            });
        });
        stompClient.send('/app/join', {}, player.name);
        stompClient.send(`/app/initializeMap/${player.name}`, {}, JSON.stringify(mapSize));
    });
}

function disconnect() {
    if (stompClient !== null)
        stompClient.send('/app/leave', {}, $("#name").val());
    stompClient.disconnect();
    $("canvas").unbind("keypress");
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
    ctx.drawImage(tilemap, sz*tiles.player.x, sz*tiles.player.y, sz, sz, sz*player.pos.x, sz*player.pos.y, sz, sz);
}
function drawMap(){
    for (let y = 0; y < map.length; y++){
        for (let x = 0; x < map[0].length; x++){
            let tile = {x: 0, y: 0};
            if (map[y][x] == '1'){
                tile = tiles.wall;
            }
            if (map[y][x] == '0'){
                tile = tiles.space;
            }
            if (map[y][x] == '$'){
                tile = tiles.entrance;
            }
            ctx.drawImage(tilemap, sz*tile.x, sz*tile.y, sz, sz, sz*x, sz*y, sz, sz);
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









