# API

## Objects:

Pixel:
  - x: signed int 4 bytes
  - y: signed int 4 bytes
  
Size: 
  - x: unsigned int 4 bytes
  - y: unsigned int 4 bytes

Player
  - id: unsigned int 4 bytes
  - position: Pixel
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
  - pixels: Pixel[]
  

## Protocol


### HTTP

1. getOnlinePlayers


1. getCurrentMap


1. authorize
id
position

### WebSocket



#### from client to server
1. move
```json
{
 "action": "move",
 "data": {
   "move": "up"
 }
}
```

1. logout
```json
{
 "action": "logout",
 "data": {}
}
```


#### from server to client
1. updates
```json
{
 "action": "updates",
 "data": {
   "updates": {
     "players": [
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
}
```


1. playerLogin
```json
{
 "action": "playerLogin",
 "data": {
   "player": {"id": 122, "name": "asdf"}
   }
 }
}
```


1. playerLogout
```json
{
 "action": "playerLogout",
 "data": {
   "player": {"id": 122}
   }
 }
}
```
