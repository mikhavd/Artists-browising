package com.example.artists.networking

import com.example.artists.networking.pojos.ArtistsResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import rx.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import rx.Observable
/**
 * Services for //todo
 * @author Mikhail Avdeev (avdeev.m92@gmail.com)
 */

private const val BASE_URL = """https://test-mobile-configuration-files.s3.eu-central-1.amazonaws.com/stories-test/"""

var rxAdapter: RxJavaCallAdapterFactory = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io())

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    //todo check, if required: .add(SingleToArray.Adapter.FACTORY)
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(rxAdapter)
    .build()

/**
 * Interface for obtaining [ShelfResponse] from
 * [https://test-mobile-configuration-files.s3.eu-central-1.amazonaws.com/stories-test/shelf.json]
 */
interface ArtistsApiService {

    @GET("shelf.json")
    fun getArtistsList(): Observable<ArtistsResponse>
}

object ArtistsApi {
    val artistsApiService: ArtistsApiService by lazy { retrofit.create(ArtistsApiService::class.java) }
}
