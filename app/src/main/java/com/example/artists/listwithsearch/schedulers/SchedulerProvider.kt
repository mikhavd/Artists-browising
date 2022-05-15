package com.example.artists.listwithsearch.schedulers

/**
 * TODO
 * @author Mikhail Avdeev (mvavdeev@sberbank.ru)
 */

import rx.Scheduler

/**
 * Provider of schedulers for RxJava
 * Allow to rewrite scheduler for testing purposes
 * //todo possibly get rid of using this Provider
 *
 * @author Mikhail Avdeev (avdeev.m92@gmail.com)
 */
interface SchedulerProvider {
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun trampoline(): Scheduler
    fun newThread(): Scheduler
    fun io(): Scheduler
}