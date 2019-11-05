package esc.voting.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class VoteManager {

    private final FlowableProcessor<Vote> voteProcessor = PublishProcessor.<Vote>create().toSerialized();
    private final ConnectableFlowable<Map<Integer, Long>> sumFlowable;
    private final ReplayProcessor<Map<Integer, Long>> lastSumFlowable = ReplayProcessor.createWithSize(1);

    public VoteManager() {
        this(Schedulers.computation());
    }
    
    public VoteManager(Scheduler scheduler) {
        sumFlowable = voteProcessor
            .map(Vote::getSongNumber)
            .buffer(100, TimeUnit.MILLISECONDS, scheduler)
            .filter(list -> ! list.isEmpty())
            //.doOnNext(buffer -> System.out.println("Got buffer of : " + buffer.size() + " votes"))
            .map((List<Integer> voteList) -> voteList.stream().collect(
                    Collectors.groupingBy(
                            Function.identity(),
                            Collectors.counting())))
            .scan((Map<Integer, Long>)new HashMap<Integer, Long>(), (sums, moreSums) ->
                    Stream.of(sums, moreSums)
                            .flatMap(map -> map.entrySet().stream())
                            .collect(Collectors.toMap(
                                    Entry::getKey,
                                    Entry::getValue,
                                    (v1, v2) -> v1 + v2)))
            //.doAfterNext(map -> System.out.println("Counts are now: " + map))
            .publish();
        sumFlowable.subscribe(lastSumFlowable);
        sumFlowable.connect();
        ConnectableFlowable<Map<Integer, Long>> lastSumFlowable2 = lastSumFlowable.publish();
        lastSumFlowable2.connect();
    }

    public void postVote(Vote newVote) {
        voteProcessor.onNext(newVote);
    }

    public Flowable<Map<Integer, Long>> getSum() {
        return sumFlowable
            .onBackpressureDrop()
            .distinctUntilChanged();
    }

    public Flowable<Vote> getVotes() {
        return voteProcessor
            .onBackpressureDrop();
    }

	public Flowable<Map<Integer, Long>> getSumWithReplay() {
        return Flowable.concat(lastSumFlowable.take(1), getSum());
	}

}
