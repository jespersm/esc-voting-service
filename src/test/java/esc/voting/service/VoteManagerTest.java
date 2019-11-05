package esc.voting.service;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;

public class VoteManagerTest {

    private final TestScheduler scheduler = new TestScheduler();
    private final VoteManager voterManager = new VoteManager(scheduler);

    @Test
    void noVotes() {
        TestSubscriber<Vote> testSubscriber = voterManager.getVotes().test();
        testSubscriber.assertNoValues();
        testSubscriber.assertNoErrors();
    }

    @Test
    void singleVote() {
        TestSubscriber<Vote> testSubscriber = voterManager.getVotes().test();
        voterManager.postVote(new Vote(null, Instant.now(), 1, "Jesper")); // Song 1 was good
        testSubscriber.assertNotTerminated();
        testSubscriber.assertValueCount(1);
    }

    @Test
    void summedVotes() throws InterruptedException {
        voterManager.postVote(new Vote(null, Instant.now(), 7, "Jesper")); // Say na na na!
        voterManager.postVote(new Vote(null, Instant.now(), 1, "Michael"));
        voterManager.postVote(new Vote(null, Instant.now(), 7, "Thomas"));
        voterManager.postVote(new Vote(null, Instant.now(), 7, "Daniel"));
        TestSubscriber<Map<Integer, Long>> testSubscriber = voterManager.getSum().test();
        testSubscriber.assertNoValues();
        scheduler.advanceTimeBy(99, TimeUnit.MILLISECONDS);
        testSubscriber.assertNoValues();
        scheduler.advanceTimeBy(1, TimeUnit.MILLISECONDS);
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(map -> map.get(1) == 1L && map.get(7) == 3L);
    }

    @Test
    void replyForNewSubscribers() throws InterruptedException {
        voterManager.postVote(new Vote(null, Instant.now(), 7, "Jesper")); // Say na na na!
        TestSubscriber<Map<Integer, Long>> firstSubscriber = voterManager.getSum().test();
        firstSubscriber.assertNoValues();
        scheduler.advanceTimeBy(101, TimeUnit.MILLISECONDS);
        firstSubscriber.assertValueCount(1);
        
        TestSubscriber<Map<Integer, Long>> nextSubscriber = voterManager.getSumWithReplay().test();
        nextSubscriber.assertValueCount(1);
        nextSubscriber.assertValue(Collections.singletonMap(7, 1L));

        voterManager.postVote(new Vote(null, Instant.now(), 2, "Hans"));

        TestSubscriber<Map<Integer, Long>> nextSubscriber2 = voterManager.getSumWithReplay().test();
        nextSubscriber2.assertValueCount(1);
    }

}
