const WebSocket = require('ws')

// local
const ws = new WebSocket('ws://localhost:4000/city')


ws.on('open', () => {
  console.log('open socket')
  ws.send("ready", (err) => {});
});

ws.on('message', (data) => {
  console.log(data)
  console.log("\n\n\n\n")
});


ws.on('close', () => {
  console.log("closing")
});
