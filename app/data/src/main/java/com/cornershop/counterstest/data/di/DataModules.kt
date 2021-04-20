package com.cornershop.counterstest.data.di

import android.content.Context
import androidx.room.Room
import com.cornershop.counterstest.data.R
import com.cornershop.counterstest.data.database.CountersDatabase
import com.cornershop.counterstest.data.generator.IdGenerator
import com.cornershop.counterstest.data.generator.IdGeneratorImpl
import com.cornershop.counterstest.data.repository.CountersRepositoryImpl
import com.cornershop.counterstest.data.sources.local.CountersLocalDataSource
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.repository.CountersRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModules {

    private const val REQUEST_TIMEOUT = 60L

    // region NETWORKING
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        @ApplicationContext context: Context,
        moshi: Moshi
    ): Retrofit {
        val apiUrl = context.getString(R.string.api_url)

        val client = OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideCountersRemoteDataSource(retrofit: Retrofit): CountersRemoteDataSource =
        retrofit.create(CountersRemoteDataSource::class.java)
    // endregion

    // region DATABASE
    @Provides
    @Singleton
    fun provideIdGenerator(): IdGenerator = IdGeneratorImpl()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            CountersDatabase::class.java,
            CountersDatabase::class.simpleName!!
        ).build()

    @Provides
    @Singleton
    fun provideCountersLocalDataSource(countersDatabase: CountersDatabase): CountersLocalDataSource =
        countersDatabase.createCountersLocalDataSource()
    // endregion

    @Provides
    @Singleton
    fun providesCountersRepository(
        remoteDataSource: CountersRemoteDataSource,
        localDataSource: CountersLocalDataSource,
        idGenerator: IdGenerator,
    ): CountersRepository = CountersRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource,
        idGenerator = idGenerator
    )
}
