package esc.voting.service;

import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.http.sse.Event;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Controller("/vote")
public class VoteController {

    @Inject
    private VoteManager voteManager;

    @Post(uri = "/async", consumes = { MediaType.ALL } )
    public Single<HttpStatus> async(
        @QueryValue @Nonnull int songNumber,
        @QueryValue @Nonnull String voterName) {

        InetSocketAddress clientAddress = ServerRequestContext.currentRequest().map(HttpRequest::getRemoteAddress).orElse(null);

        Vote vote = new Vote(clientAddress.toString(), Instant.now(), songNumber, voterName);
        //System.out.println("Got vote for " + songNumber);
        return Song.ALL
            .filter(s -> s.getNumber() == songNumber)
            .doOnEach(song -> {
                voteManager.postVote(vote);
            })
            .map(s -> HttpStatus.CREATED)
            .first(HttpStatus.NOT_FOUND);
    }

    @Post(uri = "/sync", consumes = { MediaType.ALL })
    public HttpStatus sync(
        @QueryValue @Nonnull int songNumber,
        @QueryValue @Nonnull String voterName) {
        return this.async(songNumber, voterName).blockingGet();
    }

    @Get(uri = "/sum")
    Flowable<Event<Map<Integer, Long>>> getSum() {
        return voteManager.getSumWithReplay()
            .map(Event::of);
    }

    @Get(uri = "/votes")
    Flowable<Event<Vote>> getVotes() {
        return voteManager.getVotes()
                .map(Event::of);
    }

}
