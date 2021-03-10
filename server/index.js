const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const cors = require("cors");

var players = [];

const port = process.env.port || 4001;
const routes = require("./routes/index");

const app = express();
app.use(cors());
app.options('*', cors());
app.use(routes);

const server = http.createServer(app);
let serverTimeInterval;

//const { snakeGame } = require('./snakeGame');

const serverTime = socket => {
    if (serverTimeInterval) { clearInterval(serverTimeInterval); }

    serverTimeInterval = setInterval(() => {
        const response = new Date();
        socket.emit("ServerTime", response);
    }, 1000);
};

const io = socketIo(server, {
    cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST"]
    }
});



io.on("connection", (socket) => {
    console.log("New client connected");

    socket.emit("socketID", { id: socket.id })
    socket.emit("getPlayers", players)


    socket.broadcast.emit("newPlayer", { id: socket.id })

    socket.emit('init_msg', { data: 'sup bruh' });

    //serverTime(socket);

    //snakeGame(socket);

    socket.on("disconnect", () => {
        console.log("Client disconnected");

        socket.broadcast.emit("playerDisconnect", {id: socket.id});
        for(var i = 0; i < players.length; i++){
            if (players[i].id == socket.id){
                players.splice(i,1)
            }
        }

        clearInterval(serverTimeInterval);
    });

players.push(new player(socket.id, 0, 0));

});



server.listen(port, () => {
    console.log(`Listening on port ${port}`);
});


function player(id, x, y){
    this.id = id;
    this.x = x;
    this.y = y;
}

