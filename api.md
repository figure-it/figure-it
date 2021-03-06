# API

## Objects:

Position:
  - x: signed int 4 bytes
  - y: signed int 4 bytes
  
Size: 
  - width: unsigned int 4 bytes
  - height: unsigned int 4 bytes

Player
  - id: unsigned int 4 bytes
  - position: Position
  - name: String
  - color: Color
  - points: unsigned int 4 bytes
  - online: boolean

Map
  - size: Size
  - players: Player[]

Figure
  - id: unsigned int 4 bytes
  - size: Size
  - image: String[] //Strings of '0's and '1's with length=size.width
  - points: unsigned int
  

## Protocol


### HTTP

##### GET /game/onlinePlayers
```json
{
  "players": [{
  }]
}
```

##### POST /game/authorize
Request:
```json
{
  "name": "asdf"
}
```

Response:
```json
{
  "player": {}
}
```

### WebSocket

#### from client to server

##### move
```json
{
  "action": "move",
  "data": {
    "move": "up"
  }
}
```

##### goActive - player wants to appear on map
```json
{
 "action": "online",
 "data": {}
}
```

##### logout - player wants to disappear on map
```json
{
 "action": "offline",
 "data": {}
}
```

#### from server to client

##### updates - players move actions
```json
{
 "action": "updates",
 "data": {
   "playerPositions": [
   {
     "id": 1,
     "position": [123, 9]
   },
   {
     "id": 3,
     "position": [121, 11]
   }
   ]
 }
}
```

##### figure - task (figure) updated
```
{
  "action":  "figure",
  "data": {
   "timeout": 20,
   "size": {
     "width": 2,
     "height": 2
   }
   "image": ["10", "01"],
   "points": 10
  }
}
```

##### dashboard - dashboard updated
```
{
   "action":  "dashboard",
   "data": {
     "players": [
       {"id": 122, "score": 20},
       {"id": 123, "score": 15}
     ]
   }
}
```

##### stopTheWorld - game pause
//todo

##### startTheWorld - game resume
//todo

##### winFigures - report which exact figures were collected as win-figures
//todo
