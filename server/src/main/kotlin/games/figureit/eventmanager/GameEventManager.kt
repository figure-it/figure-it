package games.figureit.eventmanager

import games.figureit.engine.first.listener.PlayerUpdateListener
import games.figureit.engine.first.listener.TaskUpdateListener
import games.figureit.engine.first.listener.WorldStateListener

interface GameEventManager : WorldStateListener, TaskUpdateListener, PlayerUpdateListener
