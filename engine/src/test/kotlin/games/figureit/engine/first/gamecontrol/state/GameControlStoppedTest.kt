package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.first.gamecontrol.Field
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState.ON_MAP
import games.figureit.engine.model.PositionState.PENDING
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class GameControlStoppedTest {

    private lateinit var playerGenerator: PlayerGenerator

    @BeforeMethod
    fun beforeEach() {
        playerGenerator = PlayerGeneratorImpl()
    }

    @Test
    fun addNoPlayersActive() {
        val state = createDefaultState()
        val players = state.getActivePlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun addNoPlayersPending() {
        val state = createDefaultState()
        val players = state.getPendingPlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun newPlayersAreActive() {
        val state = createDefaultState()
        state.addPlayer()
        val players = state.getActivePlayers()
        assertThat(players, hasSize(1))
        val playerState = players.stream().findAny().get().positionState
        assertThat(playerState, equalTo(ON_MAP))
    }

    @Test
    fun newPlayersAreNotPending() {
        val gameState = createDefaultState()
        gameState.addPlayer()
        val players = gameState.getPendingPlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun pendingPlayersAddAsNonPending() {
        val pendingPlayers = setOf(generatePendingPlayer(0, 0))
        val gameState = createDefaultState(playersToAdd = pendingPlayers)
        val players = gameState.getPendingPlayers()
        assertThat(players, hasSize(0))
    }

    @Test
    fun pendingPlayersAddAsActive() {
        val player = generatePendingPlayer(0, 0)
        val gameState = createDefaultState(playersToAdd = setOf(player))
        val players = gameState.getActivePlayers()
        assertThat(players, hasSize(1))
        assertThat(players, hasItem(player))
    }

    @Test
    fun removePlayer() {
        val gameState = createDefaultState()
        val player = gameState.addPlayer()
        gameState.removePlayer(player.id)

        val players = gameState.getActivePlayers()
        assertThat(players, hasSize(0));
    }

    @Test
    fun movePlayer() {
        val gameState = createDefaultState()
        val player = gameState.addPlayer()
        gameState.move(player.id, RIGHT)

        val players = gameState.getActivePlayers()
        assertThat(players, hasSize(1))
        assertThat(players.stream().findAny().get().position, equalTo(Position(0,0)))
    }

    private fun createDefaultState(
        positionGenerator: PositionGenerator = PositionGeneratorFirstFree(),
        playerGenerator: PlayerGenerator = PlayerGeneratorImpl(),
        field: Field = Field(5, 5),
        players: MutableMap<Long, Player> = HashMap(),
        playersToAdd: Set<Player> = emptySet(),
        playersToRemove: Set<Long> = emptySet()
    ): GameControlState {
        return GameControlStopped(
            positionGenerator = positionGenerator,
            playerGenerator = playerGenerator,
            field = field,
            players = players,
            playersToAdd = playersToAdd,
            playersToRemove = playersToRemove
        )
    }

    private fun generatePendingPlayer(x: Int, y: Int): Player {
        val player = playerGenerator.generate()
        player.position = Position(0, 0)
        player.positionState = PENDING
        return player
    }

}
