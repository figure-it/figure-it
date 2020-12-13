package games.figureit.engine.first.scorecontrol.scorescheduler

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.lang.Thread.sleep


class ScoreSchedulerTimerTest {

    private lateinit var checkRun: CheckRun
    private lateinit var scoreScheduler: ScoreSchedulerTimer

    @BeforeMethod
    fun setUp() {
        checkRun = CheckRun()
        scoreScheduler = ScoreSchedulerTimer(50)
    }

    @Test
    fun testBeforeStart() {
        assertThat(checkRun.runned, equalTo(false))
    }

    @Test
    fun testAfterStart() {
        scoreScheduler.start(checkRun)
        assertThat(checkRun.runned, equalTo(false))
    }

    @Test
    fun testAfterStartAndWait() {
        scoreScheduler.start(checkRun)
        sleep(100)
        assertThat(checkRun.runned, equalTo(true))
    }

    @Test
    fun testAfterStartAndWaitDouble() {
        scoreScheduler.start(checkRun)
        sleep(75)
        checkRun.runned = false
        sleep(50)
        assertThat(checkRun.runned, equalTo(true))
    }

    @Test
    fun testAfterStop() {
        scoreScheduler.start(checkRun)
        scoreScheduler.stop()
        sleep(100)
        assertThat(checkRun.runned, equalTo(false))
    }

    class CheckRun: Runnable {
        var runned = false
        override fun run() {
            runned = true
        }

    }
}
