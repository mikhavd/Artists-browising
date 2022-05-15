package com.example.artists.listwithsearch

import com.example.artists.listwithsearch.schedulers.SchedulerProvider
import rx.Scheduler
import rx.schedulers.Schedulers

/**
 * Provider of schedulers for RxJava in tests
 *
 * @author Mikhail Avdeev (avdeev.m92@gmail.com)
 */
class TestSchedulerProvider : SchedulerProvider {
    override fun ui(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun trampoline(): Scheduler = Schedulers.trampoline()

    override fun newThread(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()
}