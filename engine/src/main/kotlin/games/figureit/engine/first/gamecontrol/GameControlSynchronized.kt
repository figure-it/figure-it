package games.figureit.engine.first.gamecontrol

import games.figureit.engine.first.GameControl
import games.figureit.engine.first.gamecontrol.TimerState.RUNNING
import games.figureit.engine.first.gamecontrol.TimerState.STOPPED
import games.figureit.engine.model.Move
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.LEFT
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Move.UP
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState.ON_MAP
import games.figureit.engine.model.PositionState.PENDING
import games.figureit.engine.model.Size

class GameControlSynchronized(
    private val positionGenerator: PositionGenerator,
    mapSide: Int
): GameControl {

    private val mapSize: Size = Size(mapSide, mapSide)
    private val field: Array<Array<Player?>> = Array(mapSize.height) { Array(mapSize.width) { null } }
    private val players: MutableMap<Long, Player> = HashMap()
    private var playerIdCounter: Long = 1
    private var timerState: TimerState = STOPPED

    private val pendingAddPlayers: MutableMap<Long, Player> = HashMap()
    private val pendingRemovePlayers: MutableSet<Long> = HashSet()

    val moves: Map<Move, (Player) -> Unit> = mapOf(
        UP to { player -> moveUp(player) },
        DOWN to { player -> moveDown(player) },
        LEFT to { player -> moveLeft(player) },
        RIGHT to { player -> moveRight(player) }
    )

    @Synchronized
    override fun move(playerId: Long, move: Move) {
        if (timerState == STOPPED) {
            return
        }
        val player = players[playerId]
        player?.let {
            moves[move]!!.invoke(it)
        }
    }

    private fun moveLeft(player: Player) {
        val new = Position(maxOf(player.position.x - 1, 0), player.position.y)
        movePlayerToPosition(new, player)
    }

    private fun moveDown(player: Player) {
        val new = Position(player.position.x, minOf(player.position.y + 1, mapSize.height - 1))
        movePlayerToPosition(new, player)
    }

    private fun moveUp(player: Player) {
        val new = Position(player.position.x, maxOf(player.position.y - 1, 0))
        movePlayerToPosition(new, player)
    }

    private fun moveRight(player: Player) {
        val new = Position(minOf(player.position.x + 1, mapSize.width - 1), player.position.y)
        movePlayerToPosition(new, player)
    }

    @Synchronized
    override fun addPlayer(): Player {
        val player = generatePlayer()
        pendingAddPlayers[player.id] = player
        checkPendingPlayers()
        return player
    }

    @Synchronized
    override fun getMapSize(): Size {
        return mapSize
    }

    @Synchronized
    override fun removePlayer(id: Long) {
        if (pendingAddPlayers[id] != null) {
            pendingAddPlayers.remove(id)
            return
        }
        if (pendingRemovePlayers.contains(id)) {
            return
        }
        if (players.containsKey(id)) {
            pendingRemovePlayers.add(id)
        }
        checkPendingPlayers()
    }

    @Synchronized
    override fun getAllPlayers(): Collection<Player> {
        return players.values + pendingAddPlayers.values
    }

    @Synchronized
    override fun stopTheWorld() {
        timerState = STOPPED
    }

    @Synchronized
    override fun startTheWorld() {
        checkPendingPlayers()
        timerState = RUNNING
    }

    private fun generatePlayer(): Player {
        return Player(
            id = playerIdCounter++,
            positionState = PENDING,
            position = Position(0, 0)
        )
    }

    private fun checkPendingPlayers() {
        if (timerState == RUNNING) {
            return
        }

        for (playerId in pendingRemovePlayers) {
            val player = players[playerId]
            player?.let {
                val position = it.position
                field[position.y][position.x] = null
                players.remove(playerId)
            }
        }

        val currentPositions = players.values.map { it.position } .toMutableList()

        for (player in pendingAddPlayers.values) {
            val position = positionGenerator.generate(mapSize, currentPositions)
            player.position = position
            player.positionState = ON_MAP
            field[position.y][position.x] = player
            currentPositions.add(position)
            pendingAddPlayers.remove(player.id)
            players[player.id] = player
        }
    }

    private fun movePlayerToPosition(newPosition: Position, player: Player) {
        if (positionFree(newPosition)) {
            field[player.position.x][player.position.y] = null
            field[newPosition.x][newPosition.y] = player
            player.position = newPosition
        }
    }

    private fun positionFree(position: Position): Boolean {
        return field[position.y][position.x] == null
    }

}
